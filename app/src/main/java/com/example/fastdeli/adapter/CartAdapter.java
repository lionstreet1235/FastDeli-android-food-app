package com.example.fastdeli.adapter;

import static java.lang.String.valueOf;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.fastdeli.model.CartProduct;
import com.example.fastdeli.model.Product;
import com.example.fastdeli.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartProduct> cartProducts;
    private FirebaseFirestore db;

    public CartAdapter(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts != null ? cartProducts : new ArrayList<>(); // Khởi tạo danh sách sản phẩm trống ban đầu
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartProduct cartProduct = cartProducts.get(position);
        holder.bind(cartProduct);

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí của mục
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    cartProduct.increaseQuantity(); // Tăng số lượng sản phẩm
                    notifyItemChanged(adapterPosition); // Cập nhật giao diện người dùng
                    updateFirestoreQuantity(cartProduct, adapterPosition); // Cập nhật trường quantity trên Firestore
                }
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí của mục
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    cartProduct.decreaseQuantity(); // Giảm số lượng sản phẩm
                    notifyItemChanged(adapterPosition); // Cập nhật giao diện người dùng
                    updateFirestoreQuantity(cartProduct, adapterPosition); // Cập nhật trường quantity trên Firestore
                }
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí của mục
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    cartProducts.remove(adapterPosition); // Xóa sản phẩm khỏi giỏ hàng
                    notifyItemRemoved(adapterPosition); // Cập nhật giao diện người dùng
                    deleteFirestoreProduct(cartProduct); // Xóa sản phẩm khỏi Firestore
                }
            }
        });
    }

    private void deleteFirestoreProduct(CartProduct cartProduct) {
        // Lấy ID của tài liệu từ CartProduct
        String documentId = cartProduct.getDocumentId();

        // Kiểm tra xem ID có tồn tại không
        if (documentId != null && !documentId.isEmpty()) {
            // Xóa tài liệu từ Firestore
            db.collection("AddToCart").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("users").document(documentId).delete()

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Xóa thành công, bạn có thể thực hiện các hành động khác nếu cần
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xảy ra lỗi khi xóa tài liệu, bạn có thể xử lý lỗi ở đây nếu cần
                        }
                    });
        } else {
            // Nếu ID không tồn tại, thông báo cho người dùng hoặc xử lý lỗi nếu cần
        }
    }


    private void updateFirestoreQuantity(CartProduct cartProduct, int adapterPosition) {
        // Lấy ID của tài liệu từ CartProduct
        String documentId = cartProduct.getDocumentId();

        // Kiểm tra xem ID có tồn tại không
        if (documentId != null && !documentId.isEmpty()) {
            // Lấy số lượng mới từ CartProduct
            int newQuantity = cartProduct.getQuantity();

            // Cập nhật số lượng trong Firestore
            db.collection("AddToCart").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("users").document(documentId)
                    .update("quantity", newQuantity)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Cập nhật thành công, bạn có thể thực hiện các hành động khác nếu cần
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xảy ra lỗi khi cập nhật số lượng, bạn có thể xử lý lỗi ở đây nếu cần
                        }
                    });
        } else {
            // Nếu ID không tồn tại, thông báo cho người dùng hoặc xử lý lỗi nếu cần
        }
    }


    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts != null ? cartProducts : new ArrayList<>();
        notifyDataSetChanged(); // Thông báo cho RecyclerView cập nhật lại dữ liệu khi có thay đổi
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTextView;
        private TextView productPriceTextView;

        private TextView productQuantity;

        private ImageView productImage;
        private Button btnPlus;
        private Button btnMinus;
        private Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.CartItemName);
            productPriceTextView = itemView.findViewById(R.id.CartItemPrice);
            productQuantity = itemView.findViewById(R.id.tvQuantity);
            productImage= itemView.findViewById(R.id.ivCartImage);
            btnPlus = itemView.findViewById(R.id.btnplus); // Thêm này
            btnMinus = itemView.findViewById(R.id.btnminus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }

        public void bind(CartProduct cartProduct) {
            productNameTextView.setText(cartProduct.getProductName());
            productPriceTextView.setText("Thành tiền: "+valueOf(cartProduct.getProductPrice()*cartProduct.getQuantity()));
            String url = cartProduct.getImageCart();
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/fastdeli-5cee0.appspot.com/o/001.jpg?alt=media&token=4b7f26d0-a4eb-4542-bb0c-0249a93ce1e1").into(productImage);

            productQuantity.setText(String.format("Số lượng: "+valueOf(cartProduct.getQuantity())));
        }
    }
}

