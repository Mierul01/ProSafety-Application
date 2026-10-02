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

        if (BuildConfig.DEBUG) {
            android.widget.LinearLayout content = (android.widget.LinearLayout)
                    findViewById(R.id.txttitle).getParent();
            Button preview = new Button(this);
            preview.setText("Explore the design  (no login needed)");
            preview.setTextColor(getResources().getColor(R.color.safety_teal));
            preview.setTextSize(13);
            preview.setAllCaps(false);
            preview.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            content.addView(preview, 1);
            preview.setOnClickListener(v -> startActivity(new Intent()
                    .setClassName(this, "com.fyp.prosafetyapps.DesignPreviewActivity")));
        }

        chk1 = (CheckBox) findViewById(R.id.chk1);

        SignInUser = (EditText) findViewById(R.id.txtSignInEmail);
        SignInPass = (EditText) findViewById(R.id.txtSignInPass);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        auth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = SignInUser.getText().toString().trim();
                final String password = SignInPass.getText().toString();

                if (TextUtils.isEmpty(user)) {
                    SignInUser.setError("Enter your email address");
                    SignInUser.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    SignInPass.setError("Enter your password");
                    SignInPass.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    SignInUser.setError("Enter a valid email address");
                    SignInUser.requestFocus();
                    return;
                }
                btnSignIn.setEnabled(false);
                btnSignIn.setText("Signing in...");
                auth.signInWithEmailAndPassword(user, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                btnSignIn.setEnabled(true);
                                btnSignIn.setText("Sign in");
                                if (!task.isSuccessful()) {

                                    if (password.length() < 6) {
                                        SignInPass.setError("Password must contain at least 6 characters");
                                    } else {
                                        new SafetyDialog.Builder(SignIn.this)
                                                .setTitle("Unable to sign in")
                                                .setMessage("Check your email and password, and make sure you are connected to the internet. You can reset your password if you have forgotten it.")
                                                .setPositiveButton("Try again", null).show();
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

}
