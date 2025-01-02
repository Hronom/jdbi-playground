package com.github.hronom.jdbi_playground.config;

import com.github.hronom.jdbi_playground.dao.MessageDao;
import com.github.hronom.jdbi_playground.service.CustomSqlLogger;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.spring.SpringConnectionFactory;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;

@Configuration
public class JdbiConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public Jdbi primaryJdbi(HikariDataSource primaryDataSource, CustomSqlLogger customSqlLogger) {
        SpringConnectionFactory springConnectionFactory = new SpringConnectionFactory(primaryDataSource);
        Jdbi jdbi = Jdbi.create(springConnectionFactory);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new PostgresPlugin());
        jdbi.setSqlLogger(customSqlLogger);
        // https://jdbi.org/#_using_prepared_arguments_for_batches
        // getConfig(Arguments.class).setPreparedArgumentsEnabled(false)
        return jdbi;
    }

    @Bean
    public MessageDao messageDao(Jdbi primaryJdbi) {
        return primaryJdbi.onDemand(MessageDao.class);
    }
}