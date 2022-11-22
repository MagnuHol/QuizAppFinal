package com.example.quizappfinal;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity {

    TextView textView_question, textView_option1, textView_option2, textView_option3, textView_option4, textView_score, textView_timer;
    DatabaseReference databaseReference;
    Button button_next;
    String category, selectedOption, correctAns;
    int score = 0;
    int option = 0;
    int index = 0;
    boolean FlagFasit = true;
    SharedPreferences spref;
    SharedPreferences.Editor edit;

    ArrayList<Integer> questionList = new ArrayList<>();

    ArrayList<String> Question = new ArrayList<String>();
    ArrayList<String> Option1 = new ArrayList<String>();
    ArrayList<String> Option2 = new ArrayList<String>();
    ArrayList<String> Option3 = new ArrayList<String>();
    ArrayList<String> Option4 = new ArrayList<String>();
    ArrayList<String> theCorrectAnswer = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        SetTimer();

        spref = getSharedPreferences("scorepref", MODE_PRIVATE);
        edit = spref.edit();

        category = getIntent().getStringExtra("category");

        textView_question = findViewById(R.id.textView_question);
        textView_option1 = findViewById(R.id.textView_option1);
        textView_option2 = findViewById(R.id.textView_option2);
        textView_option3 = findViewById(R.id.textView_option3);
        textView_option4 = findViewById(R.id.textView_option4);
        textView_timer = findViewById(R.id.textView_timer);

        textView_score = findViewById(R.id.textView_score);
        textView_score.setText(score + " /10");

        button_next = findViewById(R.id.button_next);


        for (int i = 0; i < 10; i++) {
            questionList.add(i);
        }
        Collections.shuffle(questionList);

        DisableClick();

        databaseReference = FirebaseDatabase.getInstance("https://quizappfinal-77f04-default-rtdb.europe-west1.firebasedatabase.app/").getReference(category);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Question.add(ds.child("Question").getValue(String.class));
                    Option1.add(ds.child("Option1").getValue(String.class));
                    Option2.add(ds.child("Option2").getValue(String.class));
                    Option3.add(ds.child("Option3").getValue(String.class));
                    Option4.add(ds.child("Option4").getValue(String.class));
                    theCorrectAnswer.add(ds.child("theCorrectAnswer").getValue(String.class));
                }
                CreateQuestion();
                StandardColor();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textView_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 1;
                selectedOption = textView_option1.getText().toString();
                StandardColor();
                HighlightOption(option);


            }
        });

        textView_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOption = textView_option2.getText().toString();
                option = 2;
                StandardColor();
                HighlightOption(option);


            }
        });

        textView_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 3;
                selectedOption = textView_option3.getText().toString();
                StandardColor();
                HighlightOption(option);

            }
        });

        textView_option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 4;
                selectedOption = textView_option4.getText().toString();
                StandardColor();
                HighlightOption(option);
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FlagFasit) {
                    ValidateAns();
                    FlagFasit = false;
                    button_next.setText("NEXT");
                    DisableClick();
                } else {
                    nextQuestion();
                    FlagFasit = true;
                }
            }
        });
    }

    void nextQuestion() {
        if (index < 9) {
            index++;
            StandardColor();
            CreateQuestion();
        } else {
            FinishQuiz();
        }
    }

    void ValidateAns() {
        if (option != 0) {
            if (selectedOption.equals(correctAns)) {
                score++;
                textView_score.setText(score + " /10");
                CorrectOption(option);
            } else {
                WrongOption(option);
            }
        }
    }

    void CreateQuestion() {

        textView_option1.setClickable(true);
        textView_option2.setClickable(true);
        textView_option3.setClickable(true);
        textView_option4.setClickable(true);

        textView_question.setText(Question.get(questionList.get(index)));
        textView_option1.setText(Option1.get(questionList.get(index)));
        textView_option2.setText(Option2.get(questionList.get(index)));
        textView_option3.setText(Option3.get(questionList.get(index)));
        textView_option4.setText(Option4.get(questionList.get(index)));
        correctAns = theCorrectAnswer.get(questionList.get(index));
        option = 0;
        button_next.setText("confirm");

    }

    void SetTimer() {
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                textView_timer.setText(String.valueOf(seconds));
            }

            public void onFinish() {
                FinishQuiz();
                textView_timer.setText("Finished!!");
                Toast.makeText(getApplicationContext(), "Timer ran out, TRY AGAIN!", Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    void FinishQuiz() {
        if (score > spref.getInt(category, 0)) {
            edit.putInt(category, score);
            edit.apply();
        }
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("category", category);
        startActivity(intent);
        finish();
    }

     void reverseTimer(){

         CountDownTimer timer = new CountDownTimer(60000, 1000) {


             public void onTick(long millisUntilFinished) {
                 String showMinAndSec = String.format("%02d:%02", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                         TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                 textView_timer.setText(showMinAndSec);

                 Log.e(TAG, getClass().toString() + textView_timer);
             }

             public void onFinish() {
                 textView_timer.setText("Completed");
                 finish();
             }
         };
    }



    void DisableClick() {
        textView_option1.setClickable(false);
        textView_option2.setClickable(false);
        textView_option3.setClickable(false);
        textView_option4.setClickable(false);
    }

    void CorrectOption(int option) {

        switch (option) {
            case 1:
                textView_option1.setBackgroundResource(R.drawable.correct_textview_button);
                break;
            case 2:
                textView_option2.setBackgroundResource(R.drawable.correct_textview_button);
                break;
            case 3:
                textView_option3.setBackgroundResource(R.drawable.correct_textview_button);
                break;
            case 4:
                textView_option4.setBackgroundResource(R.drawable.correct_textview_button);
                break;
        }
    }

    void HighlightOption(int option) {
        switch (option) {
            case 1:
                textView_option1.setBackgroundResource(R.drawable.selected_button);
                break;
            case 2:
                textView_option2.setBackgroundResource(R.drawable.selected_button);
                break;
            case 3:
                textView_option3.setBackgroundResource(R.drawable.selected_button);
                break;
            case 4:
                textView_option4.setBackgroundResource(R.drawable.selected_button);
                break;
        }
    }



    void WrongOption(int option) {

        switch (option) {

            case 1:
                textView_option1.setBackgroundResource(R.drawable.wrong_textview_button);
                break;
            case 2:
                textView_option2.setBackgroundResource(R.drawable.wrong_textview_button);
                break;
            case 3:
                textView_option3.setBackgroundResource(R.drawable.wrong_textview_button);
                break;
            case 4:
                textView_option4.setBackgroundResource(R.drawable.wrong_textview_button);
                break;
        }
            if (textView_option1.getText().toString().equals(correctAns)) {
                textView_option1.setBackgroundResource(R.drawable.correct_textview_button);
            } else if (textView_option2.getText().toString().equals(correctAns)) {
                textView_option2.setBackgroundResource(R.drawable.correct_textview_button);
            } else if (textView_option3.getText().toString().equals(correctAns)) {
                textView_option3.setBackgroundResource(R.drawable.correct_textview_button);
            } else if (textView_option4.getText().toString().equals(correctAns)) {
                textView_option4.setBackgroundResource(R.drawable.correct_textview_button);
            }

    }

    void StandardColor() {

        textView_option1.setBackgroundResource(R.drawable.textview_button);
        textView_option2.setBackgroundResource(R.drawable.textview_button);
        textView_option3.setBackgroundResource(R.drawable.textview_button);
        textView_option4.setBackgroundResource(R.drawable.textview_button);
    }

}