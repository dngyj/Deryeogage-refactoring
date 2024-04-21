package com.kkosunnae.deryeogage.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private Integer id;
    private String roomName;
    private String createdDate;
    private String updatedDate;

    private Boolean schedule;
    private LocalDate scheduledDate;

    private String myNickName;
    private String yourNickName;

    private String yourImg;

    private Integer boardId;

//    public ChatRoomResponseDto() {
//        this.id = chatRoom.getId();
//        this.roomName = chatRoom.getRoomName();
//        this.createdDate = chatRoom.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
//        this.updatedDate = chatRoom.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
//        this.schedule = false;
//    }
}