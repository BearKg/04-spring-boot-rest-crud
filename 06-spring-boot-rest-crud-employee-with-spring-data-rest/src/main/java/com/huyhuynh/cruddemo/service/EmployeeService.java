package com.huyhuynh.cruddemo.service;

import com.huyhuynh.cruddemo.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(int id);
    Employee save(Employee employee);
    void deleteById(int theId);
}
