package com.padme.emaone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AprooveEvent extends AppCompatActivity {
    String Docid;

    FirebaseAuth FAuth;
    FirebaseFirestore FStore;
    FirebaseStorage FStorgae;

    Button joinbut;

    ImageView MainCoverImage;
    DocumentReference userEvents,eventUsers;
    TextView TitleV,LocationV,DateV,TimeV,DescV,ComitV,DeptV,CatV,partC;
    DocumentReference Docref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproove_event);
        Intent pi=getIntent();
        Docid=pi.getStringExtra("DOCID");
        Log.d("Docid",Docid);

        FAuth=FirebaseAuth.getInstance();
        FStore= FirebaseFirestore.getInstance();
        FStorgae=FirebaseStorage.getInstance();
        TitleV=findViewById(R.id.imagelinks);
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

        Docref=FStore.collection("Events").document(Docid);
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



    }

    public void cancelButton(View v)
    {
        super.onBackPressed();
    }

    public void joinButton(View v)
    {

      Docref.update("docid","").addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void unused) {

              finish();

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Toast.makeText(AprooveEvent.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
              finish();
          }
      });



    }
}
