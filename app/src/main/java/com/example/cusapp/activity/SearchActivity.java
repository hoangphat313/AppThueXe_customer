package com.example.cusapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cusapp.R;
import com.example.cusapp.adapter.AdapterRcvSearch;
import com.example.cusapp.adapter.RcvListCarAdapter;
import com.example.cusapp.model.Car;
import com.example.cusapp.model.Firebase;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private AdapterRcvSearch adapter;
    private ArrayList<Car> carList;
    private Firebase mfirebase;
    private SearchView searchView;
    private RecyclerView rcv_searchbooks;
    Toolbar toolbar;
    String musername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Anhxa();

        mfirebase.getAllXe(new Firebase.FirebaseCallback<Car>() {
            @Override
            public void onCallback(ArrayList<Car> list2) {
                carList = list2;
                adapter = new AdapterRcvSearch(carList, SearchActivity.this);
                rcv_searchbooks.setAdapter(adapter);
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Car> filteredList = filter(carList, newText);
                adapter.setData(filteredList);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void Anhxa() {
        searchView =findViewById(R.id.sv_cars);
        rcv_searchbooks = findViewById(R.id.rcv_searchcar);
        toolbar = findViewById(R.id.toolbarsearch);
        rcv_searchbooks.setLayoutManager(new LinearLayoutManager(this));
        mfirebase = new Firebase(this);
    }
    private ArrayList<Car> filter(List<Car> books, String query) {
        query = query.toLowerCase().trim();

        final ArrayList<Car> filteredList = new ArrayList<>();
        for (Car car : books) {
            if (car.getNamecar().toLowerCase().contains(query)) {
                filteredList.add(car);
            }
        }
        return filteredList;
    }
}