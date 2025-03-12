package service;

import java.io.*;
import java.util.*;

import DTO.CreateCommentDTO;
import app.App;
import model.Comment;

public class CommentService {
    CreateCommentDTO dto = new CreateCommentDTO();
    private List<Comment> comments;

    public CommentService() {
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment newComment) {
        this.comments.add(newComment);
    }

    public List<Comment> searchComments() {
        List<Comment> findCustomers = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getId().equals(App.userNickName) && comment.getMonth() == App.month) {
                findCustomers.add(comment);
            }
        }
        return findCustomers;
    }
    //--------------------------------------------------------------------------------------------------
    
    public List<Comment> addContextToList(String context){
    	List<Comment> a = new ArrayList<>();
    	return a.add(context);
    }

    //-------------------------------------------- 파일 입출력 --------------------------------------------

    // 🔹 객체 리스트를 .ser 파일에 저장 (직렬화)
    public static void writeListToFile(List<Comment> commentList, String filename) {
        File file = new File("data" + File.separator + "Allcomment.ser");

        // 디렉토리가 없으면 생성
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // 객체 직렬화하여 파일에 저장
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(commentList);
            System.out.println("리스트 데이터를 파일(" + filename + ")에 저장했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 .ser 파일에서 객체 리스트 읽기 (역직렬화)
    @SuppressWarnings("unchecked")
    public static List<Comment> readListFromFile() {
        File file = new File("data" + File.separator + "Allcomment.ser");

        // 파일이 존재하지 않으면 빈 리스트 반환
        if (!file.exists()) {
            return new ArrayList<>();
        }

        // 파일에서 객체 리스트 읽기
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Comment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}   
