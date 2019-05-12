package com.game.fallingwords.model;

import android.content.res.AssetManager;

import com.game.fallingwords.api.WordsRepository;
import com.game.fallingwords.api.WordsRepositoryImpl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

public class WordsViewModel extends ViewModel {
    private final MediatorLiveData<List<Word>> mWordsWrapper;

    private final WordsRepository mWordsRepository;

    public WordsViewModel() {
        mWordsWrapper = new MediatorLiveData<>();
        mWordsRepository = new WordsRepositoryImpl();
    }

    @NonNull
    public LiveData<List<Word>> getWordsWrapper() {
        return mWordsWrapper;
    }

    public void loadWordsFromAssets(@NonNull AssetManager assetManager) {
        mWordsWrapper.addSource(
                mWordsRepository.getWords(assetManager),
                mWordsWrapper::setValue
        );
    }
}
