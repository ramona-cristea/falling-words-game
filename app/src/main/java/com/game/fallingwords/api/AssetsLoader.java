package com.game.fallingwords.api;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;

class AssetsLoader {

    static String loadContentFromAssetFile(@NonNull final AssetManager assetManager, @NonNull final String fileName) {
        String json = "";
        try {
            InputStream is = assetManager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            Log.e("AssetsLoader", "Error loading file " + fileName);
        }
        return json;
    }
}
