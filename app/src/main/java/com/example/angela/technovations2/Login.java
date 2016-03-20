package com.example.angela.technovations2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity{

    private EditText username,password;
    private Button sign_in;
    private Button register;
    private RequestQueue requestQueue;
    SessionManagement session;

    private static final String URL = "http://10.0.0.8/Technovations2/php/user_control.php"; //changeable
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        sign_in = (Button)findViewById(R.id.sign_in);
        register = (Button)findViewById(R.id.register);

        requestQueue = Volley.newRequestQueue(this);

        session = new SessionManagement(getApplication());

        sign_in.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(),"SUCCESS: "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),"USER: "+jsonObject.getString("user"),Toast.LENGTH_SHORT).show();
                                String user = jsonObject.getString("user");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                int year = jsonObject.getInt("year");
                                session.createLoginSession(user, name, email, year);
                                startActivity(new Intent(getApplicationContext(),Welcome.class));
                            }else{
                                Toast.makeText(getApplicationContext(),"ERROR: "+jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }

                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap=new HashMap<String,String>();
                        hashMap.put("username",username.getText().toString());
                        hashMap.put("password",password.getText().toString());

                        return hashMap;
                    }

                };

                requestQueue.add(request);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }


}
