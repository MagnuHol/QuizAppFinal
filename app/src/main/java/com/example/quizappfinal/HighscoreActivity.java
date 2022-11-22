package com.example.quizappfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HighscoreActivity extends AppCompatActivity {

    TextView textView_historyScore, textView_sportScore, textView_musicScore;
    Button button_mainActivity;
    int scoreH, scoreM, scoreP;

    SharedPreferences spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        spref = getSharedPreferences("scorepref",MODE_PRIVATE);

        scoreH = spref.getInt("history", 0);
        scoreM = spref.getInt("music", 0);
        scoreP = spref.getInt("sport", 0);


        textView_sportScore = findViewById(R.id.textView_sportScore);
        textView_historyScore = findViewById(R.id.textView_historyScore);
        textView_musicScore= findViewById(R.id.textView_musicScore);

        textView_sportScore.setText(String.valueOf(+scoreP));
        textView_historyScore.setText(String.valueOf(+scoreH));
        textView_musicScore.setText(String.valueOf(+scoreM));

        textView_sportScore.setText(scoreP+"/10");
        textView_historyScore.setText(scoreH+"/10");
        textView_musicScore.setText(scoreM+"/10");

        button_mainActivity = findViewById(R.id.button_mainActivity);

        button_mainActivity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });

    }
}
