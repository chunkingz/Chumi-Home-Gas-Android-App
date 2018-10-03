package com.blogspot.chunkingz.eat_it2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    //LinearLayout layout1;
    private EditText phonenumber, password;
    private com.rey.material.widget.CheckBox checkboxRememberMe;

    private DatabaseReference table_user;

    /*Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            layout1.setVisibility(View.VISIBLE);
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //layout1 = findViewById(R.id.layout1);
        phonenumber = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        Button buttonLogin = findViewById(R.id.buttonSignIn);
        Button buttonSignup = findViewById(R.id.buttonSignUp);
        checkboxRememberMe = findViewById(R.id.checkbox1);
        ImageView topIcon = findViewById(R.id.icon1);
        TextView forgotPassword = findViewById(R.id.forgotpassword1);

        // 2000ms = 2secs which is the delay for the splash screen
        //handler.postDelayed(runnable, 2000);

        //initialize Paper
        Paper.init(this);

        // check remember user
        String phone = Paper.book().read(Common.USER_KEY);
        String pass = Paper.book().read(Common.PASS_KEY);

        if (phone != null && pass != null){
            if (!phone.isEmpty() && !pass.isEmpty()){
                login(phone, pass);
            }
        }

        // init Firebase
        table_user = FirebaseDatabase.getInstance().getReference("User");

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotLayout = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(forgotLayout);

            }

        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // make sure the input fields are not empty
                if (phonenumber.getText().length() > 0 && password.getText().length() > 0) {

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        //save username and password
                        if (checkboxRememberMe.isChecked()){
                            Paper.book().write(Common.USER_KEY, phonenumber.getText().toString());
                            Paper.book().write(Common.PASS_KEY, password.getText().toString());
                        }

                        // display a loading dialog
                        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                        mDialog.setMessage("Please wait...");
                        mDialog.show();

                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                // check user exists in db
                                if (dataSnapshot.child(phonenumber.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    // get user info
                                    User user = dataSnapshot.child(phonenumber.getText().toString()).getValue(User.class);
                                    assert user != null;
                                    if (user.getPassword().equals(password.getText().toString())) {
                                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                                        Intent home = new Intent(MainActivity.this, Home.class);
                                        Common.currentUser = user;
                                        startActivity(home);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        Toast.makeText(MainActivity.this, "Please make sure you're connected to the Internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "Username/Password can not be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signup = new Intent(MainActivity.this, SignUp.class);
                startActivity(signup);

            }

        });
    }

    private void login(final String phone, final String pass) {
        // init Firebase
        table_user = FirebaseDatabase.getInstance().getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {

            // display a loading dialog
            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait...");
            mDialog.show();

            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // check user exists in db
                    if (dataSnapshot.child(phone).exists()) {
                        mDialog.dismiss();
                        // get user info
                        User user = dataSnapshot.child(phone).getValue(User.class);

                        assert user != null;
                        if (user.getPassword().equals(pass)) {
                            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                            Intent home = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(home);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User doesnt exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(MainActivity.this, "Please make sure you're connected to the Internet", Toast.LENGTH_SHORT).show();
        }
    }

}
