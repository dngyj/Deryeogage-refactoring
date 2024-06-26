import React, { useState, useEffect } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import Edit from "../../components/User/Edit";
import * as S from "../../styled/User/UserInfo.style";

function UserInfo() {
  const nickname = localStorage.getItem("nickname");
  const [showEditModal, setShowEditModal] = useState(false);
  const [profileData, setProfileData] = useState(null);
  const REACT_APP_API_URL = process.env.REACT_APP_API_URL;
  const [userInfo, setUserInfo] = useState([]);
  const profileImg = userInfo.profilePic || "/assets/default.png";

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("nickname");
    window.location.href = "/";
  };

  // 유저 데이터 get
  const fetchUserProfile = async () => {
    try {
      const token = localStorage.getItem("accessToken");
      const response = await axios.get(
        `${REACT_APP_API_URL}/users/profile/mypage`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setUserInfo(response.data);
    } catch (error) {
      console.error("Failed to fetch user profile:", error);
    }
  }

  // 프로필 데이터 get
  useEffect(() => {
    const ProfileData = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await axios.get(
          `${REACT_APP_API_URL}/users/profile/mypage`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setUserInfo(response.data); // userInfo 상태 업데이트
      } catch (error) {
        console.error("Failed to fetch user profile:", error);
      }
    };
    fetchUserProfile();
    ProfileData();
  }, [REACT_APP_API_URL]);

  // 모달 창을 닫고 프로필 이미지를 새로고침하는 함수
  const handleEditClose = () => {
    setShowEditModal(false);
    fetchUserProfile(); // 프로필 이미지 수정 후 유저 정보 다시 가져옴
    setProfileData();
  };

  return (
    <S.UserInfoWrap className="row">
      <div>
        <S.NickName>
          안녕하세요 <S.NickName className="nickname">{nickname}</S.NickName>님
          !
        </S.NickName>
      </div>
      <div className="col-6">
        <br />
        <div className="container">
          <div className="row">
            <div className="col-4">
                <S.ProfileImage src={profileImg} alt="프로필 이미지" />
            </div>
            <div className="col-4 d-flex flex-column justify-content-between">
              <div className="d-flex justify-content-between">
                <S.UserInfoText>닉네임 : {nickname}</S.UserInfoText>
              </div>
              <div className="d-flex justify-content-between">
                <S.UserInfoText>입양 수 : {userInfo.adoptToCount}건</S.UserInfoText>
              </div>
              <S.UserInfoText>
                사전 테스트 점수 : {userInfo.preTestScore}점
              </S.UserInfoText>
              <S.TestBtn href="/survey">선호도 조사하기</S.TestBtn>
            </div>
            <div className="col-4">
                <S.UserInfoText>
                  칭호 : {userInfo.simulationTitle}
                </S.UserInfoText>
                <S.UserInfoText className="margin">
                  분양 수 : {userInfo.adoptFromCount}건
                </S.UserInfoText>
            </div>
          </div>
        </div>
      </div>
      <div className="col-6 d-flex flex-column justify-content-evenly align-items-end">
        <S.MyBtn onClick={handleLogout}>로그아웃</S.MyBtn>
        <S.MyBtn onClick={() => setShowEditModal(true)}>프로필 수정</S.MyBtn>
        {showEditModal && (
          <S.ModalLocation className="d-flex justify-content-center align-items-center">
            <S.ModalStyle>
              <div className="d-flex justify-content-end">
                <S.ModalClose onClick={() => setShowEditModal(false)}>
                  X
                </S.ModalClose>
              </div>
              <Edit onClose={handleEditClose} />{" "}
              {/* 여기에 onClose prop을 전달해줍니다. */}
            </S.ModalStyle>
          </S.ModalLocation>
        )}
      </div>
    </S.UserInfoWrap>
  );
}

export default UserInfo;
