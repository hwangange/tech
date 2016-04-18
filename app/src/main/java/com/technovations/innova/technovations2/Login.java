package com.technovations.innova.technovations2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.technovations.innova.technovations2.*;
import com.technovations.innova.technovations2.AdminNav;
import com.technovations.innova.technovations2.Register;
import com.technovations.innova.technovations2.SessionManagement;
import com.technovations.innova.technovations2.WelcomeNav;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity{

    private EditText username,password;
    private Button sign_in;
    private Button register;
    private RequestQueue requestQueue;
    com.technovations.innova.technovations2.SessionManagement session;

    private String URL = ""; //changeable
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


        URL = "http://ajuj.comlu.com/user_control.php";


        sign_in.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.has("success")) {
                                    Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                    String user = jsonObject.getString("user");
                                    String name = jsonObject.getString("name");
                                    String email = jsonObject.getString("email");
                                    int year = jsonObject.getInt("year");
                                    int hours = jsonObject.getInt("hours");


                                    session.createLoginSession(user, name, email, year, hours);
                                    if (username.getText().toString().equals("admin") && (password.getText().toString().equals("admin"))) {
                                        startActivity(new Intent(getApplicationContext(), AdminNav.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
                                    }

                            }
                            else
                                Toast.makeText(getApplicationContext(),"ERROR: "+jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
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
