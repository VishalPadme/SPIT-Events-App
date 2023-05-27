package com.padme.emaone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ReportUpload extends AppCompatActivity {

    FirebaseStorage FStore;
    ActivityResultLauncher<String> pickMedia;
    DocumentReference docref;
    Uri reportFile;
    ProgressDialog progressDialog;
    String docid;
    EditText imageLinks;
    TextView fName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_upload);
        Intent i =getIntent();
        docid=i.getStringExtra("DOCID");
        FStore=FirebaseStorage.getInstance();
        docref= FirebaseFirestore.getInstance().collection("Events").document(docid);
        imageLinks=findViewById(R.id.imagelinks);

        fName=findViewById(R.id.filename);
        pickMedia=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result!=null)
                {fName.setText(result.getLastPathSegment());
                    reportFile=result;
                }

            }
        });

    }



    public void backbutton(View v)
    {
        super.onBackPressed();
    }

    public void uploadData(View v)
    {
        ProgressDialog progressDialog = new ProgressDialog(ReportUpload.this);
        progressDialog.setTitle("Uploading Event Data....");
        if(imageLinks.getText().toString().length()<6)
            imageLinks.setError("Please Provide Valid Link");

        else if(reportFile!=null)
        {
            progressDialog.show();
            progressDialog.setTitle("Uploading File....");
            docref.update("upvoteCount",imageLinks.getText().toString());
            String filename=docid.toString()+"reportImage";
            StorageReference storageReference= FirebaseStorage.getInstance().getReference("eventReports/"+filename);
            storageReference.putFile(reportFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(ReportUpload.this,"Report Uploaded",Toast.LENGTH_SHORT).show();
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(ReportUpload.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(ReportUpload.this,"Please Select Report File",Toast.LENGTH_SHORT).show();


        }
    }

    public void pdfpicker(View v)
    {
        pickMedia.launch("*/*");
    }

}
