package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test4.FCM.httpsconnectionActivity;
import com.example.test4.firestoreDB.InputActivity;
import com.example.test4.firestoreDB.QRInfoActivity;
import com.example.test4.firestoreDB.SearchActivity;
import com.example.test4.firestoreDB.TakeNumberActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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
        btn_img.setOnClickListener((this));
        Glide.with(this).load(R.drawable.woowang).into(btn_img);

        Button btn_QR = (Button) findViewById(R.id.btn_QR); //출입
        btn_QR.setOnClickListener((this));

        Button btn_take_number = (Button) findViewById(R.id.btn_take_number); //번따
        btn_take_number.setOnClickListener((this));

        Button btn_makeQR = (Button) findViewById(R.id.btn_makeQR); //QR만들기
        btn_makeQR.setOnClickListener((this));

        Button btn_join = (Button)findViewById(R.id.btn_join); //등록
        btn_join.setOnClickListener((this));

        Button btn_search = (Button)findViewById(R.id.btn_search); // 조회
        btn_search.setOnClickListener((this));

        Button btn_covid = (Button)findViewById(R.id.btn_covid); //확진
        btn_covid.setOnClickListener((this));

    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_QR: // 출입
                Intent i=new Intent(this, QRScanActivity.class );//TakeNumberActivity
                startActivity(i);
                break;
            case R.id.gif_image: // 크레딧
                i=new Intent(this, CreditActivity.class );
                startActivity(i);
                break;
            case R.id.btn_join: //등록
                i=new Intent(this, InputActivity.class );
                startActivity(i);
                break;
            case R.id.btn_search: // 조회
                i=new Intent(this, SearchActivity.class );
                startActivity(i);
                break;
            case R.id.btn_covid: // 확진
                i=new Intent(this, httpsconnectionActivity.class );
                startActivity(i);
                break;
            case R.id.btn_take_number: // 번따
                i=new Intent(this, TakeNumberActivity.class );
                startActivity(i);
                break;
            case R.id.btn_makeQR: // QR 생성
                i=new Intent(this, QRInfoActivity.class );
                startActivity(i);
                break;
            default:
            break;
        }

    }
}