package com.example.cusapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cusapp.R;
import com.example.cusapp.adapter.Loginstatus;
import com.example.cusapp.adapter.VpgImageCarAdapter;
import com.example.cusapp.model.Car;
import com.example.cusapp.model.Firebase;
import com.example.cusapp.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class DeitailCarActivity extends AppCompatActivity {
    private TextView tv_namecar, tv_pricecar, tv_typecar, tv_statuscar, tv_describebook;
    private ViewPager vpg_detailcar;
    private Car currentCar;
    private Toolbar toolbar;
    private AppCompatButton btn_thuexe;
    private String userID;
    private User user;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deitail_car);
        currentCar = (Car) getIntent().getSerializableExtra("currentCar");
        Anhxa();
        setdata();
        getin4user();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        btn_thuexe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    String giaypheplaixe = user.getGiaypheplaixe();
                    if (giaypheplaixe != null && giaypheplaixe.length() == 12){
                        Intent i = new Intent(DeitailCarActivity.this, OrderingActivity.class);
                        i.putExtra("currentCar", currentCar);
                        startActivity(i);
                    } else if (giaypheplaixe == null) {
                        showDialogComfirmGPLX();
                    } else {
                        showDialogComfirmGPLX();
                    }
                }
                else {
                    Toast.makeText(DeitailCarActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DeitailCarActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }
    private void setdata() {
        tv_namecar.setText(currentCar.getNamecar());
        tv_pricecar.setText(String.format("%,d", Math.round(currentCar.getPricecar())) + " VNĐ/Ngày");
        tv_typecar.setText("Loại xe: "+ currentCar.getTypecar());
        tv_statuscar.setText("Tình trạng: "+ currentCar.getStatuscar());
        tv_describebook.setText(currentCar.getDescriptioncar());
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(currentCar.getImage1());
        imageUrls.add(currentCar.getImage2());
        imageUrls.add(currentCar.getImage3());
        VpgImageCarAdapter adapter = new VpgImageCarAdapter(this, imageUrls);
        vpg_detailcar.setAdapter(adapter);
        if(Loginstatus.getInstance().isLoggedIn() == true){
            userID = Loginstatus.getInstance().getUserID();
        }
        mfirebase = new Firebase(this);
    }
    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailcar);
        tv_namecar = findViewById(R.id.tv_namecar);
        tv_pricecar = findViewById(R.id.tv_pricecar);
        tv_typecar = findViewById(R.id.tv_typecar);
        tv_statuscar = findViewById(R.id.tv_statuscar);
        tv_describebook = findViewById(R.id.tv_describebook);
        vpg_detailcar = findViewById(R.id.vpg_detailcar);
        btn_thuexe = findViewById(R.id.btn_thuexe);
    }
    private void showDialogComfirmGPLX(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DeitailCarActivity.this);
        View dialogView = LayoutInflater.from(DeitailCarActivity.this).inflate(R.layout.dialog_comfirmgplx, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        AppCompatButton btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        AppCompatButton btn_confirm = dialogView.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeitailCarActivity.this, UpdateInforUserActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void getin4user() {
        if(userID != null){
            mfirebase.getInforUserByUserId(userID, new Firebase.UserCallback() {
                @Override
                public void onCallback(User muser) {
                    user = muser;
                }
            });
        }
    }
}