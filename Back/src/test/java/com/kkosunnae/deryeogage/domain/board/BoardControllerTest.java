package com.kkosunnae.deryeogage.domain.board;
import com.kkosunnae.deryeogage.domain.board.dto.BoardRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) //JUniit5와 Mockito 연동
class BoardControllerTest {

    @Mock //가짜 객체 생성
    private JwtUtil jwtUtil;

    @Mock
    private BoardService boardService;

    @Mock
    private S3FileService s3FileService;

    @InjectMocks //테스트 대상
    private BoardController boardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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

//    @Test
//    void selectBoard() {
//        // given
//
//        // when
//
//        // then
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