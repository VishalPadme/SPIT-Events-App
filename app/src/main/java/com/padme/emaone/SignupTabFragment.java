package com.padme.emaone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupTabFragment extends Fragment implements TextWatcher {
ViewGroup root;
EditText Pswd,Cpswd;
EditText Emaili;
Button SignupButton;
FirebaseAuth Fauth;
LottieAnimationView Ld1;
Var ld1;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
       root = (ViewGroup) inflater.inflate(R.layout.signup_frag,container,false);

      Emaili = (EditText)root.findViewById(R.id.EmailSignUp);
      Emaili.getText().toString();
       Cpswd = (EditText)root.findViewById(R.id.c_password);
    Pswd = (EditText)root.findViewById(R.id.password_sup);
    SignupButton =(Button)root.findViewById(R.id.signup_but);
      Pswd.addTextChangedListener(this);
Fauth=FirebaseAuth.getInstance();
Ld1=root.findViewById(R.id.ld1);

      SignupButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              signup();
          }
      });
       return root;
   }


    @Override
    public void beforeTextChanged(
            CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        TextView strengthView = (TextView) root.findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(getActivity()));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(getActivity()).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(getActivity()).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(getActivity()).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    /////////////////////////////////////////////////////||||||||||||||||||||||\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


    //Client Side Validation //
    public void signup()
    {
        String Email =Emaili.getText().toString().trim();

        String Password = Pswd.getText().toString().trim();
        String Cpasswd = Cpswd.getText().toString().trim();

        if(!isValidEmail(Email))
        {
            Emaili.setError("Please Enter Valid Email");
        }



        else if(PasswordStrength.calculateStrength(Password).getText(getActivity()).toString().equals("Weak"))
        {
            Pswd.setError("Password is too Weak");
        }
        else if (!Password.equals(Cpasswd))
        {
            Cpswd.setError("Passwords Does Not Match");
        }
        else
        {
            SignupButton.setVisibility(View.INVISIBLE);
            Ld1.setVisibility(View.VISIBLE);

            Fauth.createUserWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Objects.requireNonNull(Fauth.getCurrentUser()).sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Account Created.Please Verify Email Id to Activate Account.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),reg_0.class));
                            getActivity().overridePendingTransition(0,0);
                            getActivity().finish();
                            Ld1.setVisibility(View.INVISIBLE);
                            SignupButton.setVisibility(View.VISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error",e.toString());
                            Ld1.setVisibility(View.INVISIBLE);
                            SignupButton.setVisibility(View.VISIBLE);
                        }
                    });



                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error",e.toString());

                            getActivity().finish();
                            getActivity().overridePendingTransition(0, 0);
                            startActivity(new Intent(getActivity(),reg_0.class));
                            getActivity().overridePendingTransition(0, 0);
                            Toast.makeText(getActivity(), "Errror: "+e.toString(), Toast.LENGTH_LONG).show();
                            Ld1.setVisibility(View.INVISIBLE);
                            SignupButton.setVisibility(View.VISIBLE);

                        }
                    });
        }


        Log.v("PASSWORD",PasswordStrength.calculateStrength(Password).getText(getActivity()).toString());


    }





    ////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//////////////\\\\\\\\\\\\\////////\
    //Firebase Auth Starts Here//


}
