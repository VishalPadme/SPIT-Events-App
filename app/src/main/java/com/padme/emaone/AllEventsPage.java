package com.padme.emaone;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllEventsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllEventsPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllEventsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllEventsPage.
     */
    // TODO: Rename and change types and number of parameters
    public static AllEventsPage newInstance(String param1, String param2) {
        AllEventsPage fragment = new AllEventsPage();
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
    CardView c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_all_events_page, container, false);

        c1=rootView.findViewById(R.id.card_c1);
        c2=rootView.findViewById(R.id.card_c2);
        c3=rootView.findViewById(R.id.card_c3);
        c4=rootView.findViewById(R.id.card_c4);
        c5=rootView.findViewById(R.id.card_c5);
        c6=rootView.findViewById(R.id.card_c6);
        c7=rootView.findViewById(R.id.card_c7);
        c8=rootView.findViewById(R.id.card_c8);
        c9=rootView.findViewById(R.id.card_c9);
        c10=rootView.findViewById(R.id.card_c10);
        c11=rootView.findViewById(R.id.card_c11);
        c12=rootView.findViewById(R.id.card_c12);
        c13=rootView.findViewById(R.id.card_c13);
        c14=rootView.findViewById(R.id.card_c14);
        c15=rootView.findViewById(R.id.card_c15);
        c16=rootView.findViewById(R.id.card_c16);
        c17=rootView.findViewById(R.id.card_c17);
        c18=rootView.findViewById(R.id.card_c18);
        c19=rootView.findViewById(R.id.card_c19);
        c20=rootView.findViewById(R.id.card_c20);
        String[] Comits= {"SPARK","ENACTUS","IEEE SPIT","IEEE WIE","IdeaLab","ELA","FACE","ACSES","EESA","ACE"};
        c1.setOnClickListener(view -> {
            launchEvents("Student Council");
        });
        c2.setOnClickListener(view -> {
            launchEvents("Sports Committee");
        });
        c3.setOnClickListener(view -> {
            launchEvents("Mudra");
        });
        c4.setOnClickListener(view -> {
            launchEvents("Oculus");
        });
        c5.setOnClickListener(view -> {
            launchEvents("CSI SPIT");
        });
        c6.setOnClickListener(view -> {
            launchEvents("Rotaract Club");
        });
        c7.setOnClickListener(view -> {
            launchEvents("IIC & E-Cell");
        });
        c8.setOnClickListener(view -> {
            launchEvents("NISP Council");
        });
        c9.setOnClickListener(view -> {
            launchEvents("SPARK");
        });
        c10.setOnClickListener(view -> {
            launchEvents("ENACTUS");
        });
        c11.setOnClickListener(view -> {
            launchEvents("IEEE SPIT");
        });
        c12.setOnClickListener(view -> {
            launchEvents("IEEE WIE");
        });
        c13.setOnClickListener(view -> {
            launchEvents("IdeaLab");
        });
        c14.setOnClickListener(view -> {
            launchEvents("ELA");
        });
        c15.setOnClickListener(view -> {
            launchEvents("FACE");
        });
        c16.setOnClickListener(view -> {
            launchEvents("ACSES");
        });
        c17.setOnClickListener(view -> {
            launchEvents("EESA");
        });
        c18.setOnClickListener(view -> {
            launchEvents("ACE");
        });
        c19.setOnClickListener(view -> {
            launchEvents("Student Council");
        });
        c20.setOnClickListener(view -> {
            launchEvents("Student Council");
        });



        return rootView;
    }

    public void launchEvents(String Com)
    {
        Intent i =new Intent(getActivity().getApplicationContext(),singleEvents.class);
        i.putExtra("path",Com);
       // Log.d("IDDDDDDDD",DocId);
        startActivity(i);
    }
}