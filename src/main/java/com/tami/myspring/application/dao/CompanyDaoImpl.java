package com.tami.myspring.application.dao;

import com.tami.myspring.application.model.Company;
import com.tami.myspring.framework.annotation.Component;
import com.tami.myspring.framework.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CompanyDaoImpl implements CompanyDao {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDaoImpl.class);

    @Override
    @Transactional
    public void createCompany(Company company) {
        logger.info("DAO:   START - create company");

        logger.info("DAO:   END - create company");
    }

    @Override
    public void updateCompany(Company company) {
        logger.info("DAO:   START - update company");

        logger.info("DAO:   END - update company");
    }
}
