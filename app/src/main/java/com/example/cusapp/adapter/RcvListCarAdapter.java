package com.example.cusapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cusapp.R;
import com.example.cusapp.activity.DeitailCarActivity;
import com.example.cusapp.activity.TrangchuActivity;
import com.example.cusapp.model.Car;

import java.util.ArrayList;

public class RcvListCarAdapter extends RecyclerView.Adapter<RcvListCarAdapter.ViewHolder>{
    private ArrayList<Car> carList;
    private Context context;
    public RcvListCarAdapter(Context context, ArrayList<Car> carList) {
        this.carList = carList;
        this.context = context;
    }

    @Override
    public RcvListCarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcvlistcar, parent, false);
        return new RcvListCarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RcvListCarAdapter.ViewHolder holder, int position) {
        Car currentCar = carList.get(position);
        holder.namecar.setText(currentCar.getNamecar());
        holder.typecar.setText(currentCar.getTypecar());
        holder.seats.setText(String.valueOf((int)currentCar.getSeats()) + " chỗ");
        holder.pricecar.setText(String.format("%,d", Math.round(currentCar.getPricecar())) + " VNĐ");
        Glide.with(context).load(currentCar.getImage1()).into(holder.imagecar);
        holder.imagecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeitailCarActivity.class);
                intent.putExtra("currentCar", currentCar);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namecar, pricecar, typecar, seats;
        ImageView imagecar;
        public ViewHolder(View itemView) {
            super(itemView);
            namecar = itemView.findViewById(R.id.tv_namecar);
            pricecar = itemView.findViewById(R.id.tv_pricecar);
            typecar = itemView.findViewById(R.id.tv_typecar);
            seats = itemView.findViewById(R.id.tv_seats);
            imagecar = itemView.findViewById(R.id.imagecar1);
        }
    }
}
