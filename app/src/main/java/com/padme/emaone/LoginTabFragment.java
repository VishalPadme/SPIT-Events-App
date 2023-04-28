package com.padme.emaone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginTabFragment extends Fragment {
    TextView ForgetPassword;
    EditText EmailText,Password;
    Button LoginButton;
    FirebaseAuth Fauth;
FirebaseFirestore FStore;
    TextView Fpswd;
    LottieAnimationView Ld2;

    float v=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_frag,container,false);

        ForgetPassword = root.findViewById(R.id.forgetpasswordbutton);
        EmailText=root.findViewById(R.id.Emailinput);
        Password=root.findViewById(R.id.password_sup);
        LoginButton=root.findViewById(R.id.login_but);
        Ld2=root.findViewById(R.id.ld2);
        Fauth=FirebaseAuth.getInstance();
        Fpswd=root.findViewById(R.id.forgetpasswordbutton);
        FStore=FirebaseFirestore.getInstance();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Fpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPassword();

            }
        });

       EmailText.setTranslationX(800); Password.setTranslationX(800);
        ForgetPassword.setTranslationX(800);
      LoginButton.setTranslationX(800);
       EmailText.setAlpha(v);
        Password.setAlpha(v);
      ForgetPassword.setAlpha(v);
      LoginButton.setAlpha(v);


        EmailText.animate().translationX(0).alpha(1).setDuration(800).setStartDelay (100).start();
        Password.animate().translationX(0).alpha (1).setDuration(800).setStartDelay(300).start();
        ForgetPassword.animate().translationX(0).alpha (1).setDuration(800).setStartDelay (300).start();
        LoginButton.animate().translationX(0).alpha (1).setDuration(800).setStartDelay (400).start();
        return root;
    }
    public void login()
    {
        String Email = EmailText.getText().toString();
        String Passwo=Password.getText().toString();

        if(Email.length()<6)
        {
            EmailText.setError("Valid Email Required");
        }

        else if(Passwo.length()<6)
        {
            Password.setError("Valid Password Required");
        }

        else {
            Ld2.setVisibility(View.VISIBLE);
            LoginButton.setVisibility(View.INVISIBLE);

            Fauth.signInWithEmailAndPassword(Email,Passwo).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(Objects.requireNonNull(Fauth.getCurrentUser()).isEmailVerified())
                    {
                        Ld2.setVisibility(View.INVISIBLE);
                        LoginButton.setVisibility(View.VISIBLE);
                      //  startActivity(new Intent(getActivity(),content_main.class));
                      //  getActivity().finish();
                       // getActivity().overridePendingTransition(0, 0);
//////////////////////////////////
                        String UID = Objects.requireNonNull(Fauth.getCurrentUser()).getUid();
                        Log.d("UIDDDDDDDDDDDDDDDDDDDDDDDDDDd", UID);
                        DocumentReference docRef = FStore.collection("users").document(UID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        startActivity(new Intent(getActivity(), content_main.class));
                                       getActivity().finish();
                                        getActivity().finish();
                                        getActivity().overridePendingTransition(0, 0);
                                    } else {
                                        // fAuth.signOut();
                                        startActivity(new Intent(getActivity(), reg_1.class));
                                        getActivity().finish();
                                        getActivity().finish();
                                        getActivity().overridePendingTransition(0, 0);
                                    }
                                }

                            }
                        });

                        //////////////////////////////////////////


                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Kindly Verify Email Address", Toast.LENGTH_SHORT).show();
                        Fauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Email Verification Link is Sent", Toast.LENGTH_SHORT).show();
                                Ld2.setVisibility(View.INVISIBLE);
                                LoginButton.setVisibility(View.VISIBLE);
                                startActivity(new Intent(getActivity(),reg_0.class));
                                getActivity().finish();
                                getActivity().overridePendingTransition(0, 0);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Ld2.setVisibility(View.INVISIBLE);
                                LoginButton.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Ld2.setVisibility(View.INVISIBLE);
                    LoginButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();


                }
            });


        }


    }

    public void ForgetPassword()
    {

    }



}
