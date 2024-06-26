import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import ImageSection from "../../components/Adopt/ImageSection";
import DogInfoSection from "../../components/Adopt/DogInfoSection";
import PersonalitySection from "../../components/Adopt/PersonalitySection";
import Precost from "./../../components/Adopt/Precosts";
import * as S from "../../styled/Adopt/AdoptBoardCreate.style";
import { PiPawPrintFill } from "react-icons/pi";

function AdoptBoardCreate() {
  const navigate = useNavigate();
  const { boardId } = useParams();
  const [isEditing, setIsEditing] = useState(false);
  const REACT_APP_API_URL = process.env.REACT_APP_API_URL;
  const [currentBoardId, setCurrentBoardId] = useState(null);

  // 모든 컴포넌트에서 Enter 키 누름을 감지하기 위한 useEffect
  useEffect(() => {
    const handleKeyPress = (event) => {
      if (event.key === "Enter" && event.target.tagName === "INPUT") {
        event.preventDefault();
      }
    };

    // 이벤트 리스너 등록
    window.addEventListener("keypress", handleKeyPress);

    // 컴포넌트 unmount 시 이벤트 리스너 제거
    return () => {
      window.removeEventListener("keypress", handleKeyPress);
    };
  }, []);

  // 요청 중인지 확인하는 상태 추가
  const [isSubmitting, setIsSubmitting] = useState(false);
  // 책임비 모달 관련

  const [showPrecost, setShowPrecost] = useState(false);

  const handlePrecostOpen = (boardId) => {
    setCurrentBoardId(boardId); // 상태를 설정
    setShowPrecost(true);
  };

  const handlePrecostClose = () => {
    setShowPrecost(false); // 모달 숨기기
  };

  // 여기서 기존 게시글 데이터를 가져와 설정할 수 있는 상태 값들을 추가해야 합니다.
  useEffect(() => {
    if (boardId) {
      axios
        .get(`${REACT_APP_API_URL}/boards/each/${boardId}`)
        .then((response) => {
          console.log(response);
          setIsEditing(true);
          const data = response.data;
          const dogData = data[0];
          const mediaData = data[1];
          // 다른 상태 설정
          // 0번 인덱스에서의 데이터 처리
          setTitle(dogData.title);
          setDogName(dogData.name);
          setDogAge(dogData.age);
          setRegion({
            address: dogData.regionCode,
            lat: dogData.lat,
            lng: dogData.lng,
          });
          setDogGender(
            dogData.dogGender === "true" || dogData.dogGender === true
          );
          setDogChip(dogData.dogChip === "true" || dogData.dogChip === true);
          setFriendly(dogData.friendly);
          setActivity(dogData.activity);
          setDependency(dogData.dependency);
          setBark(dogData.bark);
          setHair(dogData.hair);
          setDogHealth(dogData.health);
          setDogIntroduction(dogData.introduction);
          setHealthCharCount(dogData.health.length);
          setIntroductionCharCount(dogData.introduction.length);
          setDogTypeCode(dogData.dogTypeCode);

          // 1번 인덱스에서의 이미지와 비디오 URL 처리
          const images = [];
          const videos = [];
          for (const key in mediaData) {
            const url = mediaData[key];
            if (url.endsWith(".mp4")) {
              videos.push(url);
            } else {
              images.push(url);
            }
          }

          setSelectedImages(images);
          setSelectedVideos(videos);
          setOriginalImages(images);
        });
    }
  }, [boardId]);

  // 이미지 등록 관련 코드
  const [selectedImages, setSelectedImages] = useState([]);
  const [selectedImageFiles, setSelectedImageFiles] = useState([]);

  const [originalImages, setOriginalImages] = useState([]);

  const handleImageChange = (event) => {
    const files = event.target.files;
    const selectedImagesArray = [...selectedImages];
    const selectedImageFilesArray = [...selectedImageFiles];

    for (let i = 0; i < files.length; i++) {
      selectedImagesArray.push(URL.createObjectURL(files[i]));
      selectedImageFilesArray.push(files[i]);
    }
    setSelectedImages(selectedImagesArray);
    setSelectedImageFiles(selectedImageFilesArray);

    event.target.value = null; // 이 부분 추가
  };

  // 동영상 등록 관련 코드
  const [selectedVideos, setSelectedVideos] = useState([]);
  const [selectedVideoFiles, setSelectedVideoFiles] = useState([]);
  const handleVideoChange = (event) => {
    const files = event.target.files;
    const selectedVideosArray = [...selectedVideos];
    const selectedVideoFilesArray = [...selectedVideoFiles];
    for (let i = 0; i < files.length; i++) {
      selectedVideosArray.push(URL.createObjectURL(files[i]));
      selectedVideoFilesArray.push(files[i]);
    }
    setSelectedVideos(selectedVideosArray);
    setSelectedVideoFiles(selectedVideoFilesArray);
    event.target.value = null; // 이 부분 추가
  };

  // 이미지 삭제 관련 코드

  const [removedImages, setRemovedImages] = useState([]);

  const handleImageRemove = (indexToRemove) => {
    const removedImage = selectedImages[indexToRemove];

    // 원본 이미지에서도 제거
    setOriginalImages(originalImages.filter((img) => img !== removedImage));

    setRemovedImages([...removedImages, removedImage]);

    setSelectedImages(
      selectedImages.filter((_, index) => index !== indexToRemove)
    );
    setSelectedImageFiles(
      selectedImageFiles.filter((_, index) => index !== indexToRemove)
    );
  };

  // 비디오 삭제 관련 코드
  const handleVideoRemove = (indexToRemove) => {
    setSelectedVideos(
      selectedVideos.filter((_, index) => index !== indexToRemove)
    );
    setSelectedVideoFiles(
      selectedVideoFiles.filter((_, index) => index !== indexToRemove)
    );
  };

  // 강아지 특성(선호도 조사) 관련 코드
  const [friendly, setFriendly] = useState(0);
  const [activity, setActivity] = useState(0);
  const [dependency, setDependency] = useState(0);
  const [bark, setBark] = useState(0);
  const [hair, setHair] = useState(0);

  // DogInfoSection에서 관리할 state들 추가
  const [dogName, setDogName] = useState("");
  const [dogAge, setDogAge] = useState(0);
  const [dogRegion, setRegion] = useState({ address: "", lat: 0, lng: 0 });
  const [dogTypeCode, setDogTypeCode] = useState(""); // 견종 정보를 저장할 상태
  const [dogGender, setDogGender] = useState(false);
  const [dogChip, setDogChip] = useState(false);

  // 강아지 소개, 건강정보 관련 코드
  const [dogHealth, setDogHealth] = useState("");
  const [dogIntroduction, setDogIntroduction] = useState("");
  const [healthCharCount, setHealthCharCount] = useState(0);
  const [introductionCharCount, setIntroductionCharCount] = useState(0);

  const handleHealthChange = (e) => {
    const input = e.target.value;
    setDogHealth(input);
    setHealthCharCount(input.length);

    if (input.length >= 300) {
      setDogHealth(input.slice(0, 299)); // 입력값이 300글자를 초과하면 300글자로 자르기
    } else {
      setDogHealth(input);
    }
  };

  const handleIntroductionChange = (e) => {
    const input = e.target.value;
    setDogIntroduction(input);
    setIntroductionCharCount(input.length);
    if (input.length >= 300) {
      setDogIntroduction(input.slice(0, 299)); // 입력값이 300글자를 초과하면 300글자로 자르기
    } else {
      setDogIntroduction(input);
    }
  };

  // 제목
  const [title, setTitle] = useState("");

  // axios 요청 보내기
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (healthCharCount < 100 || introductionCharCount < 100) {
      event.preventDefault(); // 제출 이벤트를 막는다.
      alert("건강정보와 소개는 각각 최소 100자 이상 작성해주세요."); // 사용자에게 알림 제공
      return;
    }
    if (isSubmitting) return;

    if (
      title.trim() === "" ||
      dogName.trim() === "" ||
      dogAge <= 0 ||
      dogRegion.address.trim() === "" || //TODO: 여기 동작을 제대로 안함
      selectedImages.length === 0 ||
      dogHealth.trim() === "" ||
      dogIntroduction.trim() === ""
    ) {
      alert("모든 항목을 입력해주세요!");
      return; // 검사에 실패하면 함수를 종료합니다.
    }

    setIsSubmitting(true);

    const token = localStorage.getItem("accessToken");

    // FormData 객체 생성
    const formData = new FormData();

    // 이미지 파일들 추가
    selectedImageFiles.forEach((image) => {
      formData.append("multipartFile", image);
    });

  // 삭제된 이미지 목록 추가
    if (removedImages.length > 0) {
      formData.append("removedImages", JSON.stringify(removedImages));
    }
  

    if (isEditing) {
      selectedImageFiles.forEach((image) => {
        formData.append("multipartFile", image);
      });
      // const imagesJSON = JSON.stringify(selectedImages);
      // formData.append("multipartFile", imagesJSON);
    }

    // 비디오 파일들 추가
    selectedVideoFiles.forEach((video) => {
      formData.append("multipartFile", video);
    });

    // 다른 필드들 추가
    formData.append("friendly", friendly);
    formData.append("activity", activity);
    formData.append("dependency", dependency);
    formData.append("bark", bark);
    formData.append("hair", hair);
    formData.append("health", dogHealth);
    formData.append("introduction", dogIntroduction);
    formData.append("name", dogName);
    formData.append("age", dogAge);
    formData.append("regionCode", dogRegion.address);
    formData.append("lat", dogRegion.lat);
    formData.append("lon", dogRegion.lng);
    formData.append("gender", dogGender);
    formData.append("chipYn", dogChip);
    formData.append("dogTypeCode", dogTypeCode);
    formData.append("title", title);

    try {
      if (isEditing) {
        // FormData 객체의 내용을 콘솔로 출력하기 위한 코드
        for (var pair of formData.entries()) {
          console.log(pair[0] + ": " + pair[1]);
        }

        await axios.put(`${REACT_APP_API_URL}/boards/${boardId}`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        });
        alert("게시글이 수정되었습니다.");
        navigate(`/adopt/${boardId}`);
        // 요청 성공 후
        setIsSubmitting(false); // 요청 상태 초기화
        sessionStorage.removeItem("adoptData");
      } else {
        // FormData 객체의 내용을 콘솔로 출력하기 위한 코드
        for (var pair of formData.entries()) {
          console.log(pair[0] + ": " + pair[1]);
        }
        const response = await axios.post(
          `${REACT_APP_API_URL}/boards`,
          formData,
          {
            headers: {
              message: "loginClaimUser",
              "Content-Type": "multipart/form-data", // 이 부분 추가
              Authorization: `Bearer ${token}`,
            },
          }
        );
        sessionStorage.removeItem("adoptData");

        const newBoardId = response.data;
        handlePrecostOpen(newBoardId); // 작성하기 버튼 클릭 시 모달 열기
      }
    } catch (error) {
      console.error(error);
      setIsSubmitting(false); // 요청 실패시에도 상태 초기화
    }
  };

  // 글자 길이에 따라 밑줄 길이를 조정하는 함수
  const calculateUnderlineWidth = () => {
    const maxLength = 50; // 예시로 50자를 최대 길이로 설정
    const baseWidth = 10;
    const increment = (title.length / maxLength) * 55 + baseWidth; // 총 100% 중 67%를 최대 증가량으로 설정
    return Math.min(increment, 100); // 100%를 넘지 않도록
  };

  return (
    <S.Container>
      <S.Title>
        {isEditing
          ? "변경사항을 수정해주세요!"
          : `${localStorage.getItem("nickname")}님이 보호중인 강아지를 소개해주세요!`}
      </S.Title>
      <form onSubmit={handleSubmit}>
        <S.ContentBox>
          <PiPawPrintFill style={{color:"#FF914D", marginBottom:"0.2vw"}}></PiPawPrintFill><S.Span> 제목</S.Span>을 작성해주세요.
          <S.Tooltip>
            ⓘ
            <S.TooltipText className="tooltiptext">
              제목은 20자까지 <br /> 작성할 수 있습니다.
            </S.TooltipText>
          </S.Tooltip>
          <S.TitleInputWrapper valueLength={calculateUnderlineWidth()}>
            <S.TitleInput
              value={title}
              placeholder="제목을 입력해주세요"
              onChange={(e) => {
                const inputValue = e.target.value;
                if (inputValue.length > 20) {
                  setTitle(inputValue.slice(0, 20)); // 입력값이 20자를 초과하면 20자로 자르기
                } else {
                  setTitle(inputValue);
                }
              }}
            />
          </S.TitleInputWrapper>
        </S.ContentBox>
        <S.ContentBox>
          <ImageSection
            selectedImages={selectedImages}
            selectedVideos={selectedVideos}
            handleImageChange={handleImageChange}
            handleVideoChange={handleVideoChange}
            handleImageRemove={handleImageRemove}
            handleVideoRemove={handleVideoRemove} // 이 부분 추가
            isEditing={isEditing} // 이 부분 추가
          />
        </S.ContentBox>
        <S.FlexContainer>
          <S.Box>
            <PersonalitySection
              friendly={friendly}
              activity={activity}
              dependency={dependency}
              bark={bark}
              hair={hair}
              setFriendly={setFriendly}
              setActivity={setActivity}
              setDependency={setDependency}
              setBark={setBark}
              setHair={setHair}
            />
          </S.Box>

          <S.Box>
            <DogInfoSection
              dogName={dogName}
              dogAge={dogAge}
              dogRegion={dogRegion}
              dogGender={dogGender}
              dogChip={dogChip}
              dogTypeCode={dogTypeCode}
              setName={setDogName}
              setAge={setDogAge}
              setRegion={setRegion}
              setGender={setDogGender}
              setChip={setDogChip}
              setDogTypeCode={setDogTypeCode}
              initialRegion={dogRegion}
              initialDogTypeCode={dogTypeCode}
              F
            />
          </S.Box>
        </S.FlexContainer>
        <PiPawPrintFill style={{color:"#FF914D", marginBottom:"0.2vw"}}></PiPawPrintFill> 강아지의 <S.Span>건강정보</S.Span>를 상세하게 작성해주세요.
        <S.Tooltip>
          ⓘ
          <S.TooltipText className="tooltiptext">
            건강정보는 <br />
            100자 이상, <br />
            300자 이하로 <br />
            작성해주세요.
          </S.TooltipText>
        </S.Tooltip>{" "}
        <S.SamllText charCount={healthCharCount}>
          (글자수: {healthCharCount} / 300)
        </S.SamllText>
        <S.DogTextarea value={dogHealth} onChange={handleHealthChange} />
        <PiPawPrintFill style={{color:"#FF914D", marginBottom:"0.2vw"}}></PiPawPrintFill> 강아지를 자유롭게 <S.Span>소개</S.Span>해주세요.
        <S.Tooltip>
          ⓘ
          <S.TooltipText className="tooltiptext">
            소개는 <br />
            100자 이상, <br />
            300자 이하로 <br />
            작성해주세요.
          </S.TooltipText>
        </S.Tooltip>{" "}
        <S.SamllText charCount={introductionCharCount}>
          (글자수: {introductionCharCount} / 300)
        </S.SamllText>
        <S.SamllText></S.SamllText>
        <S.DogTextarea
          value={dogIntroduction}
          onChange={handleIntroductionChange}
        />
        <S.Button type="submit">{isEditing ? "수정하기" : "작성하기"}</S.Button>
        {showPrecost && (
          <Precost onClose={handlePrecostClose} boardId={currentBoardId} />
        )}{" "}
      </form>
    </S.Container>
  );
}

export default AdoptBoardCreate;
