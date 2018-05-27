package com.example.hylxmu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {
    private Button playButton;
    TextView textViewState, textViewRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playButton = (Button) findViewById(R.id.playButton);
        textViewState = (TextView)findViewById(R.id.state);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    public void startGame(){
        Intent intent = new Intent(this,GameBoardActivity.class);
        startActivity(intent);

    }
}
