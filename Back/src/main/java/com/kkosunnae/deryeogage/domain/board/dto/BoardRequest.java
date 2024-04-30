package com.kkosunnae.deryeogage.domain.board.dto;

import com.kkosunnae.deryeogage.domain.adopt.AdoptStatus;
import com.kkosunnae.deryeogage.domain.board.BoardEntity;
import com.kkosunnae.deryeogage.domain.user.UserEntity;
import com.kkosunnae.deryeogage.domain.user.UserRepository;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardRequest {
    private int id;
    private long userId;
    private String regionCode;
    private Double lat;
    private Double lon;
    private String dogTypeCode;
    private String userNickname;
    private String title;
    private char friendly;
    private char activity;
    private char dependency;
    private char bark;
    private char hair;
    private String name;
    private boolean gender;
    private byte age;
    private boolean chipYn;
    private String health;
    private String introduction;
    private LocalDateTime createdDate;
    private List fileList;
    private boolean isWriter;
    private boolean isAdopter;
    private AdoptStatus status;

    public BoardEntity toEntity( UserEntity user){
        return BoardEntity.builder()
                .user(user)
                .regionCode(regionCode)
                .lat(lat)
                .lon(lon)
                .dogTypeCode(dogTypeCode)
                .userNickname(userNickname)
                .title(title)
                .friendly(friendly)
                .activity(activity)
                .dependency(dependency)
                .bark(bark)
                .hair(hair)
                .name(name)
                .gender(gender)
                .age(age)
                .chipYn(chipYn)
                .health(health)
                .introduction(introduction)
                .createdDate(createdDate)
                .build();
    }
//    public BoardRequest setCreatedDate(LocalDateTime createdDate){
//        this.createdDate=createdDate;
//        return this;
//    }

}
