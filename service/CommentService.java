package service;

import java.io.*;
import java.util.*;

import DTO.CreateCommentDTO;
import app.App;
import model.Comment;

/**
 * 댓글 관리 서비스
 */
public class CommentService {
    private List<Comment> comments;
    private static final String DATA_DIRECTORY = "data";
    private static final String COMMENT_FILE = "Allcomment.ser";

    /**
     * 생성자 - 댓글 목록 초기화
     */
    public CommentService() {
        this.comments = readListFromFile();
    }

    /**
     * 댓글 추가
     * 
     * @param newComment 추가할 댓글
     */
    public void addComment(Comment newComment) {
        this.comments.add(newComment);
        saveComments(); // 변경사항 저장
    }

    /**
     * 현재 사용자의 댓글 검색
     * 
     * @return 댓글 목록
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
     * 특정 사용자의 댓글 검색
     * 
     * @param nickname 사용자 닉네임
     * @param month 월
     * @return 댓글 목록
     */
    public List<Comment> searchCommentsByUser(String nickname, int month) {
        List<Comment> foundComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getId().equals(nickname) && comment.getMonth() == month) {
                foundComments.add(comment);
            }
        }
        return foundComments;
    }
    
    /**
     * 현재 사용자의 특정 일자 댓글 검색
     * 
     * @param day 검색할 일
     * @return 댓글 목록
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
     * 특정 사용자의 특정 일자 댓글 검색
     * 
     * @param nickname 사용자 닉네임
     * @param month 검색할 월
     * @param day 검색할 일
     * @return 댓글 목록
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
     * 댓글 내용으로 추가
     * 
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
     * DTO로 댓글 추가
     * 
     * @param dto 댓글 생성 DTO
     * @param day 댓글 작성 일자
     * @return 업데이트된 댓글 목록
     */
    public List<Comment> addCommentWithDTO(CreateCommentDTO dto, int day) {
        return addCommentWithContext(dto.context, day);
    }
    
    /**
     * 친구 가계부에 댓글 추가
     * 
     * @param friendNickname 친구 닉네임
     * @param context 댓글 내용
     * @param month 월
     * @param day 일
     * @return 업데이트된 댓글 목록
     */
    public List<Comment> addCommentToFriendAccountBook(String friendNickname, String context, int month, int day) {
        // 새로운 Comment 객체 생성 (id는 친구의 닉네임으로, author는 현재 사용자로 설정)
        Comment newComment = new Comment(friendNickname, App.userNickName, context, month, day);
        
        // 댓글 목록에 추가
        this.comments.add(newComment);
        
        // 변경사항 저장
        saveComments();
        
        // 업데이트된 댓글 목록 반환
        return this.comments;
    }

    /**
     * 댓글 목록 저장
     */
    private void saveComments() {
        File file = getCommentFile();
        
        // 디렉토리가 없으면 생성
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // 객체 직렬화하여 파일에 저장
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
     * 
     * @return 댓글 목록
     */
    @SuppressWarnings("unchecked")
    private List<Comment> readListFromFile() {
        File file = getCommentFile();

        // 파일이 존재하지 않으면 빈 리스트 반환
        if (!file.exists()) {
            return new ArrayList<>();
        }

        // 파일에서 객체 리스트 읽기
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
     * 
     * @return 댓글 파일
     */
    private File getCommentFile() {
        return new File(DATA_DIRECTORY + File.separator + COMMENT_FILE);
    }
}   
