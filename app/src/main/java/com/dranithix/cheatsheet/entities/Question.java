package com.dranithix.cheatsheet.entities;

import java.util.UUID;

/**
 * Created by user on 5/22/2016.
 */

public class Question {
    private String id, question, answer;

    public Question() {
        this.id = UUID.randomUUID().toString();

    }
    public Question(String question, String answer) {
        super();
        this.question = question;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
