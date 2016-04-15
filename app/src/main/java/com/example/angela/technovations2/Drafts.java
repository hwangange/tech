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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
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

public class Drafts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue requestQueue;

    private StringRequest request;

    private String username, email, name;

    private SessionManagement session;

    private TabHost tabhost;

    private ListView tab1, tab2;

    private ArrayList draftID;


    private RelativeLayout view1, view2;

    List<Map<String, String>> draftsList, submittedList;
    private TextView navDrawerStudentName, navDrawerStudentUsername;

    BaseAdapter simpleAdapter;
    String[] from = {"servicedate", "description", "orgname"};
    int[] to = {R.id.dateDraftItem, R.id.textDraftItem, R.id.titleDraftItem};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
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

        draftID = new ArrayList<String>();

        draftsList = new ArrayList<Map<String, String>>();

        requestQueue = Volley.newRequestQueue(this);

        tab1 = (ListView) findViewById(R.id.tab1_draft);


        view1 = new RelativeLayout(getApplicationContext());
        view2 = new RelativeLayout(getApplicationContext());

        simpleAdapter = new SimpleAdapter(this, draftsList,
                R.layout.draft_view_list_items,
                from, to);
        tab1.setAdapter(simpleAdapter);

        String drafts_url = "http://ajuj.comlu.com/listviewdraft.php/?user=" + username;
        getDraftsContent(drafts_url);

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
        getMenuInflater().inflate(R.menu.drafts, menu);
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
            startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
        } else if (id == R.id.nav_profile_draft) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_create_draft) {
            startActivity(new Intent(getApplicationContext(), Create.class));
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

    public void getDraftsContent(String url) {

        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    draftsList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("success")){
                        int length = jsonObject.getInt("length");
                        for(int i = 0; i < length; i++) {

                            JSONObject row = jsonObject.getJSONObject(i+"");
                            String uniqueid = row.getString("uniqueid");
                            draftID.add(uniqueid);
                            String username = row.getString("username");
                            String first = row.getString("first");
                            String last = row.getString("last");
                            int id = row.getInt("id");
                            int classof = row.getInt("class");
                            String teacher = row.getString("teacher");
                            String servicedate = row.getString("servicedate");
                            int hours = row.getInt("hours");
                            String log = row.getString("log");
                            String description = row.getString("description");
                            String paid = row.getString("paid");
                            String studentsig = row.getString("studentsig");
                            String orgname = row.getString("orgname");
                            String phonenum = row.getString("phonenum");
                            String website = row.getString("website");
                            String address = row.getString("address");
                            String conname = row.getString("conname");
                            String conemail = row.getString("conemail");
                            String consig = row.getString("consig");
                            String condate = row.getString("date");
                            String parsig = row.getString("parsig");

                            if(servicedate.equals("0000-00-00"))
                                servicedate = "(No Date)";
                            if(description.equals(""))
                                description = "(No Description)";
                            if(orgname.equals(""))
                                orgname = "(Unknown)";

                            draftsList.add(createForm(uniqueid, servicedate, description, orgname));
                            simpleAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getApplicationContext(), "SUCCESS: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }else{
                        if(jsonObject.has("empty")) {
                            Toast.makeText(getApplicationContext(),"EMPTY: "+jsonObject.getString("empty"),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "drafts", Toast.LENGTH_LONG).show();//show the selected image in toast according to position
                Intent intent = new Intent(getApplicationContext(), viewDraft.class);
                intent.putExtra("UNIQUE_ID",draftID.get(i).toString());
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