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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;

    private FirebaseFirestore db;

    private Product selectedProduct;



    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }



    FirebaseAuth auth;

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
               addtoCart(product);
            }

            private void addtoCart(Product product) {
                // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("users")
                        .whereEqualTo("productName", product.getName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng giá trị quantity lên 1
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            int quantity = document.getLong("quantity").intValue();
                                            document.getReference().update("quantity", quantity + 1)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Thông báo khi cập nhật thành công
                                                                Toast.makeText(context,"Added to cart",Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Thông báo khi cập nhật thất bại
                                                                Toast.makeText(context, "Failed to Add",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm một bản ghi mới
                                        addNewProductToCart(product);
                                    }
                                } else {
                                    // Xử lý lỗi khi truy vấn
                                    Toast.makeText(context, "Failed to Add",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            // Phương thức thêm một sản phẩm mới vào giỏ hàng
            private void addNewProductToCart(Product product) {
                String savecurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("m, dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
                savecurrentTime = currentTime.format(calForDate.getTime());

                // Tạo một bản ghi mới cho sản phẩm được thêm vào giỏ hàng
                final HashMap<String,Object> cartMap = new HashMap<>();
                String productName = (product.getName() != null) ? product.getName().toString() : "Unknown Product";
                Double productPrice = Double.valueOf((product.getCost() != null) ? product.getCost().toString() : "0");
                String productImage = (product.getImage()!= null) ? product.getImage().toString():"No image";
                Integer quantity = 1;

                cartMap.put("productName", productName);
                cartMap.put("productPrice", productPrice);
                cartMap.put("currentTime", savecurrentTime);
                cartMap.put("Currentdate", saveCurrentDate);
                cartMap.put("image", productImage);
                cartMap.put("quantity", quantity);

                // Thêm bản ghi vào Firestore
                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("users").add(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    // Thông báo khi sản phẩm được thêm vào giỏ hàng thành công
                                    Toast.makeText(context,"Added to cart",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Thông báo khi thất bại khi thêm sản phẩm vào giỏ hàng
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
