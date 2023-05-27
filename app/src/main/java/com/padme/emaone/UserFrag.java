package com.padme.emaone;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFrag newInstance(String param1, String param2) {
        UserFrag fragment = new UserFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FirebaseAuth FAuth;
    FirebaseFirestore FStore;
    FirebaseStorage Fstorage;
    FirebaseUser user;
    DocumentReference userdocref;
    CircleImageView ProfileImage;
    Button SignOut,CreateEvent,AdinEventsButtpn,PendingEvents;
    TextView UserName,UCID;
    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview = inflater.inflate(R.layout.fragment_user, container, false);

        UserName=rootview.findViewById(R.id.imagelinks);
        UCID=rootview.findViewById(R.id.ucid);
        SignOut=rootview.findViewById(R.id.SignOutButton);
        CreateEvent=rootview.findViewById(R.id.createEventButton);
       FStore=FirebaseFirestore.getInstance();
       FAuth=FirebaseAuth.getInstance();
       Fstorage=FirebaseStorage.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        ProfileImage=rootview.findViewById(R.id.profileImage);
        AdinEventsButtpn=rootview.findViewById(R.id.button5);
        PendingEvents=rootview.findViewById(R.id.pending_events);




SignOut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        signOut();

    }
});

CreateEvent.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        createContent();
    }
});

AdinEventsButtpn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        viewAdminEvents();
    }
});

PendingEvents.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        pendingEvents();
    }
});


        userdocref=FStore.collection("users").document(FAuth.getCurrentUser().getUid().toString());
        userdocref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(user.getPhotoUrl()!=null)
                {


                    Resources res = getResources();
                    Drawable placeholder = ResourcesCompat.getDrawable(res, R.drawable.profileimage_placeholder, null);
                 Uri uri= user.getPhotoUrl();
                    Log.d("PHOTO URI ////////////",user.getPhotoUrl().toString());


                    Glide.with(content_main.context)
                            .load(uri)
                            .centerCrop()
                            .placeholder(placeholder)
                            .into(ProfileImage);


                }

                UserName.setText(documentSnapshot.getString("name"));
                UCID.setText(documentSnapshot.getString("ucid"));

                if(documentSnapshot.getString("userLevel").equals("ADMIN"))
                {
                    PendingEvents.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), documentSnapshot.getString("userLevel"), Toast.LENGTH_SHORT).show();
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

       return rootview;




    }

    public void pendingEvents()
    {
        startActivity(new Intent(getActivity().getApplicationContext(),PendingEvents.class));
    }

    public void createContent()
    {
        startActivity(new Intent(getActivity().getApplicationContext(),CreateEvent.class));

    }

    public void viewAdminEvents()
    {
        startActivity(new Intent(getActivity().getApplicationContext(),AdminEventsList.class));
    }

    public void signOut()
    {

        Toast.makeText(getActivity(),UserName.getText().toString()+" Signing Out ",Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity().getApplicationContext(),reg_0.class));
        getActivity().finish();
    }


}