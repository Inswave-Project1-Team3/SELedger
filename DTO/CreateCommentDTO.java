package DTO;

public class CreateCommentDTO {
	public String ownerNickName; //사용자가 열어볼 게시글의 주인의 아이디
	public String writerNickName; //내 이름
	public String context; //사용자에게 받을 댓글 내용
	public int commentSwitchNumber; //사용자가 댓글 기능을 선택할 때, 선택지 번호를 받을 변수
	public String addressFile; //데이터를 저장하고 있는 파일의 주소
}
