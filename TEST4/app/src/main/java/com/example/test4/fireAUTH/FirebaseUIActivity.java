package com.example.test4.fireAUTH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.View;
import android.widget.Button;

import com.example.test4.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUIActivity extends AppCompatActivity implements View.OnClickListener
{
    private  static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_uiactivity);

        Button firebaseuiauthbtn = (Button) findViewById(R.id.firebaseuiauthbtn);
        firebaseuiauthbtn.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK)
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//getCurrentUser();

            }
            else
            {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.firebaseuiauthbtn:
                signin();
                break;
            default:
                break;
        }
    }

    private void signin()
    {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())
                        .setLogo(getSelectedLogo())
                        .setAvailableProviders(getSelectedProviders())
                        .setTosAndPrivacyPolicyUrls("https://naver.com","https://google.com")
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);
    }

    private int getSelectedTheme()
    {
        return AuthUI.getDefaultTheme();
    }

    private int getSelectedLogo()
    {
        return AuthUI.NO_LOGO;
    }

    private List<AuthUI.IdpConfig> getSelectedProviders()
    {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        CheckBox googlechk = (CheckBox) findViewById(R.id.google_provider);
        CheckBox facebookchk = (CheckBox) findViewById(R.id.facebook_provider);
        CheckBox twitterchk = (CheckBox) findViewById(R.id.twitter_provider);
        CheckBox emailchk = (CheckBox) findViewById(R.id.email_provider);

        if(googlechk.isChecked())
        {
            selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }
        if(facebookchk.isChecked())
        {
            selectedProviders.add(new AuthUI.IdpConfig.FacebookBuilder().build());
        }
        if(twitterchk.isChecked())
        {
            selectedProviders.add(new AuthUI.IdpConfig.TwitterBuilder().build());
        }
        if(emailchk.isChecked())
        {
            selectedProviders.add(new AuthUI.IdpConfig.EmailBuilder().build());
        }
        return selectedProviders;
    }




}