package model;

import java.util.Date;

public class Comment { //댓글에 사용되는 데이터 관리
	public Date commentDate;
	public Date updateDate;
	public String nickName;
	public String context;
	
	public Comment() {
		
	}

	public Comment(String nickName, String context, Date now) {
		this.nickName = nickName;
		this.context = context;
		this.commentDate = now;
	}

	@Override
	public String toString() {
		return "Comment [commentDate=" + commentDate + ", updateDate=" + updateDate + ", nickName=" + nickName
				+ ", context=" + context + "]";
	}

	public void update(String updateComment) {
		this.context = updateComment;
		this.updateDate = new Date();
	}
	
	
	
}
