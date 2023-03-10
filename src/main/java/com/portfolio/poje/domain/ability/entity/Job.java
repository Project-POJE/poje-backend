package com.portfolio.poje.domain.ability.entity;

import com.portfolio.poje.domain.portfolio.entity.Portfolio;
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
public class Job {

    @Id @GeneratedValue
    @Column(name = "job_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Portfolio> portfolioList = new ArrayList<>();


    @Builder(builderMethodName = "enrollJob")
    private Job(String name){
        this.name = name;
    }

    public void updateJob(String name){
        if (name != null)
            this.name = name;
    }

}
