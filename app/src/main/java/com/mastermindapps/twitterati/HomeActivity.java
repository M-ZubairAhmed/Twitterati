package com.mastermindapps.twitterati;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
     String q = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        Log.e("aaaaaaaaaaaaaaaaaaaaaa", token);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = getSearchQuery();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getDataFromMainAc();
    }

    void getDataFromMainAc() {
        Bundle bundle = getIntent().getExtras();
        TextView userNameTextV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_name_xml);
        userNameTextV.setText(bundle.getString("UserName"));
        TextView userHandleTextV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_handle_xml);
        String userHandle = bundle.getString("UserHandle");
        String at = "@";
        userHandleTextV.setText(at.concat(userHandle));
        ImageView userPicIV = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_pic_xml);
        String userPicURL = bundle.getString("UserPic");
        ImageView userCoverIV = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.twitter_user_cover_xml);
        Picasso.with(this)
                .load(userPicURL.replace("_normal", ""))
                .resize(150, 150)
                .into(userPicIV);
        String userCoverURL = bundle.getString("UserCover");
        Picasso.with(this)
                .load(userCoverURL)
                .fit()
                .into(userCoverIV);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_report:
                break;
            case R.id.action_logout:
                logoutTwitter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:
                break;
            case R.id.nav_timeline:
                break;
            case R.id.nav_search:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_help:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void logoutTwitter() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        Intent gobackSignin = new Intent(HomeActivity.this, SigninActivity.class);
        startActivity(gobackSignin);
        finish();
        Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_LONG).show();
    }

    String getSearchQuery() {
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View alertdialogCustomView = inflater.inflate(R.layout.search_alertdia_layout, null);
        final EditText queryEditT = (EditText) alertdialogCustomView.findViewById(R.id.search_query_editt_xml);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(alertdialogCustomView)
                .setTitle("Enter your search query")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        q = queryEditT.getText().toString().trim().toLowerCase();
                        Toast.makeText(HomeActivity.this,q,Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        alertDialog.show();
        return q;
    }
}

