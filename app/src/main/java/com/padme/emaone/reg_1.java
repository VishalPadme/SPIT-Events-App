package com.padme.emaone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class reg_1 extends AppCompatActivity {
    FirebaseAuth FAuth;
    FirebaseFirestore FStore;
    FirebaseStorage Fstorage;
    String Name,Email,UCID;
    TextView ucidTextView,NameTextView;
    ActivityResultLauncher<String> pickMedia;
    CircleImageView ProfileImage,EditImage;
    ProgressDialog  progressDialog;

    User_Model_Class UserDocMOJO;
    LottieAnimationView LDB;
    int FlagProfileImage;
    Button FinishButton;

    public StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg1);
        FAuth=FirebaseAuth.getInstance();
        FStore=FirebaseFirestore.getInstance();
        Fstorage=FirebaseStorage.getInstance();
        ucidTextView=findViewById(R.id.UCID);
        NameTextView=findViewById(R.id.name);
        ProfileImage=findViewById(R.id.profile_image);
        LDB=findViewById(R.id.ldb);
        FinishButton=findViewById(R.id.finish_login);
        EditImage=findViewById(R.id.edit_image);
        Email=FAuth.getCurrentUser().getEmail().toString();

        FlagProfileImage=0;
        UserDocMOJO=new User_Model_Class();
        pickMedia=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result!=null)
                {ProfileImage.setImageURI(result);

                    String filename=FAuth.getCurrentUser().getUid().toString()+"profileimage";
                    progressDialog = new ProgressDialog(reg_1.this);
                    progressDialog.setTitle("Uploading File....");
                    progressDialog.show();
                    storageReference= Fstorage.getReference("profileimages/"+filename);
                    storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseUser User= FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();
                                    User.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(reg_1.this,"Successfully Uploaded"+FAuth.getCurrentUser().getPhotoUrl().toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(reg_1.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                            Log.e("Er",e.getMessage().toString());
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(reg_1.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                    Log.e("Er",e.getMessage().toString());
                                }
                            });



                            FlagProfileImage=1;
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(reg_1.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
                        }
                    });





                }

            }
        });

        EditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch("image/*");
            }
        });


    }
    public void createUserDoc(View v)
    {


        if(NameTextView.getText().toString().length()<3||NameTextView.getText().toString()=="")
        {
            NameTextView.setError("Please Enter Valid Name");
        }
        else if(ucidTextView.getText().toString().length()!=10)
        {
            ucidTextView.setError("Please Enter Valid UCID");
        }
        else {
            FinishButton.setVisibility(View.INVISIBLE);
            LDB.setVisibility(View.VISIBLE);
            uploadPost(UserDocMOJO);
        }
    }

    public void uploadPost(User_Model_Class user)
    {
         // (String UID, String docID, String UCID, String name, String email, String userLevel, String profilePhotoUri)
        user.setUID(FAuth.getCurrentUser().getUid().toString());
        user.setDocID(" ");
        user.setUCID(ucidTextView.getText().toString());
        user.setName(NameTextView.getText().toString());
        String Temp=NameTextView.getText().toString();
        user.setEmail(FAuth.getCurrentUser().getEmail().toString());
        user.setUserLevel("ADMIN");
        if(FlagProfileImage==1) user.setProfilePhotoUri(storageReference.getDownloadUrl().toString());
        final CollectionReference USER=FStore.collection("users");
/*
        USER.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(reg_1.this,"Welcome ,"+Name,Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(reg_1.this,"Sorry Some Technical Error Occured,Pls try again"+Name,Toast.LENGTH_SHORT);

            }
        }); */

        USER.document(FAuth.getCurrentUser().getUid().toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                startActivity(new Intent(getApplicationContext(), content_main.class));
                finish();

                Toast.makeText(reg_1.this,"Welcome ,"+Temp,Toast.LENGTH_SHORT).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(reg_1.this,"Sorry Some Technical Error Occured,Pls try again"+Name,Toast.LENGTH_SHORT).show();
                FinishButton.setVisibility(View.VISIBLE);
                LDB.setVisibility(View.INVISIBLE);
            }
        });
    }


}