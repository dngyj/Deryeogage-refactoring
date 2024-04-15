package com.kkosunnae.deryeogage.domain.board;

import com.kkosunnae.deryeogage.domain.adopt.AdoptEntity;
import com.kkosunnae.deryeogage.domain.adopt.AdoptRepository;
import com.kkosunnae.deryeogage.global.s3file.S3FileService;
import com.kkosunnae.deryeogage.global.util.JwtUtil;
import com.kkosunnae.deryeogage.global.util.Response;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Api
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final JwtUtil jwtUtil;

    private final BoardService boardService;

    private final S3FileService s3FileService;

    private final BoardFileRepository boardFileRepository;

    private final AdoptRepository adoptRepository;

    // 글 작성 // Swagger 하려면 @requestBody 삭제 필요
    // 한 가지 주의할 점은, @RequestBody와 @RequestPart를
    // 동시에 사용하려면 요청의 Content-Type이 multipart/form-data이어야 합니다.
    @PostMapping
    public ResponseEntity<?> saveBoard(@RequestHeader("Authorization") String authorizationHeader, BoardDto boardDto, @RequestPart("multipartFile") List<MultipartFile> multipartFile) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("saveBoardController");
        // 실행 코드
        String jwtToken = authorizationHeader.substring(7);
        log.info("헤더에서 가져온 토큰 정보: " + jwtToken);
        Long userId = jwtUtil.getUserId(jwtToken);
        boardDto.setUserId(userId);
        log.info("userId :", boardDto.getUserId());

        Integer boardId = boardService.save(boardDto);

        // 원본 파일명과 S3에 저장된 파일명이 담긴 Map
        Map<String, List> nameList = s3FileService.uploadFile(multipartFile);

        // DB에 파일이름 저장
        boardService.saveBoardFile(boardId, nameList);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        log.info("코드 실행 시간 (s): " + stopWatch.getTotalTimeSeconds());
        return new ResponseEntity<>(boardId, HttpStatus.OK);
    }


    //글 상세조회 + 작성자 여부 boolean으로 반영
    @GetMapping("/each/{boardId}")
    public ResponseEntity<?> selectBoard(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable int boardId) {
        BoardDto thisBoard = boardService.getBoard(boardId);

        // 요청한 사용자가 로그인 되어 있는 경우
        if (authorizationHeader != null) {
            String jwtToken = authorizationHeader.substring(7);
            Long requestUser = jwtUtil.getUserId(jwtToken);
            log.info("userid : "+thisBoard.getUserId()+" requestuserid : "+requestUser);
            // 작성자 여부 파악하여 DTO에 담기
            if (thisBoard.getUserId() == requestUser) {
                thisBoard.setWriter(true);
            } else {
                thisBoard.setWriter(false);
            }

            Optional<AdoptEntity> adoptEntity = adoptRepository.findByBoardId(boardId);
            if(!adoptEntity.isEmpty()){
                thisBoard.setAdopter(adoptEntity.get().getToUser().getId().equals(requestUser));
                thisBoard.setStatus(adoptEntity.get().getStatus());
                log.info("adapter : "+thisBoard.isAdopter());
            }
        } else {// 로그인하지 않은 경우
            thisBoard.setWriter(false);
        }

        Map<String, String> uploadedFiles = boardService.getBoardFiles(boardId);
        List<Object> boardSet = new ArrayList<>();
        boardSet.add(thisBoard);
        boardSet.add(uploadedFiles);
        return new ResponseEntity<>(boardSet,HttpStatus.OK);
    }

    //글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@RequestHeader("Authorization") String authorizationHeader,
                                        BoardDto boardDto,
                                        @PathVariable int boardId,
                                        @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFile,
                                        @RequestParam(value = "removedImages", required = false) List<String> removedImages
    ) {
        // ... 기타 코드는 유지 ...

        String jwtToken = authorizationHeader.substring(7);
        Long requestUserId = jwtUtil.getUserId(jwtToken);

        BoardDto thisBoard = boardService.getBoard(boardId);

        log.info("수정: 게시글 유저 정보 : " + thisBoard.getUserId());
        log.info("요청 유저 정보 : " + requestUserId);

        if (thisBoard.getUserId() != requestUserId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boardDto.setUserId(requestUserId);

        boardService.update(boardId, boardDto);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            Map<String, List> nameList = s3FileService.uploadFile(multipartFile);
            boardService.saveBoardFile(boardId, nameList);
        }

        if(!removedImages.isEmpty()){
            for(String path : removedImages){
                path=path.replaceAll("[\"\\[\\]]", "");
                log.info("remove : "+path);
                boardFileRepository.deleteByBoard_IdAndPath(boardId, path );
                s3FileService.deleteFileByUrl(path);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int boardId) {

        String jwtToken = authorizationHeader.substring(7);
        Long requestUserId = jwtUtil.getUserId(jwtToken);

        BoardDto thisBoard = boardService.getBoard(boardId);
        if (thisBoard.getUserId() != requestUserId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 해당 게시글이 가진 모든 파일을 리스트로 가져와서 삭제 수행
        s3FileService.deleteFile(boardService.getBoardFiles(boardId));
        // 이후에 게시글 삭제
        boardService.deleteById(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> findBoards() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("findAllBoardController");

        List<BoardDto> boardSetList = boardService.findAll();

        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        log.info("코드 실행 시간 (s): " + stopWatch.getTotalTimeSeconds());
        return new ResponseEntity<>(boardSetList,HttpStatus.OK);
//    Response.success(boardSetList);
    }

    //내가 쓴 글 목록 조회(마이페이지)
    @GetMapping("/list/user")
    public ResponseEntity<?> findMyBoards(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        List<BoardDto> boardSetMap = boardService.findMyBoards(userId);
        return new ResponseEntity<>(boardSetMap,HttpStatus.OK);
    }

    //글 목록 조회 추천
    @GetMapping("/recommendation")
    public ResponseEntity<?> findRecommendedBoards(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        List<BoardDto> boardList = boardService.findRecommendation(userId);
        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }


    //분양글 찜
    @PostMapping("/{boardId}/like")
    public ResponseEntity<?> boardLike(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int boardId, JjimDto jjimDto) {

        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        jjimDto.setUserId(userId);
        jjimDto.setBoardId(boardId);

        boardService.like(jjimDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //분양글 찜 취소
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<?> boardUnlike(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int boardId) {
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        boardService.unlike(userId, boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //내가 찜한 목록 조회
    @GetMapping("/like")
    public ResponseEntity<?> getboardLike(@RequestHeader("Authorization") String authorizationHeader) {

        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        List<JjimDto> jjimDtoList = boardService.myLikes(userId);

        return new ResponseEntity<>(jjimDtoList,HttpStatus.OK);
    }
}
