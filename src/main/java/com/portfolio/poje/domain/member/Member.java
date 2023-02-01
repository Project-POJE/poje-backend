package com.portfolio.poje.domain.member;

import com.portfolio.poje.common.BaseEntity;
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

    private String profileImg;

    private String gitHubLink;

    private String blogLink;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "owner")
    private List<License> licenseList = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Portfolio> portfolioList = new ArrayList<>();


    @Builder(builderMethodName = "createMember")
    private Member(Long id, String loginId, String password, String nickName, String email,
                  String phoneNum, String gender, String birth, String profileImg, RoleType role){
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.birth = birth;
        this.profileImg = profileImg;
        this.role = role;
    }


    // 좋은 방법이 떠오르면 변경
    public void updateInfo(String nickName, String email, String phoneNum, String gender,
                           String academic, String dept, String birth, String profileImg,
                           String gitHubLink, String blogLink){
        if (nickName != null) this.nickName = nickName;
        if (email != null) this.email = email;
        if (phoneNum != null) this.phoneNum = phoneNum;
        if (gender != null) this.gender = gender;
        if (academic != null) this.academic = academic;
        if (dept != null) this.dept = dept;
        if (birth != null) this.birth = birth;
        if (profileImg != null) this.profileImg = profileImg;
        if (gitHubLink != null) this.gitHubLink = gitHubLink;
        if (blogLink != null) this.blogLink = blogLink;
    }

}
