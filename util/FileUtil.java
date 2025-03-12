package util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 파일 및 디렉토리 관리 유틸리티 클래스
 */
public class FileUtil {
	
	/** 기본 데이터 디렉토리 경로 */
	private static final String DATA_DIR = "data";
	
	/**
	 * 이메일을 폴더명으로 변환
	 * 
	 * @param email 이메일 주소
	 * @return 폴더명으로 사용 가능한 문자열
	 */
	public static String emailToFolderName(String email) {
		// 대부분의 현대 파일 시스템은 @, . 등의 특수문자를 폴더명으로 허용합니다.
		// 하지만 필요한 경우 아래 주석 처리된 코드를 사용하여 특수문자를 대체할 수 있습니다.
		
		// return email.replace("@", "_at_").replace(".", "_dot_");
		return email;
	}
	
	/**
	 * 사용자 디렉토리 생성
	 * 
	 * @param email 사용자 이메일
	 * @return 생성 성공 여부
	 */
	public static boolean createUserDirectory(String email) {
		try {
			// 이메일을 폴더명으로 변환
			String folderName = emailToFolderName(email);
			
			// 사용자 디렉토리 경로 생성
			Path userDir = Paths.get(DATA_DIR, folderName);
			
			// 디렉토리가 이미 존재하는지 확인
			if (Files.exists(userDir))
				return true;
			
			// 상위 디렉토리(data)가 없으면 함께 생성
			Files.createDirectories(userDir);
			
			System.out.println("사용자 디렉토리 생성 완료: " + userDir);
			return true;
		} catch (Exception e) {
			System.err.println("사용자 디렉토리 생성 실패: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 사용자 디렉토리 경로 조회
	 * 
	 * @param email 사용자 이메일
	 * @return 디렉토리 경로
	 */
	public static String getUserDirectoryPath(String email) {
		String folderName = emailToFolderName(email);
		return Paths.get(DATA_DIR, folderName).toString();
	}
	
	/**
	 * 사용자 디렉토리 존재 여부 확인
	 * 
	 * @param email 사용자 이메일
	 * @return 존재 여부
	 */
	public static boolean userDirectoryExists(String email) {
		String folderName = emailToFolderName(email);
		Path userDir = Paths.get(DATA_DIR, folderName);
		return Files.exists(userDir);
	}
} 