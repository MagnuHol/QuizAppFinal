package com.example.quizappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private final int RC_SIGN_IN = 1;

    // user authenticator source example on how to use firebase from canvas.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets up exit button on main screen, if exit is presset logout function will activate.
        Button button_log_out = findViewById(R.id.button_exit);

        button_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // function which checks if user already exists in firebase. if current user dont exist, it will create a page for
        // users to enter their email and password, when the process is done it will display your login name as signed in.

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser == null){
                    AuthUI.IdpConfig provider;
                    provider = new AuthUI.IdpConfig.EmailBuilder().build();

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.Theme_AppCompat_DayNight)
                                    .setAvailableProviders(Collections.singletonList(provider))
                                    .build(),RC_SIGN_IN
                    );
                }
                else {
                    Toast.makeText(getApplicationContext(), currentUser.getDisplayName() + " signed in", Toast.LENGTH_LONG).show();
                }
            }
        };

        // when play button is pressed it will redirect the user to the category activity
        Button button_play = findViewById(R.id.button_play);

        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), com.example.quizappfinal.CategoryActivity.class);
                startActivity(intent);
            }
        });

        // when highscore button is pressed it will redirect the user to the highscore activity
        Button button_highscores = findViewById(R.id.button_stats);

        button_highscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HighscoreActivity.class);
                startActivity(intent);
            }
        });
    }

    // code for activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != RC_SIGN_IN)
            return;

        if(resultCode == RESULT_OK){
            FirebaseUser currentUser = auth.getCurrentUser();
            Toast.makeText(getApplicationContext(), currentUser.getDisplayName() + " signed in", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "not signed in", Toast.LENGTH_LONG).show();
        }
    }

    //logout function signs out the user.
    protected void logout(){
        AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authStateListener);
    }
}