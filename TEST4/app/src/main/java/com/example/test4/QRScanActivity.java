package com.example.test4;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test4.MLKIT.CameraXLivePreviewActivity;
import com.example.test4.firestoreDB.InputActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class QRScanActivity extends AppCompatActivity implements View.OnClickListener{
    private IntentIntegrator qrScan;
    private static String placeName=null;
    private static String userInfo = null;
    private static String Token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entry();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.entrybtn:
                entry();
                break;
        }
    }

    private void entry() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("jang", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Token = task.getResult();
                        String msg = "토큰 얻기 : "+Token;
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
                                qrScan = new IntentIntegrator(QRScanActivity.this);
                                qrScan.setOrientationLocked(false);
                                qrScan.setPrompt("강의실의 QR코드를 찍어주세요");
                                qrScan.initiateScan();
                            }
                        }else {
                            Log.d("jang", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                placeName=result.getContents();
                firedbReg();

//                Intent intent = new Intent(QRScanActivity.this, CameraXLivePreviewActivity.class);
//                startActivity(intent);
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void firedbReg() {

        LocalTime nowtime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNowtime = nowtime.format(formatter);
        LocalDate nowdate = LocalDate.now(); //0000-00-00
        String date = nowdate.toString();

        Map<String, Object> PlaceVisit = new HashMap<>();
        PlaceVisit.put(formatedNowtime, placeName);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(placeName).document("생성");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(QRScanActivity.this, "Scanned: " + placeName, Toast.LENGTH_LONG).show();

                        db.collection("users").document(userInfo)
                                .collection("visit").document(date).set(PlaceVisit,SetOptions.merge());

                        Map<String, Object> PlaceTimeUser = new HashMap<>();
                        PlaceTimeUser.put(formatedNowtime, userInfo);

                        db.collection(placeName).document(date).set(PlaceTimeUser, SetOptions.merge());
                        Log.d("jang", "reg complete "+placeName+date);
                        finish();

                    } else {
                        String error = placeName+" is not found";
                        Toast.makeText(QRScanActivity.this, error, Toast.LENGTH_LONG).show();
                        Log.d("jang", "sacned failed "+placeName);

                    }
                } else {
                    Log.d("jang", "get failed with ", task.getException());
                }
            }
        });
    }

}