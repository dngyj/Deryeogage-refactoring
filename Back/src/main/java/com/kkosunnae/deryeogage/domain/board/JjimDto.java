package com.kkosunnae.deryeogage.domain.board;

import com.kkosunnae.deryeogage.domain.user.UserEntity;
import com.kkosunnae.deryeogage.domain.user.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.NoSuchElementException;

@Getter @Setter
@NoArgsConstructor
public class JjimDto {
    private int id;
    private int boardId;
    private long userId;

    @Builder
    public JjimDto(int id, int boardId, long userId) {
        this.id = id;
        this.boardId = boardId;
        this.userId = userId;
    }
    public JjimEntity toEntity(BoardEntity board, UserEntity user){
        return JjimEntity.builder()
                .board(board)
                .user(user)
                .build();
    }
}
