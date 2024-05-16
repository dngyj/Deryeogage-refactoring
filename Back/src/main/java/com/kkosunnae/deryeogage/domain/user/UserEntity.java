package com.kkosunnae.deryeogage.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kkosunnae.deryeogage.domain.adopt.AdoptEntity;
import com.kkosunnae.deryeogage.domain.board.BoardEntity;
import com.kkosunnae.deryeogage.domain.board.JjimEntity;
import com.kkosunnae.deryeogage.domain.chat.ChatMessageEntity;
import com.kkosunnae.deryeogage.domain.chat.ChatRoomEntity;
import com.kkosunnae.deryeogage.domain.cost.PostCostEntity;
import com.kkosunnae.deryeogage.domain.cost.PreCostEntity;
import com.kkosunnae.deryeogage.domain.mission.MissionEntity;
import com.kkosunnae.deryeogage.domain.pretest.PreTestEntity;
import com.kkosunnae.deryeogage.domain.review.ReviewCommentEntity;
import com.kkosunnae.deryeogage.domain.review.ReviewEntity;
import com.kkosunnae.deryeogage.domain.simulation.SimulationEntity;
import com.kkosunnae.deryeogage.domain.survey.SurveyEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public class UserEntity implements Serializable {
//    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(length = 20)
    private String nickname;

    @Column(name="image_url", length = 100)
    private String imageUrl;

    @Column(name="age_range")
    private String ageRange;

    @Column(name="created_date")
    private LocalDateTime createdDate;
    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private SurveyEntity survey;
    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private PreTestEntity preTest;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ReviewCommentEntity> reviewComments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<SimulationEntity> simulations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user1")
    private List<ChatRoomEntity> user1ChatRooms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user2")
    private List<ChatRoomEntity> user2ChatRooms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ChatMessageEntity> chatMessages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<JjimEntity> jjims = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BoardEntity> boards = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "fromUser")
    private List<AdoptEntity> fromUserAdopts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "toUser")
    private List<AdoptEntity> toUserAdopts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PostCostEntity> postCosts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PreCostEntity> preCosts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<MissionEntity> missions = new ArrayList<>();


    @Builder
    protected UserEntity(Long id, String nickname, String ageRange, String imageUrl, LocalDateTime createdDate) {
        this.id = id;
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
    }

    public UserDto toDto(){
        return UserDto.builder()
                .id(this.id)
                .nickname(this.nickname)
                .ageRange(this.ageRange)
                .imageUrl(this.imageUrl)
                .createdDate(this.createdDate)
                .build();
    }
    public void update(String path){
        this.imageUrl = path;
    }

}