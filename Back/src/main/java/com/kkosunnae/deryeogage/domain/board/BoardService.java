package com.kkosunnae.deryeogage.domain.board;

import com.kkosunnae.deryeogage.domain.adopt.AdoptEntity;
import com.kkosunnae.deryeogage.domain.adopt.AdoptRepository;
import com.kkosunnae.deryeogage.domain.adopt.AdoptStatus;
import com.kkosunnae.deryeogage.domain.board.dto.BoardRequest;
import com.kkosunnae.deryeogage.domain.board.dto.BoardResponse;
import com.kkosunnae.deryeogage.domain.board.dto.GetBoardListResponse;
import com.kkosunnae.deryeogage.domain.survey.SurveyEntity;
import com.kkosunnae.deryeogage.domain.survey.SurveyRepository;
import com.kkosunnae.deryeogage.domain.user.UserCacheService;
import com.kkosunnae.deryeogage.domain.user.UserEntity;
import com.kkosunnae.deryeogage.domain.user.UserRepository;
import com.kkosunnae.deryeogage.domain.user.UserService;
import com.kkosunnae.deryeogage.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JjimRepository jjimRepository;
    private final BoardFileRepository boardFileRepository;
    private final SurveyRepository surveyRepository;
    private final AdoptRepository adoptRepository;
    private final UserService userService;
    private final UserCacheService userCacheService;

    //게시글 작성
    @Transactional
    public int save(BoardRequest request, Long userId) {
        UserEntity user = userCacheService.findUsersById(userId);

        request.setCreatedDate(LocalDateTime.now());
        request.setUserId(userId);
        request.setUserNickname(user.getNickname());

        BoardEntity board = boardRepository.save(request.toEntity(user));
        return board.getId();
    }


    //게시글 수정
    @Transactional
    public int update(Integer id, BoardRequest request, Long requestUserId) {
        BoardEntity board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저의 게시글이 없습니다. id : " + id));

        Optional<UserEntity> user = userRepository.findById(request.getUserId());
        if (user.isPresent()) {
            request.setUserId(requestUserId);
            request.setUserNickname(user.get().getNickname());
            request.setCreatedDate(LocalDateTime.now());
            board.update(request);
            return board.getId();
        } else {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다. user id: " + request.getUserId());
        }
    }

    //게시글 삭제
    @Transactional
    public void deleteById(Integer id) {
        boardRepository.deleteById(id);
    }

    //게시글 상세 조회
    @Transactional(readOnly = true)
    public BoardResponse getBoard(Integer boardId, Long userId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. boardId: "+ boardId));

        BoardResponse response = board.from();
        Optional<AdoptEntity> adopt = adoptRepository.findByBoardId(boardId);

        log.info("userID: "+userId+" response.getUserID: " +response.getUserId());
        if (userId != null && response.getUserId()==userId) {
            response.setWriter(true);
            if (adopt.isPresent()) {
                response.setAdopter(adopt.get().getToUser().getId().equals(userId));
                response.setStatus(adopt.get().getStatus());
            }
        } else {
            response.setWriter(false);
        }
        // 입양 정보에 존재 여부 및 depart/arrive 확인하여 게시글별 status 반환
        AdoptStatus status = adopt.map(AdoptEntity::getStatus).orElse(null);
        response.setStatus(status);

        return response;
    }

    @Transactional(readOnly = true)
    // 내가 작성한 게시글 조회
    public List<BoardResponse> findMyBoards(Long userId) {
        List<BoardResponse> boardSetList = new ArrayList<>();
        List<BoardEntity> myBoardLists = boardRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 작성한 게시물 목록이 존재하지 않습니다. userId" + userId));

        for (BoardEntity boardEntity : myBoardLists) {

            // 하나의 게시글 정보를 dto로 변환
            BoardResponse thisBoard = boardEntity.from();
            // 하나의 게시글 ID를 가져오고
            Integer thisBoardId = boardEntity.getId();
            // 특정 게시글에 업로드된 파일을 꺼내기
            List<String> uploadedFiles = this.getBoardFileUrls(thisBoardId);
            // Dto에 담기
            thisBoard.setFileList(uploadedFiles);

            boardSetList.add(thisBoard);
        }
        return boardSetList;
    }

    //전체 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<GetBoardListResponse> findAll() {
        List<GetBoardListResponse> boardSetList = boardRepository.findAllBoardList();
        Collections.shuffle(boardSetList);
        return boardSetList;
    }


    @Transactional(readOnly = true)
    public Page<GetBoardListResponse> findAllPage(Pageable pageable) {
        Page<GetBoardListResponse> boardSetPage = boardRepository.findAllBoardPageList(pageable);
        return boardSetPage;
    }

    //전체 게시글 목록 조회 (추천)
    @Transactional(readOnly = true)
    public List<BoardResponse> findRecommendation(Long userId) {
        Optional<SurveyEntity> survey = surveyRepository.findByUserId(userId);
        String order = survey.get().getRanking();
        int[] userPreferences = {
                Character.getNumericValue(survey.get().getFriendly()),
                Character.getNumericValue(survey.get().getActivity()),
                Character.getNumericValue(survey.get().getDependency()),
                Character.getNumericValue(survey.get().getBark()),
                Character.getNumericValue(survey.get().getHair())
        };

        List<BoardEntity> boardEntityList = boardRepository.findAllByUserIdNot(userId);
        List<BoardResponse> responseList = new ArrayList<>();
        Map<Integer, int[]> boardMap = new HashMap<>();
        for (BoardEntity boardEntity : boardEntityList) {
            Integer boardId = boardEntity.getId();
            // 입양 정보에 존재 여부 및 depart/arrive 확인하여 게시글별 status 반환
            Optional<AdoptEntity> entity = adoptRepository.findByBoardId(boardId);
            // 초기화

            if (entity.isPresent()) {
                continue;
            }
            int[] five = {Character.getNumericValue(boardEntity.getFriendly()),
                    Character.getNumericValue(boardEntity.getActivity()),
                    Character.getNumericValue(boardEntity.getDependency()),
                    Character.getNumericValue(boardEntity.getBark()),
                    Character.getNumericValue(boardEntity.getHair())};
            boardMap.put(boardEntity.getId(), five);
        }

        EuclideanSimilarityRecommendation euclideanSimilarityRecommendation = new EuclideanSimilarityRecommendation();
        List<Integer> result = euclideanSimilarityRecommendation.recommendDogs(userPreferences, boardMap, order);
        for (Integer index : result) {
            BoardResponse response = boardRepository.findById(index).get().from();
            List<String> uploadedFiles = this.getBoardFileUrls(response.getId());
            response.setFileList(uploadedFiles);
            responseList.add(response);
        }
        return responseList;
    }

    //게시글 찜
    @Transactional
    public int like(JjimDto jjimDto) {
        BoardEntity board = boardRepository.findById(jjimDto.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다."));
        UserEntity user =userCacheService.findUsersById(jjimDto.getUserId());

        if (!jjimRepository.existsByUserIdAndBoardId(jjimDto.getUserId(), jjimDto.getBoardId())) {

            JjimEntity jjim = jjimRepository.save(jjimDto.toEntity(board, user));
            return jjim.getId();
        }//만약 이미 찜한 게시글이라면
        else throw new IllegalArgumentException("이미 찜한 게시판입니다.");
    }

    //게시글 찜 취소
    @Transactional
    public void unlike(Long userId, Integer boardId) {
        jjimRepository.deleteByUserIdAndBoardId(userId, boardId);
    }


    //내가 찜한 목록 조회
    public List<JjimDto> myLikes(Long userId) {
        List<JjimEntity> jjimEntityList = null;
        try {
            jjimEntityList = jjimRepository.findByUserId(userId)
                    .orElseThrow(() -> new NoSuchElementException("해당 사용자가 찜한 분양글이 없습니다. userId: " + userId));
        } catch (NoSuchElementException e) {
            return new ArrayList<>(); // 찜한 분양글이 없는 경우 빈 리스트 반환
        }

        List<JjimDto> jjimDtoList = new ArrayList<>();

        for (JjimEntity jjimEntity : jjimEntityList) {
            JjimDto jjimDto = jjimEntity.toDto();
            jjimDtoList.add(jjimDto);
        }

        return jjimDtoList;
    }

    //게시글에 업로드한 파일 저장
    public void saveBoardFile(Integer boardId, Map<String, List> nameList) {

        LocalDateTime uploadTime = LocalDateTime.now();

        List<String> originalNames = nameList.get("original");
        List<String> savedNames = nameList.get("saved");
        List<String> savedPaths = nameList.get("path");

        // Dto에 담기: IntStream을 사용해서 index 기반으로 처리
        IntStream.range(0, originalNames.size())
                .forEach(i -> {
                    BoardFileDto boardFileDto = new BoardFileDto();

                    boardFileDto.setBoardId(boardId);
                    boardFileDto.setOriginalName(originalNames.get(i));
                    boardFileDto.setSavedName(savedNames.get(i));
                    String fileType = savedNames.get(i).split("\\.")[1];

                    if (fileType.equals("mp4")) { //동영상
                        boardFileDto.setType(true);
                    } else { //이미지
                        boardFileDto.setType(false);
                    }
                    boardFileDto.setPath(savedPaths.get(i));
                    boardFileDto.setCreatedDate(uploadTime);

                    try {
                        // 그 다음 Entity로 변환하여 DB에 저장
                        boardFileRepository.save(boardFileDto.toEntity(boardRepository));
                        log.info("File [{}] was successfully saved to the database.", savedNames.get(i));
                    } catch (Exception e) {
                        log.error("Error saving file [{}] to the database. Error: {}", savedNames.get(i), e.getMessage());
                    }
                });
    }


    //게시글에 저장된 파일 조회
    @Transactional
    public Map<String, String> getBoardFiles(int boardId) {

        // 저장한 이름과 주소목록 담을 Map 선언
        Map<String, String> uploadedFiles = new HashMap<>();

        // 1개 이상 이미지 또는 동영상을 등록해야 하기 때문에 찾아오지 못하면 잘못된 값이므로 에러 반환 맞음
        List<BoardFileEntity> boardFiles = boardFileRepository.findByBoardId(boardId)
                .orElseThrow(() -> new RuntimeException("게시글에 등록된 파일이 없습니다." + boardId));

        // 해당 게시글의 파일이 저장된 S3상 파일별 주소목록 반환 (이미지 파일 등록 개수 하한 정하지 않은 경우 활용)
//        List<BoardFileEntity> boardFiles = boardFileRepository.findByBoardId(boardId)
//                .orElse(Collections.emptyList()); //등록된 파일 없으면 빈 리스트 반환

        // 파일 엔티티 목록 반복문 돌면서 하나씩 프론트에 반환할 map에 넣기
        IntStream.range(0, boardFiles.size())
                .forEach(i -> {
                    BoardFileEntity boardFileEntity = boardFiles.get(i);
                    BoardFileDto eachFileDto = boardFileEntity.toDto();
                    uploadedFiles.put(eachFileDto.getSavedName(), eachFileDto.getPath()); //저장된 이름과 경로 반환
                });

        return uploadedFiles;
    }


    //게시글에 저장된 파일 주소만 조회
    @Transactional
    public List<String> getBoardFileUrls(int boardId) {

        // 저장한 이름과 주소목록 담을 Map 선언
        List<String> uploadedFiles = new ArrayList<>();

        // 1개 이상 이미지 또는 동영상을 등록해야 하기 때문에 찾아오지 못하면 잘못된 값이므로 에러 반환 맞음
        List<BoardFileEntity> boardFiles = boardFileRepository.findByBoardId(boardId)
                .orElseThrow(() -> new RuntimeException("게시글에 등록된 파일이 없습니다." + boardId));

        // 해당 게시글의 파일이 저장된 S3상 파일별 주소목록 반환 (이미지 파일 등록 개수 하한 정하지 않은 경우 활용)
//        List<BoardFileEntity> boardFiles = boardFileRepository.findByBoardId(boardId)
//                .orElse(Collections.emptyList()); //등록된 파일 없으면 빈 리스트 반환

        // 파일 엔티티 목록 반복문 돌면서 하나씩 프론트에 반환할 map에 넣기
        IntStream.range(0, boardFiles.size())
                .forEach(i -> {
                    BoardFileEntity boardFileEntity = boardFiles.get(i);
                    BoardFileDto eachFileDto = boardFileEntity.toDto();
                    uploadedFiles.add(eachFileDto.getPath()); //저장된 이름과 경로 반환
                });

        return uploadedFiles;
    }

    @Transactional
    public void deleteBoardFiles(int boardId, List<String> removedImages) {
        for(String path : removedImages){
            path = path.replaceAll("[\"\\[\\]]", "");
            log.info("remove : "+path);
            boardFileRepository.deleteByBoard_IdAndPath(boardId, path);
        }
    }

    //게시글로 ID 조회
    @Transactional(readOnly = true)
    public Long getBoardWriterId(Integer boardId) {
        return boardRepository.findUserIdByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. boardId: " + boardId));
    }

    //게시글로 ID와 Title 조회
    @Transactional(readOnly = true)
    public Object[] getBoardWriterAndTitle(Integer boardId) {
        return boardRepository.findUserIdAndTitleByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. boardId: " + boardId));
    }
}