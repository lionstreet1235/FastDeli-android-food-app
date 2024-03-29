package com.example.fastdeli.login;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastdeli.MainActivity;
import com.example.fastdeli.PurchaseHistory;
import com.example.fastdeli.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainLoginActivity extends AppCompatActivity {
    TextView name,phone,address,email,password;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    Button logOut,changeProfileImage, showHistory;
    ImageView backToHome;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });



        //anh xa du lieu
        showHistory= findViewById(R.id.btnHistoryU);
        logOut=findViewById(R.id.btnLogOutU);
        backToHome=findViewById(R.id.ivBackToHome);
        email=findViewById(R.id.tvEmailDetailU);
        name=findViewById(R.id.tvNameDetailU);
        phone=findViewById(R.id.tvPhoneDetailU);
        address=findViewById(R.id.tvAddresslDetailU);
        changeProfileImage=findViewById(R.id.btnChangeProfile);
//        password=findViewById(R.id.tvPassDetailU);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

            if(fAuth.getCurrentUser()!=null)
            {
                userId=fAuth.getCurrentUser().getUid();
                DocumentReference documentReference=fStore.collection("users").document(userId);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        email.setText(value.getString("email"));
                        name.setText(value.getString("fname"));
                        address.setText(value.getString("address"));
                        phone.setText(value.getString("phone"));

                    }
                });

            }
            else {
                Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,LoginActivity.class);
                startActivity(i);
            }







        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }
        });
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainLoginActivity.this, MainActivity.class);

                startActivity(i);
            }
        });
        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fname","Tuan Duy");
                i.putExtra("email","tuanduy1411@gmail.com");
                i.putExtra("address","Hau Giang");
                i.putExtra("phone","0909327716");
                startActivity(i);
            }
        });
        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainLoginActivity.this, PurchaseHistory.class);
                startActivity(i);
            }
        });


//        DocumentReference documentReference=fStore.collection("users").document(userId);
//        documentReference.addSnapshotListener(this,(documentSnapshot,e))

    }
}