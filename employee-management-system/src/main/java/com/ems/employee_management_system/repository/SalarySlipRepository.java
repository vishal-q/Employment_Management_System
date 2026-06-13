package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.SalarySlip;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SalarySlipRepository extends MongoRepository<SalarySlip, String> {
    List<SalarySlip> findByEmployeeIdOrderByPayPeriodDesc(String employeeId);
    List<SalarySlip> findAllByOrderByPayPeriodDesc();
    Optional<SalarySlip> findByEmployeeIdAndPayPeriod(String employeeId, String payPeriod);
}
