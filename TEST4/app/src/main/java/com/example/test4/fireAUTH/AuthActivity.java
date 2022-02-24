package com.example.test4.fireAUTH;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.test4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.firebase.ui.auth.AuthUI;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button firebaseuibtn = (Button) findViewById(R.id.firebaseui);
        firebaseuibtn.setOnClickListener(this);

        Button firebasesignout = (Button)findViewById(R.id.firebasesignout);
        firebasesignout.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            firebaseuibtn.setEnabled(false);
            firebasesignout.setEnabled(true);
        }
        else {
            firebaseuibtn.setEnabled(true);
            firebasesignout.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.firebaseui:
                Intent i = new Intent(this, FirebaseUIActivity.class);
                startActivity(i);
                break;
            case R.id.firebasesignout:
                signOut();
                break;
            default:
                break;
        }
    }

    private void signOut()
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            finish();
                        }
                        else {
                        }
                    }
                });
    }
}