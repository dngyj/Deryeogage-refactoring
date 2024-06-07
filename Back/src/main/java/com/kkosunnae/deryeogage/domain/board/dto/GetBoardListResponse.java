package com.kkosunnae.deryeogage.domain.board.dto;

import com.kkosunnae.deryeogage.domain.adopt.AdoptStatus;
import com.kkosunnae.deryeogage.domain.board.BoardEntity;
import com.kkosunnae.deryeogage.domain.user.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardListResponse {
    private int id;
    private String regionCode;
    private Double lat;
    private Double lon;
    private String userNickname;
    private String title;
    private String name;
    private byte age;
    private LocalDateTime createdDate;
    private String status;
    private String fileList;

    public BoardEntity toEntity(UserEntity user){
        return BoardEntity.builder()
                .user(user)
                .regionCode(regionCode)
                .lat(lat)
                .lon(lon)
                .userNickname(userNickname)
                .title(title)
                .name(name)
                .age(age)
                .createdDate(createdDate)
                .build();
    }
}