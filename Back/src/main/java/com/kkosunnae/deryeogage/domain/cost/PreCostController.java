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
@RequestMapping("/api/precosts")
@RequiredArgsConstructor
public class PreCostController {
    //TODO: 프론트 데이터 입력값 수정
    private final JwtUtil jwtUtil;
    private final PreCostService preCostService;

    // 선 책임비 납부하기(게시글 작성 시 납부)
    @PostMapping("/{boardId}")
    public ResponseEntity<?> payPreCost(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PreCostDto preCostDto, @PathVariable int boardId) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        preCostDto.setUserId(userId);
        preCostDto.setBoardId(boardId);
        int preCostId = preCostService.save(preCostDto);
        return new ResponseEntity<>(preCostId, HttpStatus.OK);
    }

    // 선 책임비 조회하기
    @GetMapping
    public ResponseEntity<?> getPreCosts(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        List<PreCostDto> myPreCosts = preCostService.getPreCosts(userId);
        return new ResponseEntity<>(myPreCosts, HttpStatus.OK);
    }

    // 입양 확정 후 선 책임비 수정하기(반환)
    @PutMapping("/confirm")
    public ResponseEntity<?> normalReturn(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PreCostDto preCostDto) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        preCostService.normalReturn(userId, preCostDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시글 삭제 버튼 클릭 후 returnYn이 null이면 선 책임비 수정하기(반환) -> 게시글 삭제까지
    @PutMapping
    public ResponseEntity<?> abnormalReturn(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PreCostDto preCostDto) { //보드ID 유일함
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        preCostService.abnormalReturn(userId, preCostDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
