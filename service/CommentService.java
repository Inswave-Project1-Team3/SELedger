package service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import model.Comment;

public class CommentService {
	//key : 가게부 주인 value : 댓글 이용자의 닉네임과 작성한 댓글
	Map<String, Map<String, Comment>> countCommentMap = new HashMap<>(); //
	
	Comment comment = new Comment();
	Scanner sc = new Scanner(System.in);
	
	public void addComment(String countOwner, String commentWriter, String context) {
		
		if(!countCommentMap.containsKey(countOwner)) {
			countCommentMap.put(countOwner, new HashMap<String, Comment>());
			
		}
		if(!countCommentMap.get(countOwner).containsKey(commentWriter)) {
			countCommentMap.get(countOwner).put(commentWriter, new Comment(commentWriter, context, new Date()));
		}else {
			updateComment(countOwner, commentWriter, context);
		}
		
	}
	
	public Map<String, Comment> getAllComment(String countOwner) {
		return countCommentMap.get(countOwner);
	}
	
	public void updateComment(String countOwner, String commentWriter, String updateComment) {
		countCommentMap.get(countOwner).get(commentWriter).update(updateComment);
	}
	
	public void removeComment(String countOwner, String commentWriter) {
		countCommentMap.get(countOwner).remove(commentWriter);
	}
	
}
