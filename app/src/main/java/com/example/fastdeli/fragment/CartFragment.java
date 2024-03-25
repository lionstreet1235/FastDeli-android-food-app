package com.example.fastdeli.fragment;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastdeli.R;
import com.example.fastdeli.model.CartProduct;
import com.example.fastdeli.adapter.CartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<CartProduct> cartItems;
    private Button btnCheckout;

    private TextView tvTotalPrice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        // Khởi tạo Firestore và FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Lấy dữ liệu giỏ hàng từ Firestore
        getCartData();
        btnCheckout.setOnClickListener(v -> checkOut());

        return view;
    }
    private void checkOut() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            db.collection("AddToCart").document(userId).collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Lấy thông tin của mỗi sản phẩm từ giỏ hàng
                                    String productName = document.getString("productName");
                                    Double productPrice = document.getDouble("productPrice");
                                    Long quantity = document.getLong("quantity");
                                    Boolean status = false;
                                    Double total = document.getDouble("productPrice") * document.getLong("quantity");
                                    db.collection("users").document(userId)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        String address = documentSnapshot.getString("address");

                                                        // Tạo một tài liệu mới trong collection "PurchaseHistory" để lưu thông tin của đơn hàng
                                                        Map<String, Object> orderData = new HashMap<>();
                                                        orderData.put("productName", productName);
                                                        orderData.put("productPrice", productPrice);
                                                        orderData.put("quantity", quantity);
                                                        orderData.put("timestamp", FieldValue.serverTimestamp());
                                                        orderData.put("DeliveryAddress", address);
                                                        orderData.put("status", status);
                                                        orderData.put("Total",total);

                                                        // Thêm tài liệu mới vào collection "PurchaseHistory"
                                                        db.collection("PurchaseHistory").document(userId).collection("orders")
                                                                .add(orderData)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        // Xóa dữ liệu của giỏ hàng sau khi đã mua hàng thành công
                                                                        deleteCartData(userId);
                                                                        Toast.makeText(getContext(), "Checkout thành công!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "Có lỗi xảy ra khi thực hiện Checkout", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    } else {

                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            } else {

                            }
                        }
                    });
        }
    }

    private void deleteCartData(String userId) {
        // Xóa dữ liệu của giỏ hàng sau khi đã mua hàng thành công
        db.collection("AddToCart").document(userId).collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        } else {

                        }
                    }
                });
    }

    private void getCartData() {
        if (auth.getCurrentUser() != null) {
            // Lấy ID của người dùng hiện tại
            String userId = auth.getCurrentUser().getUid();

            // Truy vấn dữ liệu giỏ hàng từ Firestore
            db.collection("AddToCart").document(userId).collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<CartProduct> cartItems = new ArrayList<>();
                                double totalPrice = 0; // Khởi tạo biến để tính tổng giá tiền
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Lấy dữ liệu từ mỗi document và chuyển đổi thành đối tượng CartProduct
                                    String productName = document.getString("productName");
                                    Double productPrice = document.getDouble("productPrice");
                                    Long quantity = document.getLong("quantity");

                                    // Tính tổng giá tiền của giỏ hàng
                                    totalPrice += (productPrice != null ? productPrice : 0) * (quantity != null ? quantity : 0);

                                    String documentId = document.getId(); // Lấy ID của tài liệu từ kết quả thêm mới
                                    // Lưu ID vào đối tượng CartProduct
                                    CartProduct cartProduct = new CartProduct(productName, productPrice,"",quantity.intValue(),"");
                                    cartProduct.setDocumentId(documentId);
                                    cartItems.add(cartProduct);
                                }

                                // Hiển thị tổng giá tiền của giỏ hàng


                                // Hiển thị dữ liệu giỏ hàng trong RecyclerView
                                cartAdapter = new CartAdapter(cartItems);
                                recyclerView.setAdapter(cartAdapter);
                            }

                        }

                    });
        }

    }

}
