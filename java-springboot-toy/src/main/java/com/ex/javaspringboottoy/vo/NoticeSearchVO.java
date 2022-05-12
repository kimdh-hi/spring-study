package com.ex.javaspringboottoy.vo;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

import static com.ex.javaspringboottoy.domain.QNotice.notice;

@Builder
@Data
public class NoticeSearchVO {

    private int page = 0;
    private int size = 10;
    private String searchText;
    private SearchType searchType;
    private SortType sortType;
    private Order order;

    public Pageable getPageable() {
        return QPageRequest.of(page, size, getQSort());
    }

    public QSort getQSort() {
        return new QSort(new OrderSpecifier<>(this.order, notice.title));
    }

    public enum SearchType {
        TITLE
    }

    public enum SortType {
        TITLE
    }
}
