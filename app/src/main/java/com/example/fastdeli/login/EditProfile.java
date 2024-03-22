package com.example.fastdeli.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fastdeli.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileFullName,profileEmail,profilePhone,profileAddress;
    ImageView profileImageView,imageViewBack;
    Button saveBtn;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Intent data=getIntent();
        String fullName=data.getStringExtra("fname");
        String email=data.getStringExtra("email");
        String phone=data.getStringExtra("phone");
        String address=data.getStringExtra("address");


        //ánh xạ
        profileFullName=findViewById(R.id.edtprofileFN);
        profileEmail=findViewById(R.id.edtprofileE);
        profilePhone=findViewById(R.id.edtprofileP);
        profileAddress=findViewById(R.id.edtprofileA);
        profileImageView=findViewById(R.id.profileimageView);
        saveBtn=findViewById(R.id.buttonSave);
        imageViewBack=findViewById(R.id.ivBackToMainLogin);

        //import thư viện trên firebase
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        user=fAuth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference();


        //set sự kiện nhấn
        StorageReference profileRef=storageReference.child("users"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
//        profileRef.getDownloadUrl().addOnSuccessListener((OnSuccessListener)(uri)->{
//            Picasso
//                }


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this,"Profile Image Clicked.",Toast.LENGTH_SHORT).show();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileFullName.getText().toString().isEmpty()||profileEmail.getText().toString().isEmpty()||profilePhone.getText().toString().isEmpty()||profileAddress.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProfile.this,"Một hoặc nhiều thông tin đang trống!",Toast.LENGTH_LONG).show();
                    return;
                }


                user. verifyBeforeUpdateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef=fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited=new HashMap<>();
                        edited.put("fname",profileFullName.getText().toString());
                        edited.put("email",profileEmail.getText().toString());
                        edited.put("phone",profilePhone.getText().toString());
                        edited.put("address",profileAddress.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProfile.this,"Thông tin đã được cập nhật!",Toast.LENGTH_LONG).show();


                                startActivity(new Intent(getApplicationContext(),MainLoginActivity.class));
                                finish();
                            }
                        });

                        Toast.makeText(EditProfile.this,"Email đã được thay đổi !",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        //
        profileFullName.setText(fullName);
        profileEmail.setText(email);
        profilePhone.setText(phone);
        profileAddress.setText(address);




        Log.d(TAG,"onCreate " +fullName + " " +email + " " +phone + " " +address + " " );

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(EditProfile.this, MainLoginActivity.class);
                startActivity(i);
            }
        });

    }
}