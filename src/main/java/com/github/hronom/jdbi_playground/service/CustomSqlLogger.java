package com.github.hronom.jdbi_playground.service;

import org.jdbi.v3.core.statement.ParsedSql;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.time.Duration;

@Service
public class CustomSqlLogger implements SqlLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CustomSqlLogger() {
    }

    @Override
    public void logAfterExecution(StatementContext context) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executed in {} '{}' with parameters '{}'",
                    format(Duration.between(context.getExecutionMoment(), context.getCompletionMoment())),
                    getSql(context),
                    context.getBinding());
        }
    }

    @Override
    public void logException(StatementContext context, SQLException ex) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Exception while executing '{}' with parameters '{}'",
                    getSql(context),
                    context.getBinding(),
                    ex);
        }
    }

    private static String getSql(StatementContext context) {
        ParsedSql parsedSql = context.getParsedSql();
        if (parsedSql != null) {
            return parsedSql.getSql();
        }
        return "<not available>";
    }

    private static String format(Duration duration) {
        final long totalSeconds = duration.getSeconds();
        final long h = totalSeconds / 3600;
        final long m = (totalSeconds % 3600) / 60;
        final long s = totalSeconds % 60;
        final long ms = duration.toMillis() % 1000;
        return String.format(
                "%d:%02d:%02d.%03d",
                h, m, s, ms);
    }
}
