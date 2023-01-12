package com.portfolio.poje.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;

}
