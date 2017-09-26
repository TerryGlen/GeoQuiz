package com.example.terry.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String QUESTIONS_ANSWERED_KEY = "questions_index";
    private static final String CHEAT_INDEX = "cheater";

    private int mQuestionsCorrect = 0;
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;


    private TextView mQuestionTextView;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_jim, false),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private boolean[] mQuestionsAnswered = new boolean[mQuestionBank.length];
    private boolean[] mQuestionsCheated = new boolean[mQuestionBank.length];

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionsAnswered = savedInstanceState.getBooleanArray(QUESTIONS_ANSWERED_KEY);
            mQuestionsCheated = savedInstanceState.getBooleanArray(CHEAT_INDEX);
        }
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
//        int question = mQuestionBank[mCurrentIndex].getTextResId();
//        mQuestionTextView.setText(question);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Does Nothing yet, but soon
//                Toast correctToast = Toast.makeText(QuizActivity.this,R.string.correct_toast,Toast.LENGTH_SHORT);
//                correctToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
//                correctToast.show();
                checkAnswer(true);
                checkAnsweredAll();


            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast incorrectToast = Toast.makeText(QuizActivity.this,R.string.incorrect_toast,Toast.LENGTH_SHORT);
//                incorrectToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
//                incorrectToast.show();//Please Send Help Does Nothing Yet But Soon
                checkAnswer(false);
                checkAnsweredAll();

            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                mQuestionsCheated[mCurrentIndex] = false;
                updateQuestion();

            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex == 0) ? mQuestionBank.length - 1 : mCurrentIndex - 1;
                updateQuestion();
            }
        });
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startcheat activity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mQuestionsCheated[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
            mCheatButton.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(QUESTIONS_ANSWERED_KEY, mQuestionsAnswered);
        savedInstanceState.putBooleanArray(CHEAT_INDEX, mQuestionsCheated);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


    private void updateQuestion() {

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton.setEnabled(!mQuestionsAnswered[mCurrentIndex]);
        mFalseButton.setEnabled(!mQuestionsAnswered[mCurrentIndex]);
        mCheatButton.setEnabled(!mQuestionsCheated[mCurrentIndex]);
    }

    private void checkAnswer(boolean userPressedTrue) {
        mQuestionsAnswered[mCurrentIndex] = true;
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        mCheatButton.setEnabled(false);
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        if (mQuestionsCheated[mCurrentIndex]) {

            messageResId = R.string.judgment_toast;
        } else {


            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mQuestionsCorrect += 1;
            } else {
                messageResId = R.string.incorrect_toast;
            }

        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    private void checkAnsweredAll() {
        for (int i = 0; i < mQuestionsAnswered.length; i++) {
            if (!mQuestionsAnswered[i]) {
                return;
            }

        }
        String toast_message;
        int PercentageCorrect = 100 * mQuestionsCorrect / mQuestionBank.length; //Have to multiple by 100 first otherwise int/int rounds toward 0
        toast_message = String.format(getResources().getString(R.string.percentage_correct), PercentageCorrect);
        Toast.makeText(this, toast_message, Toast.LENGTH_SHORT).show();


    }
}
