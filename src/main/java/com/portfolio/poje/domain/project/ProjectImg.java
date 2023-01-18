package com.portfolio.poje.domain.project;

import com.portfolio.poje.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectImg extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "project_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String url;


    @Builder(builderMethodName = "enrollProjectImg")
    private ProjectImg(String url, Project project){
        this.url = url;
        this.project = project;

        project.getProjectImgs().add(this);
    }

}
