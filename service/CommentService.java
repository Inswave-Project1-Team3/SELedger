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

public class CommentService {
	//key : 가게부 주인 value : 댓글 이용자의 닉네임과 작성한 댓글
	Map<String, Map<String, Comment>> countCommentMap = new HashMap<>(); //
	
	Comment comment = new Comment(); //모델에서 댓글 자료 가져오기
	Scanner sc = new Scanner(System.in); //임시적으로 스캐너 생성
	
	public void addComment(String countOwner, String commentWriter, String context) { //댓글생성
		
		if(!countCommentMap.containsKey(countOwner)) { //댓글을 단 사람이 한 명도 없을 때, 생성해주기
			countCommentMap.put(countOwner, new HashMap<String, Comment>());
		}
		if(!countCommentMap.get(countOwner).containsKey(commentWriter)) {
			countCommentMap.get(countOwner).put(commentWriter, new Comment(commentWriter, context, new Date()));
		}else {
			updateComment(countOwner, commentWriter, context); //댓글 생성하기
		}
		
	}
	
	public Map<String, Comment> getAllComment(String countOwner) { //댓글 보기
		
		if(!countCommentMap.containsKey(countOwner)) { //댓글이 없을 때 맵 생성
			countCommentMap.put(countOwner, new HashMap<String, Comment>());
		}
		
		return countCommentMap.get(countOwner);
	}
	
	public void updateComment(String countOwner, String commentWriter, String updateComment) { //댓글 수정
	    if (countCommentMap.containsKey(countOwner) && countCommentMap.get(countOwner).containsKey(commentWriter)) {
	        countCommentMap.get(countOwner).get(commentWriter).update(updateComment);
	    } else {
	        System.out.println("댓글이 존재하지 않아 수정할 수 없습니다.");
	    }
	}
	
	public void removeComment(String countOwner, String commentWriter) { //댓글 삭제
	    if (countCommentMap.containsKey(countOwner) && countCommentMap.get(countOwner).containsKey(commentWriter)) {
	        countCommentMap.get(countOwner).remove(commentWriter);
	    } else {
	        System.out.println("삭제할 댓글이 존재하지 않습니다.");
	    }
	}
	
	
	public void saveCommentsToFile(String fileName) { //파일에 저장하기
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(countCommentMap);
            System.out.println("댓글 데이터를 파일에 저장했습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public void loadCommentsFromFile(String fileName) { //파일에서 Map 읽어오기
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            countCommentMap = (Map<String, Map<String, Comment>>) in.readObject();
            System.out.println("댓글 데이터를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 불러오기 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

}
