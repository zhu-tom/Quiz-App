package com.comp1601.truefalsequiz;

import java.util.Arrays;

public class Question {
    private String mQuestion;
    private String mAnswer;
    private String[] mOptions;

    public Question(String[] aQuestionArray) {
        mQuestion = aQuestionArray[0];
        mAnswer = aQuestionArray[aQuestionArray.length-1];
        mOptions = Arrays.copyOfRange(aQuestionArray, 1, aQuestionArray.length-1);
        System.out.println(mOptions);
    }

    public String getQuestion() {return mQuestion;}
    public String[] getOptions() {return mOptions;}
    public String getAnswer() {return mAnswer;}
}
