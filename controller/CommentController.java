package controller;

import java.util.List;
import java.util.Scanner;
import model.Comment;
import service.CommentService;
import app.App;

/**
 * 댓글 관련 요청 처리 컨트롤러
 */
public class CommentController {
    private final CommentService commentService;
    
    /**
     * 생성자 - 서비스 초기화
     */
    public CommentController() {
        this.commentService = new CommentService();
    }
    
    /**
     * 현재 사용자의 특정 일에 해당하는 댓글 목록 조회 및 출력
     * @param day 조회할 일
     */
    public void showCommentByDay(int day) {
        List<Comment> results = commentService.searchCommentsByDay(day);
        displayComments(results, App.userNickName, App.month, day);
    }
    
    /**
     * 특정 사용자의 특정 일에 해당하는 댓글 목록 조회 및 출력
     * @param nickname 사용자 닉네임
     * @param month 조회할 월
     * @param day 조회할 일
     */
    public void showCommentByUserAndDay(String nickname, int month, int day) {
        List<Comment> results = commentService.searchCommentsByUserAndDay(nickname, month, day);
        displayComments(results, nickname, month, day);
    }
    
    /**
     * 댓글 목록 출력 헬퍼 메소드
     * @param comments 출력할 댓글 목록
     * @param nickname 사용자 닉네임
     * @param month 월
     * @param day 일
     */
    private void displayComments(List<Comment> comments, String nickname, int month, int day) {
        if (comments.isEmpty()) {
            System.out.println("댓글이 없습니다.");
        } else {
            System.out.println("===== " + nickname + "님의 " + month + "월 " + day + "일 댓글 목록 =====");
            for (Comment comment : comments) {
                System.out.println("[작성자: " + comment.getAuthor() + "] " + comment.getcontext());
            }
            System.out.println("===============================");
        }
    }
    
    /**
     * 자신의 가계부에 댓글 추가
     * @param content 댓글 내용
     * @param day 댓글 작성 일자
     */
    public void addComment(String content, int day) {
        commentService.addCommentWithContext(content, day);
        System.out.println("댓글이 추가되었습니다.");
    }
    
    /**
     * 친구 가계부에 댓글 추가
     * @param friendNickname 친구 닉네임
     * @param content 댓글 내용
     * @param month 월
     * @param day 일
     */
    public void addCommentToFriend(String friendNickname, String content, int month, int day) {
        commentService.addCommentToFriendAccountBook(friendNickname, content, month, day);
        System.out.println(friendNickname + "님의 가계부에 댓글이 추가되었습니다.");
    }
    
    /**
     * 사용자의 댓글 목록을 보여주고 삭제할 댓글 번호를 입력받아 삭제
     * @param scanner 입력을 받을 Scanner 객체
     */
    public void deleteCommentByUserSelection(Scanner scanner) {
        List<Comment> userComments = commentService.searchComments();
        if (userComments.isEmpty()) {
            System.out.println("삭제할 댓글이 없습니다.");
            return;
        }
        
        // 사용자의 댓글 목록 표시
        System.out.println("===== " + App.userNickName + "님의 댓글 목록 =====");
        for (int i = 0; i < userComments.size(); i++) {
            Comment comment = userComments.get(i);
            System.out.println((i+1) + ". [" + comment.getMonth() + "월 " + comment.getday() + "일] " + comment.getcontext());
        }
        System.out.println("===============================");
        
        // 삭제할 댓글 번호 입력 받기
        System.out.println("삭제할 댓글 번호를 입력하세요 (0: 취소):");
        // 버퍼 비우기 (이전 입력의 개행 문자 제거)
        scanner.nextLine();
        
        int selection;
        try {
            selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("유효한 숫자를 입력해주세요.");
            return;
        }
        
        if (selection == 0) {
            System.out.println("댓글 삭제가 취소되었습니다.");
            return;
        }
        
        if (selection < 1 || selection > userComments.size()) {
            System.out.println("유효하지 않은 번호입니다.");
            return;
        }
        
        // 선택한 댓글 삭제
        boolean deleted = commentService.deleteCommentByIndex(userComments, selection - 1);
        if (deleted) {
            System.out.println("댓글이 삭제되었습니다.");
        } else {
            System.out.println("댓글 삭제에 실패했습니다.");
        }
    }
}
