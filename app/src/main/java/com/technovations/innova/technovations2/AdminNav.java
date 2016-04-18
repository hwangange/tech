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

import com.technovations.innova.technovations2.AdminProfile;
import com.technovations.innova.technovations2.AdminReview;
import com.technovations.innova.technovations2.R;
import com.technovations.innova.technovations2.SessionManagement;

import java.util.HashMap;

public class AdminNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private com.technovations.innova.technovations2.SessionManagement session;
    private TextView navDrawerStudentName, navDrawerStudentUsername, navDrawerWelcome;
    private Button profileButton, reviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new com.technovations.innova.technovations2.SessionManagement(getApplicationContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(com.technovations.innova.technovations2.SessionManagement.KEY_USERNAME);
        String name = user.get(com.technovations.innova.technovations2.SessionManagement.KEY_NAME);
        String email = user.get(com.technovations.innova.technovations2.SessionManagement.KEY_EMAIL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_welcome_nav, null);
        navigationView.addHeaderView(header);


        navDrawerStudentName = (TextView) header.findViewById(R.id.navDrawerStudentName);
        navDrawerStudentUsername = (TextView) header.findViewById(R.id.navDrawerStudentUsername);

        navDrawerStudentName.setText(name);
        navDrawerStudentUsername.setText(username);

        //navDrawerStudentName = (TextView) findViewById(R.id.navDrawerStudentName);
        //navDrawerStudentUsername = (TextView) findViewById(R.id.navDrawerStudentUsername);
        navDrawerWelcome = (TextView) findViewById(R.id.navDrawerWelcome);

        //   navDrawerStudentName.setText(Html.fromHtml("" + name)); //i   crie
        // navDrawerStudentUsername.setText(Html.fromHtml(username));
        navDrawerWelcome.setText(Html.fromHtml("Welcome <b>" + username + "</b>"));

        profileButton = (Button)findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.technovations.innova.technovations2.AdminProfile.class));
            }
        });
        reviewButton = (Button)findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.technovations.innova.technovations2.AdminReview.class));
            }
        });

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
        getMenuInflater().inflate(R.menu.admin_nav, menu);
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

        if (id == R.id.nav_admin_home) {
            //startActivity(new Intent(getApplicationContext(), WelcomeNav.class));
        } else if (id == R.id.nav_admin_profile) {
            startActivity(new Intent(getApplicationContext(), com.technovations.innova.technovations2.AdminProfile.class));
        } else if (id == R.id.nav_admin_review) {
            startActivity(new Intent(getApplicationContext(), com.technovations.innova.technovations2.AdminReview.class));
        }
        else if (id == R.id.nav_admin_logout) {
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}