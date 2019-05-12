package com.game.fallingwords.api;

import android.content.res.AssetManager;

import com.game.fallingwords.model.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WordsRepositoryImpl implements WordsRepository {
    @Override
    public LiveData<List<Word>> getWords(@NonNull final AssetManager assetManager) {
        final MutableLiveData<List<Word>> wordsLiveData = new MutableLiveData<>();
        String jsonContent = AssetsLoader.loadContentFromAssetFile(assetManager, "words_v2.json");
        Type listType = new TypeToken<List<Word>>(){}.getType();
        List<Word> posts = new Gson().fromJson(jsonContent, listType);
        wordsLiveData.setValue(posts);

        return wordsLiveData;
    }
}
