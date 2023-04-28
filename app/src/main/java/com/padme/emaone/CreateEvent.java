package com.padme.emaone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;
import com.google.type.DateTimeOrBuilder;

import java.util.Calendar;
import java.util.Date;

public class CreateEvent extends AppCompatActivity {

    String[] items =  {"Material","Design","Components","Android","5.0 Lollipop"};


    Calendar Today;
    Timestamp EventDateLocal;
    EditText Title,Location1,Description;
    Eventcard_Modal_Class EventMOJO;
    TextView PickImage;

    ActivityResultLauncher<String> pickMedia;

    ProgressDialog progressDialogImage;
    private SimpleDateFormat dateFormat,timeformat;
    AutoCompleteTextView autoCompleteTxtCommits,autoCompleteTxtDepts,autoCompleteTxtCategories;
    ArrayAdapter<String> adapterItems,adapterCommits,adapterDepts,adapterCategories;
    Uri CoverImageUri;
    FirebaseAuth FAuth;
    FirebaseFirestore FStore;
    FirebaseStorage FStoreage;
    ImageView CoverImage;




Button Date,Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);

        FStore=FirebaseFirestore.getInstance();
        FAuth=FirebaseAuth.getInstance();
        FStoreage=FirebaseStorage.getInstance();
        Title=findViewById(R.id.Title);
        Location1=findViewById(R.id.Location);
        Description=findViewById(R.id.Contentt);
        EventMOJO=new Eventcard_Modal_Class();
        CoverImage=findViewById(R.id.coverImage);
        Date=findViewById(R.id.editTextDate3);
        Time=findViewById(R.id.editTextTime);
        FStoreage=FirebaseStorage.getInstance();
        autoCompleteTxtCommits=findViewById(R.id.Cmti);
        autoCompleteTxtDepts=findViewById(R.id.Dept);
        autoCompleteTxtCategories=findViewById(R.id.Category);
        PickImage=findViewById(R.id.pickcoverimage);


        String[] Depts={"COMPS","IT","ETRX","EXTC","MCA","Other"};
        String[] Comits= {"Student Council","Sports Committee","Mudra","Oculus ","CSI SPIT","Rotaract Club","IIC & E-Cell","NISP Council","SPARK","ENACTUS","IEEE SPIT","IEEE WIE","IdeaLab","ELA","FACE","ACSES","EESA","ACE"};
        String[] Categories={"Art","Technical","Sports","Other"};
        //adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        //autoCompleteTxtCommits.setAdapter(adapterItems);

        adapterCommits = new ArrayAdapter<String>(this,R.layout.list_item,Comits);
        autoCompleteTxtCommits.setAdapter(adapterCommits);

        adapterDepts = new ArrayAdapter<String>(this,R.layout.list_item,Depts);
        autoCompleteTxtDepts.setAdapter(adapterDepts);

        adapterCategories = new ArrayAdapter<String>(this,R.layout.list_item,Categories);
        autoCompleteTxtCategories.setAdapter(adapterCategories);

        Today=Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        timeformat=new SimpleDateFormat("hh:mm a");
      Date.setText(dateFormat.format(Today.getTime()));
      Time.setText(timeformat.format(Today.getTime()));



        pickMedia=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result!=null)
                {CoverImage.setImageURI(result);
                    CoverImageUri=result;
                }

            }
        });



        autoCompleteTxtCommits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }
        });

        autoCompleteTxtDepts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }
        });

        autoCompleteTxtCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }
        });


        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        CoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch("image/*");
            }
        });

        PickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch("image/*");
            }
        });


    }


    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Showing the picked value in the textView
                if(month<10&&day>10)
                {
                    Date.setText(String.valueOf(year)+ "/"+"0"+String.valueOf(month+1)+ "/"+String.valueOf(day));
                }
                else if(month<10&&day<10)
                {
                    Date.setText(String.valueOf(year)+ "/"+"0"+String.valueOf(month+1)+ "/"+"0"+String.valueOf(day));
                }
               else if(month>10&&day<10)
                {
                    Date.setText(String.valueOf(year)+ "/"+String.valueOf(month+1)+ "/"+"0"+String.valueOf(day));
                }
               else {
                    Date.setText(String.valueOf(year)+ "/"+String.valueOf(month+1)+ "/"+String.valueOf(day));
                }



            }
        }, Today.get(Calendar.YEAR), Today.get(Calendar.MONTH), Today.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    private void openTimePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {


                //Showing the picked value in the textView
                if(hour<10&&minute<10)
                Time.setText("0"+String.valueOf(hour)+ ":"+"0"+String.valueOf(minute));
                else if(hour<10&&minute>10)
                    Time.setText("0"+String.valueOf(hour)+ ":"+String.valueOf(minute));
                else if(hour>10&&minute<10)
                    Time.setText(String.valueOf(hour)+ ":"+"0"+String.valueOf(minute));
                else
                    Time.setText(String.valueOf(hour)+ ":"+String.valueOf(minute));



            }
        }, Today.get(Calendar.HOUR), Today.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    public void UploadEvent(View v)
    {
        ProgressDialog progressDialog = new ProgressDialog(CreateEvent.this);
        progressDialog.setTitle("Uploading Event Data....");


        if(Title.getText().toString().length()<4)
        {
            Title.setError("Please Enter Valid Title");

        }

        else if(Location1.getText().toString().length()<4)
        {
            Title.setError("Please Enter Valid Title");
        }
        else if(Description.getText().toString().length()<4)
        {
            Description.setError("Please Enter Valid Title");
        }

        else {
            progressDialog.show();
            EventMOJO.setTitle(Title.getText().toString());
            EventMOJO.setSubTitle(Location1.getText().toString());
            EventMOJO.setTextContent(Description.getText().toString());
            EventMOJO.setEventDateTime(Date.getText().toString()+" "+Time.getText().toString());
            EventMOJO.setDocid("");
            EventMOJO.setCUID(FAuth.getCurrentUser().getUid().toString());
            if(CoverImageUri!=null)
            {
                EventMOJO.setDisplayImageUri(CoverImageUri.toString());
            }
            else
            {
                EventMOJO.setDisplayImageUri("");
            }
            EventMOJO.setUpvoteCount("0");
            EventMOJO.setParticipantsCount("0");
            EventMOJO.setCategory(autoCompleteTxtCategories.getText().toString());
            EventMOJO.setCommiteeName(autoCompleteTxtCommits.getText().toString());
            EventMOJO.setDepartmentName(autoCompleteTxtDepts.getText().toString());
            EventMOJO.setPostDateTime(new Timestamp(new Date()));

            final CollectionReference Events=FStore.collection("Events");
            Events.add(EventMOJO).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {


                    final DocumentReference ParticipantCounterShards=FStore.collection("ParticipantCounterShards").document(documentReference.getId());
                    SolutionCounters sc=new SolutionCounters();
                    sc.createCounter(ParticipantCounterShards,15);

                    final DocumentReference UpvoteCounterShards=FStore.collection("UpvoteCounterShards").document(documentReference.getId());
                    SolutionCounters sc2=new SolutionCounters();
                    sc.createCounter(ParticipantCounterShards,15);

                    ////



                    /////


                    if(CoverImageUri!=null)
                    {

                        progressDialog.setTitle("Uploading Image....");
                        String filename=documentReference.getId().toString()+"cover_image";
                        StorageReference storageReference= FirebaseStorage.getInstance().getReference("coverimages/"+filename);
                        storageReference.putFile(CoverImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Toast.makeText(CreateEvent.this,"Added",Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Toast.makeText(CreateEvent.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    else {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(CreateEvent.this,"Added",Toast.LENGTH_SHORT).show();
                        finish();

                    }










                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateEvent.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    //upd.dismiss();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    finish();
                }
            });





        }
    }
    public void backbutton(View v)
    {
        super.onBackPressed();
    }
}