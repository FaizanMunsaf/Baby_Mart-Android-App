package com.example.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link form_signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class form_signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public form_signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment form_signup.
     */
    // TODO: Rename and change types and number of parameters
    public static form_signup newInstance(String param1, String param2) {
        form_signup fragment = new form_signup();
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
   private TextInputEditText et_user,et_email,et_phone,et_address,et_pass;
   private Button btn_signup;
   FirebaseDatabase root;
   DatabaseReference reference;
   private FirebaseAuth FAuth;
   private TextView login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_signup, container, false);


        et_user = v.findViewById(R.id.username);
        et_email = v.findViewById(R.id.email);
        et_phone = v.findViewById(R.id.phonenumber);
        et_address = v.findViewById(R.id.address);
        et_pass = v.findViewById(R.id.passwords);
        btn_signup = v.findViewById(R.id.singIn);
        login = v.findViewById(R.id.Login_Now);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_user.getText().toString();
                String email = et_email.getText().toString();
                String phone = et_phone.getText().toString();
                String address = et_address.getText().toString();
                String password = et_pass.getText().toString();
                String image = "https://firebasestorage.googleapis.com/v0/b/babymart-729bd.appspot.com/o/profile_pic.png?alt=media&token=043a9e68-5683-4c7c-8578-e6982a2b3093";
                FAuth = FirebaseAuth.getInstance();

                try{if(et_user.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter your Username", Toast.LENGTH_SHORT).show();
                    et_user.requestFocus();
                }else if(et_email.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter your Email", Toast.LENGTH_SHORT).show();
                    et_email.requestFocus();
                }else if(et_phone.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter your Phone Number", Toast.LENGTH_SHORT).show();
                    et_phone.requestFocus();
                }else if(et_address.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter your Address", Toast.LENGTH_SHORT).show();
                    et_address.requestFocus();
                }else if(et_pass.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter your Password", Toast.LENGTH_SHORT).show();
                    et_pass.requestFocus();
                }
                else {
                    FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {
                                String uid;
                                uid = FAuth.getCurrentUser().getUid();

                                root = FirebaseDatabase.getInstance();
                                reference = root.getReference("signup");
                                UserHelperClass helperClass = new UserHelperClass(name, email, phone, password, address, image, uid);
                                reference.child(uid).setValue(helperClass);

                                Toast.makeText(getContext(), name + " You are Registered! Let's login!", Toast.LENGTH_SHORT).show();

                                et_user.setText("");
                                et_email.setText("");
                                et_phone.setText("");
                                et_pass.setText("");
                                et_address.setText("");
                                startActivity(new Intent(getContext(), form_login.class));

                            } else {
                                Toast.makeText(getContext(), name + " You are Not Registered!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                }catch (Exception e){
                    Toast.makeText(getContext(),"Enter your correct Email!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), form_login.class));
            }
        });


        return v;

    }

}