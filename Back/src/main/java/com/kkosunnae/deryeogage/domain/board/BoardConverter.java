package com.kkosunnae.deryeogage.domain.board;

import com.kkosunnae.deryeogage.domain.user.UserEntity;
import com.kkosunnae.deryeogage.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kkosunnae.deryeogage.domain.board.BoardDto.BoardRequestDto;


import java.util.NoSuchElementException;

@Component
public class BoardConverter {
    private final UserRepository userRepository;

    @Autowired
    public BoardConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public BoardEntity toEntity(BoardRequestDto request){
//        UserEntity user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));
//        return BoardEntity.builder()
//                .user(user)
//                .regionCode(request.getRegionCode())
//                .lat(request.getLat())
//                .lon(request.getLon())
//                .dogTypeCode(request.getDogTypeCode())
//                .userNickname(request.getUserNickname())
//                .title(request.getTitle())
//                .friendly(request.getFriendly())
//                .activity(request.getActivity())
//                .dependency(request.getDependency())
//                .bark(request.getBark())
//                .hair(request.getHair())
//                .name(request.getName())
//                .gender(request.isGender())
//                .age(request.getAge())
//                .chipYn(request.isChipYn())
//                .health(request.getHealth())
//                .introduction(request.getIntroduction())
//                .createdDate(request.getCreatedDate())
//                .build();
//    }
}
