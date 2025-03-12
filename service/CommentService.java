package service;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import model.Comment;

/**
 * 댓글 관련 비즈니스 로직 처리
 */
public class CommentService {
	// 가계부 주인별 댓글 관리 맵 (key: 가계부 주인, value: 댓글 작성자와 댓글 내용)
	Map<String, Map<String, Comment>> countCommentMap = new HashMap<>();
	
	// 댓글 모델 인스턴스
	Comment comment = new Comment();
	
	// 입력 처리용 스캐너
	Scanner sc = new Scanner(System.in);
	
	/**
	 * 댓글 추가
	 */
	public void addComment(String countOwner, String commentWriter, String context) {
		// 해당 가계부 주인의 댓글 맵이 없으면 생성
		if(!countCommentMap.containsKey(countOwner)) {
			countCommentMap.put(countOwner, new HashMap<String, Comment>());
		}
		
		// 작성자의 댓글이 없으면 새로 추가, 있으면 업데이트
		if(!countCommentMap.get(countOwner).containsKey(commentWriter)) {
			countCommentMap.get(countOwner).put(commentWriter, new Comment(commentWriter, context, new Date()));
		} else {
			updateComment(countOwner, commentWriter, context);
		}
	}
	
	/**
	 * 특정 가계부의 모든 댓글 조회
	 */
	public Map<String, Comment> getAllComment(String countOwner) {
		// 댓글이 없으면 빈 맵 생성
		if(!countCommentMap.containsKey(countOwner)) {
			countCommentMap.put(countOwner, new HashMap<String, Comment>());
		}
		
		return countCommentMap.get(countOwner);
	}
	
	/**
	 * 댓글 수정
	 */
	public void updateComment(String countOwner, String commentWriter, String updateComment) {
	    if (countCommentMap.containsKey(countOwner) && countCommentMap.get(countOwner).containsKey(commentWriter)) {
	        countCommentMap.get(countOwner).get(commentWriter).update(updateComment);
	    } else {
	        System.out.println("댓글이 존재하지 않아 수정할 수 없습니다.");
	    }
	}
	
	/**
	 * 댓글 삭제
	 */
	public void removeComment(String countOwner, String commentWriter) {
	    if (countCommentMap.containsKey(countOwner) && countCommentMap.get(countOwner).containsKey(commentWriter)) {
	        countCommentMap.get(countOwner).remove(commentWriter);
	    } else {
	        System.out.println("삭제할 댓글이 존재하지 않습니다.");
	    }
	}
	
	/**
	 * 댓글 데이터 파일 저장
	 */
	public void saveCommentsToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(countCommentMap);
            System.out.println("댓글 데이터를 파일에 저장했습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

	/**
	 * 댓글 데이터 파일 로드
	 */
    public void loadCommentsFromFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            countCommentMap = (Map<String, Map<String, Comment>>) in.readObject();
            System.out.println("댓글 데이터를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 불러오기 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
