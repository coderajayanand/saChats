package com.example.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    TextView loginButton;
    EditText rg_email, rg_pass, rg_repass, rg_username;
    Button rg_signup;
    String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    CircleImageView rg_pic;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establishing your Account");
        progressDialog.setCancelable(false);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        rg_signup = findViewById(R.id.signUpButton);
        rg_email = findViewById(R.id.signUpEmail);
        rg_pass = findViewById(R.id.signUpPassword);
        rg_repass = findViewById(R.id.signUpRePassword);
        rg_username = findViewById(R.id.signUpUsername);
        rg_pic = findViewById(R.id.signUpProfilePic);
        loginButton = findViewById(R.id.loginRedi);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee = rg_username.getText().toString();
                String eemail = rg_email.getText().toString();
                String ppass = rg_pass.getText().toString();
                String rrepass = rg_repass.getText().toString();
                String status = "Hey, I am using this application";

                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(eemail) || TextUtils.isEmpty(ppass) || TextUtils.isEmpty(rrepass))
                {
                    progressDialog.dismiss();
                    Toast.makeText(registration.this, "Please enter Valid Information", Toast.LENGTH_SHORT).show();
                }
                else if (!eemail.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    rg_email.setError("Type a Valid Email here");
                }
                else if (ppass.length() < 6)
                {
                    progressDialog.dismiss();
                    rg_pass.setError("Password length can't be less than 6");
                }
                else if(!ppass.equals(rrepass))
                {
                    progressDialog.dismiss();
                    rg_pass.setError("Password does not match");
                }
                else {
                    auth.createUserWithEmailAndPassword(eemail,ppass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if(imageURI != null)
                                {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users = new Users(id, namee, eemail, ppass, imageuri, status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(registration.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(registration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    String status = "Hey, I am using this application";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/aschats-11638.appspot.com/o/man.png?alt=media&token=7e3447bd-c8be-4fd9-8d62-41c06481f888";
                                    Users users = new Users(id, namee, eemail, ppass, imageuri, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Intent intent = new Intent(registration.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(registration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rg_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if (data != null)
            {
                imageURI = data.getData();
                rg_pic.setImageURI(imageURI);
            }
        }
    }
}