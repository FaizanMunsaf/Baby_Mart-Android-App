package com.example.project;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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

    private void showUserProfile(FirebaseUser firebaseUser){
        String Uid = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("signup");
        reference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass user = snapshot.getValue(UserHelperClass.class);
                if (user != null){
                    name_ = user.username;
                    user_ = user.username;
                    email_ = firebaseUser.getEmail();
                    phone_ = user.phoneNo;
                    address_ = user.address;
                    password_ = user.getPassword();
                    id_ = firebaseUser.getUid();
                    Picasso.with(getContext()).load(user.getImage()).into(image);

                    id.setText(id_);
                    password.setText(password_);
                    full_name.setText(name_);
                    username.setText(user_);
                    email.setText(email_);
                    phone.setText(phone_);
                    address.setText(address_);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void movetologin(){
        Intent i = new Intent(getContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000)
//        {
//            if (resultCode== Activity.RESULT_OK)
//            {
//                Uri imageuri=data.getData();
//                //Profile_image.setImageURI(imageuri);
//                uploadImagetoFirebase(imageuri);
//            }
//
//        }
//    }
//
//
//    private void uploadImagetoFirebase(Uri imageuri) {
//        progressDialog.show();
//        StorageReference fileref=storageReference.child("users/"+auth.getCurrentUser().getUid()+"profile.jpg");
//        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // Toast.makeText(MyProfile_page.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
//                while(!uriTask.isSuccessful());
//                Uri downloadImageUri=uriTask.getResult();
//
//                if(uriTask.isSuccessful()){
//                    HashMap<String, Object> hashMap=new HashMap<>();
//                    FirebaseFirestore.getInstance().collection("user")
//                            .document(auth.getUid())
//                            .update("image",downloadImageUri.toString())
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Picasso.with(getContext()).load(downloadImageUri).into(image);
//                                   // Picasso.get().load(downloadImageUri.toString()).into(image);
//                                    progressDialog.dismiss();
//                                }
//                            });

//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void openFileChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(getActivity()).load(imageUri).into(image);

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadFile(){
        if (imageUri != null){
            String uid;
            uid = auth.getCurrentUser().getUid();
            String user_1 = full_name.getText().toString();
            String email_1 = email.getText().toString();
            String phone_1 = phone.getText().toString();
            String address_1 = address.getText().toString();
            String pass_1 = password.getText().toString();
            root = FirebaseDatabase.getInstance();

            StorageReference fileref=storageReference.child(auth.getCurrentUser().getUid()+"profile.jpg");
            fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    },1000);
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    Uri downloadImageUri=uriTask.getResult();
                    UserHelperClass helperClass = new UserHelperClass(user_1, email_1, phone_1, pass_1, address_1,downloadImageUri.toString(), uid);
                    reference.child(uid).setValue(helperClass);
                    Picasso.with(getContext()).load(downloadImageUri.toString()).into(image);
                    full_name.setText(user_1);
                    username.setText(user_1);
                    email.setText(email_1);
                    phone.setText(phone_1);
                    address.setText(address_1);
                    id.setText(uid);
                    password.setText(pass_1);
                    Toast.makeText(getContext(),user_1+"UploadSuccessful",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            Toast.makeText(getContext(),"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }

    private String name_,user_,email_,password_,phone_,id_,address_;
    private ShapeableImageView image;
    private TextView id,username;
    private TextInputEditText full_name,email,phone,address,password;
    private Button logout_btn,update_btn;
    private FirebaseAuth auth;
    private FirebaseDatabase root;
    private DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("signup");
    private static final int PICK_IMAGE_REQUEST = 1;
//    private ProgressBar mprogress;
//    private FirebaseFirestore fstore;
//    private StorageTask muploadTask;
    private StorageReference storageReference;



    private Uri imageUri;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_profile, container, false);

        image = v.findViewById(R.id.image);
        id = v.findViewById(R.id.user_id);
        password = v.findViewById(R.id.pass_et);
        username = v.findViewById(R.id.full_name);
        full_name = v.findViewById(R.id.user);
        email = v.findViewById(R.id.email);
        phone = v.findViewById(R.id.phone);
        address = v.findViewById(R.id.address);
        logout_btn = v.findViewById(R.id.logout_btn);
        update_btn = v.findViewById(R.id.update_btn);
        auth = FirebaseAuth.getInstance();
        String uid;
        uid = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("signup");
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }});


                FirebaseUser firebaseUser = auth.getCurrentUser();
                update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        uploadFile();


                    }
                });
                if (firebaseUser == null) {
                    Toast.makeText(getContext(), "Something went wrong in User details!", Toast.LENGTH_SHORT).show();
                } else {
                    showUserProfile(firebaseUser);
                }

                logout_btn.setOnClickListener(view -> {
                    auth.signOut();
                    SessionManagement sessionManagement = new SessionManagement(getContext());
                    sessionManagement.removeSession();
                    movetologin();


                });
                return v;
            }


//            public class upload{
//                private String mimageurl;
//
//                public upload(String s){
//
//                }
//                public upload(String name, String imageurl){
//
//                    mimageurl = imageurl;
//                }
//
//                public String getimageurl() {
//                    return mimageurl;
//                }
//                public void setimageurl(String mimageurl) {
//                    this.mimageurl = mimageurl;
//                }
//            }

        }