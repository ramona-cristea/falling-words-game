package com.game.fallingwords.views;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.game.fallingwords.R;
import com.game.fallingwords.model.Word;
import com.game.fallingwords.model.WordItem;
import com.game.fallingwords.model.WordsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePlayerActivity extends ImmersiveActivity implements View.OnClickListener {

    private RelativeLayout mContentView;
    private WordsViewModel mWordsViewModel;

    private List<Word> mWordsList;
    private TextView mTextTranslation;
    private TextView mTextWordToTranslate;
    private TextView mTextCorrectAnswers;
    private TextView mTextWrongAnswers;
    private TextView mTextPlayFeedback;
    private AppCompatImageButton mButtonCorrectWord;
    private AppCompatImageButton mButtonWrongWord;
    private View mDividerControls;
    private Random mRandomFallingwords;

    private int mYPositionEnd;
    private boolean mStartGame;
    private WordItem mWordItem;
    private List<WordItem> mPlayedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_player);
        mContentView = findViewById(R.id.fullscreen_content);
        mTextTranslation = findViewById(R.id.text_translation);
        mTextWordToTranslate = findViewById(R.id.text_word_to_be_translated);
        mTextCorrectAnswers = findViewById(R.id.text_counter_correct_answers);
        mTextWrongAnswers = findViewById(R.id.text_counter_wrong_answers);
        mTextPlayFeedback = findViewById(R.id.text_feedback);
        mButtonCorrectWord = findViewById(R.id.button_correct_translation);
        mButtonWrongWord = findViewById(R.id.button_wrong_translation);
        AppCompatImageButton buttonNextWord = findViewById(R.id.button_next_word);
        AppCompatImageButton buttonRestartGame = findViewById(R.id.button_restart);
        mDividerControls = findViewById(R.id.view_divider_controls);
        mButtonCorrectWord.setOnClickListener(this);
        mButtonWrongWord.setOnClickListener(this);
        buttonNextWord.setOnClickListener(this);
        buttonRestartGame.setOnClickListener(this);
        initGameWindow();

        mWordsViewModel = ViewModelProviders.of(this).get(WordsViewModel.class);
        mWordsViewModel.getWordsWrapper().observe(this, this::handleResponse);
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        //Remove the listener before proceeding
                        mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int[] location = new int[2];
                        mDividerControls.getLocationOnScreen(location);
                        if (location[1] != 0) {
                            mYPositionEnd = location[1] - mTextTranslation.getHeight();
                            startGame();
                        }
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWordsViewModel.loadWordsFromAssets(getAssets());
    }

    private void initGameWindow() {
        mStartGame = false;
        mTextPlayFeedback.setText("");
        mTextCorrectAnswers.setText(getString(R.string.correct_answers, 0));
        mTextWrongAnswers.setText(getString(R.string.wrong_answers, 0));
    }

    private void startGame() {
        if (!mStartGame && mWordsList != null && mYPositionEnd != 0) {
            mStartGame = true;
            mPlayedWords = new ArrayList<>();
            mWordItem = new WordItem();
            mWordItem.setWord(mWordsList.get(0));
            playCurrentWord();
        }
    }

    private void playCurrentWord() {
        enableGameControls(true);
        mTextWordToTranslate.setText(mWordItem.getWord().getTextInEnglish());
        mTextTranslation.setText(generateFallingWord());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, mYPositionEnd);
        anim.setDuration(10000);
        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                //nothing here
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //nothing here
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextTranslation.setVisibility(View.GONE);
                if (!mWordItem.isPlayed()) {
                    mWordItem.setCorrect(false);
                    mWordItem.setPlayed(true);
                }
                updateResults();
            }
        });

        mTextTranslation.startAnimation(anim);
    }

    private String generateFallingWord() {
        if (mRandomFallingwords == null) {
            mRandomFallingwords = new Random();
        }
        int randomIndex = mRandomFallingwords.nextInt(mWordsList.size());
        return mWordsList.get(randomIndex).getTextInSpanish();
    }

    private void updateResults() {
        mPlayedWords.add(mWordItem);
        long countCorrectAnswers = Stream.of(mPlayedWords)
                .filter(WordItem::isCorrect)
                .count();

        long countWrongAnswers = Stream.of(mPlayedWords)
                .filter(value -> !value.isCorrect())
                .count();
        String correctTranslationIs = getString(R.string.correct_translation_is, mWordItem.getWord().getTextInSpanish());
        String userAnswerIs = getString(R.string.your_answer_is, getString(mWordItem.isCorrect() ? R.string.correct : R.string.wrong));
        mTextPlayFeedback.append(correctTranslationIs);
        mTextPlayFeedback.append("\n");
        mTextPlayFeedback.append(userAnswerIs);
        mTextPlayFeedback.setVisibility(View.VISIBLE);
        mTextCorrectAnswers.setText(getString(R.string.correct_answers, countCorrectAnswers));
        mTextWrongAnswers.setText(getString(R.string.wrong_answers, countWrongAnswers));
    }

    private void handleResponse(List<Word> wordsList) {
        if (wordsList == null || wordsList.isEmpty()) {
            return;
        }

        mWordsList = wordsList;
        startGame();
    }

    private void checkIfAnswerIsCorrect(boolean isCorrectTranslation) {
        String fallingWord = mTextTranslation.getText().toString();
        boolean correctTranslation = mWordItem.getWord().getTextInSpanish().equals(fallingWord);
        if (isCorrectTranslation) {
            mWordItem.setCorrect(correctTranslation);
        } else {
            mWordItem.setCorrect(!correctTranslation);
        }
        mWordItem.setPlayed(true);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_correct_translation) {
            enableGameControls(false);
            checkIfAnswerIsCorrect(true);
            enableGameControls(false);
        } else if (id == R.id.button_wrong_translation) {
            checkIfAnswerIsCorrect(false);
        } else if (id == R.id.button_next_word) {
            playNextWord();
        } else {
            restartGame();
        }
    }

    private void restartGame() {
        initGameWindow();
        if(mPlayedWords != null) {
            mPlayedWords.clear();
            mPlayedWords = null;
        }
        startGame();
    }

    private void enableGameControls(boolean enable) {
        mButtonCorrectWord.setEnabled(enable);
        mButtonWrongWord.setEnabled(enable);
    }

    private void playNextWord() {
        int index = mWordsList.indexOf(mWordItem.getWord());
        if(index + 1 == mWordsList.size()) {
            Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_LONG).show();
        }
        else {
            mTextPlayFeedback.setText("");
            mWordItem = new WordItem();
            mWordItem.setWord(mWordsList.get(index + 1));
            playCurrentWord();
        }
    }
}
