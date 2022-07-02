package com.ex.javaspringboottoy.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

public class QueryDslRepositorySupportCustom extends QuerydslRepositorySupport {

    public QueryDslRepositorySupportCustom(Class<?> domainClass) {
        super(domainClass);
    }

    public <T> Page<T> getPage(JPAQuery<Object> query, Expression<T> selectClause, Pageable pageable) {
        Long totalCount = query.select(Wildcard.count).fetchFirst();
        List<T> content = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query.select(selectClause)).fetch();
        return new PageImpl(content, pageable, totalCount);
    }
}
