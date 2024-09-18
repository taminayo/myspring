package com.tami.myspring.application.dao;

import com.tami.myspring.application.model.Company;

public interface CompanyDao {
    void createCompany(Company company);
    void updateCompany(Company compnay);
}
