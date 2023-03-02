package com.portfolio.poje.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {

    private String emailAddress;
    private String title;
    private String message;
}
