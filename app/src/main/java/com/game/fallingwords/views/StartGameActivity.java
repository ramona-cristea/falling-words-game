package com.game.fallingwords.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.game.fallingwords.R;

public class StartGameActivity extends ImmersiveActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Button singlePlayerButton = findViewById(R.id.button_single_player);
        Button howToPlay = findViewById(R.id.button_how_to_play);
        singlePlayerButton.setOnClickListener(view -> {
            Intent playIntent = new Intent(this, SinglePlayerActivity.class);
            startActivity(playIntent);
        });
        howToPlay.setOnClickListener(view -> {
            Intent howToPlayIntent = new Intent(this, HowToPlay.class);
            startActivity(howToPlayIntent);
        });
    }
}
