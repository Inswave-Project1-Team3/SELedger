package model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable { //댓글에 사용되는 데이터 관리
	public Date commentDate; //댓글 생성 시간
	public Date updateDate; //댓글 수정 시간
	public String ownerwriterNickName; //게시글 주인의 닉네임
	public String writerNickName; //사용자의 닉네임
	public String context; //댓글 내용
	
	public Comment() {
	}

	public Comment(String writerNickName, String context, Date now) {
		this.writerNickName = writerNickName;
		this.context = context;
		this.commentDate = now;
	}

	@Override
	public String toString() {
		return "Comment [commentDate=" + commentDate + ", updateDate=" + updateDate + ", writerNickName=" + writerNickName
				+ ", context=" + context + "]";
	}

	public void update(String updateComment) { //댓글 수정될 때 사용할 생성자
		this.context = updateComment;
		this.updateDate = new Date();
	}
	
	
	
}
