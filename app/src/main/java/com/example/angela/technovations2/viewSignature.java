package com.example.angela.technovations2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class viewSignature extends AppCompatActivity {
    private Button viewSignatureButton;
    private LinearLayout signatureLayout;
    private RequestQueue requestQueue;
    private StringRequest request;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_signature);
        requestQueue = Volley.newRequestQueue(this);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        final String username = user.get(SessionManagement.KEY_USERNAME);

        viewSignatureButton = (Button) findViewById(R.id.viewSignatureButton);
        signatureLayout = (LinearLayout) findViewById(R.id.signatureLayout);

        viewSignatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String sig_url = "http://ajuj.comlu.com/viewSignature.php/?user=" + username;

                request = new StringRequest(Request.Method.GET, sig_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                int length = jsonObject.getInt("length");
                                for(int i = 2; i < length + 2; i++) {
                                    String name = (String)jsonObject.names().get(i);
                                    String base64 = jsonObject.getString(name);

                                    signatureLayout.addView(convert(base64));
                                }
                                Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            }else{
                                if(jsonObject.names().get(0).equals("empty")) {
                                    Toast.makeText(getApplicationContext(),"EMPTY: "+jsonObject.getString("empty"),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "ERROR: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }

                });

                requestQueue.add(request);

            }
        });

    }

    public ImageView convert(String base64) {
        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        byte[] arr = Base64.decode(base64, flags);

        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        ImageView image = new ImageView(this);
        image.setImageBitmap(bmp);
        return image;
    }

}
