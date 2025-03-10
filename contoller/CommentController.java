package contoller;


import DTO.CreateCommentDTO;
import service.CommentService;

public class CommentController {
	CreateCommentDTO dtoData = new CreateCommentDTO(); // DTO 데이터 가져오기
    CommentService service = new CommentService(); // Service 가져오기

    public void handleRequest() {  // 요청 처리 메서드
        if (dtoData.ownerNickName == null || dtoData.writerNickName == null) {
            System.out.println("이메일 또는 닉네임이 올바르지 않습니다.");
            return;
        }
        
        if ((dtoData.commentSwitchNumber == 1 || dtoData.commentSwitchNumber == 3) && dtoData.context == null) {
            System.out.println("댓글 내용을 입력하세요.");
            return;
        }
        
        //파일 읽기
        service.loadCommentsFromFile(dtoData.addressFile);
        
        switch (dtoData.commentSwitchNumber) {
            case 1: // 댓글 생성
                service.addComment(dtoData.ownerNickName, dtoData.writerNickName, dtoData.context);
                System.out.println("댓글이 생성되었습니다.");
                break;
            case 2: // 댓글 보기
                var comments = service.getAllComment(dtoData.ownerNickName);
                if (comments.isEmpty()) {
                    System.out.println("작성된 댓글이 없습니다.");
                } else {
                    comments.forEach((nick, comment) -> System.out.println(nick + ": " + comment));
                }
                break;
            case 3: // 댓글 수정
                service.updateComment(dtoData.ownerNickName, dtoData.writerNickName, dtoData.context);
                System.out.println("댓글이 수정되었습니다.");
                break;
            case 4: // 댓글 삭제
                service.removeComment(dtoData.ownerNickName, dtoData.writerNickName);
                System.out.println("댓글이 삭제되었습니다.");
                break;
            default:
                System.out.println("비정상적인 입력입니다.");
        }
        //파일에 저장
        service.saveCommentsToFile(dtoData.addressFile);
    }
}




