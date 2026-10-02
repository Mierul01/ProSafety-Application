package com.fyp.prosafetyapps;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

/** Local visual review of real layouts; never creates accounts, requests permissions or sends alerts. */
public class DesignPreviewActivity extends AppCompatActivity {
    private FrameLayout canvas;
    private Spinner screens;
    private final int[] layouts = {R.layout.activity_main_view, R.layout.activity_main_page,
            R.layout.activity_gps, R.layout.activity_instruction, R.layout.activity_profile,
            R.layout.activity_sign_in, R.layout.activity_sign_up, R.layout.activity_reset_password,
            R.layout.activity_main_page, R.layout.activity_about_us, R.layout.popup_preview};
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(getResources().getColor(R.color.safety_background));
        TextView label = new TextView(this);
        label.setText("DESIGN PREVIEW  /  No account required");
        label.setTextColor(getResources().getColor(R.color.safety_teal));
        label.setTextSize(11);
        int padding = (int)(12 * getResources().getDisplayMetrics().density);
        label.setPadding(padding, padding, padding, 0);
        page.addView(label);
        screens = new Spinner(this);
        screens.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Home", "Trusted contacts", "Directions", "Safety guide", "Profile (sample)",
                        "Sign in", "Create account", "Reset password", "Contacts (sample)", "About and support", "Pop-up previews"}));
        page.addView(screens);
        canvas = new FrameLayout(this);
        page.addView(canvas, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(page);
        screens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showScreen(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    private void showScreen(int position) {
        canvas.removeAllViews();
        getLayoutInflater().inflate(layouts[position], canvas, true);
        DrawerLayout legacyDrawer=canvas.findViewById(R.id.drawer_layout);
        int[] selectedRows={R.id.navHome,R.id.navContacts,R.id.navDirections,0,R.id.navProfile,
                0,0,0,R.id.navContacts,R.id.navSupport,0};
        if (legacyDrawer!=null) DrawerUi.configure(legacyDrawer, selectedRows[position]);
        int[] cards = {R.id.CrdMain, R.id.CrdTrack, R.id.CrdInstruct, R.id.CrdProfile};
        int[] destinations = {1, 2, 3, 4};
        for (int i=0; i<cards.length; i++) {
            View card = canvas.findViewById(cards[i]);
            final int destination=destinations[i];
            if (card != null) card.setOnClickListener(v -> screens.setSelection(destination));
        }
        bindMessage(R.id.CrdNumber); bindMessage(R.id.CrdNews); bindMessage(R.id.btnSOS);
        bindMessage(R.id.torchlight); bindMessage(R.id.bt_track);
        bindMessage(R.id.btnSignIn); bindMessage(R.id.btnSignUp); bindMessage(R.id.btn_reset_password);
        ListView list = canvas.findViewById(R.id.ListView);
        if (list != null) list.setEmptyView(canvas.findViewById(R.id.contactEmpty));
        if (position == 8) {
            list.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_item_user, R.id.tvName,
                    new String[]{"Jordan (sample)", "Sam (sample)"}) {
                @Override public View getView(int position, View reused, android.view.ViewGroup parent) {
                    View row=super.getView(position, reused, parent);
                    ((TextView)row.findViewById(R.id.tvPhone)).setText("Sample contact / no phone number");
                    row.findViewById(R.id.removeContact).setOnClickListener(v -> removePreview());
                    return row;
                }
            });
        }
        if (position == 4) {
            ((TextView)canvas.findViewById(R.id.profile_name_textView)).setText("Alex Taylor");
            ((TextView)canvas.findViewById(R.id.profile_surname_textView)).setText("alex.taylor");
            ((TextView)canvas.findViewById(R.id.textViewEmailAdress)).setText("alex@example.com");
        }
        android.widget.CheckBox checkbox=canvas.findViewById(R.id.chk1);
        if (checkbox != null) checkbox.setOnCheckedChangeListener((button, checked) -> {
            android.widget.EditText field=canvas.findViewById(position==6 ? R.id.txtSignUpPass : R.id.txtSignInPass);
            if (field!=null) field.setTransformationMethod(checked ?
                    android.text.method.HideReturnsTransformationMethod.getInstance() :
                    android.text.method.PasswordTransformationMethod.getInstance());
        });
        if(position==10) {
            canvas.findViewById(R.id.previewLogout).setOnClickListener(v -> ClickLogout(v));
            canvas.findViewById(R.id.previewRemove).setOnClickListener(v -> removePreview());
            canvas.findViewById(R.id.previewError).setOnClickListener(v -> new SafetyDialog.Builder(this)
                    .setTitle("Unable to sign in").setMessage("Check your email and password, and make sure you are connected to the internet.")
                    .setPositiveButton("Try again", null).show());
            canvas.findViewById(R.id.previewSuccess).setOnClickListener(v -> new SafetyDialog.Builder(this)
                    .setEyebrow("PASSWORD RESET").setIcon(R.drawable.ic_email).setTitle("Check your inbox")
                    .setMessage("If this email is registered, you will receive a password reset link. Check your spam folder too.")
                    .setPositiveButton("Got it", null).show());
            canvas.findViewById(R.id.previewEdit).setOnClickListener(v -> buttonClickedEditName(v));
            canvas.findViewById(R.id.previewFeedback).setOnClickListener(v -> SafetyFeedback.show(this, "Your changes have been saved. (Preview)"));
        }
    }
    private void removePreview() {
        new SafetyDialog.Builder(this).setEyebrow("YOUR SAFETY CIRCLE").setIcon(R.drawable.ic_remove_contact)
                .setDestructive(true).setTitle("Remove trusted contact?")
                .setMessage("This person will no longer receive your shake-triggered help messages. You can add them again later.")
                .setNegativeButton("Keep contact", null).setPositiveButton("Remove contact", (d,w)->previewMessage()).show();
    }
    private void bindMessage(int id) {
        View view=canvas.findViewById(id);
        if(view!=null) view.setOnClickListener(v -> previewMessage());
    }
    private void previewMessage() {
        SafetyFeedback.show(this, "Design preview only. No account or contact changes were made.");
    }
    public void ClickMenu(View v) { DrawerLayout drawer=canvas.findViewById(R.id.drawer_layout); if(drawer!=null) drawer.openDrawer(GravityCompat.START); }
    private void navigate(int position) {
        DrawerLayout drawer=canvas.findViewById(R.id.drawer_layout);
        if(drawer!=null) drawer.closeDrawer(GravityCompat.START);
        screens.setSelection(position);
    }
    public void ClickHome(View v) { navigate(0); }
    public void ClickMain(View v) { navigate(1); }
    public void ClickTrack(View v) { navigate(2); }
    public void ClickAboutUs(View v) { navigate(9); }
    public void ClickProfile(View v) { navigate(4); }
    public void ClickLogo(View v) {
        DrawerLayout drawer=canvas.findViewById(R.id.drawer_layout);
        if(drawer!=null) drawer.closeDrawer(GravityCompat.START);
    }
    public void ClickNews(View v) { previewMessage(); }
    public void ClickEmergency(View v) { previewMessage(); }
    public void ClickShare(View v) { previewMessage(); }
    public void ClickLogout(View v) { SafetyDialog.logout(this, () -> previewMessage()); }
    public void NavigateSignUp(View v) { screens.setSelection(6); }
    public void navigate_sign_in(View v) { screens.setSelection(5); }
    public void NavigateForgetMyPassword(View v) { screens.setSelection(7); }
    public void toggleFlashLight(View v) { previewMessage(); }
    public void buttonClickedEditName(View v) {
        View form=getLayoutInflater().inflate(R.layout.activity_edit_name,null);
        ((android.widget.EditText)form.findViewById(R.id.et_username)).setText("Alex Taylor");
        new SafetyDialog.Builder(this).setEyebrow("YOUR PROFILE").setIcon(R.drawable.ic_person)
                .setTitle("Edit your name").setView(form).setNegativeButton("Cancel", null)
                .setPositiveButton("Save changes", (d,w)->previewMessage()).show();
    }
    public void buttonClickedEditSurname(View v) { buttonClickedEditName(v); }
    public void navigateLogOut(View v) { ClickLogout(v); }
}
