package org.javacoo.cowswing.plugin.kbs.domain;

import java.util.Date;


public class KbsModuleAnswer{
	private Integer id;
	private Integer questionid;
	private String username;
	private String usercode;
	private String content;
	private Date answerdate;
	private String ischeck;
	private Integer score;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the questionid
	 */
	public Integer getQuestionid() {
		return questionid;
	}
	/**
	 * @param questionid the questionid to set
	 */
	public void setQuestionid(Integer questionid) {
		this.questionid = questionid;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the usercode
	 */
	public String getUsercode() {
		return usercode;
	}
	/**
	 * @param usercode the usercode to set
	 */
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the answerdate
	 */
	public Date getAnswerdate() {
		return answerdate;
	}
	/**
	 * @param answerdate the answerdate to set
	 */
	public void setAnswerdate(Date answerdate) {
		this.answerdate = answerdate;
	}
	/**
	 * @return the ischeck
	 */
	public String getIscheck() {
		return ischeck;
	}
	/**
	 * @param ischeck the ischeck to set
	 */
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	
	

	
}