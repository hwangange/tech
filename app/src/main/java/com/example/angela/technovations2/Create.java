package com.example.angela.technovations2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Create extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText first, last, id, classof, teacher, servicedate, hours, description, orgname, phonenum, website, address, conname, conemail, condate;
    private SignaturePad studentsig, consig, parentsig;
    private Spinner paid_spinner;

    private Button submit, drafts;
    private RequestQueue requestQueue;

    private StringRequest request;

    private String URL = "";

    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        final String username = user.get(SessionManagement.KEY_USERNAME);
        String name = user.get(SessionManagement.KEY_NAME);
        String email = user.get(SessionManagement.KEY_EMAIL);


        first = (EditText) findViewById(R.id.first_sub);
        first.setText("");
        last = (EditText) findViewById(R.id.last_sub);
        last.setText("");
        id = (EditText) findViewById(R.id.id_sub);
        id.setText("");
        classof = (EditText) findViewById(R.id.classof_sub);
        classof.setText("");
        teacher = (EditText) findViewById(R.id.teacher);
        teacher.setText("");
        servicedate = (EditText) findViewById(R.id.servicedate_sub);
        teacher.setText("");
        hours = (EditText) findViewById(R.id.hours_sub);
        hours.setText("");
        description = (EditText) findViewById(R.id.description_sub);
        description.setText("");
        /*paid_spinner = (Spinner) findViewById(R.id.paid_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.paid_array, android.R.layout.activity_create);
            adapter.setDropDownViewResource(android.R.layout.activity_create);
            paid_spinner.setAdapter(adapter); */
        studentsig = (SignaturePad) findViewById(R.id.studentsig);

        orgname = (EditText) findViewById(R.id.orgname_sub);
        orgname.setText("");
        phonenum = (EditText) findViewById(R.id.phonenum_sub);
        phonenum.setText("");
        website = (EditText) findViewById(R.id.website_sub);
        website.setText("");
        address = (EditText) findViewById(R.id.address_sub);
        address.setText("");
        conname = (EditText) findViewById(R.id.conname_sub);
        conname.setText("");
        conemail = (EditText) findViewById(R.id.conemail_sub);
        conemail.setText("");
        condate = (EditText) findViewById(R.id.condate_sub);
        condate.setText("");
        consig = (SignaturePad) findViewById(R.id.consig);
        parentsig = (SignaturePad) findViewById(R.id.parentsig);

        submit = (Button) findViewById(R.id.submit_sub);
        drafts = (Button) findViewById(R.id.drafts_button);

        requestQueue = Volley.newRequestQueue(this);

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                URL = "http://ajuj.comlu.com/submission.php";

                Bitmap studentBitmap = studentsig.getSignatureBitmap();
                final String student_signature = convert(studentBitmap);

                Bitmap conBitmap = consig.getSignatureBitmap();
                final String con_signature = convert(conBitmap);

                Bitmap parentBitmap = parentsig.getSignatureBitmap();
                final String parent_signature = convert(parentBitmap);



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
                        hashMap.put("username",username);
                        hashMap.put("first",first.getText().toString());
                        hashMap.put("last",last.getText().toString());
                        hashMap.put("id", id.getText().toString());
                        hashMap.put("class", classof.getText().toString());
                        hashMap.put("teacher", teacher.getText().toString());
                        hashMap.put("servicedate", servicedate.getText().toString());
                        hashMap.put("hours", hours.getText().toString());
                        hashMap.put("log", "yes");
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("paid", "no");
                        hashMap.put("studentsig",student_signature);
                        hashMap.put("orgname", orgname.getText().toString());
                        hashMap.put("phonenum", phonenum.getText().toString());
                        hashMap.put("website",website.getText().toString());
                        hashMap.put("address",address.getText().toString());
                        hashMap.put("conname",conname.getText().toString());
                        hashMap.put("conemail",conemail.getText().toString());
                        hashMap.put("consig", con_signature);
                        hashMap.put("date",condate.getText().toString());
                        hashMap.put("parsig",parent_signature);
                        return hashMap;
                    }

                };

                requestQueue.add(request);
            }
        });

        drafts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                URL = "http://ajuj.comlu.com/draft.php";

                Bitmap studentBitmap = studentsig.getSignatureBitmap();
                final String student_signature = convert(studentBitmap);

                Bitmap conBitmap = consig.getSignatureBitmap();
                final String con_signature = convert(conBitmap);

                Bitmap parentBitmap = parentsig.getSignatureBitmap();
                final String parent_signature = convert(parentBitmap);


                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("username", username);
                        hashMap.put("first", first.getText().toString());
                        hashMap.put("last", last.getText().toString() + "");
                        hashMap.put("id", id.getText().toString() + "");
                        hashMap.put("class", classof.getText().toString() + "");
                        hashMap.put("teacher", teacher.getText().toString() + "");
                        hashMap.put("servicedate", servicedate.getText().toString() + "");
                        hashMap.put("hours", hours.getText().toString() + "");
                        hashMap.put("log", "yes");
                        hashMap.put("description", description.getText().toString() + "");
                        hashMap.put("paid", "no");
                        hashMap.put("studentsig", student_signature);
                        hashMap.put("orgname", orgname.getText().toString()  +"");
                        hashMap.put("phonenum", phonenum.getText().toString()  + "");
                        hashMap.put("website", website.getText().toString() + "");
                        hashMap.put("address", address.getText().toString() + "");
                        hashMap.put("conname", conname.getText().toString() + "");
                        hashMap.put("conemail", conemail.getText().toString()  +"");
                        hashMap.put("consig", con_signature);
                        hashMap.put("date", condate.getText().toString() + "");
                        hashMap.put("parsig", parent_signature);
                        return hashMap;
                    }

                };

            requestQueue.add(request);
        }
    });


}

    public String convert(Bitmap bitmap) { //bitmap->byte array->base64 string
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        String str = Base64.encodeToString(bitmapdata, flags);
        return str;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home_create) {
            startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
        } else if (id == R.id.nav_profile_create) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_create_create) {
           // startActivity(new Intent(getApplicationContext(), Create.class));
        } else if (id == R.id.nav_drafts_create) {

        } else if (id == R.id.nav_log_create) {
            startActivity(new Intent(getApplicationContext(), Log.class));
        } else if (id == R.id.nav_logout_create) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
