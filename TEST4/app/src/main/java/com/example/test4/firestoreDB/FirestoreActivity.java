package com.example.test4.firestoreDB;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.test4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirestoreActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        Button adddatabtn = (Button) findViewById(R.id.firestoreadddatabtn);
        adddatabtn.setOnClickListener(this);
        Button setdatabtn = (Button) findViewById(R.id.firestoresetdatabtn);
        setdatabtn.setOnClickListener(this);
        Button deletedocbtn = (Button) findViewById(R.id.firestoredeletedocbtn);
        deletedocbtn.setOnClickListener(this);
        Button deletefieldbtn = (Button) findViewById(R.id.firestoredeletefieldbtn);
        deletefieldbtn.setOnClickListener(this);
        Button selectdocbtn = (Button) findViewById(R.id.firestoreseldatabtn);
        selectdocbtn.setOnClickListener(this);
        Button selectwheredocbtn = (Button) findViewById(R.id.firestoreselwheredatabtn);
        selectwheredocbtn.setOnClickListener(this);
        Button listenrdocbtn = (Button) findViewById(R.id.firestorelistenerdatabtn);
        listenrdocbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.firestoreadddatabtn:
                addData();
                break;
            case R.id.firestoresetdatabtn:
                setData();
                break;
            case R.id.firestoredeletedocbtn:
                deleteDoc();
                break;
            case R.id.firestoredeletefieldbtn:
                deleteField();
                break;
            case R.id.firestoreseldatabtn:
                selectDoc();
                break;
            case R.id.firestoreselwheredatabtn:
                selectwhereDoc();
                break;
            case R.id.firestorelistenerdatabtn:
                listenerDoc();
                break;
        }

    }

    private void listenerDoc() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("users").document("userinfo");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    Log.w("jang", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists())
                {
                    Log.d("jang", "Current data: " + snapshot.getData());
                }
                else
                {
                    Log.d("jang", "Current data: null");
                }
            }
        });
    }

    private void selectwhereDoc()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("age",25)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("jang", document.getId() + "=>" + document.getData());

                                UserInfo userInfo = document.toObject(UserInfo.class);
                                Log.d("jang", "name = " + userInfo.getName());
                                Log.d("jang", "addres = " + userInfo.getAddress());
                                Log.d("jang", "id = " + userInfo.getId());
                                Log.d("jang", "pwd = " + userInfo.getPwd());
                                Log.d("jang", "age = " + userInfo.getAge());
                            }
                        } else {
                            Log.d("jang", "Error getting documents ", task.getException());
                        }
                    }

                });

    }

    private void selectDoc()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document("userinfo");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Log.d("jang", "DocumentSnapshot data: " + document.getData());
                        UserInfo userInfo = document.toObject(UserInfo.class);
                        Log.d("jang","name = "+userInfo.getName());
                        Log.d("jang","addres = "+userInfo.getAddress());
                        Log.d("jang","id = "+userInfo.getId());
                        Log.d("jang","pwd = "+userInfo.getPwd());
                        Log.d("jang","age = "+userInfo.getAge());
                    }
                    else
                    {
                        Log.d("jang", "No such document");
                    }
                }
                else
                {
                    Log.d("jang", "get failed with ", task.getException());
                }
            }
        });
    }

    private void deleteField()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document("userinfo");

        Map<String,Object> updates = new HashMap<>();
        updates.put("address", FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("jang", "DocumentSnapshot successfully deleted!");
            }
        });
    }

    private void deleteDoc()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("userinfo")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("jang", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("jang", "Error deleting document", e);
                    }
                });
    }

    private void setData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> member = new HashMap<>();
        member.put("name", "나야나");
        member.put("address", "경기도");
        member.put("age", 25);
        member.put("id", "my");
        member.put("pwd", "hello!");

        db.collection("users")
                .document("userinfo")
                .set(member)
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

    private void addData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp time = new Timestamp(new Date());

        Map<String, Object> member = new HashMap<>();
        member.put("time",time);
//        member.put("name", "홍길동");
//        member.put("address", "수원시");
//        member.put("age", 25);
//        member.put("id", "hong");
//        member.put("pwd", "hello!");

        db.collection(time.toString())
                .add(member)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Log.d("jang","Document ID = " + documentReference.get());
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("jang","Document Error!!");
                    }
                });
    }
}

