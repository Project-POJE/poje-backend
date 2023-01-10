package com.portfolio.poje.domain.member;

import com.portfolio.poje.domain.BaseEntity;
import com.portfolio.poje.domain.ability.License;
import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.AccessLevel;
import lombok.Builder;
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


    @Builder(builderMethodName = "createMember")
    private Member(String loginId, String password, String nickName, String email,
                  String phoneNum, String gender, String academic, String dept,
                  String birth, String blogLink, String profileImg, String intro, RoleType role){
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.academic = academic;
        this.dept = dept;
        this.birth = birth;
        this.blogLink = blogLink;
        this.profileImg = profileImg;
        this.intro = intro;
        this.role = role;
    }

}
