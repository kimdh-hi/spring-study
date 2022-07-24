package com.ex.javaspringboottoy.repository;

import com.ex.javaspringboottoy.config.QueryDslConfig;
import com.ex.javaspringboottoy.domain.Notice;
import com.ex.javaspringboottoy.vo.NoticeResponseVO;
import com.ex.javaspringboottoy.vo.NoticeSearchVO;
import com.querydsl.core.types.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@Import(QueryDslConfig.class)
@SpringBootTest
class NoticeRepositoryTest {

    @Autowired NoticeRepository noticeRepository;

    @Test
    void test(){
        //given
        for (int i=0;i<10;i++) {
            noticeRepository.save(Notice.builder().title("title" + i).content("content" + i).build());
        }

        //when
        NoticeSearchVO vo = NoticeSearchVO.builder().sortType(NoticeSearchVO.SortType.TITLE).order(Order.DESC).build();

        //then
        Page<NoticeResponseVO> list = noticeRepository.list(vo);

    }

}