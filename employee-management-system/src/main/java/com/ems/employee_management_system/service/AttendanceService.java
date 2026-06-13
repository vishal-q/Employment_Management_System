package com.ems.employee_management_system.service;

import com.ems.employee_management_system.model.Attendance;
import com.ems.employee_management_system.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    public List<Attendance> getAllAttendance() {
        return repository.findAll();
    }

    public Attendance saveAttendance(Attendance attendance) {
        return repository.save(attendance);
    }

    public List<Attendance> getByEmployeeId(String employeeId) {
        return repository.findByEmployeeIdOrderByDateDesc(employeeId);
    }

    public List<Attendance> getByDate(String date) {
        return repository.findByDate(date);
    }

    public long countPresentToday() {
        String today = LocalDate.now().toString();
        return repository.countByDateAndStatus(today, "Present");
    }

    public boolean alreadyMarked(String employeeId, String date) {
        return !repository.findByEmployeeIdAndDate(employeeId, date).isEmpty();
    }
}
