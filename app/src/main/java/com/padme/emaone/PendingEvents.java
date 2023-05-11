package com.padme.emaone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class PendingEvents extends AppCompatActivity {

    RecyclerView AdminREcyclerView;
    FirestoreRecyclerAdapter AdminEventsAdapter;
    public void onStart() {
        super.onStart();
        AdminEventsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        AdminEventsAdapter.stopListening();
    }

    ProgressDialog progressDialogImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_events);
        AdminREcyclerView=findViewById(R.id.PendingEventsRecyclerView);
        Query query = FirebaseFirestore.getInstance().collection("Events").orderBy("postDateTime", Query.Direction.DESCENDING).whereEqualTo("docid", "Pending");
        FirestoreRecyclerOptions<Eventcard_Modal_Class> options=
                new FirestoreRecyclerOptions.Builder<Eventcard_Modal_Class>().setQuery(query,Eventcard_Modal_Class.class)
                        .build();

        AdminEventsAdapter= new FirestoreRecyclerAdapter<Eventcard_Modal_Class,PendingEvents.EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PendingEvents.EventViewHolder holder, int position, @NonNull Eventcard_Modal_Class model) {
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Events").document(model.getCUID());
                Log.d("DDDDDDDDDDDDDDDDDDDDDDDDDD",model.getDocid().toString());
                holder.EventTitle.setText(model.getTitle());
                holder.Card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                        final String DocId=snapshot.getId();
                        Intent i =new Intent(getApplicationContext(),AprooveEvent.class);
                        i.putExtra("DOCID",DocId);
                        Log.d("IDDDDDDDD",DocId);
                        startActivity(i);

                    }
                });
                holder.UploadDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                        final String DocId=snapshot.getId();
                        Intent i =new Intent(getApplicationContext(),ReportUpload.class);
                        i.putExtra("DOCID",DocId);
                        Log.d("IDDDDDDDD",DocId);
                        startActivity(i);
                    }
                });
                Timestamp Created_TimeStamp=model.getPostDateTime();
                Timestamp live_Timestamp=new Timestamp(new Date());
                long TimeSpace=live_Timestamp.getSeconds()-Created_TimeStamp.getSeconds();
                if(TimeSpace<=0)//justnow
                {
                    holder.timego.setText("Just Now");
                }
                else if(TimeSpace<60)//in secs
                {holder.timego.setText(TimeSpace+" sec ago");}
                else if(TimeSpace<3600)//in minutes
                {
                    long minutes=(TimeSpace/60);
                    holder.timego.setText(minutes+" min ago");
                }
                else if(TimeSpace==3600) //in hour
                {
                    long hours=(TimeSpace/3600);
                    holder.timego.setText(hours+" hour ago");

                }
                else if(TimeSpace<86400) //in hours
                {
                    long hours=(TimeSpace/3600);
                    holder.timego.setText(hours+" hrs ago");

                }
                else if(TimeSpace==86400) //in day
                {
                    long days=(TimeSpace/86400);
                    holder.timego.setText(days+" day ago");

                }
                else if(TimeSpace<604800) //in days
                {
                    long days=(TimeSpace/86400);
                    holder.timego.setText(days+" days ago");

                }
                else if(TimeSpace==604800)//in week
                {
                    long weeks=(TimeSpace/604800);
                    holder.timego.setText(weeks+" week ago");
                }
                else if(TimeSpace<31449600)//in weeks
                {
                    long weeks=(TimeSpace/604800);
                    holder.timego.setText(weeks+" weeks ago");
                }
                else if(TimeSpace==(31449600))//in year
                {
                    long years=(TimeSpace/31449600);
                    holder.timego.setText(years+" year ago");
                }
                else if(TimeSpace<(31449600*52))//in years
                {
                    long years=(TimeSpace/31449600);
                    holder.timego.setText(years+" yrs ago");
                }
                else
                {
                    holder.timego.setText("");
                }

                ///////////////////////////////////////////////////////////////////////// delete update report////////////

                holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PendingEvents.this);

                        builder.setMessage("Clicking on \"Delete\" will delete the post permanently")
                                .setTitle("Delete Post?");

// Add the buttons
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                                progressDialog.setTitle("Deleting");
                                getSnapshots().getSnapshot(position).getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
                                        DocumentReference pcts=FirebaseFirestore.getInstance().collection("participatedUserMeta").document(snapshot.getId());
                                        String filename=snapshot.getId().toString()+"cover_image";
                                        StorageReference cref= FirebaseStorage.getInstance().getReference("coverimages/"+filename);
                                        cref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pcts.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        int temp=holder.getBindingAdapterPosition();
                                                        getSnapshots().getSnapshot(position).getReference().delete();

                                                        AdminEventsAdapter.notifyItemRemoved(holder.getBindingAdapterPosition());
                                                        progressDialog.dismiss();
                                                        Toast.makeText(PendingEvents.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                        recreate();

                                                    }

                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                int temp=holder.getBindingAdapterPosition();
                                                getSnapshots().getSnapshot(position).getReference().delete();
                                                AdminEventsAdapter.notifyItemRemoved(holder.getBindingAdapterPosition());

                                                progressDialog.dismiss();
                                                Toast.makeText(PendingEvents.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                recreate();

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
// Set other dialog properties


// Create the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });


                ////////////////////////////////////////////// Update//////////////////////

                holder.UpdateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PendingEvents.this);

                        builder.setMessage("Clicking on \"Delete\" will delete the post permanently")
                                .setTitle("Delete Post?");

// Add the buttons
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                Toast.makeText(PendingEvents.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
// Set other dialog properties


// Create the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });







            }



            @NonNull
            @Override
            public PendingEvents.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listeventcard,parent,false);
                return new PendingEvents.EventViewHolder(v);

            }
        };
        AdminREcyclerView.setHasFixedSize(true);
        AdminREcyclerView.setLayoutManager(new HomePageEvents.LinearLayoutManagerWrapper(PendingEvents.this, LinearLayoutManager.VERTICAL, false));
        AdminREcyclerView.setAdapter(AdminEventsAdapter);
    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

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

    private class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView EventTitle,timego;
        private LinearLayout Card;

        private ImageView CoverImageH,DeleteButton,UpdateButton,UploadDataButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            EventTitle=itemView.findViewById(R.id.eventTitle);
            timego=itemView.findViewById(R.id.eventTimeago);
            DeleteButton=itemView.findViewById(R.id.deleteButton);
            UpdateButton=itemView.findViewById(R.id.editButton);
            UploadDataButton=itemView.findViewById(R.id.reportUploadButton);
            Card=itemView.findViewById(R.id.adminlistcard);


        }
    }


    public void backbutton(View v)
    {
        super.onBackPressed();
    }
}
