package com.portfolio.poje.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String password;

    private String nickName;

    private String email;

    private String phoneNum;

    private String gender;

    private String academic;

    private String dept;

    private String birth;

    private String blogLink;

    private String profileImg;

    private String intro;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "owner")
    private List<License> licenseList = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Portfolio> portfolioList = new ArrayList<>();

}
