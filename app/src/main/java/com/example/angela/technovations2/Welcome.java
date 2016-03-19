package com.example.angela.technovations2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.*;

/**
 * Created by Angie on 3/13/2016.
 */
public class Welcome extends Activity {

    private Button logout, signature, viewSignature;
    private SignaturePad mSignaturePad;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        signature=(Button)findViewById(R.id.signatureButton);
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Signature.class));
            }
        });

        viewSignature=(Button)findViewById(R.id.signatureButton);
        viewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), viewSignature.class));
            }
        });

        logout=(Button)findViewById(R.id.button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

}
