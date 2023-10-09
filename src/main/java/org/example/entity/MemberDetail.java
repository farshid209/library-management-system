package org.example.entity;

import lombok.Data;

@Data
public class MemberDetail {
    private Long id;
    private Long memberId;
    private String province;
    private String city;
    private String address;
    private String phoneNumber;
    private String mobileNumber;
    private String membershipDate;
}
