package com.comp1601.truefalsequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button mNextButton;
    private Button mPrevButton;
    private Button mSubmitButton;
    private ArrayList<Question> mQuestions;
    private int[] mUserAnswers;
    private TextView mQuestionTextView;
    private int mCurrentQuestionIndex = 0;
    private Button mAButton;
    private Button mBButton;
    private Button mCButton;
    private Button mDButton;
    private Button mEButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrevButton = findViewById(R.id.prev_button);
        mNextButton = findViewById(R.id.next_button);
        mSubmitButton = findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(v -> {
            int numCorrect = 0, userAnswerID;
            for (int i = 0; i < mQuestions.size(); i++) {
                userAnswerID = mUserAnswers[i];
                if (userAnswerID != 0) { // user answer is non-empty
                    String userAnswer = ((Button) findViewById(userAnswerID)).getTag().toString();
                    String correctAnswer = mQuestions.get(i).getAnswer();

                    if (userAnswer.equals(correctAnswer)) numCorrect++; // compare user and actual answer
                }
            }
            String toastText = "You got " + numCorrect + "/" + mQuestions.size();
            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();

            unselectButton(mUserAnswers[mCurrentQuestionIndex]); // unselect button
            mUserAnswers = new int[mQuestions.size()];  // reset user answers
        });
        mPrevButton.setOnClickListener(v -> {
            int nextQuestion = mCurrentQuestionIndex - 1;
            goToQuestion(nextQuestion);
        });
        mNextButton.setOnClickListener(v -> {
            int nextQuestion = mCurrentQuestionIndex + 1;
            goToQuestion(nextQuestion);
        });

        mAButton = findViewById(R.id.a_button);
        mBButton = findViewById(R.id.b_button);
        mCButton = findViewById(R.id.c_button);
        mDButton = findViewById(R.id.d_button);
        mEButton = findViewById(R.id.e_button);

        mAButton.setOnClickListener(onClickListener);
        mBButton.setOnClickListener(onClickListener);
        mCButton.setOnClickListener(onClickListener);
        mDButton.setOnClickListener(onClickListener);
        mEButton.setOnClickListener(onClickListener);

        mQuestionTextView = findViewById(R.id.question_text_view);

        mQuestions = new ArrayList<>();
        TypedArray rawQuestions = getResources().obtainTypedArray(R.array.questions);
        for (int i = 0; i < rawQuestions.length(); i++) {
            int rawQuestionResourceId = rawQuestions.getResourceId(i, 0);
            if (rawQuestionResourceId != 0) {
                String[] rawQuestion = getResources().getStringArray(rawQuestionResourceId);
                mQuestions.add(new Question(rawQuestion));
            }
        }

        mUserAnswers = new int[mQuestions.size()];

        goToQuestion(0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            unselectButton(mUserAnswers[mCurrentQuestionIndex]); // remove coloring from last answer

            int id = v.getId();
            mUserAnswers[mCurrentQuestionIndex] = id; // replace old answer with new
            selectButton(id); // add coloring to new answer
        }
    };

    private void goToQuestion(int nextQuestionIndex) {
        unselectButton(mUserAnswers[mCurrentQuestionIndex]); // remove color from old answer

        mCurrentQuestionIndex = nextQuestionIndex; // change current question to input
        if (mCurrentQuestionIndex >= mQuestions.size()) {
            mCurrentQuestionIndex = 0; // shift to front
        }
        else if (mCurrentQuestionIndex < 0) {
            mCurrentQuestionIndex = mQuestions.size()-1; // shift to back
        }
        Question currentQuestion = mQuestions.get(mCurrentQuestionIndex);
        mQuestionTextView.setText(currentQuestion.getQuestion()); // change question text
        String[] options = currentQuestion.getOptions(); // add in options
        mAButton.setText(options[0]);
        mBButton.setText(options[1]);
        mCButton.setText(options[2]);
        mDButton.setText(options[3]);
        mEButton.setText(options[4]);

        selectButton(mUserAnswers[mCurrentQuestionIndex]); // add color to new answer
    }

    private void unselectButton(int id) {
        if (id != 0) {
            Button toUnselect = findViewById(id);
            toUnselect.setBackgroundTintList(getResources().getColorStateList(R.color.unselected_button));
        }
    }

    private void selectButton(int id) {
        if (id != 0) {
            Button selectedAnswer = findViewById(id);
            selectedAnswer.setBackgroundTintList(getResources().getColorStateList(R.color.selected_button));
        }
    }
}
