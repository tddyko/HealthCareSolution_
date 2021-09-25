package com.gchc.ing.content;

/**
 * 건강컨텐츠 데이터 저장 빈<br/>
 */
public class ContentBean {
	private String title;
	private String content;
	private String address;
	private String day;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}

