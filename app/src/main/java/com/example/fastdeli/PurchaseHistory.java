package com.example.fastdeli;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fastdeli.adapter.ReceiptAdapter;
import com.example.fastdeli.model.Receipt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class PurchaseHistory extends AppCompatActivity {

    private static final String TAG = "PurchaseHistory";
    private RecyclerView recyclerView;
    private ReceiptAdapter adapter;
    private List<Receipt> receiptList;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        receiptList = new ArrayList<>();
        adapter = new ReceiptAdapter(this, receiptList);
        recyclerView.setAdapter(adapter);

        loadPurchaseHistoryFromFirestore();
    }

    private void loadPurchaseHistoryFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserID = auth.getCurrentUser().getUid(); // Implement this method to get the current user's ID
        if (currentUserID != null) {
            db.collection("PurchaseHistory")
                    .document(currentUserID)
                    .collection("orders")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                receiptList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Receipt receipt = document.toObject(Receipt.class);
                                    receiptList.add(receipt);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting purchase history: ", task.getException());
                            }
                        }
                    });
        }
    }

    // Implement this method to get the current user's ID

}
