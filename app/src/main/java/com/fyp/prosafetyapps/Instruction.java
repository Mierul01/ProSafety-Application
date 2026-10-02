package com.fyp.prosafetyapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Instruction extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        drawerLayout = findViewById(R.id.drawer_layout);
        DrawerUi.configure(drawerLayout, 0);
    }

    /*----------------------------- DRAWER LAYOUT --------------------------*/

    public void ClickMenu(View view){
        MainView.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){

        MainView.closeDrawer(drawerLayout);
    }

    public void ClickMain(View view){

        MainView. redirectActivity(this,MainPage.class);
    }

    public void ClickHome(View view){

        MainView. redirectActivity(this,MainView.class);
    }

    public void ClickNews(View view){

        MainView. redirectActivity(this,NewsPage.class);
    }

    public void ClickTrack(View view){

        MainView.redirectActivity(this,GPS.class);
    }

    public void ClickProfile(View view){

        MainView.redirectActivity(this,Profile.class);
    }

    public void ClickAboutUs(View view){

        MainView.redirectActivity(this, AboutUs.class);

    }

    public void ClickEmergency(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

    public void ClickLogout(View view){
        SafetyDialog.confirmLogout(this);
    }

    public void ClickShare(View view) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "ProSafety — your everyday safety companion. https://github.com/Mierul01/ProSafety-Application");
        startActivity(Intent.createChooser(share, "Share ProSafety"));
    }

    protected void onPause(){
        super.onPause();

        MainView.closeDrawer(drawerLayout);
    }
}
