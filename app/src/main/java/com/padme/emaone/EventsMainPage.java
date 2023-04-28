package com.padme.emaone;

import static android.icu.lang.UProperty.INT_START;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.curios.textformatter.FormatText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EventsMainPage extends AppCompatActivity {
    String Docid;

    FirebaseAuth FAuth;
    FirebaseFirestore FStore;
    FirebaseStorage FStorgae;

    Button joinbut;

    ImageView MainCoverImage;
   DocumentReference userEvents,eventUsers;
    TextView TitleV,LocationV,DateV,TimeV,DescV,ComitV,DeptV,CatV,partC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main_page);
        Intent pi=getIntent();
        Docid=pi.getStringExtra("DOCID");
        Log.d("Docid",Docid);

        FAuth=FirebaseAuth.getInstance();
        FStore= FirebaseFirestore.getInstance();
        FStorgae=FirebaseStorage.getInstance();
        TitleV=findViewById(R.id.Title);
        LocationV=findViewById(R.id.location);
        DateV=findViewById(R.id.Date);
        TimeV=findViewById(R.id.Time);
        DescV=findViewById(R.id.description);
        ComitV=findViewById(R.id.comitl);
        DeptV=findViewById(R.id.depl);
        CatV=findViewById(R.id.catl);
        partC=findViewById(R.id.participation_count);
        MainCoverImage=findViewById(R.id.mainCoverImage);
        userEvents=FirebaseFirestore.getInstance().collection("user_events_list").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).collection("joinedevents").document(Docid);
        eventUsers=FirebaseFirestore.getInstance().collection("participatedUserMeta").document(Docid).collection("participants").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        joinbut=findViewById(R.id.joinButtonMat);

        DocumentReference Docref=FStore.collection("Events").document(Docid);
        Docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("Title",documentSnapshot.getString("title"));
                TitleV.setText(documentSnapshot.getString("title"));
                LocationV.setText(documentSnapshot.getString("subTitle"));
                DescV.setText(FormatText.boldAndItalics(documentSnapshot.getString("textContent")));

                //DescV.setText(documentSnapshot.getString("textContent"));

             DateV.setText(documentSnapshot.getString("eventDateTime").substring(0,10));
               TimeV.setText(documentSnapshot.getString("eventDateTime").substring(10,16));
                partC.setText(documentSnapshot.getString("participantsCount"));
               DeptV .setText(documentSnapshot.getString("departmentName"));
                ComitV.setText(documentSnapshot.getString("commiteeName"));
                CatV.setText(documentSnapshot.getString("category"));




                Resources res = getResources();
                Drawable placeholder = ResourcesCompat.getDrawable(res, R.drawable.bg_cover2, null);
                String filename=documentSnapshot.getId().toString()+"cover_image";
                StorageReference srefcover=FirebaseStorage.getInstance().getReference("coverimages/"+filename);
                srefcover.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .centerCrop()
                                .placeholder(placeholder)
                                .into(MainCoverImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        userEvents.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    joinbut.setText("Joined");
                    joinbut.setTextColor(Color.BLACK);
                    joinbut.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EventsMainPage.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

public void cancelButton(View v)
{
    super.onBackPressed();
}

    public void joinButton(View v)
    {
        DocumentReference joinedShards=FirebaseFirestore.getInstance().collection("ParticipantCounterShards").document(Docid);
       userEvents.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    userEvents.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            eventUsers.delete();
                            SolutionCounters sc= new SolutionCounters();
                            sc.decrementCounter(joinedShards,15);
                            joinbut.setText("Join");
                            joinbut.setTextColor(Color.WHITE);
                            joinbut.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.greenJoinBtnColor)));


                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
                else
                {
                    Map<String,Object> joiner= new HashMap<>();
                    joiner.put("joined","True");
                 joiner.put("datetime",  new Timestamp(new Date()));
                    userEvents.set(joiner).addOnFailureListener(e -> {
                        Toast.makeText(EventsMainPage.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Map<String,Object> participants= new HashMap<>();
                            participants.put("joined","True");
                            SolutionCounters sc= new SolutionCounters();
                            sc.incrementCounter(joinedShards,15);
                            eventUsers.set(participants);


                            joinbut.setText("Joined");
                            joinbut.setTextColor(Color.BLACK);
                            joinbut.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                            // Toast.makeText(EventsMainPage.this, "Joined ", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });


    }
}

