package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface HolidayRepository extends MongoRepository<Holiday, String> {
    List<Holiday> findAllByOrderByDateAsc();
    List<Holiday> findByDateGreaterThanEqualOrderByDateAsc(String date);
}
