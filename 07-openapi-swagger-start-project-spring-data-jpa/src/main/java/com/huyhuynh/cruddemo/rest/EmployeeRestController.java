package com.huyhuynh.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huyhuynh.cruddemo.entity.Employee;
import com.huyhuynh.cruddemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    // inject dao employee
    private EmployeeService employeeService;
    private ObjectMapper objectMapper;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService,  ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }

    // expose "/employees" to return list of employee
    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeID}")
    public Employee findById(@PathVariable int employeeID) {
        Employee theEmployee = employeeService.findById(employeeID);
        if(theEmployee == null)
            throw new RuntimeException("Employee with id " + employeeID + " not found");
        return theEmployee;
    }

    // Add mapping for POST /employees
    @PostMapping("/employees")
    public Employee save(@RequestBody Employee theEmployee) {
        theEmployee.setId(0);
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

    @PutMapping("/employees")
    public Employee update(@RequestBody Employee theEmployee) {
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

    @PatchMapping("/employees/{employeeId}")
    public Employee updateEmployee(@RequestBody Map <String, Object> patchPayload, @PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if null
        if(tempEmployee == null)
            throw new RuntimeException("Employee with id " + employeeId + " not found");

        // throw exception if request body contains "id" key
        if(patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee id not allowed in request body" );
        }

        Employee patchedEmployee = apply(patchPayload,tempEmployee);

        Employee dbEmployee = employeeService.save(patchedEmployee);

        return dbEmployee;
    }

    private Employee apply(Map<String, Object> patchPayload, Employee tempEmployee) {

        // Convert employee object to a JSON object node
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);

        // Convert the patchPayload map to a JSON object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // Merge the patch updates into the employee node
        employeeNode.setAll(patchNode);

        return  objectMapper.convertValue(employeeNode, Employee.class);

    }

    // delete mapping
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId);

        if(tempEmployee == null)
            throw new RuntimeException("Employee with id " + employeeId + " not found");

        employeeService.deleteById(employeeId);
        return "Delete employee with id " + employeeId;
    }
}
