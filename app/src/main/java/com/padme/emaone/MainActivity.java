package com.padme.emaone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    private Handler mH = new Handler();
    FirebaseFirestore FStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        FStore = FirebaseFirestore.getInstance();

        mH.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getApplicationContext(), reg_0.class));
                    finish();

                } else {
                    String UID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    Log.d("UIDDDDDDDDDDDDDDDDDDDDDDDDDDd", UID);
                    DocumentReference docRef = FStore.collection("users").document(UID);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    startActivity(new Intent(getApplicationContext(), content_main.class));
                                    finish();
                                } else {
                                    fAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(), reg_0.class));
                                     finish();
                                }
                            }

                        }
                    });
                }

            }
        }, 600);
    }


}