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

public class Log extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String EXTRA_MESSAGE = "com.example.angela.MESSAGE";
    private RequestQueue requestQueue;

    private StringRequest request;

    private String URL = "";
    private String username, email, name;

    private SessionManagement session;

    private TabHost tabhost;

    private ListView tab1, tab2, tab3;

    private RelativeLayout view1, view2, view3;

    List<Map<String, String>> approvedList, deniedList, pendingList;

    private ArrayList approvedID, deniedID, pendingID;

    private String[] from = {"orgname", "servicedate", "description"};
    private int[] to = {R.id.titleLogItem, R.id.dateLogItem, R.id.textLogItem};

    private BaseAdapter simpleAdapterPending, simpleAdapterApproved, simpleAdapterDenied;

    private TextView navDrawerStudentName, navDrawerStudentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
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

        approvedList = new ArrayList<Map<String, String>>();
        deniedList = new ArrayList<Map<String, String>>();
        pendingList = new ArrayList<Map<String, String>>();

        approvedID = new ArrayList<String>();
        deniedID = new ArrayList<String>();
        pendingID = new ArrayList<String>();

        tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();
        TabHost.TabSpec approved = tabhost.newTabSpec("Approved");
        TabHost.TabSpec denied = tabhost.newTabSpec("Denied");
        TabHost.TabSpec pending = tabhost.newTabSpec("Pending");

        requestQueue = Volley.newRequestQueue(this);

        tab1 = (ListView) findViewById(R.id.tab1);
        tab2 = (ListView) findViewById(R.id.tab2);
        tab3 = (ListView) findViewById(R.id.tab3);


        view1 = new RelativeLayout(getApplicationContext());
        view2 = new RelativeLayout(getApplicationContext());
        view3 = new RelativeLayout(getApplicationContext());

        simpleAdapterApproved = new SimpleAdapter(this, approvedList,
                R.layout.approved_list_items,
                from, to);
        tab1.setAdapter(simpleAdapterApproved);

        simpleAdapterDenied = new SimpleAdapter(this, deniedList,
                R.layout.denied_list_items,
                from, to);
        tab2.setAdapter(simpleAdapterDenied);

        simpleAdapterPending = new SimpleAdapter(this, pendingList,
                R.layout.pending_list_items,
                from, to);
        tab3.setAdapter(simpleAdapterPending);


        String approved_url = "http://ajuj.comlu.com/approved.php/?user=" + username;
        String denied_url = "http://ajuj.comlu.com/denied.php/?user=" + username;
        String pending_url = "http://ajuj.comlu.com/pending.php/?user=" + username;
        getApprovedContent(approved_url);
        getDeniedContent(denied_url);
        getPendingContent(pending_url);

        approved.setIndicator("Approved");
        approved.setContent(tab1.getId());
        denied.setIndicator("Denied");
        denied.setContent(tab2.getId());
        pending.setIndicator("Pending");
        pending.setContent(tab3.getId());

        tabhost.addTab(approved);
        tabhost.addTab(denied);
        tabhost.addTab(pending);
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
        getMenuInflater().inflate(R.menu.log, menu);
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

        if (id == R.id.nav_home_log) {
            startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
        } else if (id == R.id.nav_profile_log) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_create_log) {
            startActivity(new Intent(getApplicationContext(), Create.class));
        } else if (id == R.id.nav_drafts_log) {
            startActivity(new Intent(getApplicationContext(), Drafts.class));
        } else if (id == R.id.nav_log_log) {
            //startActivity(new Intent(getApplicationContext(), Log.class));
        } else if (id == R.id.nav_logout_log) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getApprovedContent(String url) {

        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    approvedList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        int length = jsonObject.getInt("length");
                        for(int i = 0; i < length; i++) {

                            JSONObject row = jsonObject.getJSONObject(i+"");
                            String uniqueid = row.getString("uniqueid");
                            approvedID.add(uniqueid);

                            String servicedate = row.getString("servicedate");
                            int hours = row.getInt("hours");
                            String description = row.getString("description");
                            String orgname = row.getString("orgname");
                            String conname = row.getString("conname");

                            approvedList.add(createForm(uniqueid, servicedate, description, orgname));
                            simpleAdapterApproved.notifyDataSetChanged();
                        }
                        //Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }else{
                        if(jsonObject.names().get(0).equals("empty")) {
                           // Toast.makeText(getApplicationContext(),"EMPTY: "+jsonObject.getString("empty"),Toast.LENGTH_SHORT).show();
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

        //perform listView item click event
        tab1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewForm.class);
                intent.putExtra(EXTRA_MESSAGE, "approved " + approvedID.get(i).toString());
                startActivity(intent);
            }
        });
    }

    public void getDeniedContent(String url) {

        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    deniedList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        int length = jsonObject.getInt("length");
                        for(int i = 0; i < length; i++) {

                            JSONObject row = jsonObject.getJSONObject(i+"");
                            String uniqueid = row.getString("uniqueid");
                            deniedID.add(uniqueid);

                            String servicedate = row.getString("servicedate");
                            int hours = row.getInt("hours");
                            String description = row.getString("description");
                            String orgname = row.getString("orgname");
                            String conname = row.getString("conname");

                            deniedList.add(createForm(uniqueid, servicedate, description, orgname));
                            simpleAdapterDenied.notifyDataSetChanged();
                        }
                        //Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }else{
                        if(jsonObject.names().get(0).equals("empty")) {
                            //Toast.makeText(getApplicationContext(),"EMPTY: "+jsonObject.getString("empty"),Toast.LENGTH_SHORT).show();
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

        //perform listView item click event
        tab2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewForm.class);
                intent.putExtra(EXTRA_MESSAGE, "denied " + deniedID.get(i).toString());
                startActivity(intent);
            }
        });
    }

    public void getPendingContent(String url) {
        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    pendingList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        int length = jsonObject.getInt("length");
                        for(int i = 0; i < length; i++) {

                            JSONObject row = jsonObject.getJSONObject(i+"");
                            String uniqueid = row.getString("uniqueid");
                            pendingID.add(uniqueid);
                            String servicedate = row.getString("servicedate");
                            int hours = row.getInt("hours");
                            String description = row.getString("description");
                            String orgname = row.getString("orgname");
                            String conname = row.getString("conname");

                            pendingList.add(createForm(uniqueid, servicedate, description, orgname));
                            simpleAdapterPending.notifyDataSetChanged();
                        }
                       // Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }else{
                        if(jsonObject.names().get(0).equals("empty")) {
                            //Toast.makeText(getApplicationContext(),"EMPTY: "+jsonObject.getString("empty"),Toast.LENGTH_SHORT).show();
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

        //perform listView item click event
        tab3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewForm.class);
                intent.putExtra(EXTRA_MESSAGE, "submission " + pendingID.get(i).toString());
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
