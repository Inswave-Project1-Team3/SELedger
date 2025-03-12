package repository;

import model.User;
import util.FilePathConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 데이터 저장소 클래스
 * 파일 기반으로 사용자 정보를 관리
 */
public class UserRepository {
	// 사용자 데이터 파일 경로
	private static final String USER_DATA_FILE = FilePathConstants.USER_DATA_FILE;
	
	// 메모리에 로드된 사용자 목록
	private List<User> users;

	/**
	 * 생성자 - 파일에서 사용자 데이터 로드
	 */
	public UserRepository() {
		this.users = new ArrayList<>();
		loadUsers();
	}

	/**
	 * 파일에서 사용자 데이터 로드
	 */
	private void loadUsers() {
		File file = new File(USER_DATA_FILE);
		
		// 파일이 없으면 디렉토리 생성
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			return;
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = ois.readObject();
			if (obj instanceof List) {
				@SuppressWarnings("unchecked")
				List<User> loadedUsers = (List<User>) obj;
				this.users = loadedUsers;
			}
		} catch (FileNotFoundException e) {
			// 파일이 없는 경우 무시 (새로운 파일 생성됨)
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("사용자 데이터 로드 중 오류 발생: " + e.getMessage());
		}
	}

	/**
	 * 사용자 데이터를 파일에 저장
	 */
	private void saveUsers() {
		File file = new File(USER_DATA_FILE);
		file.getParentFile().mkdirs(); // 디렉토리 생성
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(users);
		} catch (IOException e) {
			System.err.println("사용자 데이터 저장 중 오류 발생: " + e.getMessage());
		}
	}

	/**
	 * 이메일로 사용자 찾기
	 * 
	 * @param email 검색할 이메일
	 * @return 사용자 객체 (Optional)
	 */
	public Optional<User> findByEmail(String email) {
		return users.stream()
				.filter(user -> user.getEmail().equals(email))
				.findFirst();
	}

	/**
	 * 새 사용자 저장
	 * 
	 * @param user 저장할 사용자 객체
	 * @return 저장 성공 여부
	 */
	public boolean save(User user) {
		// 이메일 중복 체크
		if (findByEmail(user.getEmail()).isPresent())
			return false; // 이미 존재하는 이메일
		
		users.add(user);
		saveUsers(); // 변경사항 파일에 저장
		return true;
	}

	/**
	 * 사용자 정보 업데이트
	 * 
	 * @param user 업데이트할 사용자 객체
	 * @return 업데이트 성공 여부
	 */
	public boolean update(User user) {
		// 이메일로 사용자 찾기
		Optional<User> existingUserOpt = findByEmail(user.getEmail());
		
		if (existingUserOpt.isPresent()) {
			// 기존 사용자 제거 후 새 정보로 추가
			users.remove(existingUserOpt.get());
			users.add(user);
			saveUsers(); // 변경사항 파일에 저장
			return true;
		}
		
		return false; // 사용자가 존재하지 않음
	}

	/**
	 * 사용자 삭제
	 * 
	 * @param email 삭제할 사용자 이메일
	 * @return 삭제 성공 여부
	 */
	public boolean delete(String email) {
		Optional<User> userOpt = findByEmail(email);
		
		if (userOpt.isPresent()) {
			users.remove(userOpt.get());
			saveUsers(); // 변경사항 파일에 저장
			return true;
		}
		
		return false; // 사용자가 존재하지 않음
	}

	/**
	 * 모든 사용자 조회
	 * 
	 * @return 사용자 목록
	 */
	public List<User> findAll() {
		return new ArrayList<>(users); // 원본 리스트 보호를 위한 복사본 반환
	}

	/**
	 * 활성 상태인 사용자만 조회
	 * 
	 * @return 활성 사용자 목록
	 */
	public List<User> findAllActive() {
		return users.stream()
				.filter(user -> !user.isDeleted())
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
} 