package com.technovations.innova.technovations2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.Button;
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
import com.technovations.innova.technovations2.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManagement session;
    TextView profileUsername, profileName, profileEmail, profileYear, profileHours;
    private TextView navDrawerStudentName, navDrawerStudentUsername;
    private Button checkHours;
    String username;

    String URL = "http://ajuj.comlu.com/checkHours.php";
    private RequestQueue requestQueue;
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        profileUsername = (TextView) findViewById(R.id.profileUsername);
        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profileYear= (TextView) findViewById(R.id.profileYear);
        profileHours= (TextView) findViewById(R.id.profileHours);

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManagement.KEY_USERNAME);
        String name = user.get(SessionManagement.KEY_NAME);
        String email = user.get(SessionManagement.KEY_EMAIL);
        String year = user.get(SessionManagement.KEY_YEAR);
        final String hours = user.get(SessionManagement.KEY_HOURS);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_welcome_nav, null);
        navigationView.addHeaderView(header);


        navDrawerStudentName = (TextView) header.findViewById(R.id.navDrawerStudentName);
        navDrawerStudentUsername = (TextView) header.findViewById(R.id.navDrawerStudentUsername);

        navDrawerStudentName.setText(name);
        navDrawerStudentUsername.setText(username);

        profileUsername.setText(Html.fromHtml("<b>User: </b>" + username));
        profileName.setText(Html.fromHtml("<b>Name: </b>" + name));
        profileEmail.setText(Html.fromHtml("<b>Email: </b>" + email));
        profileYear.setText(Html.fromHtml("<b>Year: </b>" + year));
        profileHours.setText(Html.fromHtml("<b>Hours: </b> " + hours));

        requestQueue = Volley.newRequestQueue(this);
        checkHours = (Button) findViewById(R.id.hoursButton);
        checkHours.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkHours(Integer.parseInt(hours));
            }
        });


    }

    public void checkHours(final int hours) {
        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    int length = jsonObject.length();
                    for(int x = 0; x < length; x++) {
                        String name = jsonObject.names().get(x).toString();
                        Toast.makeText(getApplicationContext(),jsonObject.getString(name),Toast.LENGTH_SHORT).show();
                        if(name.equals("successUpdate")) {
                            String newHours = jsonObject.getString("totalHours");
                            session.updateHours(newHours);
                            profileHours.setText(Html.fromHtml("<b>Hours: </b> " + newHours));
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

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<String,String>();
                hashMap.put("username",username);
                hashMap.put("hours", String.valueOf(hours));

                return hashMap;
            }

        };

        requestQueue.add(request);
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
        getMenuInflater().inflate(R.menu.profile, menu);
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
        if (id == R.id.nav_home_profile) {
            startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
        } else if (id == R.id.nav_profile_profile) {
            startActivity(new Intent(getApplicationContext(), com.technovations.innova.technovations2.Profile.class));
        } else if (id == R.id.nav_create_profile) {
            startActivity(new Intent(getApplicationContext(), Create.class));
        } else if (id == R.id.nav_drafts_profile) {
            startActivity(new Intent(getApplicationContext(), Drafts.class));
        } else if (id == R.id.nav_log_profile) {
            startActivity(new Intent(getApplicationContext(), Log.class));
        } else if (id == R.id.nav_logout_profile) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
