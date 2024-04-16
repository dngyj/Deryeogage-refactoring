package com.kkosunnae.deryeogage.domain.board;
import com.kkosunnae.deryeogage.domain.board.*;
import com.kkosunnae.deryeogage.global.s3file.S3FileService;
import com.kkosunnae.deryeogage.global.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BoardService boardService;

    @Mock
    private S3FileService s3FileService;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Transactional
    void saveBoard() {
        // given
        String authorizationHeader = "Bearer token";
        BoardDto boardDto = new BoardDto();
        List<MultipartFile> multipartFile = Collections.singletonList(mock(MultipartFile.class));
        Long userId = 1L;
        Integer boardId = 1;
        Map<String, List> nameList = Collections.singletonMap("filename", Collections.singletonList("s3filename"));

        // when
        when(jwtUtil.getUserId("token")).thenReturn(userId);
        when(boardService.save(boardDto)).thenReturn(boardId);
        when(s3FileService.uploadFile(multipartFile)).thenReturn(nameList);

        ResponseEntity<?> responseEntity = boardController.saveBoard(authorizationHeader, boardDto, multipartFile);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(boardId, responseEntity.getBody());

        verify(jwtUtil, times(1)).getUserId("token");
        verify(boardService, times(1)).save(boardDto);
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