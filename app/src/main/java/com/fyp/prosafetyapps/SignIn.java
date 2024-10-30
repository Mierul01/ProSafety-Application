package com.fyp.prosafetyapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText SignInUser, SignInPass;
    private FirebaseAuth auth;
    private Button btnSignIn;
    private CheckBox chk1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        chk1 = (CheckBox) findViewById(R.id.chk1);

        SignInUser = (EditText) findViewById(R.id.txtSignInEmail);
        SignInPass = (EditText) findViewById(R.id.txtSignInPass);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        auth = FirebaseAuth.getInstance();

        SharedPreferences pro = getSharedPreferences("pro", MODE_PRIVATE);
        boolean firstStart = pro.getBoolean("firstStart", true);

        if (firstStart) {
            showStartDialog();
        }

        SharedPreferences safety = getSharedPreferences("safety", MODE_PRIVATE);
        boolean secondStart = safety.getBoolean("secondStart", true);

        if (secondStart) {
            showSecondDialog();
        }

        SharedPreferences App = getSharedPreferences("App", MODE_PRIVATE);
        boolean thirdStart = App.getBoolean("thirdStart", true);

        if (thirdStart) {
            showThirdDialog();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = SignInUser.getText().toString();
                final String password = SignInPass.getText().toString();

                if (TextUtils.isEmpty(user)) {
                    Toast.makeText(getApplicationContext(), "Enter your username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(user, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {

                                    if (password.length() < 6) {
                                        Toast.makeText(getApplicationContext(),"Password must be more than 6 digit",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"You have an ERROR!",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent(SignIn.this, MainView.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });


        chk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    SignInPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    SignInPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


    }


    public void NavigateSignUp(View v) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    public void NavigateForgetMyPassword(View v) {
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Location Permission")
                .setMessage("ProSafety Application enables the GPS location of the user to track the location of the sender")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences pro = getSharedPreferences("pro", MODE_PRIVATE);
        SharedPreferences.Editor editor = pro.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void showSecondDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Contact Permission")
                .setMessage("ProSafety Application is give access for the user to choose the contact list from their contact to send the Help request Message")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences safety = getSharedPreferences("safety", MODE_PRIVATE);
        SharedPreferences.Editor editor = safety.edit();
        editor.putBoolean("secondStart", false);
        editor.apply();
    }

    private void showThirdDialog() {
        new AlertDialog.Builder(this)
                .setTitle("SMS Permission")
                .setMessage("ProSafety Application is enable SMS access for user to send the request message using the SMS")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences App = getSharedPreferences("App", MODE_PRIVATE);
        SharedPreferences.Editor editor = App.edit();
        editor.putBoolean("thirdStart", false);
        editor.apply();
    }
}