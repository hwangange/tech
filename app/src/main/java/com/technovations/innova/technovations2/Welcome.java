package com.technovations.innova.technovations2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.technovations.innova.technovations2.*;
import com.technovations.innova.technovations2.Profile;
import com.technovations.innova.technovations2.SessionManagement;
import com.technovations.innova.technovations2.WelcomeNav;
import com.technovations.innova.technovations2.viewSignature;

import java.util.HashMap;

/**
 * Created by Angie on 3/13/2016.
 */
public class Welcome extends Activity {

    private Button logout, signature, viewSignature, profile, welcomeNav;
    private TextView welcome;

    private com.technovations.innova.technovations2.SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        session = new com.technovations.innova.technovations2.SessionManagement(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(com.technovations.innova.technovations2.SessionManagement.KEY_USERNAME);
        String name = user.get(SessionManagement.KEY_NAME);

        welcome = (TextView) findViewById(R.id.textView);
        welcome.setText(Html.fromHtml("Welcome <b>" + username + "</b>"));

        signature=(Button)findViewById(R.id.signatureButton);
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Signature.class));
            }
        });

        viewSignature=(Button)findViewById(R.id.viewSignatureButton);
        viewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), viewSignature.class));
            }
        });

        profile=(Button)findViewById(R.id.profileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });

        welcomeNav = (Button)findViewById(R.id.welcomeNavButton);
        welcomeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
            }
        });

        logout=(Button)findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                session.logoutUser();
            }
        });
    }

}
