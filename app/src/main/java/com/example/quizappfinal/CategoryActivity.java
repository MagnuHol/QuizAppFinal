package com.example.quizappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //makes the 3 different quiz categories into one "map" so it can be called on as just category in quiz activity.
        // and sets value to each category.
        Button buttonHistory = findViewById(R.id.historybutton);
        buttonHistory.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), QuizActivity.class);
            intent.putExtra("category", "history");
            startActivity(intent);
        });
        Button ButtonSport = findViewById(R.id.sportsbutton);
        ButtonSport.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), QuizActivity.class);
            intent.putExtra("category", "sport");
            startActivity(intent);
        });
        Button ButtonMusic = findViewById(R.id.musicbutton);
        ButtonMusic.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), QuizActivity.class);
            intent.putExtra("category", "music");
            startActivity(intent);
        });
    }
}
