package com.padme.emaone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class singleEvents extends AppCompatActivity {
    RecyclerView HomeRecyclerView;

    EditText searchbox;

    String com;
    FirestoreRecyclerAdapter EventsAdapter;

    @Override
    public void onStart() {
        super.onStart();
        EventsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventsAdapter.stopListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_events);
     Intent i=getIntent();
     com=i.getStringExtra("path");


        // Inflate the layout for this fragment



        StorageReference sss= FirebaseStorage.getInstance().getReference("profileimages/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"profileimage");
        Log.d("URLLLLL", sss.getDownloadUrl().toString());

        HomeRecyclerView=findViewById(R.id.Home_Recycle_View_);
       // Query query2 = FirebaseFirestore.getInstance().collection("Events").wh
        Query query = FirebaseFirestore.getInstance().collection("Events").whereEqualTo("docid","").whereEqualTo("commiteeName",com).orderBy("postDateTime", Query.Direction.DESCENDING);
////////////////////////////////////////////////////
        //Search Query//




















        ///////////////////////////////////////////////////////////////////////////////////////////////


        FirestoreRecyclerOptions<Eventcard_Modal_Class> options=
                new FirestoreRecyclerOptions.Builder<Eventcard_Modal_Class>().setQuery(query,Eventcard_Modal_Class.class)
                        .build();

        EventsAdapter= new FirestoreRecyclerAdapter<Eventcard_Modal_Class, singleEvents.EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull singleEvents.EventViewHolder holder, int position, @NonNull Eventcard_Modal_Class model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                final String DocId=snapshot.getId();
                DocumentReference joinedShards=FirebaseFirestore.getInstance().collection("ParticipantCounterShards").document(DocId);
                SolutionCounters sc=new SolutionCounters();
                sc.getCount(joinedShards).addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        holder.EventCount.setText(integer+"");
                    }
                });

                Log.d("DDDDDDDDDDDDDDDDDDDDDDDDDD",model.getDocid().toString());
                holder.EventTitle.setText(model.getTitle());
                holder.EventLocation.setText(model.getSubTitle());
                holder.EventTime.setText(model.getEventDateTime().substring(10,16));
                holder.EventMonth.setText(monthText(model.getEventDateTime().substring(5,7)));
                holder.EventDay.setText(model.getEventDateTime().substring(8,10));


                holder.Card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                        final String DocId=snapshot.getId();
                        Intent i =new Intent(getApplicationContext(),EventsMainPage.class);
                        i.putExtra("DOCID",DocId);
                        Log.d("IDDDDDDDD",DocId);
                        startActivity(i);



                    }
                });
                Resources res = getResources();
                Drawable placeholder = ResourcesCompat.getDrawable(res, R.drawable.bg_cover2, null);
                String filename=getSnapshots().getSnapshot(position).getId().toString()+"cover_image";
                StorageReference srefcover=FirebaseStorage.getInstance().getReference("coverimages/"+filename);
                srefcover.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(content_main.context)
                                .load(uri)
                                .centerCrop()
                                .placeholder(placeholder)
                                .into(holder.CoverImageH);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                holder.LikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.LikeButton.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_24,content_main.context.getTheme()));
                    }
                });



            }



            @NonNull
            @Override
            public singleEvents.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
                return new singleEvents.EventViewHolder(v);

            }
        };
        HomeRecyclerView.setHasFixedSize(true);
        HomeRecyclerView.setLayoutManager(new HomePageEvents.LinearLayoutManagerWrapper(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        HomeRecyclerView.setAdapter(EventsAdapter);

    }


    private class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView EventTitle,EventLocation,EventDay,EventMonth,EventTime,EventCount;
        private LinearLayout Card;

        private ImageView CoverImageH,LikeButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            EventTitle=itemView.findViewById(R.id.eventTitle);
            EventLocation=itemView.findViewById(R.id.location);
            EventDay=itemView.findViewById(R.id.day);
            EventMonth=itemView.findViewById(R.id.month);
            EventTime=itemView.findViewById(R.id.time);
            EventCount=itemView.findViewById(R.id.participation_count);
            Card=itemView.findViewById(R.id.eventCardLayout);
            CoverImageH=itemView.findViewById(R.id.card_image);
            LikeButton=itemView.findViewById(R.id.upvote);


        }
    }

    public static String monthText(String n)
    {
        if(n.equals("01"))
        {
            return "JAN";
        }
        else if(n.equals("02"))
        {
            return "FEB";
        }
        else if(n.equals("03"))
        {
            return "MAR";
        }
        else if(n.equals("04"))
        {
            return "APR";
        }
        else if(n.equals("05"))
        {
            return "MAY";
        }
        else if(n.equals("06"))
        {
            return "JUN";
        }
        else if(n.equals("07"))
        {
            return "JUL";
        }
        else if(n.equals("08"))
        {
            return "AUG";
        }
        else if(n.equals("09"))
        {
            return "SEP";
        }
        else if(n.equals("10"))
        {
            return "OCT";
        }
        else if(n.equals("11"))
        {
            return "NOV";
        }
        else if(n.equals("12"))
        {
            return "DEC";
        }
        else return "";
    }

    public static class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }



}

