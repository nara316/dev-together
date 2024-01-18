package com.project.devtogether.common.config;

import com.querydsl.jpa.DefaultQueryHandler;
import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.jpa.QueryHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomHibernate5Templates extends Hibernate5Templates {

    @Override
    public QueryHandler getQueryHandler() {
        return DefaultQueryHandler.DEFAULT;
    }
}
