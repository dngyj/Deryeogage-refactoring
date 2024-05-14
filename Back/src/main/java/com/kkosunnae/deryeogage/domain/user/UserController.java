package com.kkosunnae.deryeogage.domain.user;

import com.kkosunnae.deryeogage.global.s3file.S3FileService;
import com.kkosunnae.deryeogage.global.util.JwtUtil;
import com.kkosunnae.deryeogage.global.util.Response;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Api
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final S3FileService s3FileService;

    @ResponseBody
    @GetMapping("/oauth")
    public ResponseEntity<?> oAuthInfo(@RequestParam("code") String code) throws UnsupportedEncodingException {
        if (code == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            log.info("code: " + code);
            String accessToken = userService.getAccessToken(code);
            Long userId = userService.regist(accessToken);


            // access 토큰 프론트에 반환
            Map<String, Object> userJwt = new HashMap<>();
            userJwt.put("accessToken", jwtUtil.createToken("claimUser", userId));
            userJwt.put("message", "loginClaimUser");
            return new ResponseEntity<>(userJwt, HttpStatus.OK);
        }
    }

    // 현재 로그인된 사용자 닉네임 반환
    @GetMapping
    public ResponseEntity<?> loginedUser(@RequestHeader("Authorization") String authorizationHeader) throws Exception {

        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        String nickname = userService.getUserNickname(userId);
        return new ResponseEntity<>(nickname,HttpStatus.OK);
    }

    /** 프로필 수정 페이지 **/
    //현재 로그인된 사용자의 프로필 사진 저장
    @PostMapping("/pic")
    public ResponseEntity<?> savePicture(@RequestHeader("Authorization") String authorizationHeader, @RequestPart("multipartFile") List<MultipartFile> multipartFile){
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        Map<String, List> nameList = s3FileService.uploadFile(multipartFile);
        String path = userService.savePicture(userId, nameList);

        return new ResponseEntity<>(path,HttpStatus.OK);
    }

    //현재 로그인된 사용자의 프로필 사진 조회
    @GetMapping("/pic")
    public ResponseEntity<?> getPicture(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        String path = userService.getPicture(userId);
        return new ResponseEntity<>(path,HttpStatus.OK);
    }

    //로그인한 사용자의 프로필 사진 수정
    @PutMapping("/pic")
    public ResponseEntity<?> updatePicture(@RequestHeader("Authorization") String authorizationHeader, @RequestPart("multipartFile") List<MultipartFile> multipartFile){
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);
        Map<String, List> nameList = s3FileService.uploadFile(multipartFile);

        String newPath = userService.updatePicture(userId, nameList);

        return new ResponseEntity<>(newPath,HttpStatus.OK);
    }

    /** 입양게시판 및 채팅 화면에서 타인 닉네임 클릭 시
     * 게시판 및 채팅 entity에 담겨 있는 사용자 id를 활용 **/
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("userId") Long userId){
        ProfileResponseDto profileResponse = userService.getProfile(userId);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }


    /** 마이페이지에 내 프로필 정보 반환 **/
    @GetMapping("/profile/mypage")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserId(jwtToken);

        ProfileResponseDto profileResponse = userService.getProfile(userId);
        return new ResponseEntity<>(profileResponse,HttpStatus.OK);
    }
}
