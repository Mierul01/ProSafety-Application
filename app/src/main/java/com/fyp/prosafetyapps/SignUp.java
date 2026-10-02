package com.fyp.prosafetyapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SignUp extends AppCompatActivity {

    EditText txtSignUpEmail,txtSignUpPass;
    Button SignUpButton;
    CheckBox chk1;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        chk1 = (CheckBox) findViewById(R.id.chk1);

        txtSignUpEmail = findViewById(R.id.txtSignUpEmail);
        txtSignUpPass = findViewById(R.id.txtSignUpPass);

        auth=FirebaseAuth.getInstance();
        SignUpButton = (Button) findViewById(R.id.btnSignUp);

        SignUpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String email = txtSignUpEmail.getText().toString().trim();
                String pass = txtSignUpPass.getText().toString();

                if(TextUtils.isEmpty(email)){
                    txtSignUpEmail.setError("Enter your email address");
                    txtSignUpEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    txtSignUpPass.setError("Enter a password");
                    txtSignUpPass.requestFocus();
                }
                else{
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        txtSignUpEmail.setError("Enter a valid email address");
                        txtSignUpEmail.requestFocus();
                        return;
                    }
                    if (pass.length() < 6) {
                        txtSignUpPass.setError("Use at least 6 characters");
                        txtSignUpPass.requestFocus();
                        return;
                    }
                    SignUpButton.setEnabled(false);
                    SignUpButton.setText("Creating account...");
                    auth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    SignUpButton.setEnabled(true);
                                    SignUpButton.setText("Create account");

                                    if (!task.isSuccessful()) {
                                        new SafetyDialog.Builder(SignUp.this)
                                                .setTitle("Unable to create account")
                                                .setMessage("Check your connection and email address. If you already have an account, sign in or reset your password.")
                                                .setPositiveButton("Try again", null).show();
                                    }
                                    else {
                                        startActivity(new Intent(SignUp.this, EditProfile.class));
                                        finish();
                                    }
                                }
                            });}
            }
        });

        chk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    txtSignUpPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    txtSignUpPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


    }

    public void navigate_sign_in(View v){
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }
}
