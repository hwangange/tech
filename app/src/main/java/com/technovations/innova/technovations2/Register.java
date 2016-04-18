package com.technovations.innova.technovations2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technovations.innova.technovations2.Login;
import com.technovations.innova.technovations2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText username, password, first_name, last_name, email, year;
    private TextView guideline;
    private Button register, back;
    private RequestQueue requestQueue;

    private StringRequest request;

    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        username = (EditText) findViewById(R.id.username_register);
        password = (EditText) findViewById(R.id.password_register);
        first_name = (EditText) findViewById(R.id.first_name_register);
        last_name = (EditText) findViewById(R.id.last_name_register);
        email = (EditText) findViewById(R.id.email_register);
        year = (EditText) findViewById(R.id.year_register);

        register = (Button) findViewById(R.id.register_register);
        back = (Button) findViewById(R.id.back_register);
        guideline = (TextView) findViewById(R.id.guideline);

        requestQueue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                request = new StringRequest(Request.Method.POST, "http://ajuj.comlu.com/register.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.has("success")){
                                Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
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
                        hashMap.put("first",first_name.getText().toString());
                        hashMap.put("last",last_name.getText().toString());
                        hashMap.put("email",email.getText().toString());
                        hashMap.put("year", year.getText().toString());

                        return hashMap;
                    }

                };

                requestQueue.add(request);
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

}
