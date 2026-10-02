package com.fyp.prosafetyapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditProfile extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = EditProfile.class.getSimpleName();
    Button btnsave;
    private FirebaseAuth firebaseAuth;
    private TextView textViewemailname;
    private DatabaseReference databaseReference;
    private EditText editTextName;
    private EditText editTextSurname;
    private ImageView profileImageView;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;

    public EditProfile() {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextName = (EditText)findViewById(R.id.EditTextName);
        editTextSurname = (EditText)findViewById(R.id.EditTextSurname);
        btnsave=(Button)findViewById(R.id.btnSaveButton);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        btnsave.setOnClickListener(this);
        textViewemailname=(TextView)findViewById(R.id.textViewEmailAdress);
        textViewemailname.setText(user.getEmail());
        profileImageView = findViewById(R.id.update_imageView);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent();
                profileIntent.setType("image/*");
                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
            }
        });
    }
    private void userInformation(){
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        UserInformation userinformation = new UserInformation(name,surname);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userinformation);
        SafetyFeedback.show(EditProfile.this, "User information updated");
    }
    @Override
    public void onClick(View view) {
        if (view==btnsave){
            if (imagePath == null) {

                Drawable drawable = this.getResources().getDrawable(R.drawable.defavatar);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.defavatar);

                userInformation();

                finish();
                startActivity(new Intent(EditProfile.this, MainView.class));
            }
            else {
                userInformation();
                sendUserData();
                finish();
                startActivity(new Intent(EditProfile.this, MainView.class));
            }
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                SafetyFeedback.show(EditProfile.this, "Error: Uploading profile picture");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                SafetyFeedback.show(EditProfile.this, "Profile picture uploaded");
            }
        });
    }

    public void openSelectProfilePictureDialog() {
        new SafetyDialog.Builder(this).setIcon(R.drawable.ic_person)
                .setEyebrow("YOUR PROFILE").setTitle("Choose a profile photo")
                .setMessage("Select an image from your gallery to personalize your profile.")
                .setPositiveButton("Continue", null).show();
    }
}