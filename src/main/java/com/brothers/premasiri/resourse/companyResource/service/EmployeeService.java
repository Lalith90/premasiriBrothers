package com.brothers.premasiri.resourse.companyResource.service;


import com.brothers.premasiri.resourse.companyResource.dao.EmployeeDao;
import com.brothers.premasiri.resourse.companyResource.entity.Employee;
import com.brothers.premasiri.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional// spring transactional annotation need to tell spring to this method work through the project
public class EmployeeService implements AbstractService<Employee, Integer> {

    private EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao){
        this.employeeDao = employeeDao;
    }


    public List<Employee> findAll() {
        return employeeDao.findAll();
    }


    public Employee findById(Integer id) {
        return employeeDao.getOne(id);
    }


    public Employee persist(Employee employee) {
                 return employeeDao.save(employee);
    }


    public boolean delete(Integer id) {
        employeeDao.deleteById(id);
        return false;
    }

    public List<Employee> search(Employee employee) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Employee> employeeExample = Example.of(employee, matcher);
        return employeeDao.findAll(employeeExample);
    }

    public boolean isEmployeePresent(Employee employee){
        return employeeDao.equals(employee);
    }


    public Employee lastEmployee(){
        return employeeDao.findFirstByOrderByIdDesc();
    }


}
