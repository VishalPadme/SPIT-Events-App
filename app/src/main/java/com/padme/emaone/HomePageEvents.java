package com.padme.emaone;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.ProgressDialog;
import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageEvents extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageEvents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageEvents newInstance(String param1, String param2) {
        HomePageEvents fragment = new HomePageEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView HomeRecyclerView;

    EditText searchbox;
   FirestoreRecyclerAdapter EventsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView=inflater.inflate(R.layout.fragment_home_page_events, container, false);


       StorageReference sss= FirebaseStorage.getInstance().getReference("profileimages/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"profileimage");
      Log.d("URLLLLL", sss.getDownloadUrl().toString());

       HomeRecyclerView=rootView.findViewById(R.id.Home_Recycle_View_);
       searchbox=rootView.findViewById(R.id.SearchEditText);
       Query query = FirebaseFirestore.getInstance().collection("Events").whereEqualTo("docid","").orderBy("postDateTime", Query.Direction.DESCENDING);
////////////////////////////////////////////////////
       //Search Query//
       searchbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                  searchEvents();
                    return true;
                }
                return false;
            }
        });



















       ///////////////////////////////////////////////////////////////////////////////////////////////


        FirestoreRecyclerOptions<Eventcard_Modal_Class> options=
                new FirestoreRecyclerOptions.Builder<Eventcard_Modal_Class>().setQuery(query,Eventcard_Modal_Class.class)
                        .build();

        EventsAdapter= new FirestoreRecyclerAdapter<Eventcard_Modal_Class,EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Eventcard_Modal_Class model) {
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
                      Intent i =new Intent(getActivity().getApplicationContext(),EventsMainPage.class);
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
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
                return new EventViewHolder(v);

            }
        };
        HomeRecyclerView.setHasFixedSize(true);
        HomeRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.VERTICAL, false));
        HomeRecyclerView.setAdapter(EventsAdapter);

        return rootView;
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

    public void searchEvents()
    {
        searchbox.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchbox.getWindowToken(), 0);
        String term=searchbox.getText().toString();
        Query Updatedquery = FirebaseFirestore.getInstance().collection("Events").orderBy("title");
        FirestoreRecyclerOptions<Eventcard_Modal_Class> new_Options=
                new FirestoreRecyclerOptions.Builder<Eventcard_Modal_Class>().setQuery(Updatedquery,Eventcard_Modal_Class.class)
                        .build();

        EventsAdapter.updateOptions(new_Options);
    }

}

