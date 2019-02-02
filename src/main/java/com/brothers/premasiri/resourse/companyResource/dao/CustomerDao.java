package com.brothers.premasiri.resourse.companyResource.dao;

import com.brothers.premasiri.resourse.companyResource.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Customer findFirstByOrderByIdDesc();

    Customer findByNic(String nic);

    Customer findByNumber(String number);
}
