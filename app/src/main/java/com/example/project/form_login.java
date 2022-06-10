package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link form_login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class form_login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public form_login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment form_login.
     */
    // TODO: Rename and change types and number of parameters
    public static form_login newInstance(String param1, String param2) {
        form_login fragment = new form_login();
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

    public  void onStart(){
        super.onStart();
        //user is loged in or not?
        checksession();

    }
    private  void checksession(){
        SessionManagement sessionManagement = new SessionManagement(getContext());
        int Userid = sessionManagement.getSession();

        if(Userid != -1){
            Movetoactivity();
        }else{

        }
    }
    public void Movetoactivity(){
        Intent i = new Intent(getContext(),navigation_bar.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    private TextInputEditText et_email,et_pass;
    //private TextView forgotpass;
    private Button btn_login,forgotpass;
    private  TextView forget,signup;
    private FirebaseAuth auth;


    private void loginuser() {
        String email = et_email.getText().toString();
        String password = et_pass.getText().toString();



        if (et_email.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter your Username", Toast.LENGTH_SHORT).show();
            et_email.requestFocus();
        } else if (et_pass.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter your Username", Toast.LENGTH_SHORT).show();
            et_pass.requestFocus();
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(),"Login SuccessFully",Toast.LENGTH_SHORT).show();

                        Usersession usersession= new Usersession(12,email);
                        SessionManagement sessionManagement = new SessionManagement(getContext());
                        sessionManagement.saveSesion(usersession);


                        String uid;
                        uid = auth.getCurrentUser().getUid();

                        Movetoactivity();

                    }else{
                        Toast.makeText(getContext(),"Please Register Now"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        et_email.setText("");
                        et_pass.setText("");
                    }
                }
            });
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_login, container, false);

        et_email = v.findViewById(R.id.email_user);
        et_pass = v.findViewById(R.id.passwords_s);
        btn_login = v.findViewById(R.id.singIn);
        forget = v.findViewById(R.id.forgot_password);
        signup = v.findViewById(R.id.Register_Now);

        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(view -> {
            loginuser();
        });
//        forgotpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextInputEditText resetMail = new TextInputEditText(v.getContext());
//                AlertDialog.Builder passwordreset = new AlertDialog.Builder(v.getContext());
//                passwordreset.setTitle("Reset Your Password");
//                passwordreset.setMessage("Enter Your Email to recieved the Reset Link");
//                passwordreset.setView(resetMail);
//
//                passwordreset.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String mail = resetMail.getText().toString();
//                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(getContext(),"Reset Link Sent to our Email",Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getContext(),"Error! reset Link not set"+e.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//                passwordreset.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        passwordreset.create().show();
//                    }
//                });
//            }
//        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ForgetPassword.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),form_signup.class));
            }
        });


        return v;
    }

}