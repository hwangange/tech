package com.example.angela.technovations2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.*;
import java.util.HashMap;

/**
 * Created by Angie on 3/13/2016.
 */
public class Welcome extends Activity {

    private Button logout, signature, viewSignature, profile;
    private TextView welcome, welcomeName;

    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        session = new SessionManagement(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

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

        welcome = (TextView) findViewById(R.id.textView);
        welcome.setText(Html.fromHtml("Welcome <b>" + username + "</b>"));

        welcomeName = (TextView) findViewById(R.id.textViewName);
        welcomeName.setText(Html.fromHtml("<b>Name: </b>" + name));

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
