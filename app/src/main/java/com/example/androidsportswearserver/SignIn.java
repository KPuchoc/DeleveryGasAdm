package com.example.androidsportswearserver;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidsportswearserver.Common.Common;
import com.example.androidsportswearserver.Model.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnSignIn;
    FirebaseDatabase db;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPassword=findViewById(R.id.edtPasswor);
        edtPhone=findViewById(R.id.edtPhone);
        btnSignIn=findViewById(R.id.BtnSignIn);

        db=FirebaseDatabase.getInstance();
        users = db.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {



                    signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());

            }

            private void signInUser(String phone, String password) {
                final String localPhone=phone;
                final String localPassword=password;
                final ProgressDialog mDialog=new ProgressDialog(SignIn.this);
                mDialog.setMessage("Por Favor Espere");
                mDialog.show();



                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(localPhone).exists())
                        {
                            mDialog.dismiss();
                            User user = dataSnapshot.child(localPhone).getValue(User.class);
                            user.setPhone(localPhone);
                            if(Boolean.parseBoolean(user.getIsStaff())) //cuenta de adm debe ser verdadedo
                            {
                                if(user.getPassword().equals(localPassword))
                                {
                                    Intent homeIntent=new Intent(SignIn.this,Home.class);
                                    Common.currentUser=user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                                else
                                    Toast.makeText(SignIn.this, "Contraseña incorecta", Toast.LENGTH_SHORT).show();

                            }
                            else
                                Toast.makeText(SignIn.this, "Por favor ingrese con una cuenta de Staff", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "El usuario no existe en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                    

                    @Override
                    public void onCancelled( DatabaseError databaseErrorerror) {

                    }
                });
            }
        });
    }
}