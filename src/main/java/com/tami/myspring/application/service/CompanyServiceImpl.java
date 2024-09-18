package com.tami.myspring.application.service;

import com.tami.myspring.application.dao.CompanyDao;
import com.tami.myspring.application.model.Company;
import com.tami.myspring.framework.annotation.Autowired;
import com.tami.myspring.framework.annotation.Component;
import com.tami.myspring.framework.annotation.Transactional;
import com.tami.myspring.framework.annotation.Cacheable;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyDao companyDao;

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public void createCompany(Company company) {
        logger.info("SERVICE:   START - create company");
        createWithTransaction(company);
        logger.info("SERVICE:   END - create company");
    }

    @Override
    @Transactional
    public void updateCompany(Company company) {
        logger.info("SERVICE:   START - update company");
        companyDao.updateCompany(company);
        logger.info("SERVICE:   END - update company");
    }

    @Override
    @Cacheable
    public String generateToken(Company company) {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public void createWithTransaction(Company company) {
        logger.info("SERVICE:   START - createWithTransaction");
        companyDao.createCompany(company);
        logger.info("SERVICE:   END - createWithTransaction");
    }

}
