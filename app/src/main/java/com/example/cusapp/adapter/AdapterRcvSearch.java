package com.example.cusapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cusapp.R;
import com.example.cusapp.activity.DeitailCarActivity;
import com.example.cusapp.model.Car;

import java.util.ArrayList;
import java.util.List;

public class AdapterRcvSearch extends RecyclerView.Adapter<AdapterRcvSearch.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Car> carList;
    private OnItemClickListener listener;

    public AdapterRcvSearch(ArrayList<Car> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rcvlistcar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Car currentCar = carList.get(position);
        holder.tvName.setText(currentCar.getNamecar());
        holder.tvPrice.setText(String.format("%,d", Math.round(currentCar.getPricecar())) + " VNĐ/Ngày");
        Glide.with(context).load(currentCar.getImage1()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeitailCarActivity.class);
                intent.putExtra("currentCar", currentCar);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                List<Car> filteredList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    filteredList.addAll(carList);
                } else {
                    for (Car car : carList) {
                        if (car.getNamecar().toLowerCase().contains(searchText)) {
                            filteredList.add(car);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                carList.clear();
                carList.addAll((List<Car>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
    public void setData(ArrayList<Car> filteredList) {
        this.carList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagecar1);
            tvName = itemView.findViewById(R.id.tv_namecar);
            tvPrice = itemView.findViewById(R.id.tv_pricecar);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Car book);
    }
}
