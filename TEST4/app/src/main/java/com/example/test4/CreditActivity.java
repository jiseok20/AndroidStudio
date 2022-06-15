package com.example.test4;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test4.MLKIT.SettingsActivity;

public class CreditActivity extends AppCompatActivity {
    private String str1;
    private TextView makers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        makers=findViewById(R.id.text_makers);
        str1 = "Access Management\n    Version 0.3.5 \n\n\n2017035217 이지석\n2017035136 장준호\n";
        makers.setText(str1);


         Button settingsButton = findViewById(R.id.settings_button_X);
        settingsButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.putExtra(
                            SettingsActivity.EXTRA_LAUNCH_SOURCE,
                            SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW);
                    startActivity(intent);
                });

        Button btn_github = findViewById(R.id.btn_github);
        btn_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jiseok20/AndroidStudio"));
                startActivity(browserIntent);

            }
        });

    }
}