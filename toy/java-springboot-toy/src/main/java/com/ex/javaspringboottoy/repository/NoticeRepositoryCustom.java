package com.ex.javaspringboottoy.repository;

import com.ex.javaspringboottoy.vo.NoticeResponseVO;
import com.ex.javaspringboottoy.vo.NoticeSearchVO;
import org.springframework.data.domain.Page;

public interface NoticeRepositoryCustom {

    Page<NoticeResponseVO> list(NoticeSearchVO searchVO);
}
