package com.comp1601.truefalsequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button mNextButton;
    private Button mPrevButton;
    private Button mSubmitButton;
    private ArrayList<Question> mQuestions;
    private ArrayList<Button> mPageButtons;
    private ArrayList<FrameLayout> mPageBorders;
    private int[] mUserAnswers;
    private TextView mQuestionTextView;
    private int mCurrentQuestionIndex = 0;
    private Button mAButton;
    private Button mBButton;
    private Button mCButton;
    private Button mDButton;
    private Button mEButton;
    private Resources mRes;
    private ColorStateList mBasicButton;
    private ColorStateList mSelectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRes = getResources();
        mSelectedButton = mRes.getColorStateList(R.color.selected_button);
        mBasicButton = mRes.getColorStateList(R.color.basic_button);

        mPrevButton = findViewById(R.id.prev_button);
        mNextButton = findViewById(R.id.next_button);
        mSubmitButton = findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(v -> {
            int numCorrect = 0, userAnswerID;
            for (int i = 0; i < mQuestions.size(); i++) {
                userAnswerID = mUserAnswers[i];
                if (userAnswerID != 0) { // user answer is non-empty
                    String userAnswer = (findViewById(userAnswerID)).getTag().toString();
                    String correctAnswer = mQuestions.get(i).getAnswer();

                    if (userAnswer.equals(correctAnswer)) numCorrect++; // compare user and actual answer
                }
                unselectButton(mUserAnswers[i]); // reset answers
                mPageButtons.get(i).setBackgroundTintList(mBasicButton); // show unanswered on page buttons
            }
            String toastText = "You got " + numCorrect + "/" + mQuestions.size();
            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();

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

        // initialize answer buttons
        mAButton = findViewById(R.id.a_button);
        mBButton = findViewById(R.id.b_button);
        mCButton = findViewById(R.id.c_button);
        mDButton = findViewById(R.id.d_button);
        mEButton = findViewById(R.id.e_button);

        // add on click listeners to answer buttons
        mAButton.setOnClickListener(onClickListener);
        mBButton.setOnClickListener(onClickListener);
        mCButton.setOnClickListener(onClickListener);
        mDButton.setOnClickListener(onClickListener);
        mEButton.setOnClickListener(onClickListener);

        mQuestionTextView = findViewById(R.id.question_text_view);

        mPageButtons = new ArrayList<>();
        mQuestions = new ArrayList<>();
        mPageBorders = new ArrayList<>();
        TypedArray rawQuestions = mRes.obtainTypedArray(R.array.questions);

        LinearLayout pageButtonsLayout = findViewById(R.id.pages);
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int buttonWidth = screenWidth/rawQuestions.length(); // divide screen width by number of question to get page button width

        for (int i = 0; i < rawQuestions.length(); i++) {
            int rawQuestionResourceId = rawQuestions.getResourceId(i, 0);
            if (rawQuestionResourceId != 0) { // if question has id
                String[] rawQuestion = mRes.getStringArray(rawQuestionResourceId);
                mQuestions.add(new Question(rawQuestion));
            }

            // add page button outline
            FrameLayout buttonBox = new FrameLayout(this);
            buttonBox.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            buttonBox.setPadding(1, 0,0,1);

            // add page button
            Button pageButton = new Button(this);
            pageButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            pageButton.setText(Integer.toString(i+1));
            buttonBox.addView(pageButton);

            int pageNum = i;
            pageButton.setOnClickListener(v -> {
                goToQuestion(pageNum);
            });
            pageButtonsLayout.addView(buttonBox);

            mPageButtons.add(pageButton);
            mPageBorders.add(buttonBox);
        }

        mUserAnswers = new int[mQuestions.size()];

        goToQuestion(0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            unselectButton(mUserAnswers[mCurrentQuestionIndex]); // remove coloring from last answer

            int id = v.getId();

            if (mUserAnswers[mCurrentQuestionIndex] == id) { // clicked original answer
                mUserAnswers[mCurrentQuestionIndex] = 0; // set to no answer
                mPageButtons.get(mCurrentQuestionIndex).setBackgroundTintList(mBasicButton); // show no answer on page buttons
            } else {
                if (mUserAnswers[mCurrentQuestionIndex] == 0) { // no previous answer
                    mPageButtons.get(mCurrentQuestionIndex).setBackgroundTintList(mSelectedButton); // show answered on page buttons
                }
                mUserAnswers[mCurrentQuestionIndex] = id; // replace old answer with new
                selectButton(id); // add coloring to new answer
            }
        }
    };

    private void goToQuestion(int nextQuestionIndex) {
        mPageBorders.get(mCurrentQuestionIndex).setBackgroundColor(mRes.getColor(R.color.transparent));
        unselectButton(mUserAnswers[mCurrentQuestionIndex]); // remove color from old answer

        mCurrentQuestionIndex = nextQuestionIndex; // change current question to input
        if (mCurrentQuestionIndex >= mQuestions.size()) {
            mCurrentQuestionIndex = 0; // shift to first
        }
        else if (mCurrentQuestionIndex < 0) {
            mCurrentQuestionIndex = mQuestions.size()-1; // shift to last
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
        mPageBorders.get(mCurrentQuestionIndex).setBackgroundColor(mRes.getColor(R.color.black));
    }

    private void unselectButton(int id) {
        if (id != 0) {
            Button toUnselect = findViewById(id);
            toUnselect.setBackgroundTintList(mBasicButton);
        }
    }

    private void selectButton(int id) {
        if (id != 0) {
            Button selectedAnswer = findViewById(id);
            selectedAnswer.setBackgroundTintList(mSelectedButton);
        }
    }
}
