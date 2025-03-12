package contoller;


import DTO.CreateCommentDTO;
import service.CommentService;

/**
 * 댓글 관련 요청 처리 컨트롤러
 */
public class CommentController {
	// DTO 데이터 객체
	CreateCommentDTO dtoData = new CreateCommentDTO();
	
    // 댓글 서비스 객체
    CommentService service = new CommentService();

    /**
     * 댓글 관련 요청 처리
     * 생성, 조회, 수정, 삭제 기능 제공
     */
    public void handleRequest() {
        // 필수 데이터 검증
        if (dtoData.ownerNickName == null || dtoData.writerNickName == null) {
            System.out.println("이메일 또는 닉네임이 올바르지 않습니다.");
            return;
        }
        
        // 댓글 내용 검증 (생성, 수정 시)
        if ((dtoData.commentSwitchNumber == 1 || dtoData.commentSwitchNumber == 3) && dtoData.context == null) {
            System.out.println("댓글 내용을 입력하세요.");
            return;
        }
        
        // 파일에서 댓글 데이터 로드
        service.loadCommentsFromFile(dtoData.addressFile);
        
        // 요청 유형에 따른 처리
        switch (dtoData.commentSwitchNumber) {
            case 1: // 댓글 생성
                service.addComment(dtoData.ownerNickName, dtoData.writerNickName, dtoData.context);
                System.out.println("댓글이 생성되었습니다.");
                break;
            case 2: // 댓글 조회
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
        
        // 변경된 댓글 데이터 저장
        service.saveCommentsToFile(dtoData.addressFile);
    }
}




