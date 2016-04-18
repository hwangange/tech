package com.technovations.innova.technovations2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technovations.innova.technovations2.*;
import com.technovations.innova.technovations2.Create;
import com.technovations.innova.technovations2.Drafts;
import com.technovations.innova.technovations2.Log;
import com.technovations.innova.technovations2.Profile;
import com.technovations.innova.technovations2.SessionManagement;
import com.technovations.innova.technovations2.WelcomeNav;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ViewForm extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private com.technovations.innova.technovations2.SessionManagement session;
    private String username, email, name;

    private TextView first, last, id, classof, teacher, servicedate, hours, description, orgname, phonenum, website, address, conname, conemail, condate;
    private SignaturePad studentsig, consig, parentsig;

    private String URL = "";

    String[] from = {"first", "last", "id", "classof", "teacher", "servicedate", "hours", "description", "orgname", "phonenum", "website", "address", "conname", "conemail", "condate", "comment"};
    int[] to = {R.id.first_sub, R.id.last_sub, R.id.id_sub, R.id.classof_sub, R.id.teacher, R.id.servicedate_sub, R.id.hours_sub, R.id.description_sub,R.id.orgname_sub, R.id.phonenum_sub, R.id.website_sub, R.id.address_sub, R.id.conname_sub, R.id.conemail_sub, R.id.condate_sub, R.id.comment};

    BaseAdapter simpleAdapter;

    private RequestQueue requestQueue;
    private StringRequest request;

    private String database = "";
    private String uniqueIDmessage = "";

    private ListView tab1;

    private HashMap<String, String> finalForm;

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private TextView navDrawerStudentName, navDrawerStudentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new com.technovations.innova.technovations2.SessionManagement(getApplicationContext());
        session.checkLogin();

        tab1 = (ListView) findViewById(R.id.tab1);

        simpleAdapter = new SimpleAdapter(this, list, R.layout.view_form_layout, from, to);
        tab1.setAdapter(simpleAdapter);

        requestQueue = Volley.newRequestQueue(this);

        HashMap<String, String> user = session.getUserDetails();
        username = user.get(com.technovations.innova.technovations2.SessionManagement.KEY_USERNAME);
        name = user.get(com.technovations.innova.technovations2.SessionManagement.KEY_NAME);
        email = user.get(SessionManagement.KEY_EMAIL);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_welcome_nav, null);
        navigationView.addHeaderView(header);


        navDrawerStudentName = (TextView) header.findViewById(R.id.navDrawerStudentName);
        navDrawerStudentUsername = (TextView) header.findViewById(R.id.navDrawerStudentUsername);

        navDrawerStudentName.setText(name);
        navDrawerStudentUsername.setText(username);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Log.EXTRA_MESSAGE);
     //   Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        StringTokenizer st = new StringTokenizer(message);
        database = st.nextToken();
        uniqueIDmessage = st.nextToken();
        URL = "http://ajuj.comlu.com/ViewForm.php/?database="+database+"&uniqueIDmessage="+uniqueIDmessage;

        getForm(database, uniqueIDmessage);
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
        getMenuInflater().inflate(R.menu.view_form, menu);
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

        if (id == R.id.nav_home_form) {
            startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
        } else if (id == R.id.nav_profile_form) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_create_form) {
            startActivity(new Intent(getApplicationContext(), Create.class));
        } else if (id == R.id.nav_drafts_form) {
            startActivity(new Intent(getApplicationContext(), Drafts.class));
        } else if (id == R.id.nav_log_form) {
            startActivity(new Intent(getApplicationContext(), Log.class));
        } else if (id == R.id.nav_logout_form) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getForm(final String database, final String uniqueIDmessage) {

        final List<Map<String, String>> temp = new ArrayList<Map<String, String>>();

        request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("success")){
                      //  Toast.makeText(getApplicationContext(),"SUCCESS: "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < 1; i++) {
                            JSONObject row = jsonObject.getJSONObject(i + "");
                            String username = row.getString("username");
                            String first = row.getString("first");
                            String last = row.getString("last");
                            String id = String.valueOf(row.getInt("id"));
                            String classThird = String.valueOf(row.getInt("class"));
                            String teacher = row.getString("teacher");
                            String servicedate = row.getString("servicedate");
                            String hours = String.valueOf(row.getInt("hours"));
                            String description = row.getString("description");
                            String studentsig = row.getString("studentsig");
                            String orgname = row.getString("orgname");
                            String phonenum = String.valueOf(row.getInt("phonenum"));
                            String website = row.getString("website");
                            String address = row.getString("address");
                            String conname = row.getString("conname");
                            String conemail = row.getString("conemail");
                            String consig = row.getString("consig");
                            String date = row.getString("date");
                            String parsig = row.getString("parsig");
                            String comment = row.getString("comment");

                            if(database.equals("denied"))
                                comment = "Comments: " + comment;

                            list.add(createForm(first, last , id, classThird, teacher, servicedate, hours, description, orgname, phonenum, website, address, conname, conemail, date, comment));

                            simpleAdapter.notifyDataSetChanged();

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
                hashMap.put("database", database);
                hashMap.put("uniqueIDmessage",uniqueIDmessage);

                return hashMap;
            }

        };
        //, "id", "classof", "teacher", "servicedate", "hours", "description", "orgname", "phonenum", "website", "address", "conname", "conemail", "condate"
        //, R.id.id_sub, R.id.classof_sub, R.id.teacher, R.id.servicedate_sub, R.id.hours_sub, R.id.description_sub,R.id.orgname_sub, R.id.phonenum_sub, R.id.website_sub, R.id.address_sub, R.id.conname_sub, R.id.conemail_sub, R.id.condate_sub
        list.clear();

       // list.add(createForm("first", "last", "id", "classthird", "teacher", "date", "horus", "desc", "orgna", "phone", "website", "add", "conname", "conemail", "date"));

        //Toast.makeText(getApplicationContext(),"Length: " + list.size(),Toast.LENGTH_LONG).show();


        requestQueue.add(request);

    }

    private HashMap<String, String> createForm(String first, String last , String id, String classThird, String teacher, String servicedate, String hours, String description, String orgname, String phonenum, String website, String address, String conname, String conemail, String date, String comment) {
        HashMap<String, String> formNameID = new HashMap<String, String>();
        formNameID.put("first", first);
        formNameID.put("last", last);
        formNameID.put("id", id);
        formNameID.put("classof", classThird);
        formNameID.put("teacher", teacher);
        formNameID.put("servicedate", servicedate);
        formNameID.put("hours", hours);
        formNameID.put("description", description);
        formNameID.put("orgname", orgname);
        formNameID.put("phonenum", phonenum);
        formNameID.put("website", website);
        formNameID.put("address", address);
        formNameID.put("conname", conname);
        formNameID.put("conemail", conemail);
        formNameID.put("condate", date);
        formNameID.put("comment", comment);
        return formNameID;
    }
}
