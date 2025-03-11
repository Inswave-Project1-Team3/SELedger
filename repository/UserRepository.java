package repository;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 데이터 관리를 담당하는 Repository 클래스
 * 
 * 이 클래스는 사용자 정보를 파일에 저장하고 불러오는 기능을 제공합니다.
 * 객체 직렬화(Serialization)를 통해 사용자 객체를 파일로 저장하고,
 * 역직렬화(Deserialization)를 통해 파일에서 사용자 객체를 불러옵니다.
 */
public class UserRepository {
	// 사용자 데이터 파일 경로
	private static final String USER_DATA_FILE = "data/users.ser";
	
	// 메모리에 로드된 사용자 목록
	private List<User> users;

	/**
	 * 생성자
	 * 인스턴스 생성 시 파일에서 사용자 데이터를 로드합니다.
	 */
	public UserRepository() {
		this.users = new ArrayList<>();
		loadUsers();
	}

	/**
	 * 파일에서 사용자 데이터 로드
	 * 파일이 존재하지 않으면 새로운 빈 리스트를 생성합니다.
	 */
	private void loadUsers() {
		File file = new File(USER_DATA_FILE);
		
		// 파일이 존재하지 않으면 디렉토리 생성 및 빈 리스트 반환
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
	 * 메모리에 있는 사용자 목록을 직렬화하여 파일에 저장합니다.
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
	 * @param email 찾을 사용자의 이메일
	 * @return 사용자 객체를 포함한 Optional (사용자가 없으면 빈 Optional)
	 */
	public Optional<User> findByEmail(String email) {
		return users.stream()
				.filter(user -> user.getEmail().equals(email))
				.findFirst();
	}

	/**
	 * 새 사용자 저장
	 * 이메일 중복 체크 후 사용자를 추가합니다.
	 * 
	 * @param user 저장할 사용자 객체
	 * @return 저장 성공 여부 (true: 성공, false: 실패)
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
	 * 이메일로 사용자를 찾아 정보를 업데이트합니다.
	 * 
	 * @param user 업데이트할 사용자 객체
	 * @return 업데이트 성공 여부 (true: 성공, false: 실패)
	 */
	public boolean update(User user) {
		Optional<User> existingUser = findByEmail(user.getEmail());
		
		if (existingUser.isPresent()) {
			int index = users.indexOf(existingUser.get());
			users.set(index, user);
			saveUsers(); // 변경사항 파일에 저장
			return true;
		}
		
		return false;
	}

	/**
	 * 사용자 삭제 (탈퇴 처리)
	 * 실제로 데이터를 삭제하지 않고, 사용자의 deleteCheck 필드를 true로 설정합니다.
	 * 
	 * @param email 삭제할 사용자의 이메일
	 * @return 삭제 성공 여부 (true: 성공, false: 실패)
	 */
	public boolean delete(String email) {
		Optional<User> user = findByEmail(email);
		
		if (user.isPresent() && user.get().isDeleted())
			return update(user.get());
		
		return false;
	}

	/**
	 * 모든 사용자 조회
	 * 
	 * @return 모든 사용자 목록
	 */
	public List<User> findAll() {
		return new ArrayList<>(users);
	}
	
	/**
	 * 활성 사용자만 조회 (탈퇴하지 않은 사용자)
	 * 
	 * @return 활성 상태인 사용자 목록
	 */
	public List<User> findAllActive() {
		return users.stream()
				.filter(user -> !user.isDeleted())
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
} 