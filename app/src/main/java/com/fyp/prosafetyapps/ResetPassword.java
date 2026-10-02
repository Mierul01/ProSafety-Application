package com.fyp.prosafetyapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset;
    private FirebaseAuth auth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.EditTextSurname);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter your email address");
                    inputEmail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Enter a valid email address");
                    inputEmail.requestFocus();
                    return;
                }
                btnReset.setEnabled(false);
                btnReset.setText("Sending link...");
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btnReset.setEnabled(true);
                                btnReset.setText("Send reset link");
                                if (task.isSuccessful()) {
                                    new SafetyDialog.Builder(ResetPassword.this).setEyebrow("PASSWORD RESET").setIcon(R.drawable.ic_email)
                                            .setTitle("Check your inbox")
                                            .setMessage("If this email is registered, you will receive a password reset link. Check your spam folder too.")
                                            .setPositiveButton("Got it", null).show();

                                } else {
                                    SafetyFeedback.show(ResetPassword.this, "Could not send the link. Check your connection and try again.");
                                }
                            }
                        });
            }
        });
    }

    public void NavigateSignUp(View v) {
        Intent inent = new Intent(this, SignUp.class);
        startActivity(inent);
    }
}
