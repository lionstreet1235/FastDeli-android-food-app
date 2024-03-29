package com.example.fastdeli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastdeli.R;
import com.example.fastdeli.model.My_Models;
import com.example.fastdeli.model.Receipt;

import com.google.firebase.Timestamp;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {

    private List<Receipt> receiptList;
    private Context context;

    public ReceiptAdapter(Context context, List<Receipt> receiptList) {
        this.context = context;
        this.receiptList = receiptList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receiptList.get(position);
        holder.bind(receipt);
    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameTextView;
        private TextView totalCostTextView;
        private TextView timeStamp, quantity;
        private Timestamp previousTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            totalCostTextView = itemView.findViewById(R.id.total_cost_text_view);
            timeStamp = itemView.findViewById(R.id.time_receipt);
            previousTimestamp = null; // Initialize previousTimestamp
            quantity= itemView.findViewById(R.id.quantity_history);
        }

        public void bind(Receipt receipt) {
            // Display username and total cost for each receipt
            usernameTextView.setText(receipt.getProductName());
            totalCostTextView.setText(String.format(receipt.getTotal().toString()+" VND"));
            quantity.setText(String.format("Số lượng: "+ receipt.getQuantity()));


            // Display timestamp only if it's different from the previous one
            if (previousTimestamp == null || !receipt.getTimestamp().equals(previousTimestamp)) {
                timeStamp.setText(receipt.getTimestamp().toDate().toString());
                timeStamp.setVisibility(View.VISIBLE); // Show the timestamp TextView
            } else {
                timeStamp.setVisibility(View.GONE); // Hide the timestamp TextView
            }

            // Update previousTimestamp for the next item
            previousTimestamp = receipt.getTimestamp();
        }
    }

}

