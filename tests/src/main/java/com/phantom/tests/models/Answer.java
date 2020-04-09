package com.phantom.tests.models;

import java.util.Set;

import javax.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String text;
    private Boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
	private Question question;
	
	@ManyToMany
	@JoinTable(name = "answer_result", joinColumns = { @JoinColumn(name = "answer_id") }, inverseJoinColumns = {
			@JoinColumn(name = "result_id") })
	private Set<Answer> results;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	public Set<Answer> getResults() {
		return results;
	}

	public void setResults(Set<Answer> results) {
		this.results = results;
	}

	
}