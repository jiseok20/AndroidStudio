package com.example.test4.FCM;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.test4.R;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class CloudMessagingActivity extends AppCompatActivity implements View.OnClickListener
{
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);

        Button tokenbtn = (Button)findViewById(R.id.tokenbtn);
        tokenbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tokenbtn:
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("jang", "getInstanceId failed", task.getException());
                                    return;
                                }
                                token = task.getResult(); // Get new Instance ID token

                                // Log and toast
                                String msg = "InstanceID Token: " + token;
                                Log.d("jang", msg);
                                Toast.makeText(CloudMessagingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            default:
                break;
        }
    }
}