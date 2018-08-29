package com.blogspot.chunkingz.eat_it2;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private EditText edtName, edtAddress, edtEmail, edtPhone, edtPassword, edtSecureCode, edtBirthday, edtWedding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtAddress = findViewById(R.id.edtAddress);
        edtEmail = findViewById(R.id.edtEmail);
        edtSecureCode = findViewById(R.id.edtSecureCode);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        edtBirthday = findViewById(R.id.edtBirthday);
        edtWedding = findViewById(R.id.edtWedding);
        TextView alreadyHaveAcct = findViewById(R.id.alreadyHaveAcct);

        // init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d, m, y;
                Calendar calendar = Calendar.getInstance();
                d = calendar.get(Calendar.DAY_OF_MONTH);
                m = calendar.get(Calendar.MONTH);
                y = calendar.get(Calendar.YEAR);
                DatePickerDialog pickerDialog = new DatePickerDialog(
                        SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {
                                mm += 1;
                                edtBirthday.setText(dd +"/"+ mm +"/"+ yyyy );
                            }
                        },
                        y, m, d);
                pickerDialog.show();
            }
        });

        edtWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d, m, y;
                Calendar calendar = Calendar.getInstance();
                d = calendar.get(Calendar.DAY_OF_MONTH);
                m = calendar.get(Calendar.MONTH);
                y = calendar.get(Calendar.YEAR);
                DatePickerDialog pickerDialog = new DatePickerDialog(
                        SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {
                                mm += 1;
                                edtWedding.setText(dd +"/"+ mm +"/"+ yyyy );
                            }
                        },
                        y, m, d);
                pickerDialog.show();
            }
        });

        alreadyHaveAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(SignUp.this, MainActivity.class);
                startActivity(signIn);

            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // make sure the input fields are not empty
                if (edtPhone.getText().length() > 0 &&
                        edtPassword.getText().length() > 0 &&
                        edtName.getText().length() > 0 &&
                        edtAddress.getText().length() > 0 &&
                        edtSecureCode.getText().length() > 0 &&
                        edtEmail.getText().length() > 0 ) {

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        // display a loading dialog
                        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                        mDialog.setMessage("Please wait...");
                        mDialog.show();

                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // check if users phone number already exists
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUp.this, "This user already exists, you can try signing in.", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog.dismiss();
                                    User user = new User(edtName.getText().toString(),
                                            edtPassword.getText().toString(),
                                            edtAddress.getText().toString(),
                                            edtEmail.getText().toString(),
                                            edtSecureCode.getText().toString(),
                                            edtPhone.getText().toString(),
                                            edtBirthday.getText().toString(),
                                            edtWedding.getText().toString());
                                    table_user.child(edtPhone.getText().toString()).setValue(user);
                                    Toast.makeText(SignUp.this, "Signed up successfully", Toast.LENGTH_SHORT).show();

                                    Intent home = new Intent(SignUp.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(home);
                                    finish();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        Toast.makeText(SignUp.this, "Please make sure you're connected to the Internet", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignUp.this, "Field(s) can not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
