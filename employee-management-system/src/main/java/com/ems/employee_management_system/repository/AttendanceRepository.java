package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByDate(String date);
    List<Attendance> findByEmployeeIdAndDate(String employeeId, String date);
    long countByDateAndStatus(String date, String status);
    List<Attendance> findByEmployeeIdOrderByDateDesc(String employeeId);
}
