package com.comp1601.truefalsequiz;

public class Question {
    private String mQuestion;
    private String mAnswer;

    public Question(String aQuestionAnswerString) {
        int index = aQuestionAnswerString.indexOf("{");
        mQuestion = aQuestionAnswerString.substring(0,index);
        mAnswer = aQuestionAnswerString.substring(index+1, aQuestionAnswerString.length()-1);

    }

    public String getQuestion() {return mQuestion;}
    public String getAnswer() {return mAnswer;}
}
