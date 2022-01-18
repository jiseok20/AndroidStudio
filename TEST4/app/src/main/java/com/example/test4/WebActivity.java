package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {


    private WebView web_view;
    private String url = "https://m.youtube.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        web_view = (WebView) findViewById(R.id.web_view);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setAllowContentAccess(true);
        web_view.getSettings().setAppCacheEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.setWebChromeClient(new WebChromeClient());
        web_view.setWebViewClient(new WebViewClientClass());
        web_view.loadUrl(url);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if((keyCode == KeyEvent.KEYCODE_BACK) && web_view.canGoBack()){
            web_view.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}