package com.example.cusapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.cusapp.R;
import com.example.cusapp.adapter.Adapterviewpager;
import com.example.cusapp.adapter.Loginstatus;
import com.example.cusapp.adapter.RcvListCarAdapter;
import com.example.cusapp.model.Firebase;
import com.example.cusapp.model.Car;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TrangchuActivity extends AppCompatActivity {

    int currentPage = 0;
    ViewPager viewPager;
    private  String userID;
    private RecyclerView rcvthuexesotudong,rcvthuexesosan;
    private RcvListCarAdapter adapter1, adapter2;
    private ArrayList<Car> carListXeSoTuDong;
    private ArrayList<Car> carListXeSoSan;
    private Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        Anhxa();
        setAutoScrollViewScroll();
        setDataForRcv();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setDataForRcv();
    }
    private void Anhxa() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Adapterviewpager adapterviewpager = new Adapterviewpager(this);
        viewPager.setAdapter(adapterviewpager);
        viewPager.setClipToOutline(true);
        rcvthuexesotudong = findViewById(R.id.rcv_xetulai);
        rcvthuexesosan = findViewById(R.id.rcv_xecotaixe);
        firebase = new Firebase(this);
    }
    public void detail_account(View view){
        if (Loginstatus.getInstance().isLoggedIn() == true){
            Intent i = new Intent(TrangchuActivity.this, DetailAccountActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(TrangchuActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
    private void setAutoScrollViewScroll() {
        Timer timer;
        final long DELAY_MS = 500;
        final long PERIOD_MS = 2000;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) currentPage = 0;
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },DELAY_MS, PERIOD_MS);
    }
    private void setDataForRcv(){
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvthuexesotudong.setLayoutManager(layoutManager1);
        rcvthuexesosan.setLayoutManager(layoutManager2);
        firebase.getAllXeSoTuDong(new Firebase.FirebaseCallback<Car>() {
            @Override
            public void onCallback(ArrayList<Car> list1) {
                carListXeSoTuDong = list1;
                adapter1 = new RcvListCarAdapter(TrangchuActivity.this, carListXeSoTuDong);
                rcvthuexesotudong.setAdapter(adapter1);
            }
        });
        firebase.getAllXeSoSan(new Firebase.FirebaseCallback<Car>() {
            @Override
            public void onCallback(ArrayList<Car> list2) {
                carListXeSoSan = list2;
                adapter2 = new RcvListCarAdapter(TrangchuActivity.this, carListXeSoSan);
                rcvthuexesosan.setAdapter(adapter2);
            }
        });
    }

    public void search(View view) {
//        Intent intent = new Intent(TrangchuActivity.this, SearchActivity.class);
//        startActivity(intent);
    }
}