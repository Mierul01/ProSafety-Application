package com.fyp.prosafetyapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class GPS extends AppCompatActivity {

    DrawerLayout drawerLayout;
    EditText etsources,etlocation;
    Button btTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        etsources = findViewById(R.id.et_source);
        etlocation = findViewById(R.id.et_location);
        btTrack = findViewById(R.id.bt_track);

        drawerLayout = findViewById(R.id.drawer_layout);
        DrawerUi.configure(drawerLayout, R.id.navDirections);

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sSource = etsources.getText().toString().trim();
                String sDestination = etlocation.getText().toString().trim();

                if(sDestination.equals("")){

                    SafetyFeedback.show(GPS.this, "You must enter location first");

                }else{
                    DisplayTrack(sSource,sDestination);
                }
            }
        });
    }

    private void DisplayTrack(String sSource, String sDestination) {
        try{
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" +sDestination);
            Intent intent = new Intent (Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){

            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    /*----------------------------- DRAWER LAYOUT --------------------------*/

    public void ClickMenu(View view){
        MainView.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){

        MainView.closeDrawer(drawerLayout);
    }

    public void ClickTrack(View view){

        recreate();
    }

    public void ClickHome(View view){

        MainView. redirectActivity(this,MainView.class);
    }

    public void ClickMain(View view){

        MainView. redirectActivity(this,MainPage.class);
    }

    public void ClickNews(View view){

        MainView.redirectActivity(this,NewsPage.class);
    }

    public void ClickProfile(View view){

        MainView.redirectActivity(this,Profile.class);
    }

    public void ClickAboutUs(View view){

        MainView.redirectActivity(this, AboutUs.class);

    }

    public void ClickShare(View view){

        Intent intent =  new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String Body = "Downalod this app";
        String Sub = "http://play.google.com";
        intent.putExtra(Intent.EXTRA_TEXT, Body);
        intent.putExtra(Intent.EXTRA_TEXT, Sub);
        startActivity(Intent.createChooser(intent,"Share Using"));

    }

    public void ClickEmergency(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

    public void ClickLogout(View view){
        SafetyDialog.confirmLogout(this);
    }

    protected void onPause(){
        super.onPause();

        MainView.closeDrawer(drawerLayout);
    }
}