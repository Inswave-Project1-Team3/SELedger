package service;

import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import model.User;
import repository.UserRepository;
import util.InputValidator;

import java.io.File;
import java.util.Optional;

/**
 * 사용자 관련 비즈니스 로직 처리
 * 회원가입, 로그인, 정보 수정, 탈퇴 등 사용자 관련 기능 담당
 */
public class UserService {
	// 사용자 데이터 접근용 리포지토리
	private final UserRepository userRepository;

	// 사용자 데이터 저장 폴더
	private static final String USER_DATA_FOLDER = "data";

	/**
	 * 생성자 - UserRepository 초기화
	 */
	public UserService() {
		this.userRepository = new UserRepository();
	}

	/**
	 * 회원가입 처리
	 * 입력값 검증 후 사용자 생성 및 개인 폴더 생성
	 *
	 * @param dto 회원가입 정보
	 * @return 성공 여부
	 * @throws IllegalArgumentException 유효성 검증 실패 또는 중복 이메일/닉네임
	 */
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

	/**
	 * 닉네임 중복 확인
	 */
	private boolean isNicknameExists(String nickname) {
		return userRepository.findAll().stream()
				.anyMatch(user -> user.getNickName().equals(nickname));
	}

	/**
	 * 사용자 개인 폴더 생성
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
	 * 이메일과 비밀번호 검증 (탈퇴 회원 체크)
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
	 * 현재 비밀번호 확인 후 이메일/비밀번호 업데이트
	 * (닉네임은 변경 불가)
	 */
	public boolean updateUser(UpdateUserDTO dto) throws IllegalArgumentException {
		// 이메일 형식 검증
		if (!InputValidator.isValidEmail(dto.getEmail()))
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");

		// 새 이메일이 있는 경우 검증
		if (dto.getNewEmail() != null && !dto.getNewEmail().isEmpty()) {
			if (!InputValidator.isValidEmail(dto.getNewEmail()))
				throw new IllegalArgumentException("새 이메일 형식이 올바르지 않습니다.");

			// 새 이메일 중복 체크
			if (!dto.getEmail().equals(dto.getNewEmail()) && userRepository.findByEmail(dto.getNewEmail()).isPresent())
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
			// 닉네임 변경 요청 무시 (null로 설정)
			dto = new UpdateUserDTO(dto.getEmail(), dto.getCurrentPassword(), dto.getNewEmail(), dto.getNewPassword(), null);
		}

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
	 * 회원 탈퇴
	 * 비밀번호 확인 후 탈퇴 처리 (실제 삭제 아님)
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
	 */
	public User getUserByEmail(String email) {
		if (email == null || email.isEmpty())
			return null;
		
		Optional<User> userOpt = userRepository.findByEmail(email);
		return userOpt.orElse(null);
	}
	
	/**
	 * 닉네임으로 사용자 조회
	 */
	public User getUserByNickName(String nickname) {
		if (nickname == null || nickname.isEmpty())
			return null;
		
		return userRepository.findAll().stream()
				.filter(user -> user.getNickName().equals(nickname))
				.findFirst()
				.orElse(null);
	}
}
