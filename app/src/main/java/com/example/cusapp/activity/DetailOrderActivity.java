package com.example.cusapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cusapp.R;
import com.example.cusapp.adapter.Loginstatus;
import com.example.cusapp.model.Car;
import com.example.cusapp.model.Firebase;
import com.example.cusapp.model.Order;
import com.example.cusapp.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailOrderActivity extends AppCompatActivity {
    private TextView tv_namecar,tv_statuscar,tv_paymentMethod, tv_rentdate, tv_returndate, tv_typecar, tv_pricecar, tv_totalprice, tv_username, tv_numberphone;
    private AppCompatButton btn_cancel;
    private Toolbar toolbar;
    private String currentDate;
    private Order mcurrentOrder;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        mcurrentOrder = (Order) getIntent().getSerializableExtra("currentOrder");
        Anhxa();

        getData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogcancelorder();
            }
        });
    }

    private void getData() {
        tv_namecar.setText(mcurrentOrder.getCarName());
        tv_pricecar.setText(String.format("%,d", Math.round(mcurrentOrder.getCarPrice())) + " VNĐ/Ngày");
        tv_typecar.setText("Loại xe: " + mcurrentOrder.getCarType());
        currentDate = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        tv_rentdate.setText(mcurrentOrder.getRentDate());
        tv_returndate.setText(mcurrentOrder.getReturnDate());
        tv_totalprice.setText(String.format("%,d", Math.round(mcurrentOrder.getTotalprice())) + " VNĐ");
        tv_numberphone.setText(mcurrentOrder.getRenterPhone());
        tv_username.setText(mcurrentOrder.getRenterName());
        tv_statuscar.setText("Trạng thái: " + mcurrentOrder.getOrderStatus());
        tv_paymentMethod.setText(mcurrentOrder.getPaymentMethod());
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailorder);
        tv_namecar = findViewById(R.id.tv_namecar);
        tv_statuscar = findViewById(R.id.tv_statuscar);
        tv_paymentMethod = findViewById(R.id.tv_paymentMethod);
        tv_rentdate = findViewById(R.id.tv_rentdate);
        tv_returndate = findViewById(R.id.tv_returndate);
        tv_typecar = findViewById(R.id.tv_typecar);
        tv_pricecar = findViewById(R.id.tv_pricecar);
        tv_totalprice = findViewById(R.id.tv_totalprice);
        tv_username = findViewById(R.id.tv_username);
        tv_numberphone = findViewById(R.id.tv_numberphone);
        btn_cancel = findViewById(R.id.btn_cancel);
        mfirebase = new Firebase(this);
    }
    private void showdialogcancelorder(){
        View dialogView = LayoutInflater.from(DetailOrderActivity.this).inflate(R.layout.dialog_cancelorder, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(DetailOrderActivity.this);

        AppCompatButton btn_submit = dialogView.findViewById(R.id.btn_submit);
        RadioGroup rg_reasonscancel = dialogView.findViewById(R.id.rg_reasonscancel);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int reasonscancelID = rg_reasonscancel.getCheckedRadioButtonId();
                String reasonscancel = "";
                if (reasonscancelID == R.id.rb_ld1) {
                    reasonscancel = "Tôi muốn cập nhật địa chỉ/sđt.";
                } else if (reasonscancelID == R.id.rb_ld2) {
                    reasonscancel = "Tôi muốn thay đổi loại xe.";
                } else if (reasonscancelID == R.id.rb_ld3) {
                    reasonscancel = "Tôi không có nhu cầu thuê nữa.";
                } else if (reasonscancelID == R.id.rb_ld4) {
                    reasonscancel = "Tôi không tìm thấy lý do hủy phù hợp.";
                } else if (reasonscancelID == R.id.rb_ld5) {
                    reasonscancel = "Tôi muốn thay đổi phương thức thanh toán.";
                }
                if (reasonscancel.equals("")) {
                    Toast.makeText(DetailOrderActivity.this, "Vui lòng chọn lý do hủy đơn!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mfirebase.saveReasonsCancel(mcurrentOrder.getOrderID(), reasonscancel, new Firebase.SaveReasonsCancelCallback() {
                    @Override
                    public void onCallback(boolean isSuccess) {
                        if (isSuccess){
                            String status = "Đơn hàng đã bị hủy";
                            mfirebase.updateStatusOrder(mcurrentOrder.getOrderID(), status, new Firebase.UpdateStatusOrderCallback() {
                                @Override
                                public void onCallback(boolean isSuccess) {
                                    if (isSuccess){
                                        mfirebase.updateStatusCar(mcurrentOrder.getCarID(), "Còn Trống", new Firebase.UpdateStatusOrderCallback() {
                                            @Override
                                            public void onCallback(boolean isSuccess) {
                                                if (isSuccess){
                                                    Toast.makeText(DetailOrderActivity.this, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                    finish();
                                                    dialog.dismiss();
                                                }
                                                else {
                                                    Toast.makeText(DetailOrderActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(DetailOrderActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(DetailOrderActivity.this, "Hủy đặt xe thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.setContentView(dialogView);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}