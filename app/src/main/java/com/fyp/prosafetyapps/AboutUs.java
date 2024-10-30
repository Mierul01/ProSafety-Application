package com.fyp.prosafetyapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AboutUs extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    /*----------------------------- DRAWER LAYOUT --------------------------*/

    public void ClickMenu(View view){
        MainView.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){

        MainView.closeDrawer(drawerLayout);
    }

    public void ClickAboutUs(View view){

        recreate();

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

    public void ClickEmergency(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

    public void ClickLogout(View view){

        AlertDialog.Builder builder2 = new AlertDialog.Builder(AboutUs.this);
        builder2.setTitle("Are you sure you want Log Out?");
        builder2.setCancelable(true);

        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AboutUs.this, LogOutHandler.class));
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