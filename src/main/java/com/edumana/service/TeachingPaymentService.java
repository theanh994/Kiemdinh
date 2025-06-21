package com.edumana.service;

import com.edumana.dto.TeachingPaymentDTO;
import com.edumana.dto.TeachingPaymentYearlyReport;
import com.edumana.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeachingPaymentService {

    @Autowired
    private CourseClassService courseClassService;

    @Autowired
    private TeachingRateService teachingRateService;

    @Autowired
    private ClassCoefficientService classCoefficientService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SemesterService semesterService;    public TeachingPaymentDTO calculatePayment(Teacher teacher, Semester semester) {
        // Lấy mức lương cơ bản hiện tại
        TeachingRate currentRate = teachingRateService.getCurrentRate()
            .orElseThrow(() -> new RuntimeException("Chưa thiết lập định mức tiền dạy!"));

        // Lấy danh sách lớp học phần của giáo viên trong kỳ
        List<CourseClass> teacherClasses = courseClassService.findBySemesterAndTeacher(semester, teacher);

        List<TeachingPaymentDTO.ClassPaymentDetail> classDetails = new ArrayList<>();
        BigDecimal totalPayment = BigDecimal.ZERO;

        for (CourseClass courseClass : teacherClasses) {
            // Tính hệ số lớp dựa trên số sinh viên
            BigDecimal classCoefficient = classCoefficientService
                .getCoefficientForStudentCount(courseClass.getMaxStudents());

            // Tính số tiết quy đổi
            BigDecimal courseCoefficient = BigDecimal.valueOf(courseClass.getCourse().getCoefficient());
            BigDecimal totalCoefficient = courseCoefficient.add(classCoefficient);
            
            BigDecimal convertedPeriods = BigDecimal.valueOf(courseClass.getCourse().getPeriods())
                .multiply(totalCoefficient)
                .setScale(2, RoundingMode.HALF_UP);

            // Tính tiền cho lớp học phần
            BigDecimal classPayment = convertedPeriods
                .multiply(teacher.getDegree().getTeachingCoefficient())
                .multiply(currentRate.getBaseRatePerPeriod())
                .setScale(0, RoundingMode.HALF_UP);

            classDetails.add(new TeachingPaymentDTO.ClassPaymentDetail(
                courseClass.getClassCode(),
                courseClass.getClassName(),
                courseClass.getCourse().getName(),
                courseClass.getCourse().getPeriods(),
                courseClass.getMaxStudents(),
                courseCoefficient,
                classCoefficient,
                convertedPeriods,
                classPayment
            ));

            totalPayment = totalPayment.add(classPayment);
        }

        return new TeachingPaymentDTO(
            teacher.getName(),
            teacher.getDegree().getFullName(),
            semester.getName() + " " + semester.getYear(),
            teacher.getDegree().getTeachingCoefficient(),
            classDetails,
            totalPayment
        );
    }

    public TeachingPaymentYearlyReport calculateYearlyPayment(Long teacherId, int year) {
        Teacher teacher = teacherService.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên"));

        List<Semester> yearSemesters = semesterService.findByYear(year);
        List<TeachingPaymentYearlyReport.SemesterPayment> semesterPayments = new ArrayList<>();
        BigDecimal totalYearlyPayment = BigDecimal.ZERO;

        for (Semester semester : yearSemesters) {
            // Get details for each semester
            List<CourseClass> classes = courseClassService.findBySemesterAndTeacher(semester, teacher);
            int totalPeriods = 0;
            BigDecimal semesterPayment = BigDecimal.ZERO;

            // Calculate totals for this semester
            for (CourseClass courseClass : classes) {
                TeachingPaymentDTO payment = calculatePayment(teacher, semester);
                Optional<TeachingPaymentDTO.ClassPaymentDetail> classDetail = payment.getClassDetails().stream()
                    .filter(detail -> detail.getClassCode().equals(courseClass.getClassCode()))
                    .findFirst();

                if (classDetail.isPresent()) {
                    totalPeriods += classDetail.get().getActualPeriods();
                    semesterPayment = semesterPayment.add(classDetail.get().getPayment());
                }
            }

            // Add semester details to the list
            semesterPayments.add(new TeachingPaymentYearlyReport.SemesterPayment(
                semester.getName() + " " + semester.getYear(),
                classes.size(),
                totalPeriods,
                semesterPayment
            ));

            totalYearlyPayment = totalYearlyPayment.add(semesterPayment);
        }

        return new TeachingPaymentYearlyReport(
            teacher.getName(),
            teacher.getDegree().getFullName(),
            teacher.getDepartment().getName(),
            year,
            teacher.getDegree().getTeachingCoefficient(),
            semesterPayments,
            totalYearlyPayment
        );
    }
}
