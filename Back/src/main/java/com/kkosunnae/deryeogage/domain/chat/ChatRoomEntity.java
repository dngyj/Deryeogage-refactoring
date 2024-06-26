package com.kkosunnae.deryeogage.domain.chat;

import com.kkosunnae.deryeogage.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "chat_room")
public class ChatRoomEntity extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id1")
    private UserEntity user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id2")
    private UserEntity user2;


    @Column(name="board_id")
    private Integer boardId;

    private String roomName;

    @Column(name="scheduled_date")
    private LocalDate scheduledDate;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessageEntity> chatMessageList;

    @Builder
    public ChatRoomEntity(Integer id, Integer boardId, UserEntity user1, UserEntity user2, String roomName, LocalDate scheduledDate) {
        this.id = id;
        this.boardId = boardId;
        this.user1 = user1;
        this.user2 = user2;
        this.roomName = roomName;
        this.scheduledDate = scheduledDate;
    }

    public LocalDate update(ChatRoomRequestDto chatRoomRequestDto) {
        this.scheduledDate = chatRoomRequestDto.getScheduledDate();
        System.out.println(this.scheduledDate);
        return this.scheduledDate;
    }

    public ChatRoomDto toDto(){
        return ChatRoomDto.builder()
                .id(this.id)
                .user1(this.user1.getId())
                .user2(this.user2.getId())
                .boardId(this.boardId)
                .scheduledDate(this.scheduledDate)
                .build();
    }

    public ChatRoomResponseDto toResponseDto(){
        return ChatRoomResponseDto.builder()
                .id(this.id)
                .boardId(this.boardId)
                .roomName(this.roomName)
                .scheduledDate(this.scheduledDate)
                .build();
    }
}