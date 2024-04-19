package com.kkosunnae.deryeogage.domain.chat;

import com.kkosunnae.deryeogage.domain.board.BoardRepository;
import com.kkosunnae.deryeogage.domain.user.UserEntity;
import com.kkosunnae.deryeogage.domain.user.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDto {
    private String roomName;

    private Integer boardId;

    private Long user1;

    private Long user2;

    private LocalDate scheduledDate;

    @Builder
    public ChatRoomRequestDto(Long user1, Long user2, Integer boardId, String roomName) {
        this.user1 = user1;
        this.user2 = user2;
        this.boardId = boardId;
        this.roomName = roomName;
    }


    public ChatRoomEntity toEntity(UserEntity user1, UserEntity user2) {
        return ChatRoomEntity.builder()
                .boardId(this.boardId)
                .user1(user1)
                .user2(user2)
                .roomName(this.roomName)
                .build();
    }

}