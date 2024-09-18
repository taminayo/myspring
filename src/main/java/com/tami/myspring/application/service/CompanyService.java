package com.tami.myspring.application.service;

import com.tami.myspring.application.model.Company;

public interface CompanyService {
    void createCompany(Company company);
    void updateCompany(Company company);
    String generateToken(Company company);
}
