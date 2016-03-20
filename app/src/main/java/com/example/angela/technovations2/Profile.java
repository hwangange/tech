package com.example.angela.technovations2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    SessionManagement session;
    TextView profileUsername, profileName, profileEmail, profileYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);

        session = new SessionManagement(getApplicationContext());
        profileUsername = (TextView) findViewById(R.id.profileUsername);
        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profileYear= (TextView) findViewById(R.id.profileYear);

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManagement.KEY_USERNAME);
        String name = user.get(SessionManagement.KEY_NAME);
        String email = user.get(SessionManagement.KEY_EMAIL);
        String year = user.get(SessionManagement.KEY_YEAR);

        profileUsername.setText(Html.fromHtml("<b>User: </b>" + username));
        profileName.setText(Html.fromHtml("<b>Name: </b>" + name));
        profileEmail.setText(Html.fromHtml("<b>Email: </b>" + email));
        profileYear.setText(Html.fromHtml("<b>Year: </b>" + year));
    }

}
