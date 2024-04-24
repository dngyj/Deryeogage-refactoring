package com.kkosunnae.deryeogage.domain.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkosunnae.deryeogage.domain.board.BoardService;
import com.kkosunnae.deryeogage.domain.user.UserService;
import com.kkosunnae.deryeogage.global.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ChatRoomService chatRoomService;

    @MockBean
    private ChatMessageService chatMessageService;

    @MockBean
    private UserService userService;

    @MockBean
    private BoardService boardService;

    @InjectMocks
    private ChatController chatController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    @Test
    @DisplayName("채팅방 생성")
    @WithMockUser
    void createRoomTest() throws Exception {
        Integer boardId = 1;
        String jwtToken = "Bearer sometoken";
        Long userId1 = 10L;
        Long userId2 = 20L;
        String boardName = "Board Sample";

        when(jwtUtil.getUserId(anyString())).thenReturn(userId2);
        when(boardService.getBoardWriterAndTitle(boardId)).thenReturn(new Object[]{userId1, boardName});
//TODO: 형식 처리 후 수정
//        ChatRoomResponseDto chatRoomResponseDto = chatRoomEntity.toResponseDto();
//        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
        when(chatRoomService.findChatRoomByUsersAndBoardId(userId1, userId2, boardId)).thenReturn(null);
//        when(chatRoomService.save(userId1, userId2, new ChatRoomRequestDto(userId1, userId2, boardId, boardName))).thenReturn(chatRoomResponseDto);

        mockMvc.perform(post("/api/chat/room/{boardId}", boardId)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(chatRoomService).save(userId1, userId2, new ChatRoomRequestDto(userId1, userId2, boardId, boardName));
    }

    @Test
    @DisplayName("채팅방 정보 조회")
    @WithMockUser
    void getRoomInfoTest() throws Exception {
        Integer roomId = 1;
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        when(chatRoomService.getRoomInfo(roomId)).thenReturn(chatRoomDto);

        mockMvc.perform(get("/api/chat/room/info/{id}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(chatRoomService).getRoomInfo(roomId);
    }
}