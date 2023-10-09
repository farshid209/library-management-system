package org.example.entity;

import lombok.Data;

@Data
public class Reserve {
    private Long id;
    private Long bookId;
    private Long memberId;
    private String reserveDate;
    private boolean isReturned;
}
