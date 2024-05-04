package com.kkosunnae.deryeogage.domain.board;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkosunnae.deryeogage.domain.board.dto.BoardRequest;
import com.kkosunnae.deryeogage.domain.board.dto.BoardResponse;
import com.kkosunnae.deryeogage.global.s3file.S3FileService;
import com.kkosunnae.deryeogage.global.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import javax.transaction.Transactional;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) //JUniit5와 Mockito 연동
class BoardControllerTest {

    private MockMvc mockMvc;
    @Mock //가짜 객체 생성
    private JwtUtil jwtUtil;
    @Mock
    private BoardService boardService;
    @Mock
    private S3FileService s3FileService;
    @InjectMocks //테스트 대상
    private BoardController boardController;
//    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @DisplayName("게시글 저장 테스트")
    @Test
    @Transactional
    void saveBoard() {
        // given
        String authorizationHeader = "Bearer token";
        BoardRequest request = new BoardRequest();
        List<MultipartFile> multipartFile = Collections.singletonList(mock(MultipartFile.class));
        Long userId = 1L;
        Integer boardId = 1;
        Map<String, List> nameList = Collections.singletonMap("filename", Collections.singletonList("s3filename"));


        when(jwtUtil.getUserId("token")).thenReturn(userId);
        when(boardService.save(request, userId)).thenReturn(boardId);
        when(s3FileService.uploadFile(multipartFile)).thenReturn(nameList);

        // when
        ResponseEntity<?> responseEntity = boardController.saveBoard(authorizationHeader, request, multipartFile);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(boardId);

        verify(jwtUtil, times(1)).getUserId("token");
        verify(boardService, times(1)).save(request, userId);
        verify(s3FileService, times(1)).uploadFile(multipartFile);
        verify(boardService, times(1)).saveBoardFile(boardId, nameList);
    }

//    @DisplayName("게시글 상세조회 테스트")
//    @Test
//    @Transactional
//    void selectBoard() throws Exception {
//        // given
//        int boardId = 1;
//        String jwtToken = "Bearer validToken1";
//        Long userId = 1L;
//
//        when(jwtUtil.getUserId(anyString())).thenReturn(userId);
//
//        BoardResponse boardResponse = new BoardResponse();
//        boardResponse.setId(boardId);
//        Map<String, String> uploadedFiles = new HashMap<>();
//        uploadedFiles.put("file1", "url1");
//
//        when(boardService.getBoard(eq(boardId), eq(userId))).thenReturn(boardResponse);
//        when(boardService.getBoardFiles(eq(boardId))).thenReturn(uploadedFiles);
//
//        // when
//        // then
//        mockMvc.perform(get("/api/boards/each/{boardId}", boardId)
//                        .header("Authorization", jwtToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(result -> {
//                    String content = result.getResponse().getContentAsString();
//                    assertThat(content).contains("url1");
//                });
//
//        verify(jwtUtil).getUserId("validToken1");
//        verify(boardService).getBoard(boardId, userId);
//        verify(boardService).getBoardFiles(boardId);
//    }

//
//    @Test
//    void updateBoard() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void deleteBoard() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void findBoards() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void findMyBoards() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void findRecommendedBoards() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void boardLike() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void boardUnlike() {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    void getboardLike() {
//        // given
//
//        // when
//
//        // then
//    }
}