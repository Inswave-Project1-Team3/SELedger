package model;

import java.io.Serializable;
import java.util.Date;

/**
 * 댓글 정보 관리 클래스
 */
public class Comment implements Serializable {
	// 댓글 생성 시간
	public Date commentDate;
	
	// 댓글 수정 시간
	public Date updateDate;
	
	// 게시글 주인 닉네임
	public String ownerwriterNickName;
	
	// 댓글 작성자 닉네임
	public String writerNickName;
	
	// 댓글 내용
	public String context;
	
	/**
	 * 기본 생성자
	 */
	public Comment() {
	}

	/**
	 * 댓글 생성 생성자
	 */
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

	/**
	 * 댓글 내용 수정
	 */
	public void update(String updateComment) {
		this.context = updateComment;
		this.updateDate = new Date();
	}
}
