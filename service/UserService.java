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
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 이 클래스는 컨트롤러와 리포지토리 사이에서 중간 역할을 하며,
 * 사용자 관련 비즈니스 로직(회원가입, 로그인, 회원정보 수정, 회원탈퇴 등)을 처리합니다.
 * 입력값 검증, 데이터 변환, 예외 처리 등을 담당합니다.
 */
public class UserService {
	// 사용자 데이터 접근을 위한 리포지토리
	private final UserRepository userRepository;

	// 사용자 데이터 폴더 경로
	private static final String USER_DATA_FOLDER = "data";

	/**
	 * 생성자
	 * UserRepository 인스턴스를 생성하여 초기화합니다.
	 */
	public UserService() {
		this.userRepository = new UserRepository();
	}

	/**
	 * 회원가입 - 입력값 검증 후 사용자 생성
	 *
	 * 이메일, 비밀번호, 닉네임의 유효성을 검증하고,
	 * 이메일 중복 여부를 확인한 후 사용자를 생성합니다.
	 * 회원가입 성공 시 사용자의 닉네임으로 폴더를 생성합니다.
	 *
	 * @param dto 회원가입 정보를 담은 DTO
	 * @return 회원가입 결과 (성공: true, 실패: false)
	 * @throws IllegalArgumentException 입력값 검증 실패 또는 이메일 중복 시 발생
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
	 *
	 * @param nickname 확인할 닉네임
	 * @return 중복 여부 (true: 중복, false: 중복 아님)
	 */
	private boolean isNicknameExists(String nickname) {
		return userRepository.findAll().stream()
				.anyMatch(user -> user.getNickName().equals(nickname));
	}

	/**
	 * 사용자 닉네임으로 폴더 생성
	 *
	 * @param nickname 사용자 닉네임
	 * @return 폴더 생성 결과 (true: 성공, false: 실패)
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
	 * 로그인
	 *
	 * 이메일과 비밀번호를 검증하여 로그인을 처리합니다.
	 *
	 * @param dto 로그인 정보를 담은 DTO
	 * @return 로그인 결과 (성공: true, 실패: false)
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
			return user.loginUser(dto.getEmail(), dto.getPassword());
		}

		return false;
	}
	
	/**
	 * 회원정보 수정
	 *
	 * 현재 비밀번호를 확인한 후, 새 이메일, 새 비밀번호, 닉네임을 업데이트합니다.
	 * 닉네임은 변경할 수 없으므로, 닉네임 변경 요청은 무시됩니다.
	 *
	 * @param dto 회원정보 수정 정보를 담은 DTO
	 * @return 수정 결과 (성공: true, 실패: false)
	 * @throws IllegalArgumentException 입력값 검증 실패 시 발생
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

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			// 현재 비밀번호 확인
			if (!user.loginUser(dto.getEmail(), dto.getCurrentPassword()))
				return false; // 비밀번호 불일치

			// 정보 업데이트
			user.updateUserInfo(dto.getNewEmail(), dto.getNewPassword(), null);
			return userRepository.update(user);
		}

		return false;
	}

	/**
	 * 회원탈퇴
	 *
	 * 비밀번호를 확인한 후, 사용자의 탈퇴 상태를 변경합니다.
	 * 실제로 데이터를 삭제하지 않고, deleteCheck 필드를 true로 설정합니다.
	 * 사용자 폴더는 삭제하지 않고 유지합니다.
	 *
	 * @param dto 회원탈퇴 정보를 담은 DTO
	 * @return 탈퇴 결과 (성공: true, 실패: false)
	 */
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

	/**
	 * 이메일로 사용자 조회
	 *
	 * @param email 조회할 사용자의 이메일
	 * @return 사용자 객체 (없으면 null)
	 */
	public User getUserByEmail(String email) {
		// 이메일 형식 검증
		if (!InputValidator.isValidEmail(email))
			return null;

		return userRepository.findByEmail(email).orElse(null);
	}

	/**
	 * 닉네임으로 사용자 조회
	 *
	 * @param nickname 조회할 사용자의 닉네임
	 * @return 사용자 객체 (없으면 null)
	 */
	public User getUserByNickName(String nickname) {
		if (nickname == null || nickname.isEmpty())
			return null;

		// 모든 사용자를 조회하여 닉네임이 일치하는 사용자 반환
		return userRepository.findAll().stream()
				.filter(user -> user.getNickName().equals(nickname))
				.findFirst()
				.orElse(null);
	}
}
