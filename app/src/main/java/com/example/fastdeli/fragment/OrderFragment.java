package com.example.fastdeli.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastdeli.R;
import com.example.fastdeli.adapter.ProductAdapter;
import com.example.fastdeli.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements ProductAdapter.OnAddButtonClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private FirebaseFirestore db;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductAdapter(getActivity());
        recyclerView.setAdapter(adapter);


        db = FirebaseFirestore.getInstance();
        loadALLProducts();
        Button allProductsButton = view.findViewById(R.id.All_product);
        Button comboForOneButton = view.findViewById(R.id.Combo_4_1);
        Button comboForGroupBox = view.findViewById(R.id.Combo_4_group);
        Button friedChickensButton = view.findViewById(R.id.Fried_chickens);
        Button dessertDrinksButton = view.findViewById(R.id.DesDrinks);


        // Set click listeners for buttons
        allProductsButton.setOnClickListener(v -> loadALLProducts());
        comboForOneButton.setOnClickListener(v -> loadCombo41());
        comboForGroupBox.setOnClickListener(v -> loadCombo4group());
        friedChickensButton.setOnClickListener(v -> loadFriedChickens());
        dessertDrinksButton.setOnClickListener(v -> loadDessertdrinks());

        return view;
    }



    private void loadALLProducts() {
        db.collection("product")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                        adapter.setProducts(products);
                    } else {
                        // Handle errors
                    }
                });
    }
    private void loadCombo41() {
        db.collection("Combo for one")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                        adapter.setProducts(products);
                    } else {
                        // Handle errors
                    }
                });
    }
    private void loadCombo4group() {
        db.collection("Combo for group")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                        adapter.setProducts(products);
                    } else {
                        // Handle errors
                    }
                });
    }
    private void loadFriedChickens() {
        db.collection("Fried chickens")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                        adapter.setProducts(products);
                    } else {
                        // Handle errors
                    }
                });
    }
    private void loadDessertdrinks() {
        db.collection("Dessert & drinks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                        adapter.setProducts(products);
                    } else {
                        // Handle errors
                    }
                });
    }


    @Override
    public void onAddButtonClick(Product product) {

    }
}
