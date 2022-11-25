package com.example.quizappfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultActivity extends AppCompatActivity {

    TextView textView_userScore;
    Button button_retry, button_finish;

    int score;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        score = getIntent().getIntExtra("score", 0);
        category = getIntent().getStringExtra("category");

        textView_userScore = findViewById(R.id.textView_userScore);

        button_retry = findViewById(R.id.button_retry);
        button_finish = findViewById(R.id.button_finish);

        textView_userScore.setText(String.valueOf(score));
        textView_userScore.setText(score + " /10");

        // when button retry is clicked it returns the user to the previous quiz category they were attempting.
        button_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this,QuizActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
                finish();
            }
        });
        //when button is clicked, it returns the user to the main screen.
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}