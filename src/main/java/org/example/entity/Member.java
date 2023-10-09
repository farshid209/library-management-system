package org.example.entity;

import lombok.Data;

@Data
public class Member {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String birthDate;
}
