package com.huyhuynh.cruddemo.dao;

import com.huyhuynh.cruddemo.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll();
    Employee findById(int id);
    Employee save(Employee employee);
    void deleteById(int theId);
}
