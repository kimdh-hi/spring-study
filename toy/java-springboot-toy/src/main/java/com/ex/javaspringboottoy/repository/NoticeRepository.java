package com.ex.javaspringboottoy.repository;

import com.ex.javaspringboottoy.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, String>, NoticeRepositoryCustom {
}
