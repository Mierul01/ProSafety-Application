package com.fyp.prosafetyapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;

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
        if (BuildConfig.DEBUG) {
            android.widget.Button preview = new android.widget.Button(this);
            preview.setText("Explore the design  (no login needed)");
            preview.setTextColor(getResources().getColor(R.color.safety_teal));
            preview.setTextSize(13);
            preview.setAllCaps(false);
            preview.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            ((android.widget.LinearLayout)CrdMain.getParent()).addView(preview, 0);
            preview.setOnClickListener(v -> startActivity(new Intent()
                    .setClassName(this, "com.fyp.prosafetyapps.DesignPreviewActivity")));
        }
        CrdNews = (CardView)findViewById(R.id.CrdNews);
        CrdNumber = (CardView)findViewById(R.id.CrdNumber);
        CrdTrack = (CardView)findViewById(R.id.CrdTrack);
        CrdProfile = (CardView)findViewById(R.id.CrdProfile);
        CrdInstruct = (CardView)findViewById(R.id.CrdInstruct);

        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerUi.configure(drawerLayout, R.id.navHome);

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
        SafetyDialog.confirmLogout(this);
    }

    public void ClickEmergency(View view){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:999"));
            startActivity(intent);
    }

    public static void logout(Activity activity) {
        new SafetyDialog.Builder(activity).setTitle("Close ProSafety?")
                .setMessage("You can open the app again whenever you need it.")
                .setNegativeButton("Keep open", null)
                .setPositiveButton("Close app", (dialog, which) -> activity.finishAffinity()).show();
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
