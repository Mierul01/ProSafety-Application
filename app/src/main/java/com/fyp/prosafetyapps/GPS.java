package com.fyp.prosafetyapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
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

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sSource = etsources.getText().toString().trim();
                String sDestination = etlocation.getText().toString().trim();

                if(sDestination.equals("")){

                    Toast.makeText(getApplicationContext(),"You must enter location first",Toast.LENGTH_SHORT).show();

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

        AlertDialog.Builder builder2 = new AlertDialog.Builder(GPS.this);
        builder2.setTitle("Are you sure you want Log Out?");
        builder2.setCancelable(true);

        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(GPS.this, LogOutHandler.class));
                overridePendingTransition(0,0);
                dialog.cancel();
            }
        });

        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder2.create();
        alert11.show();

    }

    protected void onPause(){
        super.onPause();

        MainView.closeDrawer(drawerLayout);
    }
}