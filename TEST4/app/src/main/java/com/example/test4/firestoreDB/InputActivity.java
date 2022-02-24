package com.example.test4.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test4.FCM.CloudMessagingActivity;
import com.example.test4.QRScanActivity;
import com.example.test4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private static String Token;
    private static String name = null;
    private static String number = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        init();
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        addChildEvent();
        //addValueEventListener();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placebtn:
                regPlace();
                break;
            case R.id.userbtn:
                regUser();
                break;
        }
    }

    private void init() {

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            this.uid = user.getUid();
//        }
    }

    private void initView() {
        Button regplacebtn = (Button) findViewById(R.id.placebtn);
        regplacebtn.setOnClickListener(this);

        Button reguserbtn = (Button) findViewById(R.id.userbtn);
        reguserbtn.setOnClickListener(this);

    }

    private void regUser()
    {
        EditText nameedit = (EditText) findViewById(R.id.inputname);
        EditText numberedit = (EditText) findViewById(R.id.number);
        name=nameedit.getText().toString();
        number=numberedit.getText().toString();


        if (nameedit.getText().toString().length() == 0) {
            Toast.makeText(this, "성함을 입력해주세요",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(numberedit.getText().toString().length() != 8)
        {
            Toast.makeText(this, "휴대폰번호는 8자리로 입력해주세요",
                    Toast.LENGTH_LONG).show();
            return;
        }


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("jang", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Token = task.getResult();
                        Log.d("jang", Token);
                        TokenReg();
                    }
                });



    }

    private void TokenReg() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("number", number);
        user.put("token",Token);
        user.put("가입일",LocalDate.now().toString());
        LocalTime nowtime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNowtime = nowtime.format(formatter);
        user.put("가입시간",formatedNowtime);

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
                                String RegName;
                                RegName=document.getData().get("name").toString();
                                String msg = "이 기기는 "+RegName+"님의 기기입니다. 다른 기기로 시도해주세요";
                                Toast.makeText(InputActivity.this, msg, Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            db.collection("users")
                                    .document(name+"-"+number)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Log.d("jang", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            Log.d("jang", "Document Error!!");
                                        }
                                    });
                            String msg = "등록이 완료되었습니다.";
                            Toast.makeText(InputActivity.this, msg, Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Log.d("jang", "first ", task.getException());

                        }
                    }
                });


//        DocumentReference docRef = db.collection("users").document(name+"-"+number);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d("jang", "DocumentSnapshot data: " + document.get("token"));
//                    } else {
//                        Log.d("jang", "No such document");
//                    }
//                } else {
//                    Log.d("jang", "get failed with ", task.getException());
//                }
//            }
//        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void regPlace() {
//        if (uid == null) {
//            Toast.makeText(this,
//                    "메모를 추가하기 위해서는 Firebase 인증이 되어야합니다. Firebase 인증 후 다시 진행해주세요.",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }

        EditText nameedit = (EditText) findViewById(R.id.inputname);
        EditText numberedit = (EditText) findViewById(R.id.number);

        String name=nameedit.getText().toString(),number=numberedit.getText().toString();
        if (nameedit.getText().toString().length() == 0) {
            Toast.makeText(this, "장소이름을 입력해주세요",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(numberedit.getText().toString().length() != 8)
        {
            Toast.makeText(this, "비밀번호는 8자리로 입력해주세요",
                    Toast.LENGTH_LONG).show();
            return;
        }

        LocalTime nowtime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNowtime = nowtime.format(formatter);
        LocalDate nowdate = LocalDate.now(); //0000-00-00
        String date = nowdate.toString();

        Map<String, Object> place = new HashMap<>();
        place.put("생성일", date);
        place.put("생성시간",formatedNowtime);
        place.put("pwd",number);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(name)
                .document("생성")
                .set(place)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Log.d("jang", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d("jang", "Document Error!!");
                    }
                });
    }
}
