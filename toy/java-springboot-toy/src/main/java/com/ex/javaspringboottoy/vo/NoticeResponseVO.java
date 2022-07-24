package com.ex.javaspringboottoy.vo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class NoticeResponseVO {

    private String id;
    private String title;

    @QueryProjection
    public NoticeResponseVO(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
