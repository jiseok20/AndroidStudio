package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.awt.font.TextAttribute;

public class SubActivity extends AppCompatActivity {

    private TextView sub_name;
    private TextView sub_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        sub_name = findViewById(R.id.sub_name);
        sub_grade=findViewById(R.id.sub_grade);

        Intent intent = getIntent();
        String str1 = intent.getStringExtra("name");
        String str2 = intent.getStringExtra("grade_number");

        sub_name.setText(str1);
        sub_grade.setText(str2);
    }
}