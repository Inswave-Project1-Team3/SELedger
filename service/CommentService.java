package service;

import java.io.*;
import java.util.*;
import DTO.CreateCommentDTO;
import app.App;
import model.Comment;

/**
 * 댓글 관리 서비스 클래스  
 * 댓글 추가, 검색, 삭제, 파일 입출력 기능 제공
 */
public class CommentService {
    private List<Comment> comments;
    private static final String DATA_DIRECTORY = "data";
    private static final String COMMENT_FILE = "Allcomment.ser";

    /**
     * 생성자 - 파일에서 댓글 목록을 읽어와 초기화
     */
    public CommentService() {
        this.comments = readListFromFile();
    }

    /**
     * 현재 사용자와 월에 해당하는 댓글 검색
     * @return 검색된 댓글 목록
     */
    public List<Comment> searchComments() {
        List<Comment> foundComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getId().equals(App.userNickName) && comment.getMonth() == App.month) {
                foundComments.add(comment);
            }
        }
        return foundComments;
    }
    
    /**
     * 현재 사용자, 현재 월, 특정 일에 해당하는 댓글 검색
     * @param day 검색할 일
     * @return 검색된 댓글 목록
     */
    public List<Comment> searchCommentsByDay(int day) {
        List<Comment> foundComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getId().equals(App.userNickName) && 
                comment.getMonth() == App.month && 
                comment.getday() == day) {
                foundComments.add(comment);
            }
        }
        return foundComments;
    }
    
    /**
     * 특정 사용자, 특정 월, 특정 일에 해당하는 댓글 검색
     * @param nickname 사용자 닉네임
     * @param month 검색할 월
     * @param day 검색할 일
     * @return 검색된 댓글 목록
     */
    public List<Comment> searchCommentsByUserAndDay(String nickname, int month, int day) {
        List<Comment> foundComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getId().equals(nickname) && 
                comment.getMonth() == month && 
                comment.getday() == day) {
                foundComments.add(comment);
            }
        }
        return foundComments;
    }

    /**
     * 문자열 context를 이용해 댓글 추가
     * @param context 댓글 내용
     * @param day 댓글 작성 일자
     * @return 업데이트된 댓글 목록
     */
    public List<Comment> addCommentWithContext(String context, int day) {
        Comment newComment = new Comment(App.userNickName, App.userNickName, context, App.month, day);
        this.comments.add(newComment);
        saveComments();
        return this.comments;
    }
    
    /**
     * 친구 가계부에 댓글 추가
     * @param friendNickname 친구 닉네임
     * @param context 댓글 내용
     * @param month 월
     * @param day 일
     * @return 업데이트된 댓글 목록
     */
    public List<Comment> addCommentToFriendAccountBook(String friendNickname, String context, int month, int day) {
        Comment newComment = new Comment(friendNickname, App.userNickName, context, month, day);
        this.comments.add(newComment);
        saveComments();
        return this.comments;
    }
    
    /**
     * 댓글 삭제 기능  
     * 현재 사용자의 댓글 중, 해당 월(App.month)과 day, 그리고 content가 일치하는 댓글을 삭제
     * @param context 삭제할 댓글 내용
     * @param month 삭제할 댓글의 월
     * @param day 삭제할 댓글의 일
     * @return 삭제 성공시 true, 아니면 false
     */
    public boolean deleteComment(int month, int day) {
        Iterator<Comment> iterator = comments.iterator();
        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            if (comment.getId().equals(App.userNickName) &&
                comment.getMonth() == month &&
                comment.getday() == day) {
                iterator.remove();
                saveComments();
                return true;
            }
        }
        return false;
    }

    /**
     * 현재 사용자의 댓글 중 특정 인덱스의 댓글을 삭제
     * @param commentList 사용자의 댓글 목록
     * @param index 삭제할 댓글의 인덱스
     * @return 삭제 성공시 true, 실패시 false
     */
    public boolean deleteCommentByIndex(List<Comment> commentList, int index) {
        if (index < 0 || index >= commentList.size()) {
            return false; // 인덱스가 범위를 벗어남
        }
        
        Comment commentToDelete = commentList.get(index);
        comments.remove(commentToDelete); // 전체 목록에서 해당 댓글 삭제
        saveComments(); // 변경사항 저장
        return true;
    }

    /**
     * 현재 댓글 목록을 파일에 저장
     */
    private void saveComments() {
        File file = getCommentFile();
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this.comments);
            System.out.println("댓글 데이터를 파일에 저장했습니다.");
        } catch (IOException e) {
            System.err.println("댓글 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 파일에서 댓글 목록 읽기
     * @return 읽어온 댓글 목록
     */
    @SuppressWarnings("unchecked")
    private List<Comment> readListFromFile() {
        File file = getCommentFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Comment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("댓글 데이터 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    /**
     * 댓글 파일 경로 반환
     * @return 댓글 파일 객체
     */
    private File getCommentFile() {
        return new File(DATA_DIRECTORY + File.separator + COMMENT_FILE);
    }
}
