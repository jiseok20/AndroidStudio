package com.example.test4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText grade_number;
    private String str1,str2;
    private Button btn_login;
    private Button btn_webview;
    private ListView list_string;
    private Button btn_start,btn_stop;
    private Button btn_music_start,btn_music_stop;
    private Spinner spinner;
    private Button btn_QR;
    private TextView array_result;
    private long backBtnTime=0;

    ImageView btn_img;

    Thread thread;
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

        name = findViewById(R.id.name);
        grade_number = findViewById(R.id.grade_number);
        btn_login=findViewById(R.id.btn_login);
        list_string =(ListView) findViewById(R.id.list_string);
        btn_img = (ImageView)findViewById(R.id.btn_img);
        btn_webview =findViewById(R.id.btn_webview);
        btn_start=findViewById(R.id.btn_start);
        btn_stop=findViewById(R.id.btn_stop);
        btn_music_start=findViewById(R.id.btn_music_start);
        btn_music_stop=findViewById(R.id.btn_music_stop);
        spinner=findViewById(R.id.spinner);
        array_result=findViewById(R.id.array_result);
        btn_QR=findViewById(R.id.btn_QR);




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str1 = name.getText().toString();
                str2=grade_number.getText().toString();
                Intent intent = new Intent(MainActivity.this , SubActivity.class);
                intent.putExtra("name",str1);
                intent.putExtra("grade_number",str2);
                startActivity(intent); //activity 이동

            }
        });

        btn_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WebActivity.class);
                startActivity(intent);
            }
        });

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"훠훠훠",Toast.LENGTH_SHORT).show();
            }
        });

        //thread start
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isThread = true;
                thread = new Thread(){
                  public void run(){
                      while (isThread){
                          try {
                              sleep(3000);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                          handler.sendEmptyMessage(0);
                      }
                  }
                };
                thread.start();
            }
        });
        //thread stop
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isThread = false;
            }
        });


        List<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        list_string.setAdapter(adapter);

        data.add("ListView");
        data.add("졸작 얼른 만들고 싶다");
        data.add("스레드 버튼을 눌러보세요!");
        adapter.notifyDataSetChanged();

        btn_music_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(),MusicService.class));

            }
        });

        btn_music_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(),MusicService.class));


            }
        });

        btn_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,QRScanActivity.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                array_result.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(),"선주 천재",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"선주 똑똑해",Toast.LENGTH_SHORT).show();
        }
    };

}