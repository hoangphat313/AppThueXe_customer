package com.example.cusapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cusapp.R;
import com.example.cusapp.activity.DetailHistoryOrderActivity;
import com.example.cusapp.activity.DetailOrderActivity;
import com.example.cusapp.model.Firebase;
import com.example.cusapp.model.Order;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class RcvHistoryOrderAdapter extends RecyclerView.Adapter<RcvHistoryOrderAdapter.ViewHolder>{
    private ArrayList<Order> orderslist;
    private Context context;
    private Firebase mfirebase;
    public RcvHistoryOrderAdapter(Context context, ArrayList<Order> orderslist) {
        this.orderslist = orderslist;
        this.context = context;
        mfirebase = new Firebase(context);
    }

    @Override
    public RcvHistoryOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcvhistoryorder, parent, false);
        return new RcvHistoryOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcvHistoryOrderAdapter.ViewHolder holder, int position) {
        Order currentOrder = orderslist.get(position);
        holder.tv_username.setText(currentOrder.getRenterName());
        holder.tv_orderdate.setText("Order date: " + currentOrder.getOrderDate());
        holder.tv_orderstatus.setText(currentOrder.getOrderStatus());
        holder.tv_orderID.setText("OrderID: " +currentOrder.getOrderID());
        holder.tv_rentdate.setText(currentOrder.getRentDate());
        holder.tv_returndate.setText(currentOrder.getReturnDate());
        holder.tv_namecar.setText(currentOrder.getCarName());
        holder.tv_typecar.setText("Loại xe: " + currentOrder.getCarType());
        holder.tv_pricecar.setText("Giá: " + String.format("%,d", Math.round(currentOrder.getCarPrice())) + " VNĐ/Ngày");
        holder.tv_totalprice.setText(String.format("%,d", Math.round(currentOrder.getTotalprice()))+ " VNĐ");
        holder.btn_viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailHistoryOrderActivity.class);
                intent.putExtra("currentOrder", currentOrder);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username, tv_orderdate, tv_orderstatus, tv_rentdate, tv_returndate, tv_namecar, tv_typecar, tv_pricecar, tv_totalprice, tv_orderID;
        AppCompatButton btn_viewdetail;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_orderdate = itemView.findViewById(R.id.tv_orderdate);
            tv_orderstatus = itemView.findViewById(R.id.tv_orderstatus);
            tv_orderID = itemView.findViewById(R.id.tv_orderID);
            tv_rentdate = itemView.findViewById(R.id.tv_rentdate);
            tv_returndate = itemView.findViewById(R.id.tv_returndate);
            tv_namecar = itemView.findViewById(R.id.tv_namecar);
            tv_typecar = itemView.findViewById(R.id.tv_typecar);
            tv_pricecar = itemView.findViewById(R.id.tv_pricecar);
            tv_totalprice = itemView.findViewById(R.id.tv_totalprice);
            btn_viewdetail = itemView.findViewById(R.id.btn_viewdetail);
        }
    }

}
