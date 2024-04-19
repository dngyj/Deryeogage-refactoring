package com.kkosunnae.deryeogage.domain.board;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    Optional<BoardEntity> findById(Integer boardId);
    Optional<List<BoardEntity>> findByUserId(Long userId);

    @Query("SELECT b FROM BoardEntity b WHERE b.user.id != ?1")
    List<BoardEntity> findAllByUserIdNot(Long userId);

    @Query("SELECT b.user.id FROM BoardEntity b WHERE b.id = :boardId")
    Optional<Long> findUserIdByBoardId(Integer boardId);

    @Query("SELECT b.user.id, b.title FROM BoardEntity b WHERE b.id = :boardId")
    Optional<Object[]> findUserIdAndTitleByBoardId(Integer boardId);

}
