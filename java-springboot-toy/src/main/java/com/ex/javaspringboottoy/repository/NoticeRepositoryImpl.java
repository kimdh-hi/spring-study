package com.ex.javaspringboottoy.repository;

import com.ex.javaspringboottoy.domain.QNotice;
import com.ex.javaspringboottoy.vo.NoticeResponseVO;
import com.ex.javaspringboottoy.vo.NoticeSearchVO;
import com.ex.javaspringboottoy.vo.QNoticeResponseVO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import static com.ex.javaspringboottoy.domain.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom extends QueryDslRepositorySupportCustom {
    private final JPAQueryFactory query;

    @Override
    public Page<NoticeResponseVO> list(NoticeSearchVO searchVO) {
        JPAQuery<?> query = this.query.from(notice);

        QNoticeResponseVO selectClause = new QNoticeResponseVO(notice.uuid, notice.title);


        return null;
    }
}
