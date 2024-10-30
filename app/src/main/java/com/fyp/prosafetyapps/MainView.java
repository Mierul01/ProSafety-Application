package com.fyp.prosafetyapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainView extends AppCompatActivity {

    CardView CrdMain, CrdNews, CrdNumber, CrdTrack, CrdProfile, CrdInstruct;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        CrdMain = (CardView)findViewById(R.id.CrdMain);
        CrdNews = (CardView)findViewById(R.id.CrdNews);
        CrdNumber = (CardView)findViewById(R.id.CrdNumber);
        CrdTrack = (CardView)findViewById(R.id.CrdTrack);
        CrdProfile = (CardView)findViewById(R.id.CrdProfile);
        CrdInstruct = (CardView)findViewById(R.id.CrdInstruct);

        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);

        CrdMain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                openMain();
            }

        });

        CrdNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openNews();
            }
        });

        CrdNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:999"));
                startActivity(intent);
            }
        });

        CrdTrack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openTrack();
            }
        });

        CrdProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProfile();
            }
        });

        CrdInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openInstruction();
            }
        });

    }

    public void openMain(){
        Intent intent = new Intent( this, MainPage.class);

        startActivity(intent);
    }

    public void openNews(){
        Intent intent = new Intent(this, NewsPage.class);

        startActivity(intent);
    }

    public void openTrack(){
        Intent intent = new Intent(this, GPS.class);

        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this, Profile.class);

        startActivity(intent);
    }

    public void openInstruction(){
        Intent intent = new Intent(this, Instruction.class);

        startActivity(intent);
    }

/*--------------------------------DRAWER LAYOUT---------------------------------*/

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){

        recreate();
    }

    public void ClickMain(View view){

        redirectActivity(this, MainPage.class);
    }

    public void ClickNews(View view){

        redirectActivity(this, NewsPage.class);
    }

    public void ClickAboutUs(View view){

        redirectActivity(this, AboutUs.class);

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

    public void ClickTrack(View view){
        redirectActivity(this, GPS.class);
    }

    public void ClickProfile(View view){
        redirectActivity(this, Profile.class);
    }

    public void ClickLogout(View view){

        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainView.this);
        builder2.setTitle("Are you sure you want Log Out?");
        builder2.setCancelable(true);

        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainView.this, LogOutHandler.class));
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

    public void ClickEmergency(View view){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:999"));
            startActivity(intent);
    }

    public static void logout(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Logout");

        builder.setMessage("Are your sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();

                System.exit(0);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
