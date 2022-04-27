package com.example.test4.firestoreDB;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, PUViewListener {
    private ArrayList<PUItem> puitem = null;
    private PlaceUserAdapter placeUserAdapter = null;
    private static Map<String, Object> inoutuser=null;
    private static Map<String, Object> pwdcheck=null;
    private static String name=null;
    private static String date=null;
    private static String pwd=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onItemClick(int position, View view) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchplace:
                search();
                break;
            default:
                break;
        }
    }

    private void init() {
        puitem = new ArrayList<>();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            this.uid = user.getUid();
//        }
    }

    private void initView() {
        Button regbtn = (Button) findViewById(R.id.searchplace);
        regbtn.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new  LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.userlist);
        placeUserAdapter = new PlaceUserAdapter(puitem, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(placeUserAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void  search() {

        name=null;
        date=null;
        pwd=null;
        EditText nameedit = (EditText) findViewById(R.id.name);
        EditText dateedit = (EditText) findViewById(R.id.date);
        EditText pwdedit = (EditText) findViewById(R.id.pwd);
        name=nameedit.getText().toString();
        date=dateedit.getText().toString();
        pwd=pwdedit.getText().toString();
        String year,month,day;
        year=date.substring(0,4);
        month=date.substring(4,6);
        day=date.substring(6);

        date=null;
        date=year+"-"+month+"-"+day;

        if (name.length() == 0 ||
                date.length() == 0||
                pwd.length() == 0) {
            Toast.makeText(this,
                    "장소 이름 또는 날짜 또는 비밀번호가 입력되지 않았습니다. 입력 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRefconf = db.collection(name).document("생성");
        docRefconf.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        pwdcheck = document.getData();
                        Log.d("jang", "DocumentS check: " + pwdcheck);
                        check();
                    } else {
                        Log.d("jang", "No such document");
                        Toast.makeText(SearchActivity.this,
                                name+"은(는) 등록되어있지 않습니다. 다시 확인해주세요",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("jang", "get failed with ", task.getException());
                }
            }
        });



    }
    private void  check()
    {
        String password = pwdcheck.get("pwd").toString();
        if(password.equals(pwd)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection(name).document(date);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            inoutuser = document.getData();
                            Log.d("jang", "DocumentS data: " + inoutuser);
                            print();
                        } else {
                            Log.d("jang", "No such document");
                        }
                    } else {
                        Log.d("jang", "get failed with ", task.getException());
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void  print()
    {
        int i=0;
        Iterator<String> keys = inoutuser.keySet().iterator();
        while (keys.hasNext() ) {
            PUItem item = new PUItem();
            String key = keys.next();
            item.setUser(inoutuser.get(key).toString());
            item.setTime(key);
            puitem.add(item);
            Log.d("jang", puitem.get(i).getTime()+"   "+puitem.get(i).getUser());
            placeUserAdapter.notifyDataSetChanged();
            i++;
        }
    }
}
