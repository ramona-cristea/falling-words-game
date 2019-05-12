package com.game.fallingwords.api;


import android.content.res.AssetManager;

import com.game.fallingwords.model.Word;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public interface WordsRepository {

    LiveData<List<Word>> getWords(@NonNull final AssetManager assetManager);
}
