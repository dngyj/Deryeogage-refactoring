package com.kkosunnae.deryeogage.domain.cost;

import com.kkosunnae.deryeogage.global.util.JwtUtil;
import com.kkosunnae.deryeogage.global.util.Response;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@Slf4j
@RestController
@RequestMapping("/api/postcosts")
@RequiredArgsConstructor
public class PostCostController {
    //TODO: 프론트 데이터 입력값 수정
    private final JwtUtil jwtUtil;
    private final PostCostService postCostService;


    // 후 책임비 납부하기(입양 일정 저장하기 전에 납부)
    @PostMapping("/{boardId}")
    public ResponseEntity<?> payPostCost(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PostCostDto postCostDto, @PathVariable int boardId) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        postCostDto.setUserId(userId);
        postCostDto.setBoardId(boardId);
        int postCostId = postCostService.save(postCostDto);
        return new ResponseEntity<>(postCostId, HttpStatus.OK);
    }

    // 후 책임비 조회하기
    @GetMapping
    public ResponseEntity<?> getPostCosts(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        List<PostCostDto> myPostCosts = postCostService.getPostCosts(userId);
        return new ResponseEntity<>(myPostCosts, HttpStatus.OK);
    }

    // 후 책임비 수정하기(미션 완료에 따른 반환)
    @PutMapping("/missioncomplete")
    public ResponseEntity<?> normalReturn(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PostCostDto postCostDto) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        postCostService.normalReturn(userId, postCostDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
