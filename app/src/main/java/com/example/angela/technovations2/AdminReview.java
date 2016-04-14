package com.example.angela.technovations2;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminReview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String EXTRA_MESSAGE = "com.example.joy.MESSAGE";
    private RequestQueue requestQueue;

    private StringRequest request;

    private String URL = "";
    private String username, email, name;

    private SessionManagement session;

    private TabHost tabhost;

    private ListView tab1;

    private RelativeLayout view1;

    List<Map<String, String>> reviewedList;

    private ArrayList reviewedID;

    private BaseAdapter simpleAdapter;
    private TextView navDrawerStudentName, navDrawerStudentUsername;

    String[] from = {"orgname", "servicedate", "description"};
    int[] to = {R.id.titleLogItem, R.id.dateLogItem, R.id.textLogItem};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_review);
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

        reviewedList = new ArrayList<Map<String, String>>();

        reviewedID = new ArrayList<String>();



        requestQueue = Volley.newRequestQueue(this);

        tab1 = (ListView) findViewById(R.id.tab1);

        view1 = new RelativeLayout(getApplicationContext());

        simpleAdapter = new SimpleAdapter(this, reviewedList,
                R.layout.reviewed_list_items,
                from, to);
        tab1.setAdapter(simpleAdapter);
        tab1.setAdapter(simpleAdapter);


        String reviewed_url = "http://ajuj.comlu.com/review.php";

        getReviewedContent(reviewed_url);

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
        getMenuInflater().inflate(R.menu.admin_review, menu);
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

        if (id == R.id.nav_admin_home_admin_review) {
            startActivity(new Intent(getApplicationContext(), AdminNav.class));
        } else if (id == R.id.nav_admin_profile_admin_review) {
            startActivity(new Intent(getApplicationContext(), AdminProfile.class));
        } else if (id == R.id.nav_admin_review_admin_review) {
            startActivity(new Intent(getApplicationContext(), AdminReview.class));
        } else if (id == R.id.nav_admin_logout_admin_review) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getReviewedContent(String url) {

        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    reviewedList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("success")){
                        int length = jsonObject.getInt("length");
                        for(int i = 0; i < length; i++) {

                            JSONObject row = jsonObject.getJSONObject(i+"");
                            String uniqueid = row.getString("uniqueid");
                            reviewedID.add(uniqueid);

                            String servicedate = row.getString("servicedate");
                            int hours = row.getInt("hours");
                            String description = row.getString("description");
                            String orgname = row.getString("orgname");
                            String conname = row.getString("conname");

                            reviewedList.add(createForm(uniqueid, servicedate, description, orgname));
                            simpleAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }else{
                        if(jsonObject.has("empty")) {
                            Toast.makeText(getApplicationContext(), "EMPTY: " + jsonObject.getString("empty"), Toast.LENGTH_SHORT).show();
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

        //perform listView item click event
        tab1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewReviewedForm.class);
                intent.putExtra(EXTRA_MESSAGE, reviewedID.get(i).toString());
                startActivity(intent);
            }
        });
    }



    private HashMap<String, String> createForm(String uniqueid, String servicedate, String description, String orgname) {
        HashMap<String, String> formNameID = new HashMap<String, String>();
        formNameID.put("orgname", orgname);
        formNameID.put("servicedate", servicedate);
        formNameID.put("description", description);
        formNameID.put("uniqueid", uniqueid);
        return formNameID;
    }
}