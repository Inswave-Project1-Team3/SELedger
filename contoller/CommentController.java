package contoller;

import java.util.ArrayList;
import java.util.List;

import model.Comment;
import service.CommentService;
import app.App;


public class CommentController {
	CommentService commentService = new CommentService();

	
	public List<Comment> getCommnet(){
		return commentService.searchComments();
	}
	
	/**
	 * 현재 사용자의 특정 일에 해당하는 댓글 목록 조회 및 출력
	 * @param day 조회할 일
	 */
	public void showCommentByDay(int day) {
		List<Comment> results = commentService.searchCommentsByDay(day);
		
		if (results.isEmpty()) {
			System.out.println("댓글이 없습니다.");
		} else {
			System.out.println("===== " + App.userNickName + "님의 " + App.month + "월 " + day + "일 댓글 목록 =====");
			for (Comment comment : results) {
				System.out.println("[작성자: " + comment.getAuthor() + "] " + comment.getcontext());
			}
			System.out.println("===============================");
		}
	}
	
	/**
	 * 특정 사용자의 특정 일에 해당하는 댓글 목록 조회 및 출력
	 * @param nickname 사용자 닉네임
	 * @param month 조회할 월
	 * @param day 조회할 일
	 */
	public void showCommentByUserAndDay(String nickname, int month, int day) {
		List<Comment> results = commentService.searchCommentsByUserAndDay(nickname, month, day);
		
		if (results.isEmpty()) {
			System.out.println("댓글이 없습니다.");
		} else {
			System.out.println("===== " + nickname + "님의 " + month + "월 " + day + "일 댓글 목록 =====");
			for (Comment comment : results) {
				System.out.println("[작성자: " + comment.getAuthor() + "] " + comment.getcontext());
			}
			System.out.println("===============================");
		}
	}
	
	/**
	 * 자신의 가계부에 댓글 추가
	 * @param context 댓글 내용
	 * @param day 댓글 작성 일자
	 */
	public void addComment(String context, int day) {
		commentService.addCommentWithContext(context, day);
		System.out.println("댓글이 추가되었습니다.");
	}
	
	/**
	 * 친구 가계부에 댓글 추가
	 * @param friendNickname 친구 닉네임
	 * @param context 댓글 내용
	 * @param month 월
	 * @param day 일
	 */
	public void addCommentToFriend(String friendNickname, String context, int month, int day) {
		commentService.addCommentToFriendAccountBook(friendNickname, context, month, day);
		System.out.println(friendNickname + "님의 가계부에 댓글이 추가되었습니다.");
	}
	
	public void controllComment() {
		// 추후 구현 예정
	}
}




