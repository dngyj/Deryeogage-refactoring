
<div align="center">
  <img src="./img/로고.PNG" width="50%"/>
</div>

<hr>

# 1. 주요 기능 시연 영상


# 2. :calendar: 리팩토링 기간 

### 23.07.10 ~ 23.08.18 (6주)


# 4. 데려가개 소개


# 5. 주요 기능 소개

### 메인페이지 / 로그인페이지

<table>
    <tr> 
        <td> <img src="img/메인페이지.png"> </td>
        <td> <img src="img/로그인페이지.png"> </td>
    </tr>
    <tr> 
        <td> 메인페이지 </td>
        <td> 로그인페이지 </td>
    </tr>
</table>

### 1) 반려견 양육 시뮬레이션

### 2) 사전 테스트

### 3) 선호도 조사

### 4) 입양 게시판

### 5) 보호자와 상담(채팅 및 화상)

### 6) 책임비 납부 및 반환

# 6. 주요 기술 스택 소개

# 7. 서비스 아키텍처


# 8. 주요 기획 및 설계 자료

### | [요구사항정의서](https://docs.google.com/spreadsheets/d/1Uqf0YmFeVwYuZPWYLiKWIPeZA8cFOR7hSE0o8xM8RLo/edit#gid=0) | [사용자스토리](https://docs.google.com/spreadsheets/d/1AOAkrt0WQE_8c0uHmOPhKNq8UcjX5U-gLw8P4L1d1dc/edit#gid=0) | [ERD](https://www.erdcloud.com/d/JZdkyKFKmvn88mqBG) | [와이어프레임](https://www.figma.com/file/JjoMiub1PyJ7eyNGYR8V0Z/E1I5-%EA%BC%AC%EC%88%9C%EB%82%B4?type=design&node-id=0-1&mode=design&t=aXh5UjBfkJRemuwG-0) | [API설계서](https://documenter.getpostman.com/view/27233223/2s9YBxZbXu#intro) |


# 9. 프로젝트 파일 구조

### BackEnd

```
📦Back
 ┣ 📂gradle
 ┃ ┗ 📂wrapper
 ┃ ┃ ┣ 📜gradle-wrapper.jar
 ┃ ┃ ┗ 📜gradle-wrapper.properties
 ┣ 📂src
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂kkosunnae
 ┃ ┃ ┃ ┃ ┃ ┗ 📂deryeogage
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂adopt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂board
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂chat
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂cost
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mission
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂pretest
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂simulation
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂survey
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂global
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂custom
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂interceptor
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂s3file
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DeryeogageApplication.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📂static
 ┃ ┗ 📂test
 ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂kkosunnae
 ┃ ┃ ┃ ┃ ┃ ┗ 📂deryeogage
```

### FrontEnd
```
📦Front
 ┣ 📂public
 ┃ ┣ 📂assets
 ┃ ┃ ┣ 📂1
 ┃ ┃ ┣ 📂2
 ┃ ┃ ┣ 📂3
 ┃ ┃ ┣ 📂4
 ┃ ┃ ┣ 📂5
 ┃ ┃ ┣ 📂adopt
 ┃ ┃ ┣ 📂background
 ┃ ┃ ┣ 📂chatimg
 ┃ ┃ ┣ 📂dog_bgi
 ┃ ┃ ┣ 📂emotion
 ┃ ┃ ┣ 📂home
 ┃ ┃ ┣ 📂requirement
 ┃ ┃ ┣ 📂rundogs
 ┃ ┃ ┣ 📂things
 ┃ ┃ ┣ 📂training
 ┃ ┣ 📂audio
 ┣ 📂src
 ┃ ┣ 📂components
 ┃ ┃ ┣ 📂Adopt
 ┃ ┃ ┣ 📂Button
 ┃ ┃ ┣ 📂Check
 ┃ ┃ ┣ 📂Radio
 ┃ ┃ ┣ 📂Review
 ┃ ┃ ┣ 📂User
 ┃ ┣ 📂pages
 ┃ ┃ ┣ 📂Adopt
 ┃ ┃ ┣ 📂ChatVideo
 ┃ ┃ ┃ ┣ 📂openvidu
 ┃ ┃ ┃ ┃ ┣ 📂models
 ┃ ┃ ┃ ┃ ┣ 📂toolbar
 ┃ ┃ ┣ 📂Check
 ┃ ┃ ┣ 📂NotFound
 ┃ ┃ ┣ 📂Review
 ┃ ┃ ┣ 📂User
 ┃ ┣ 📂recoil
 ┃ ┣ 📂styled
 ┃ ┃ ┣ 📂Adopt
 ┃ ┃ ┣ 📂ChatVideo
 ┃ ┃ ┣ 📂Check
 ┃ ┃ ┣ 📂Review
 ┃ ┃ ┣ 📂User
```
