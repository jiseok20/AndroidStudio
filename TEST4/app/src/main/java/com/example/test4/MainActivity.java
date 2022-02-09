package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_QR,btn_join,btn_search,btn_covid;
    private long backBtnTime=0;
    ImageView btn_img;
    boolean isThread = false;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime-backBtnTime;

        if(0<=gapTime && 2000>=gapTime){
            super.onBackPressed();
        }
        else{
            backBtnTime = curTime;
            Toast.makeText(this,"한번 더 누르면 종료됩니다",Toast.LENGTH_SHORT).show();
        }
    }

    @Override //처음 켜면 나오는 화면
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_img = findViewById(R.id.gif_image);
        Glide.with(this).load(R.drawable.cancer).into(btn_img);

        btn_QR=findViewById(R.id.btn_QR); //출입
        btn_join=findViewById(R.id.btn_join); //등록
        btn_search=findViewById(R.id.btn_search); // 조회
        btn_covid=findViewById(R.id.btn_covid); //확진

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreditActivity.class);
                startActivity(intent);
            }
        });

        btn_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,QRScanActivity.class);
                startActivity(intent);
            }
        });

    }
}