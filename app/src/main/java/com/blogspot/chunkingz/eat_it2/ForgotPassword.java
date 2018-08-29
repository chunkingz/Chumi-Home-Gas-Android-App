package com.blogspot.chunkingz.eat_it2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    private EditText phoneNumber1;
    private EditText secureCode;
    private String phoneNumber2;
    private String secureCode2;

    private DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phoneNumber1 = findViewById(R.id.edtPhoneForgot);
        secureCode = findViewById(R.id.edtSecureCode);


        // init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

    }

    public void forgotpassword1(View view) {
        if(phoneNumber1.getText().length() > 0 && secureCode.getText().length() > 0){

            if (Common.isConnectedToInternet(getBaseContext())) {
                // display a loading dialog
                final ProgressDialog mDialog = new ProgressDialog(ForgotPassword.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();


                phoneNumber2 = phoneNumber1.getText().toString();
            secureCode2 = secureCode.getText().toString();

            // check if user exists
            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.child(phoneNumber2).getValue(User.class);

                    if (user != null) {
                        if (user.getSecureCode().equals(secureCode2)) {
                            mDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, "Your Password: " + user.getPassword(), Toast.LENGTH_LONG).show();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, "Wrong secure code!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        mDialog.dismiss();
                        Toast.makeText(ForgotPassword.this, "This phone number doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            }else {
                Toast.makeText(ForgotPassword.this, "Please make sure you're connected to the Internet", Toast.LENGTH_SHORT).show();
            }

            //Toast.makeText(this, "Phone number: "+phoneNumber2+".\n Secure Code: "+secureCode2, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please enter your phone number and secure code.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {

        Intent signIn = new Intent(ForgotPassword.this, MainActivity.class);
        startActivity(signIn);
        finish();
    }
}
