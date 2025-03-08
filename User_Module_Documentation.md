# 가계부 프로젝트 - 사용자 관리 모듈 구현

## 프로젝트 개요 및 담당 업무

안녕하세요, 팀원 여러분! 이번 가계부 프로젝트에서 저는 **사용자 관리 모듈**을 담당하여 개발했습니다. 이 모듈은 User 클래스를 기반으로 회원가입, 로그인, 로그아웃, 회원정보 수정, 회원탈퇴 기능을 구현하고 있습니다.

프로젝트 요구사항에 따라 데이터베이스 대신 파일 I/O를 통해 데이터를 저장하는 방식을 채택했으며, MVC 패턴을 적용하여 코드의 유지보수성과 확장성을 높였습니다.

## 구현 기능 및 핵심 로직

### 1. 회원가입 (User Registration)

회원가입 기능은 사용자의 이메일, 비밀번호, 닉네임을 입력받아 새로운 사용자를 생성하는 기능입니다.

**핵심 로직:**
- 이메일, 비밀번호, 닉네임의 유효성 검증 (정규식 활용)
- 이메일 및 닉네임 중복 체크
- 비밀번호 SHA-256 암호화 저장
- 사용자 정보 직렬화하여 파일에 저장
- 사용자 닉네임 기반 폴더 자동 생성

**고민했던 점:**
- 비밀번호 암호화 방식 선택 (SHA-256 선택)
- 닉네임 중복 방지 및 변경 불가능 정책 결정
- 사용자 데이터 저장 방식 (객체 직렬화 vs 문자열 저장)

```java
// 회원가입 처리 (UserService 클래스)
public boolean createUser(CreateUserDTO dto) throws IllegalArgumentException {
    // 입력값 검증
    String validationResult = InputValidator.validateUserInput(
            dto.getEmail(), dto.getPassword(), dto.getNickname());
    
    if (validationResult != null)
        throw new IllegalArgumentException(validationResult);
    
    // 이메일 중복 체크
    if (userRepository.findByEmail(dto.getEmail()).isPresent())
        throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    
    // 닉네임 중복 체크
    if (isNicknameExists(dto.getNickname()))
        throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    
    // 사용자 객체 생성 및 저장
    User user = new User();
    try {
        user.registerUser(dto.getEmail(), dto.getPassword(), dto.getNickname());
        boolean saveResult = userRepository.save(user);
        
        // 회원가입 성공 시 사용자 폴더 생성
        if (saveResult) {
            createUserFolder(dto.getNickname());
        }
        
        return saveResult;
    } catch (Exception e) {
        System.err.println("회원가입 중 오류 발생: " + e.getMessage());
        return false;
    }
}
```

### 2. 로그인 (Login)

로그인 기능은 사용자의 이메일과 비밀번호를 검증하여 시스템 접근 권한을 부여하는 기능입니다.

**핵심 로직:**
- 이메일 형식 검증
- 사용자 존재 여부 확인
- 비밀번호 암호화 후 저장된 값과 비교
- 로그인 성공 시 세션 정보 업데이트

**고민했던 점:**
- 세션 관리 방식 (정적 변수 vs 객체 상태)
- 탈퇴한 사용자 로그인 방지 로직

```java
// 로그인 처리 (UserService 클래스)
public boolean login(LoginUserDTO dto) {
    // 이메일 형식 검증
    if (!InputValidator.isValidEmail(dto.getEmail()))
        return false;
    
    // 이메일로 사용자 조회
    Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
    
    // 사용자가 존재하면 로그인 시도
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        return user.loginUser(dto.getEmail(), dto.getPassword());
    }
    
    return false;
}
```

### 3. 회원정보 수정 (Update User Info)

회원정보 수정 기능은 현재 비밀번호 확인 후 새 비밀번호를 변경할 수 있는 기능입니다. 닉네임은 변경할 수 없도록 설계했습니다.

**핵심 로직:**
- 현재 비밀번호 확인을 통한 본인 인증
- 새 비밀번호 유효성 검증
- 닉네임 변경 요청 무시 처리
- 변경된 정보 저장

**고민했던 점:**
- 닉네임 변경 불가능 정책 (폴더 구조 일관성 유지)
- 부분 업데이트 처리 방식 (null 값 처리)

```java
// 회원정보 수정 처리 (UserService 클래스)
public boolean updateUser(UpdateUserDTO dto) throws IllegalArgumentException {
    // 이메일 형식 검증
    if (!InputValidator.isValidEmail(dto.getEmail()))
        throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    
    // 새 비밀번호가 있는 경우 검증
    if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
        if (!InputValidator.isValidPassword(dto.getNewPassword()))
            throw new IllegalArgumentException("새 비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
    }
    
    // 닉네임 변경 요청이 있는 경우 경고 메시지 출력 후 무시
    if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
        System.out.println("닉네임은 변경할 수 없습니다. 닉네임 변경 요청이 무시됩니다.");
        // 닉네임 변경 요청 무시 (null로 설정)
        dto = new UpdateUserDTO(dto.getEmail(), dto.getCurrentPassword(), dto.getNewPassword(), null);
    }
    
    // 이메일로 사용자 조회
    Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
    
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        
        // 현재 비밀번호 확인
        if (!user.loginUser(dto.getEmail(), dto.getCurrentPassword()))
            return false; // 비밀번호 불일치
        
        // 정보 업데이트 (비밀번호만 변경 가능)
        user.updateUserInfo(dto.getNewPassword(), null);
        return userRepository.update(user);
    }
    
    return false;
}
```

### 4. 회원탈퇴 (Delete User)

회원탈퇴 기능은 비밀번호 확인 후 사용자 계정을 비활성화하는 기능입니다. 실제로 데이터를 삭제하지 않고 탈퇴 상태만 변경합니다.

**핵심 로직:**
- 비밀번호 확인을 통한 본인 인증
- 탈퇴 상태(deleteCheck) 필드 업데이트
- 사용자 폴더는 유지 (데이터 관리 목적)

**고민했던 점:**
- 실제 삭제 vs 논리적 삭제 (논리적 삭제 선택)
- 탈퇴 사용자 데이터 관리 방안
- 사용자 폴더 유지 정책

```java
// 회원탈퇴 처리 (UserService 클래스)
public boolean deleteUser(DeleteUserDTO dto) {
    // 이메일 형식 검증
    if (!InputValidator.isValidEmail(dto.getEmail()))
        return false;
    
    // 이메일로 사용자 조회
    Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
    
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        
        // 비밀번호 확인 후 탈퇴 처리
        if (user.deleteUser(dto.getPassword()))
            return userRepository.update(user);
    }
    
    return false;
}
```

### 5. 로그아웃 (Logout)

로그아웃 기능은 현재 로그인된 사용자의 세션 정보를 초기화하는 기능입니다.

**핵심 로직:**
- 세션 정보 초기화 (App.loginCheck, App.userEmail)
- 컨트롤러 내부 상태 초기화 (isLoggedIn, currentUserEmail)

**고민했던 점:**
- 세션 정보 관리 방식
- 컨트롤러와 App 클래스 간의 상태 동기화

```java
// 로그아웃 처리 (UserController 클래스)
public void logout() {
    // 컨트롤러 내부 상태 초기화
    this.isLoggedIn = false;
    this.currentUserEmail = null;
    System.out.println("로그아웃 되었습니다.");
    
    // App 클래스의 정적 변수도 업데이트 (기존 코드와의 호환성 유지)
    if (App.class != null) {
        try {
            App.loginCheck = false;
            App.userEmail = "";
        } catch (Exception e) {
            // App 클래스가 없거나 접근할 수 없는 경우 무시
        }
    }
}
```

## 정규식(Regex) 활용

입력값 검증을 위해 정규식을 활용했습니다. 정규식은 사용자 입력의 유효성을 효율적으로 검증할 수 있는 강력한 도구입니다.

**이메일 정규식:**
```java
^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$
```
- 로컬 파트(@앞): 영문자, 숫자, 일부 특수문자(._%+-)
- 도메인 파트(@뒤): 영문자, 숫자, 하이픈(-), 최소 하나의 점(.), 2자 이상의 최상위 도메인

**비밀번호 정규식:**
```java
^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=\\S+$).{8,}$
```
- 최소 8자 이상
- 최소 하나 이상의 특수문자 포함
- 공백 문자 불허용

**닉네임 정규식:**
```java
^[가-힣a-zA-Z0-9]{2,12}$
```
- 한글, 영문, 숫자만 허용
- 2~12자 이내

정규식을 통해 사용자 입력을 일관되게 검증하고, 잘못된 입력에 대한 명확한 피드백을 제공할 수 있었습니다.

## 데이터 저장 방식

데이터 저장 방식으로는 **객체 직렬화(Serialization)**를 선택했습니다.

**선택 이유:**
1. **객체 구조 유지**: 객체의 상태를 그대로 저장하고 복원할 수 있어 데이터 무결성 유지
2. **구현 용이성**: Java의 기본 직렬화 메커니즘을 활용하여 간단하게 구현 가능
3. **타입 안전성**: 역직렬화 시 원래 객체 타입으로 복원되어 타입 안전성 보장

```java
// 사용자 데이터 저장 (UserRepository 클래스)
private void saveUsers() {
    File file = new File(USER_DATA_FILE);
    file.getParentFile().mkdirs(); // 디렉토리 생성
    
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
        oos.writeObject(users);
    } catch (IOException e) {
        System.err.println("사용자 데이터 저장 중 오류 발생: " + e.getMessage());
    }
}
```

또한, 사용자별로 닉네임 기반의 폴더를 생성하여 향후 사용자별 데이터를 관리할 수 있는 구조를 마련했습니다.

```java
// 사용자 닉네임으로 폴더 생성 (UserService 클래스)
private boolean createUserFolder(String nickname) {
    try {
        // 사용자 폴더 경로 생성
        File userFolder = new File(USER_DATA_FILE + File.separator + nickname);
        
        // 폴더가 이미 존재하는 경우 성공으로 간주
        if (userFolder.exists())
            return true;
        
        // 상위 디렉토리(data)가 없는 경우 생성
        userFolder.getParentFile().mkdirs();
        
        // 사용자 폴더 생성
        boolean result = userFolder.mkdir();
        
        if (result)
            System.out.println("사용자 폴더 생성 성공: " + userFolder.getAbsolutePath());
        else
            System.err.println("사용자 폴더 생성 실패: " + userFolder.getAbsolutePath());
        
        return result;
    } catch (Exception e) {
        System.err.println("폴더 생성 중 오류 발생: " + e.getMessage());
        return false;
    }
}
```

## MVC 패턴 적용

프로젝트의 구조화와 유지보수성을 높이기 위해 MVC 패턴을 적용했습니다.

**Model (모델):**
- `User.java`: 사용자 데이터 모델 및 기본 로직

**View (뷰):**
- `MainPage.java`: 사용자 인터페이스 제공
- `App.java`: 사용자 입력 처리 및 화면 출력

**Controller (컨트롤러):**
- `UserController.java`: 사용자 요청 처리 및 Model과 View 연결

**추가 계층:**
- **Service**: `UserService.java` - 비즈니스 로직 처리
- **Repository**: `UserRepository.java` - 데이터 접근 로직
- **DTO**: `CreateUserDTO.java`, `LoginUserDTO.java` 등 - 데이터 전송 객체
- **Util**: `InputValidator.java` - 유틸리티 클래스

이러한 계층 분리를 통해 코드의 가독성, 유지보수성, 확장성을 높였습니다.

## 테스트 코드

테스트 코드는 `UserTest.java`와 `TestMain.java`에 구현되어 있습니다. 이 테스트 코드는 실제 애플리케이션과 독립적으로 사용자 관련 기능을 테스트할 수 있도록 설계되었습니다.

**테스트 항목:**
1. 회원가입 테스트
2. 로그인 테스트
3. 회원정보 수정 테스트
4. 회원탈퇴 테스트
5. 로그아웃 테스트
6. 현재 사용자 정보 조회 테스트

**테스트 실행 방법:**
1. `TestMain.java` 파일을 실행합니다.
2. 메뉴에서 테스트할 기능을 선택합니다.
3. 안내에 따라 필요한 정보를 입력합니다.
4. 테스트 결과를 확인합니다.

```java
// 테스트 메인 메서드 (UserTest 클래스)
public static void main(String[] args) {
    System.out.println("===== 사용자 기능 테스트 =====");
    
    boolean running = true;
    while (running) {
        printMenu();
        int choice = getIntInput("메뉴 선택: ");
        
        switch (choice) {
            case 1:
                testRegister();
                break;
            case 2:
                testLogin();
                break;
            // ... 기타 테스트 케이스
            case 0:
                running = false;
                System.out.println("테스트를 종료합니다.");
                break;
            default:
                System.out.println("잘못된 메뉴 선택입니다.");
        }
        
        System.out.println(); // 줄바꿈
    }
    
    scanner.close();
}
```

## 실행 방법

### 1. 회원가입

1. 프로그램 실행 후 메인 메뉴에서 `1. 회원가입` 선택
2. 이메일 입력 (example@domain.com 형식)
3. 비밀번호 입력 (8자리 이상, 특수문자 포함)
4. 닉네임 입력 (한글, 영문, 숫자만 허용, 2~12자)
5. 회원가입 완료 메시지 확인

### 2. 로그인

1. 메인 메뉴에서 `2. 로그인` 선택
2. 이메일 입력
3. 비밀번호 입력
4. 로그인 성공 시 사용자 메인 페이지로 이동

### 3. 회원정보 수정

1. 로그인 후 사용자 메인 페이지에서 `2. 회원정보 수정` 선택
2. 현재 비밀번호 입력
3. 새 비밀번호 입력 (변경하지 않으려면 'skip' 입력)
4. 회원정보 수정 완료 메시지 확인

### 4. 회원탈퇴

1. 로그인 후 사용자 메인 페이지에서 `3. 회원탈퇴` 선택
2. 탈퇴 확인 메시지에 'Y' 입력
3. 비밀번호 입력
4. 회원탈퇴 완료 메시지 확인 및 로그아웃 처리

### 5. 로그아웃

1. 로그인 후 사용자 메인 페이지에서 `4. 로그아웃` 선택
2. 로그아웃 완료 메시지 확인

## 주의사항

1. **닉네임 변경 불가능**: 회원가입 시 설정한 닉네임은 이후 변경할 수 없습니다. 닉네임은 사용자 폴더 생성에 사용되므로 신중하게 선택해주세요.

2. **비밀번호 요구사항**: 비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.

3. **회원탈퇴 후 복구 불가능**: 회원탈퇴 처리 후에는 해당 계정으로 로그인할 수 없습니다. 단, 데이터는 완전히 삭제되지 않고 탈퇴 상태로 유지됩니다.

4. **사용자 폴더 유지**: 회원탈퇴 후에도 사용자 폴더는 삭제되지 않고 유지됩니다. 이는 데이터 관리를 위한 정책입니다.

5. **이메일 중복 불가**: 이미 등록된 이메일로는 회원가입할 수 없습니다.

6. **닉네임 중복 불가**: 이미 사용 중인 닉네임으로는 회원가입할 수 없습니다.

## 마무리

이상으로 가계부 프로젝트의 사용자 관리 모듈에 대한 설명을 마치겠습니다. 이 모듈은 MVC 패턴을 적용하여 구조화되었으며, 객체 직렬화를 통한 데이터 저장, 정규식을 활용한 입력값 검증, 사용자별 폴더 관리 등의 기능을 제공합니다.

추가 질문이나 개선 사항이 있으시면 언제든지 말씀해주세요. 감사합니다! 