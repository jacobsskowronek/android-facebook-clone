package com.jacobskowronek.facebookclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {



    public void login(View view) {
        String username = String.valueOf(((EditText) findViewById(R.id.usernameField)).getText());
        String password = String.valueOf(((EditText)findViewById(R.id.passwordField)).getText());

        // Sign in user, and if successful, take user to timeline page
        if (username != null && password != null) {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Toast.makeText(getApplicationContext(), "You have successfully logged in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, FragmentControllerActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Log in failed", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });

        } else if (username == null) {

            Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_SHORT).show();

        } else if (password == null) {

            Toast.makeText(getApplicationContext(), "You must enter a password", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();

        }

    }

    public void createAccount(View view) {
        // Go to Create Account activity
        Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
        startActivity(intent);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if user is already logged in, and if they are, take them to the Timeline page
        if (ParseUser.getCurrentUser() != null) {
            Log.i("User", "Already signed in");
            Intent intent = new Intent(this, FragmentControllerActivity.class);
            startActivity(intent);
            finish();

        }

    }
}
