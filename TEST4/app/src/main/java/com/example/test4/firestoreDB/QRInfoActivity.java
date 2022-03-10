package com.example.test4.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test4.QRScanActivity;
import com.example.test4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRInfoActivity extends AppCompatActivity {

    private static String Token = null;
    private static String userInfo = null;
    private ImageView iv;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrinfo);

        iv = (ImageView)findViewById(R.id.qrcode);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("jang", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Token = task.getResult();
                        String msg = "토큰 얻기 : " + Token;
                        Log.d("jang", msg);
                        getUserInfo();
                    }
                });
    }

        private void getUserInfo() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .whereEqualTo("token", Token)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("jang", document.getId() + " => " + document.getData());
                                    userInfo = document.getId();
                                    Log.d("jang", "유저인포:"+userInfo);
                                    creatQR();
                                }
                            }else {
                                Log.d("jang", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }

    private void creatQR() {

        text=convertUnicode(userInfo);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
        }catch (Exception e){}
    }

    private static String convertUnicode(String val) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < val.length(); i++) {
            int code = val.codePointAt(i);
            if (code < 128) {
                sb.append(String.format("%c", code));
            } else {
                sb.append(String.format("\\u%04x", code));
            }
        }
        return sb.toString();
    }
}