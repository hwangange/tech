package com.example.angela.technovations2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
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
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ViewReviewedForm extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManagement session;
    private String username, email, name;

    private TextView first, last, id, classof, teacher, servicedate, hours, description, orgname, phonenum, website, address, conname, conemail, condate;
    private SignaturePad studentsig, consig, parentsig;

    private String URL = "";

    String[] from = {"first", "last", "id", "classof", "teacher", "servicedate", "hours", "description", "orgname", "phonenum", "website", "address", "conname", "conemail", "condate"};
    int[] to = {R.id.first_sub, R.id.last_sub, R.id.id_sub, R.id.classof_sub, R.id.teacher, R.id.servicedate_sub, R.id.hours_sub, R.id.description_sub,R.id.orgname_sub, R.id.phonenum_sub, R.id.website_sub, R.id.address_sub, R.id.conname_sub, R.id.conemail_sub, R.id.condate_sub};

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
        setContentView(R.layout.activity_view_reviewed_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        tab1 = (ListView) findViewById(R.id.tab1);

        simpleAdapter = new SimpleAdapter(this, list, R.layout.view_reviewed_form_layout, from, to);
        tab1.setAdapter(simpleAdapter);

        requestQueue = Volley.newRequestQueue(this);

        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManagement.KEY_USERNAME);
        name = user.get(SessionManagement.KEY_NAME);
        email = user.get(SessionManagement.KEY_EMAIL);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_welcome_nav, null);
        navigationView.addHeaderView(header);


        navDrawerStudentName = (TextView) header.findViewById(R.id.navDrawerStudentName);
        navDrawerStudentUsername = (TextView) header.findViewById(R.id.navDrawerStudentUsername);

        navDrawerStudentName.setText(name);
        navDrawerStudentUsername.setText(username);

        Intent intent = getIntent();
        uniqueIDmessage = intent.getStringExtra(AdminReview.EXTRA_MESSAGE);
        URL = "http://ajuj.comlu.com/ViewReviewedForm.php/?uniqueIDmessage="+uniqueIDmessage;

        getForm();
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
        getMenuInflater().inflate(R.menu.view_reviewed_form, menu);
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

        if (id == R.id.nav_admin_home_admin_form) {
            startActivity(new Intent(getApplicationContext(), AdminNav.class));
        } else if (id == R.id.nav_admin_profile_admin_form) {
            startActivity(new Intent(getApplicationContext(), AdminProfile.class));
        } else if (id == R.id.nav_admin_review_admin_form) {
            startActivity(new Intent(getApplicationContext(), AdminReview.class));
        } else if (id == R.id.nav_admin_logout_admin_form) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getForm() {

        final List<Map<String, String>> temp = new ArrayList<Map<String, String>>();

        request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
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

                            list.add(createForm(first, last , id, classThird, teacher, servicedate, hours, description, orgname, phonenum, website, address, conname, conemail, date));

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
                hashMap.put("uniqueIDmessage",uniqueIDmessage);

                return hashMap;
            }

        };
        list.clear();

        // list.add(createForm("first", "last", "id", "classthird", "teacher", "date", "horus", "desc", "orgna", "phone", "website", "add", "conname", "conemail", "date"));
        requestQueue.add(request);

    }

    private HashMap<String, String> createForm(String first, String last , String id, String classThird, String teacher, String servicedate, String hours, String description, String orgname, String phonenum, String website, String address, String conname, String conemail, String date) {
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
        return formNameID;
    }
}