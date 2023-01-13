package com.portfolio.poje.domain.member;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken { // 추후 redis 에 저장하도록 변경

    @Id @GeneratedValue
    @Column(name = "refresh_token_id")
    private Long id;

    private String loginId;

    private String refreshToken;


    public static RefreshToken enrollRefreshToken(String loginId, String refreshToken){
        RefreshToken token = new RefreshToken();
        token.loginId = loginId;
        token.refreshToken = refreshToken;

        return token;
    }

    public RefreshToken updateToken(String token){
        this.refreshToken = token;
        return this;
    }

}
