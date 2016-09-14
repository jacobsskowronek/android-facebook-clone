package com.jacobskowronek.facebookclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    public void createAccount(View view) {

        ParseUser user = new ParseUser();

        String username = String.valueOf(((EditText)findViewById(R.id.createUsernameField)).getText());
        String password = String.valueOf(((EditText) findViewById(R.id.createPasswordField)).getText());
        String confirmPassword = String.valueOf(((EditText) findViewById(R.id.confirmCreatePasswordField)).getText());

        if (!confirmPassword.equals(password)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            user.setUsername(username);
            user.setPassword(password);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        // Go to MainMenuActivity with newly created account
                        Intent MainMenuIntent = new Intent(CreateAccountActivity.this, MainActivity.class);
                        startActivity(MainMenuIntent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }
}
