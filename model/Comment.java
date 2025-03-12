package model;

import java.io.Serializable;

public class Comment implements Serializable{
	private String id; //게시판 주인의 아이디
	private String context;
	private int month;  //enum 열거 타입 
	private int day; //날짜 검색 기능을 추가할 경우 Day day로 변경
	
	
	public Comment(String id, String context, int month, int day) {
		super();
		this.id = id;
		this.context = context;
		this.month = month;
		this.day = day;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getcontext() {
		return context;
	}


	public void setcontext(String context) {
		this.context = context;
	}


	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}


	public int getday() {
		return day;
	}


	public void setday(int day) {
		this.day = day;
	}


	@Override
	public String toString() {
		return "Customer [id=" + id + ", context=" + context + ", month=" + month + ", day=" + day + "]";
	}

}
