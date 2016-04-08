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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

public class viewDraft extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText first, last, id, classof, teacher, servicedate, hours, description, orgname, phonenum, website, address, conname, conemail, condate;
    private SignaturePad studentsig, consig, parentsig;
    private Spinner paid_spinner;

    private Button submit, drafts;
    private RequestQueue requestQueue;

    private StringRequest request;

    private String URL = "", uniqueId;

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


        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                uniqueId = null;
            } else {
                uniqueId = extras.getString("UNIQUE_ID");
            }
        } else {
            uniqueId = (String) savedInstanceState.getSerializable("UNIQUE_ID");
        }

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

        //submit = (Button) findViewById(R.id.submit_sub);
        //drafts = (Button) findViewById(R.id.drafts_button);

        requestQueue = Volley.newRequestQueue(this);

        String drafts_url = "http://ajuj.comlu.com/viewDraft.php/?uniqueIDmessage=" + uniqueId;

        getDraftInfo(drafts_url, uniqueId);




        /*first.setText(first1+"");
        last.setText(last1+"");
        id.setText(id1+"");
        classof.setText(classof1+"");
        teacher.setText(teacher1+"");
        servicedate.setText(servicedate1+"");
        hours.setText(hours1+"");
        //log.setText(log1+"");
        description.setText(description1+"");
        orgname.setText(orgname1+"");
        phonenum.setText(phonenum1+"");
        website.setText(website1+"");
        address.setText(address1+"");
        conname.setText(conname1+"");
        conemail.setText(conemail1+"");
        condate.setText(condate1+"");*/

        /*submit.setOnClickListener(new View.OnClickListener(){

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
                                startActivity(new Intent(getApplicationContext(), Welcome.class));
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
                        hashMap.put("orgname", orgname.getText().toString() + "");
                        hashMap.put("phonenum", phonenum.getText().toString() + "");
                        hashMap.put("website", website.getText().toString() + "");
                        hashMap.put("address", address.getText().toString() + "");
                        hashMap.put("conname", conname.getText().toString() + "");
                        hashMap.put("conemail", conemail.getText().toString() + "");
                        hashMap.put("consig", con_signature);
                        hashMap.put("date", condate.getText().toString() + "");
                        hashMap.put("parsig", parent_signature);
                        return hashMap;
                    }

                };

                requestQueue.add(request);
            }
        });*/




    }

    public void getDraftInfo(String url, final String uniqueIDmessage){

        /*request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
                try{
                    Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    int length = jsonObject.getInt("length");

                    //for(int i = 0;i<length;i++) {
                        JSONObject r = jsonObject.getJSONObject("0");
                        //String uniqueid1 = r.getString("uniqueid");
                        if(jsonObject.names().get(0).equals("success")){
                            Toast.makeText(getApplicationContext(), "UNIQUE ID"+uniqueId, Toast.LENGTH_SHORT).show();
                            first.setText(r.getString("first"), TextView.BufferType.EDITABLE);
                            last.setText(r.getString("last"), TextView.BufferType.EDITABLE);
                            id.setText(r.getString("id"), TextView.BufferType.EDITABLE);
                            classof.setText(r.getString("class"), TextView.BufferType.EDITABLE);
                            teacher.setText(r.getString("teacher"), TextView.BufferType.EDITABLE);
                            servicedate.setText(r.getString("servicedate"), TextView.BufferType.EDITABLE);
                            hours.setText(r.getString("hours"), TextView.BufferType.EDITABLE);
                            description.setText(r.getString("description"), TextView.BufferType.EDITABLE);
                            orgname.setText(r.getString("orgname"), TextView.BufferType.EDITABLE);
                            phonenum.setText(r.getString("phonenum"), TextView.BufferType.EDITABLE);
                            website.setText(r.getString("website"), TextView.BufferType.EDITABLE);
                            address.setText(r.getString("address"), TextView.BufferType.EDITABLE);
                            conname.setText(r.getString("conname"), TextView.BufferType.EDITABLE);
                            conemail.setText(r.getString("conemail"), TextView.BufferType.EDITABLE);
                            condate.setText(r.getString("date"), TextView.BufferType.EDITABLE);
                            String username1 = r.getString("username");
                            String first1 = r.getString("first");
                            String last1 = r.getString("last");
                            String id1 = r.getString("id");
                            String classof1 = r.getString("class");
                            String teacher1 = r.getString("teacher");
                            String servicedate1 = r.getString("servicedate");
                            String hours1 = r.getString("hours");
                            String description1 = r.getString("description");
                            String orgname1 = r.getString("orgname");
                            String phonenum1 = r.getString("phonenum");
                            String website1 = r.getString("website");
                            String address1 = r.getString("address");
                            String conname1 = r.getString("conname");
                            String conemail1 = r.getString("conemail");
                            String condate1 = r.getString("date");

                            first.setText(first1+"");
                            last.setText(last1+"");
                            id.setText(id1+"");
                            classof.setText(classof1+"");
                            teacher.setText(teacher1+"");
                            servicedate.setText(servicedate1+"");
                            hours.setText(hours1+"");
                            //log.setText(log1+"");
                            description.setText(description1+"");
                            orgname.setText(orgname1+"");
                            phonenum.setText(phonenum1+"");
                            website.setText(website1+"");
                            address.setText(address1+"");
                            conname.setText(conname1+"");
                            conemail.setText(conemail1+"");
                            condate.setText(condate1+"");

                            first.setText(first1);
                            last.setText(last1);
                            id.setText(id1);
                            classof.setText(classof1);
                            teacher.setText(teacher1);
                            servicedate.setText(servicedate1);
                            hours.setText(hours1);
                            //log.setText(log1+"");
                            description.setText(description1);
                            orgname.setText(orgname1);
                            phonenum.setText(phonenum1);
                            website.setText(website1);
                            address.setText(address1);
                            conname.setText(conname1);
                            conemail.setText(conemail1);
                            condate.setText(condate1);

                            //Toast.makeText(getApplicationContext(), "MadefORM", Toast.LENGTH_SHORT).show();
                            draftsList.add(createForm(username1, first1, last1, id1, classof1, teacher1, servicedate1,
                                    hours1, description1, orgname1, phonenum1, website1, address1, conname1,
                                    conemail1, condate1));

                            Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        }else{
                            if(jsonObject.names().get(0).equals("empty")) {
                            Toast.makeText(getApplicationContext(),"EMPTY: "+jsonObject.getString("empty"),Toast.LENGTH_SHORT).show();
                            }else {
                            Toast.makeText(getApplicationContext(), "ERROR: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    //}
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }

        });
        requestQueue.add(request);*/


        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        Toast.makeText(getApplicationContext(),"SUCCESS: "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < 1; i++) {
                            JSONObject row = jsonObject.getJSONObject(i + "");
                            first.setText(row.getString("first"), TextView.BufferType.EDITABLE);
                            last.setText(row.getString("last"), TextView.BufferType.EDITABLE);
                            id.setText(String.valueOf(row.getInt("id")), TextView.BufferType.EDITABLE);
                            classof.setText(row.getString("class"), TextView.BufferType.EDITABLE);
                            teacher.setText(row.getString("teacher"), TextView.BufferType.EDITABLE);
                            servicedate.setText(row.getString("servicedate"), TextView.BufferType.EDITABLE);
                            hours.setText(String.valueOf(row.getInt("hours")), TextView.BufferType.EDITABLE);
                            description.setText(row.getString("description"), TextView.BufferType.EDITABLE);
                            orgname.setText(row.getString("orgname"), TextView.BufferType.EDITABLE);
                            phonenum.setText(String.valueOf(row.getInt("phonenum")), TextView.BufferType.EDITABLE);
                            website.setText(row.getString("website"), TextView.BufferType.EDITABLE);
                            address.setText(row.getString("address"), TextView.BufferType.EDITABLE);
                            conname.setText(row.getString("conname"), TextView.BufferType.EDITABLE);
                            conemail.setText(row.getString("conemail"), TextView.BufferType.EDITABLE);
                            condate.setText(row.getString("date"), TextView.BufferType.EDITABLE);

                        }

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
                hashMap.put("uniqueIDmessage",uniqueIDmessage);

                return hashMap;
            }

        };
        //, "id", "classof", "teacher", "servicedate", "hours", "description", "orgname", "phonenum", "website", "address", "conname", "conemail", "condate"
        //, R.id.id_sub, R.id.classof_sub, R.id.teacher, R.id.servicedate_sub, R.id.hours_sub, R.id.description_sub,R.id.orgname_sub, R.id.phonenum_sub, R.id.website_sub, R.id.address_sub, R.id.conname_sub, R.id.conemail_sub, R.id.condate_sub

        // list.add(createForm("first", "last", "id", "classthird", "teacher", "date", "horus", "desc", "orgna", "phone", "website", "add", "conname", "conemail", "date"));

        //Toast.makeText(getApplicationContext(),"Length: " + list.size(),Toast.LENGTH_LONG).show();


        requestQueue.add(request);
    }

    public void putInfo(HashMap<String, String> drafts){
        first.setText(drafts.get("first") + "");
        /*last.setText(drafts.get("last") + "");
        id.setText(drafts.get("id") + "");
        classof.setText(drafts.get("class") + "");
        teacher.setText(drafts.get("teacher") + "");
        servicedate.setText(drafts.get("servicedate") + "");
        hours.setText(drafts.get("hours") + "");
        description.setText(drafts.get("description") + "");
        orgname.setText(drafts.get("orgname") + "");
        phonenum.setText(drafts.get("phonenum") + "");
        website.setText(drafts.get("website")+"");
        address.setText(drafts.get("address")+"");
        conname.setText(drafts.get("conname")+"");
        conemail.setText(drafts.get("conemail")+"");
        condate.setText(drafts.get("condate")+"");*/
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

    /*public HashMap<String, String> createForm(String username, String first1, String last1, String id1, String classof1,
                                              String teacher1, String servicedate1, String hours1, String description1,
                                              String orgname1, String phonenum1, String website1, String address1, String conname1,
                                              String conemail1,  String condate1) {
        first.setText(first1+"");
        last.setText(last1+"");
        id.setText(id1+"");
        classof.setText(classof1+"");
        teacher.setText(teacher1+"");
        servicedate.setText(servicedate1+"");
        hours.setText(hours1+"");
        //log.setText(log1+"");
        description.setText(description1+"");
        orgname.setText(orgname1+"");
        phonenum.setText(phonenum1+"");
        website.setText(website1+"");
        address.setText(address1+"");
        conname.setText(conname1+"");
        conemail.setText(conemail1+"");
        condate.setText(condate1+"");
        Toast.makeText(getApplicationContext(), "MadefORM: ", Toast.LENGTH_SHORT).show();
        HashMap<String, String> formNameID = new HashMap<String, String>();
        formNameID.put("username", username);
        formNameID.put("first", first1);
        formNameID.put("last", last1);
        formNameID.put("id", id1);
        formNameID.put("class", classof1);
        formNameID.put("teacher", teacher1);
        formNameID.put("servicedate", servicedate1);
        formNameID.put("hours", hours1);
        formNameID.put("description", description1);
        formNameID.put("orgname", orgname1);
        formNameID.put("phonenum", phonenum1);
        formNameID.put("website", website1);
        formNameID.put("address", address1);
        formNameID.put("conname", conname1);
        formNameID.put("conemail", conemail1);
        formNameID.put("date", condate1);
        Toast.makeText(getApplicationContext(), "FORM NAME ID MADE", Toast.LENGTH_SHORT).show();
        return formNameID;
    }*/

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

        if (id == R.id.nav_home_draft) {
            startActivity(new Intent(getApplicationContext(), Welcome.class));
        } else if (id == R.id.nav_profile_draft) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_create_draft) {
            // startActivity(new Intent(getApplicationContext(), Create.class));
        } else if (id == R.id.nav_drafts_draft) {
            startActivity(new Intent(getApplicationContext(), Drafts.class));
        } else if (id == R.id.nav_log_draft) {
            startActivity(new Intent(getApplicationContext(), Log.class));
        } else if (id == R.id.nav_logout_draft) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}