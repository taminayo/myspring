package com.tami.myspring.framework.handler;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tami.myspring.framework.annotation.Transactional;

public class TransactionalHandler extends AbstractProxyHandler {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalHandler.class);

    public TransactionalHandler(final Object objectToHandle) {
        super(objectToHandle, Transactional.class);
    }

    public Object executeWithTransaction(final Supplier<Object> resultSupplier) {
        beginTransaction();
        try {
            Object result = resultSupplier.get();
            commitTransaction();
            return result;
        } catch (Exception e) {
            rollbackTransaction();
            return e;
        }
    }

    private void beginTransaction() {
        logger.debug("BEGIN TRANSACTION");
    }

    private void commitTransaction() {
        logger.debug("COMMIT TRANSACTION");
    }

    private void rollbackTransaction() {
        logger.error("ROLLBACK TRANSACTION");
    }
}
