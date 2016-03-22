package com.example.angela.technovations2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
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
import com.example.angela.technovations2.R;
import com.example.angela.technovations2.Welcome;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Submission extends AppCompatActivity {

    private EditText first, last, id, classof, servicedate, hours, description, studentsig, orgname, phonenum, website, address, conname, conemail, condate, consig, parentsig;
    private Button submit;
    private RequestQueue requestQueue;

    private StringRequest request;

    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        String ip_address = getString(R.string.ip_address);
        URL = "http://" + ip_address + "/Technovations2/php/submission.php";

        first = (EditText) findViewById(R.id.first_sub);
        last = (EditText) findViewById(R.id.last_sub);
        id = (EditText) findViewById(R.id.id_sub);
        classof = (EditText) findViewById(R.id.classof_sub);
        servicedate = (EditText) findViewById(R.id.servicedate_sub);
        hours = (EditText) findViewById(R.id.hours_sub);
        description = (EditText) findViewById(R.id.description_sub);
        studentsig = (EditText) findViewById(R.id.studentsig_sub);
        orgname = (EditText) findViewById(R.id.orgname_sub);
        phonenum = (EditText) findViewById(R.id.phonenum_sub);
        website = (EditText) findViewById(R.id.website_sub);
        address = (EditText) findViewById(R.id.address_sub);
        conname = (EditText) findViewById(R.id.conname_sub);
        conemail = (EditText) findViewById(R.id.conemail_sub);
        condate = (EditText) findViewById(R.id.condate_sub);
        consig = (EditText) findViewById(R.id.consig_sub);
        parentsig = (EditText) findViewById(R.id.parentsig_sub);

        submit = (Button) findViewById(R.id.submit_sub);

        requestQueue = Volley.newRequestQueue(this);

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),WelcomeNav.class));
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
                        hashMap.put("first",first.getText().toString());
                        hashMap.put("last",last.getText().toString());
                        hashMap.put("id", id.getText().toString());
                        hashMap.put("classof", classof.getText().toString());
                        hashMap.put("servicedate", servicedate.getText().toString());
                        hashMap.put("hours", hours.getText().toString());
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("studentsig",studentsig.getText().toString());
                        hashMap.put("orgname", orgname.getText().toString());
                        hashMap.put("phonenum", phonenum.getText().toString());
                        hashMap.put("website",website.getText().toString());
                        hashMap.put("address",address.getText().toString());
                        hashMap.put("conname",conname.getText().toString());
                        hashMap.put("conemail",conemail.getText().toString());
                        hashMap.put("condate",condate.getText().toString());
                        hashMap.put("consig",consig.getText().toString());
                        hashMap.put("parentsig",parentsig.getText().toString());
                        return hashMap;
                    }

                };

                requestQueue.add(request);
            }
        });
    }

}
