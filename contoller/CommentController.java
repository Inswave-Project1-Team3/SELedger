package contoller;

import java.util.ArrayList;
import java.util.List;

import model.Comment;
import service.CommentService;


public class CommentController {
	CommentService commentService = new CommentService();

	
	public List<Comment> getCommnet(){
		return commentService.searchComments();
	}
	
	public void showComment() {
		List<Comment> results = new ArrayList<Comment>();
		
		CommentService.readListFromFile(); //파일 읽기
		results = commentService.searchComments();
		
		for(Comment comment : results) {
			System.out.println(comment);
		}
	}
	
	public void controllComment() {
		
	}
	
	

	
	
}




