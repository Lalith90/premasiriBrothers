package com.brothers.premasiri.asset.supplier.service;


import com.brothers.premasiri.asset.supplier.entity.Supplier;
import com.brothers.premasiri.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SupplierService implements AbstractService<Supplier, Long > {



    public List<Supplier> findAll() {
        return null;
    }


    public Supplier findById(Long id) {
        return null;
    }


    public Supplier persist(Supplier supplier) {
        return null;
    }


    public boolean delete(Long id) {
        return false;
    }


    public List<Supplier> search(Supplier supplier) {
        return null;
    }
}
