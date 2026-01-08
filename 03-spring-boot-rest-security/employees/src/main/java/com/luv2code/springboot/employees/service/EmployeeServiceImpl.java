package com.luv2code.springboot.employees.service;

import com.luv2code.springboot.employees.dao.EmployeeDAO;
import com.luv2code.springboot.employees.entity.Employee;
import com.luv2code.springboot.employees.request.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(long id) {
        Employee employee = employeeDAO.findById(id);
        return employee;
    }

    @Transactional
    @Override
    public Employee save(EmployeeRequest employeeRequest) {
        Employee employee = Employee.from(0, employeeRequest);
        return employeeDAO.save(employee);
    }

    @Transactional
    @Override
    public Employee update(long id, EmployeeRequest employeeRequest) {
        Employee employee = Employee.from(id, employeeRequest);
        return employeeDAO.save(employee);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        employeeDAO.deleteById(id);
    }
}

