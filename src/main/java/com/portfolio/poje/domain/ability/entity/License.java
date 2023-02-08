package com.portfolio.poje.domain.ability.entity;

import com.portfolio.poje.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class License {

    @Id @GeneratedValue
    @Column(name = "license_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;


    @Builder(builderMethodName = "enrollLicense")
    private License(String name, Member owner){
        this.name = name;
        this.owner = owner;

        owner.getLicenseList().add(this);
    }

    public void updateInfo(String name){
        this.name = name;
    }

}
