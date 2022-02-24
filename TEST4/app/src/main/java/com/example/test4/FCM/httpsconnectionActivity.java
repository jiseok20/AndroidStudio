package com.example.test4.FCM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.test4.QRScanActivity;
import com.example.test4.R;
import com.example.test4.firestoreDB.PUItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.MediaType;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import android.content.ContentValues;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Vector;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class httpsconnectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static Map<String, Object> contactPlace=null;
    private static Map<String, Object> contactor=null;
    private static String Token=null;
    private static String UserInfo=null;
    private static String date=null;
    private static Vector<String> nameCP=new Vector<>();
    private static Vector<String> placeNameCP=new Vector<>();
    private static Vector<String> TokenCP=new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httpsconnection);

        Button confirmedbtn = (Button) findViewById(R.id.confirmedbtn);
        confirmedbtn.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmedbtn:
                confirmed();
                break;
        }

    }
        private void confirmed () {

            EditText dateedit = (EditText) findViewById(R.id.infecteddate);
            date = dateedit.getText().toString();
            if (date.length() == 0) {
                Toast.makeText(this, "날짜를 입력하시오(8자리)",
                        Toast.LENGTH_LONG).show();
                return;
            }
            String year,month,day;
            year=date.substring(0,4);
            month=date.substring(4,6);
            day=date.substring(6);

            date=null;
            date=year+"-"+month+"-"+day;




            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users")
                    .document(UserInfo)
                    .collection("visit").document(date); //날짜에 따른 방문장소 검색
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            contactPlace = document.getData(); //{시간 = 장소}
                            Log.d("jang", "contactPlace data: " + contactPlace);
                            placeSearch(date);
                        } else {
                            Log.d("jang", "No such contactPlace document");
                        }
                    } else {
                        Log.d("jang", "get failed with ", task.getException());
                    }
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
                                UserInfo = document.getId();
                            }
                        }else {
                            Log.d("jang", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }




    private void placeSearch(String date) {

        Iterator<String> keys = contactPlace.keySet().iterator();
        while (keys.hasNext() ) { //같은 장소에 대한 접촉자 찾는 과정 반복
            String key = keys.next();
            String placeName=contactPlace.get(key).toString();
            for(int i=0;i<placeNameCP.size();i++)
            {
                if(placeNameCP.elementAt(i).equals(placeName))
                    placeName=null;
            }
            if(placeName!=null) {
                placeNameCP.add(placeName);
                Log.d("jang", "placeNameCP.size: " + placeNameCP.size());
                Log.d("jang", "placeNameCP: " + placeNameCP.toString());
            }
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int i=0;i<placeNameCP.size();i++) {
            DocumentReference docRef = db.collection(placeNameCP.elementAt(i)).document(date);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            contactor = document.getData(); //{시간 = 이름-번호}
                            Log.d("jang", "contactor data: " + contactor);
                            tokenCollect(date);
                        } else {
                            Log.d("jang", "No such contactor document");
                        }
                    } else {
                        Log.d("jang", "get failed with ", task.getException());
                    }
                }
            });
        }

    }

    private void tokenCollect(String date) {
        Iterator<String> keys = contactor.keySet().iterator();
        nameCP=new Vector<>();

        while (keys.hasNext() ) {
            String key = keys.next();
            String name=contactor.get(key).toString();
            for(int i=0;i<nameCP.size();i++)
            {
                if(nameCP.elementAt(i).equals(name))
                    name=null;
            }
            if(name!=null) {
                nameCP.add(name);
                Log.d("jang", "nameCP: " + nameCP.toString());
            }
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int i=0;i<nameCP.size();i++)
        {

            db.collection("users").document(nameCP.elementAt(i)).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) { //token 검색
                                    Token = document.getData().get("token").toString();
                                    TokenCP.add(Token);
                                    if(nameCP.size()==TokenCP.size())
                                    {
                                        PushAlarm(date); //토큰마다 푸쉬알림
                                    }

                                } else {
                                    Log.d("jang", "No such token");
                                }

                            } else {
                                Log.d("jang", "get failed with ", task.getException());
                            }
                        }
                    });
        }

    }

    private void PushAlarm(String date) {

        //메시지 가공
        JsonObject jsonObj = new JsonObject();

        //token
        Gson gson = new Gson();
        JsonArray registToken = new JsonArray();

        for(int i=0;i<TokenCP.size();i++)
        {
            registToken.add(TokenCP.elementAt(i));
        }

        //jsonObj.add("to", jsonElement);
        jsonObj.add("registration_ids", registToken);

        //Notification
        JsonObject notification = new JsonObject();
        notification.addProperty("title", date+" : 밀접접촉자 안내");
        notification.addProperty("message", "자가검사를 진행하시오");
        jsonObj.add("data", notification);

        /*발송*/
        class BackgroundThread extends Thread {
            public void run() {
                final MediaType mediaType = MediaType.parse("application/json");
                OkHttpClient httpClient = new OkHttpClient();
                try {
                    Request request = new Request.Builder().url("https://fcm.googleapis.com/fcm/send")
                            .addHeader("Content-Type", "application/json; UTF-8")
                            .addHeader("Authorization", "Key=" + "AAAAKA0piQQ:APA91bEVerDnCBuiJWJSbWO5eF9cOFymhU5w7SUUxcNg5nvLeYC3leDbaXI5aVHhNNvyACVJLyDnh0-qFhBQ_csAIVQ8Q23sq3Ce7TFazYPLB0hEMjN8iT0s9bwr2iz8cK1FWmuAsHsW")
                            .post(RequestBody.create(mediaType, jsonObj.toString())).build();
                    Response response = httpClient.newCall(request).execute();
                    String res = response.body().string();
                    Log.d("jang", jsonObj.toString());
                    Log.d("jang", "notification response " + res);
                } catch (IOException e) {
                    Log.d("jang", "Error in sending message to FCM server " + e);
                }
            }
        }
        BackgroundThread thread = new BackgroundThread();
        thread.start();
        finish();
    }
}

