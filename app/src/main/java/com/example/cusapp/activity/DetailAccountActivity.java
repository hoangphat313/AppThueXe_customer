package com.example.cusapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cusapp.R;
import com.example.cusapp.adapter.Loginstatus;
import com.example.cusapp.model.Firebase;
import com.example.cusapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class DetailAccountActivity extends AppCompatActivity {
    private ImageView img_avataruser;
    private TextView tv_username, tv_historyorder, tv_signout, tv_ordering, tv_updateinforuer;
    private ImageButton back;
    private String userID;
    private User user;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_account);

        Anhxa();
        getin4user();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        tv_updateinforuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailAccountActivity.this, UpdateInforUserActivity.class);
                startActivity(i);
            }
        });
        tv_historyorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailAccountActivity.this, HistoryOrderActivity.class);
                startActivity(i);
            }
        });
        tv_ordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailAccountActivity.this, OrdersActivity.class);
                startActivity(i);
            }
        });
        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Loginstatus.getInstance().setLoggedIn(false);
                Loginstatus.getInstance().setUserID(null);
                Intent intent = new Intent(DetailAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getin4user();
    }

    private void Anhxa() {
        img_avataruser = findViewById(R.id.img_avataruser);
        back = findViewById(R.id.back);
        tv_username = findViewById(R.id.tv_username);
        tv_ordering = findViewById(R.id.tv_ordering);
        tv_historyorder = findViewById(R.id.tv_historyorder);
        tv_signout = findViewById(R.id.tv_signout);
        tv_updateinforuer = findViewById(R.id.tv_updateinforuer);
        userID = Loginstatus.getInstance().getUserID();
        mfirebase = new Firebase(this);
    }
    private void getin4user() {
        mfirebase.getInforUserByUserId(userID, new Firebase.UserCallback() {
            @Override
            public void onCallback(User muser) {
                user = muser;
                setin4user();
            }
        });
    }
    private void setin4user() {
        if(user.getUsername() != null) tv_username.setText(user.getUsername());
        if(user.getUseravatar() != null) Glide.with(DetailAccountActivity.this).load(user.getUseravatar()).into(img_avataruser);
    }
}