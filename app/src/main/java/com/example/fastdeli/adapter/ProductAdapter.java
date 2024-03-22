package com.example.fastdeli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastdeli.fragment.OrderFragment;
import com.example.fastdeli.model.Cart;
import com.example.fastdeli.model.Product;
import com.example.fastdeli.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;

    private FirebaseFirestore db;

    private Product selectedProduct;

    private Cart cart;

    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }



    FirebaseAuth auth;
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    private OnAddButtonClickListener addButtonClickListener;


    public interface OnAddButtonClickListener {
        void onAddButtonClick(Product product);
    }
    public void setOnAddButtonClickListener(OnAddButtonClickListener listener) {
        this.addButtonClickListener = listener;
    }





    public Product getSelectedProduct() {
        return selectedProduct;
    }


    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addtoCart();
            }

            private void addtoCart() {
                String savecurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("mm, dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
                savecurrentTime = currentTime.format(calForDate.getTime());

                final HashMap<String,Object> cartMap = new HashMap<>();
                String productName = (product.getName() != null) ? product.getName().toString() : "Unknown Product";
                String productPrice = (product.getCost() != null) ? product.getCost().toString() : "0";


                cartMap.put("productName", productName);
                cartMap.put("productPrice", productPrice);
                cartMap.put("currentTime", savecurrentTime);
                cartMap.put("Currentdate", saveCurrentDate);

                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("users").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context,"Added to cart",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Failed to Add",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTextView;
        private TextView productPriceTextView;
        private TextView productDetail;
        private ImageView IvProduct;
        private Button btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productDetail = itemView.findViewById(R.id.productDetail);
            IvProduct = itemView.findViewById(R.id.ivProduct);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }

        public void bind(Product product) {
            if (product.getImage() != null && (product.getImage().startsWith("http://") || product.getImage().startsWith("https://"))) {
                // Nếu là URL hợp lệ, sử dụng Glide để load hình ảnh
                Glide.with(itemView.getContext()).load(product.getImage()).into(IvProduct);
            } else {
                IvProduct.setImageResource(R.drawable.mrfresh);
            }

            productNameTextView.setText(product.getName());
            productDetail.setText(product.getDetail());
            if (product.getCost() != null) {
                productPriceTextView.setText(String.format(String.valueOf(product.getCost()))+" VND");
            } else {
                productPriceTextView.setText("N/A");
            }
        }



    }
}
