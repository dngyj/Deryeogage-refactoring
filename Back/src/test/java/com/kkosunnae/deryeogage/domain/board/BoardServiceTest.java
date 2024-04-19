package com.kkosunnae.deryeogage.domain.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("게시글 작성자 ID get 테스트")
    @Test
    public void testGetBoardWriterId() {
        // Given
        Integer boardId = 1;
        Long expectedWriterId = 123L;
        when(boardRepository.findUserIdByBoardId(boardId)).thenReturn(Optional.of(expectedWriterId));

        // When
        Long actualWriterId = boardService.getBoardWriterId(boardId);

        // Then
        assertEquals(expectedWriterId, actualWriterId);
    }

    @DisplayName("게시글 없을 때 작성자 ID get 테스트")
    @Test
    public void testGetBoardWriterId_WhenBoardNotExists() {
        // Given
        Integer boardId = 1;
        when(boardRepository.findUserIdByBoardId(boardId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            boardService.getBoardWriterId(boardId);
        });
    }

    @DisplayName("게시글 작성자 ID,Title get 테스트")
    @Test
    public void testGetBoardWriterAndTitle_WhenBoardExists() {
        // Given
        Integer boardId = 1;
        Long expectedWriterId = 123L;
        String expectedTitle = "테스트 제목";
        Object[] expectedResults = new Object[]{expectedWriterId, expectedTitle};
        when(boardRepository.findUserIdAndTitleByBoardId(boardId)).thenReturn(Optional.of(expectedResults));

        // When
        Object[] actualResults = boardService.getBoardWriterAndTitle(boardId);

        // Then
        assertArrayEquals(expectedResults, actualResults);
    }

    @Test
    void getBoardWriterId() {
    }

    @Test
    void getBoardWriterAndTitle() {
    }
}