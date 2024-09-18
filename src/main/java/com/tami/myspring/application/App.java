package com.tami.myspring.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tami.myspring.application.model.Company;
import com.tami.myspring.application.service.CompanyService;
import com.tami.myspring.framework.ApplicationContext;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ApplicationContext(App.class);
        final CompanyService companyServiceProxy = applicationContext.getBean(CompanyService.class);

        logger.info("======== Transactional ========");
        companyServiceProxy.createCompany(new Company());
        logger.info("===============================");


        logger.info("========== Cacheable ==========");
        final Company company1 = new Company();
        logger.info(companyServiceProxy.generateToken(company1));
        logger.info(companyServiceProxy.generateToken(company1));

        final Company company2 = new Company();
        logger.info(companyServiceProxy.generateToken(company2));
        logger.info("===============================");


        logger.info("============= Scope ===========");
        final CompanyService companyServiceProxy1 = applicationContext.getBean(CompanyService.class);
        final CompanyService companyServiceProxy2 = applicationContext.getBean(CompanyService.class);

        logger.info(String.valueOf(companyServiceProxy1 == companyServiceProxy2));
        logger.info("===============================");
    }
}
