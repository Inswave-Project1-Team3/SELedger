package service;

// App 클래스 의존성 제거
// import static app.App.*;
import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import model.User;
import repository.UserRepository;
import util.FilePathConstants;
import util.InputValidator;
import util.UserCheck;

import java.io.File;
import java.util.Optional;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
public class UserService {
	// 사용자 데이터 접근용 리포지토리
	private final UserRepository userRepository;

	// 사용자 데이터 저장 폴더
	private static final String USER_DATA_FOLDER = FilePathConstants.DATA_DIRECTORY;

	/**
	 * 생성자 - UserRepository 초기화
	 */
	public UserService() {
		this.userRepository = new UserRepository();
	}

	/**
	 * 회원가입 처리
	 * 
	 * @param dto 회원가입 정보
	 * @return 가입 성공 여부
	 * @throws IllegalArgumentException 유효성 검증 실패 시
	 */
	public boolean createUser(CreateUserDTO dto) throws IllegalArgumentException {
		// 입력값 검증
		validateCreateUserInput(dto);

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

	/**
	 * 회원가입 입력값 검증
	 * 
	 * @param dto 검증할 회원가입 정보
	 * @throws IllegalArgumentException 유효성 검증 실패 시
	 */
	private void validateCreateUserInput(CreateUserDTO dto) throws IllegalArgumentException {
		// 입력값 검증
		String validationResult = InputValidator.validateUserInput(
				dto.getEmail(), dto.getPassword(), dto.getNickname());

		if (validationResult != null)
			throw new IllegalArgumentException(validationResult);

		// 이메일 중복 체크
		if (UserCheck.isEmailExists(dto.getEmail(), userRepository))
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

		// 닉네임 중복 체크
		if (isNicknameExists(dto.getNickname()))
			throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
	}

	/**
	 * 닉네임 중복 확인
	 * 
	 * @param nickname 확인할 닉네임
	 * @return 중복 여부
	 */
	private boolean isNicknameExists(String nickname) {
		return userRepository.findAll().stream()
				.anyMatch(user -> user.getNickName().equals(nickname));
	}

	/**
	 * 사용자 개인 폴더 생성
	 * 
	 * @param nickname 사용자 닉네임
	 * @return 생성 성공 여부
	 */
	private boolean createUserFolder(String nickname) {
		try {
			// 사용자 폴더 경로 생성
			File userFolder = new File(USER_DATA_FOLDER + File.separator + nickname);

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
		} catch (SecurityException e) {
			// 파일 시스템 접근 권한 오류
			System.err.println("폴더 생성 중 보안 오류 발생: " + e.getMessage());
			return false;
		} catch (Exception e) {
			// 기타 예외 처리
			System.err.println("폴더 생성 중 오류 발생: " + e.getMessage());
			return false;
		}
	}

	/**
	 * 로그인 처리
	 * 
	 * @param dto 로그인 정보
	 * @return 로그인 성공 여부
	 */
	public boolean login(LoginUserDTO dto) {
		// 이메일 형식 검증
		if (!InputValidator.isValidEmail(dto.getEmail()))
			return false;

		// 이메일로 사용자 조회
		Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());

		// 사용자가 존재하면 로그인 시도
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			
			// 탈퇴한 회원인 경우
			if (user.isDeleted()) {
				System.out.println("탈퇴한 회원입니다. 계정 복구를 원하시면 관리자에게 문의하세요.");
				System.out.println("관리자 이메일: LOVE@inswave.com");
				return false;
			}
			
			return user.loginUser(dto.getEmail(), dto.getPassword());
		}

		return false;
	}
	
	/**
	 * 회원정보 수정
	 * 
	 * @param dto 수정할 정보
	 * @return 수정 성공 여부
	 * @throws IllegalArgumentException 유효성 검증 실패 시
	 */
	public boolean updateUser(UpdateUserDTO dto) throws IllegalArgumentException {
		// 입력값 검증
		validateUpdateUserInput(dto);

		// 이메일로 사용자 조회
		Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
		if (userOpt.isEmpty())
			throw new IllegalArgumentException("존재하지 않는 사용자입니다.");

		User user = userOpt.get();
		
		// 현재 비밀번호 확인
		if (!user.loginUser(dto.getEmail(), dto.getCurrentPassword()))
			throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
		
		// 사용자 정보 업데이트
		user.updateUserInfo(
				dto.getNewEmail() != null && !dto.getNewEmail().isEmpty() ? dto.getNewEmail() : null,
				dto.getNewPassword() != null && !dto.getNewPassword().isEmpty() ? dto.getNewPassword() : null,
				null // 닉네임은 변경 불가
		);
		
		// 변경사항 저장
		return userRepository.update(user);
	}
	
	/**
	 * 회원정보 수정 입력값 검증
	 * 
	 * @param dto 검증할 수정 정보
	 * @throws IllegalArgumentException 유효성 검증 실패 시
	 */
	private void validateUpdateUserInput(UpdateUserDTO dto) throws IllegalArgumentException {
		// 이메일 형식 검증
		if (!InputValidator.isValidEmail(dto.getEmail()))
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");

		// 새 이메일이 있는 경우 검증
		if (dto.getNewEmail() != null && !dto.getNewEmail().isEmpty()) {
			if (!InputValidator.isValidEmail(dto.getNewEmail()))
				throw new IllegalArgumentException("새 이메일 형식이 올바르지 않습니다.");

			// 새 이메일 중복 체크
			if (!dto.getEmail().equals(dto.getNewEmail()) && UserCheck.isEmailExists(dto.getNewEmail(), userRepository))
				throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		
		// 새 비밀번호가 있는 경우 검증
		if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
			if (!InputValidator.isValidPassword(dto.getNewPassword()))
				throw new IllegalArgumentException("새 비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
		}

		// 닉네임 변경 요청이 있는 경우 경고 메시지 출력 후 무시
		if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
			System.out.println("닉네임은 변경할 수 없습니다. 닉네임 변경 요청이 무시됩니다.");
		}
	}
	
	/**
	 * 회원 탈퇴 처리
	 * 
	 * @param dto 탈퇴 정보
	 * @return 탈퇴 성공 여부
	 */
	public boolean deleteUser(DeleteUserDTO dto) {
		// 이메일 형식 검증
		if (!InputValidator.isValidEmail(dto.getEmail()))
			return false;
		
		// 이메일로 사용자 조회
		Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
		if (userOpt.isEmpty())
			return false;
		
		User user = userOpt.get();
		
		// 비밀번호 확인 후 탈퇴 처리
		if (user.deleteUser(dto.getPassword())) {
			// 변경사항 저장
			return userRepository.update(user);
		}
		
		return false;
	}
	
	/**
	 * 이메일로 사용자 조회
	 * 
	 * @param email 조회할 이메일
	 * @return 사용자 객체 (없으면 null)
	 */
	public User getUserByEmail(String email) {
		if (email == null || email.isEmpty())
			return null;
		
		Optional<User> userOpt = userRepository.findByEmail(email);
		return userOpt.orElse(null);
	}
	
	/**
	 * 닉네임으로 사용자 조회
	 * 
	 * @param nickname 조회할 닉네임
	 * @return 사용자 객체 (없으면 null)
	 */
	public User getUserByNickName(String nickname) {
		if (nickname == null || nickname.isEmpty())
			return null;
		
		return userRepository.findAll().stream()
				.filter(user -> user.getNickName().equals(nickname))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * 닉네임 존재 여부 확인
	 * 
	 * @param nickname 확인할 닉네임
	 * @return 존재 여부
	 */
	public boolean checkNicknameExists(String nickname) {
		return isNicknameExists(nickname);
	}
}
