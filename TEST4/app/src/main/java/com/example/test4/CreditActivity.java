package com.example.test4;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test4.MLKIT.SettingsActivity;

public class CreditActivity extends AppCompatActivity {
    private String str1,str2;
    private TextView makers;
    private TextView git_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        makers=findViewById(R.id.text_makers);
        git_link=findViewById(R.id.git_link);
        str1 = "Access Management \n\n\n2017035217 이지석\n2017035136 장준호\n";
        str2 = "https://github.com/jiseok20/AndroidStudio/tree/master";
        makers.setText(str1);
        git_link.setText(str2);

         Button settingsButton = findViewById(R.id.settings_button_X);
        settingsButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.putExtra(
                            SettingsActivity.EXTRA_LAUNCH_SOURCE,
                            SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW);
                    startActivity(intent);
                });

    }
}