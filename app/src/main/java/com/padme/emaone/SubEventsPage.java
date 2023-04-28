package com.padme.emaone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubEventsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubEventsPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubEventsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubEventsPage.
     */
    // TODO: Rename and change types and number of parameters
    public static SubEventsPage newInstance(String param1, String param2) {
        SubEventsPage fragment = new SubEventsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void onStart() {
        super.onStart();

        SubEventsAdapter.startListening();
       /* if(SubEventsAdapter.getItemCount()==0||SubEventsAdapter.getItemCount()==-1)
        {
            SubREcyclerView.setVisibility(View.INVISIBLE);
            emtyView.setVisibility(View.VISIBLE);
        }
        else
        {
            SubREcyclerView.setVisibility(View.VISIBLE);
            emtyView.setVisibility(View.INVISIBLE);
        }*/

        Log.d("Count",SubEventsAdapter.getItemCount()+"");

    }

    @Override
    public void onStop() {
        super.onStop();
        SubEventsAdapter.stopListening();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    CollectionReference userEvents;
    LinearLayout emtyView;
    RecyclerView SubREcyclerView;
    FirestoreRecyclerAdapter SubEventsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_sub_events_page, container, false);
        emtyView=rootview.findViewById(R.id.emptyview);
        userEvents=FirebaseFirestore.getInstance().collection("user_events_list").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).collection("joinedevents");
        Query query=userEvents.orderBy("datetime", Query.Direction.DESCENDING);
        SubREcyclerView=rootview.findViewById(R.id.subRecyclerView);


       // ArrayList<DocumentSnapshot> documentSnapshots = new ArrayList<>();
      //  while (userEvents)
        FirestoreRecyclerOptions<Joined_Event_Modal> options=
                new FirestoreRecyclerOptions.Builder<Joined_Event_Modal>().setQuery(query,Joined_Event_Modal.class)
                        .build();

        SubEventsAdapter=new FirestoreRecyclerAdapter<Joined_Event_Modal,SubEventsPage.EventViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull SubEventsPage.EventViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Joined_Event_Modal model) {

                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                DocumentReference eventref=FirebaseFirestore.getInstance().collection("Events").document(snapshot.getId());

                eventref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("title")==null)
                        {
                            int temp=holder.getBindingAdapterPosition();
                            if(temp!=-1){
                                getSnapshots().getSnapshot(temp).getReference().delete();
                                notifyItemRemoved(temp);
                                return;
                            }

                        }
                        else {
                            String dateTime= documentSnapshot.getString("eventDateTime");
                            holder.EventTitle.setText(documentSnapshot.getString("title"));
                            holder.EventLocation.setText(documentSnapshot.getString("subTitle"));
                            holder.EventTime.setText(dateTime.substring(10,16));
                            holder.EventMonth.setText(HomePageEvents.monthText(dateTime.substring(5,7)));
                            holder.EventDay.setText(dateTime.substring(8,10));

                            holder.Card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                                    final String DocId=snapshot.getId();
                                    Intent i =new Intent(getActivity().getApplicationContext(),EventsMainPage.class);
                                    i.putExtra("DOCID",DocId);
                                    Log.d("IDDDDDDDD",DocId);
                                    startActivity(i);



                                }
                            });

                              if(SubEventsAdapter.getItemCount()==0||SubEventsAdapter.getItemCount()==-1)
        {
            SubREcyclerView.setVisibility(View.INVISIBLE);
            emtyView.setVisibility(View.VISIBLE);
        }
        else
        {
            SubREcyclerView.setVisibility(View.VISIBLE);
            emtyView.setVisibility(View.INVISIBLE);
        }

                            Resources res = getResources();
                            Drawable placeholder = ResourcesCompat.getDrawable(res, R.drawable.bg_cover2, null);
                            String filename=getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getId().toString()+"cover_image";
                            StorageReference srefcover= FirebaseStorage.getInstance().getReference("coverimages/"+filename);
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
                        }




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getSnapshots().getSnapshot(position).getReference().delete();
                        notifyDataSetChanged();
                    }
                });


            }



            @NonNull
            @Override
            public SubEventsPage.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
                return new SubEventsPage.EventViewHolder(v);
            }
        };
        SubREcyclerView.setHasFixedSize(true);
        SubREcyclerView.setLayoutManager(new HomePageEvents.LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));
        SubREcyclerView.setAdapter(SubEventsAdapter);

return  rootview;
    }
    private class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView EventTitle,EventLocation,EventDay,EventMonth,EventTime,EventCount;
        private LinearLayout Card;

        private ImageView CoverImageH;

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



        }
    }
}