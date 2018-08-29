package com.blogspot.chunkingz.eat_it2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Model.Feedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFeedback extends AppCompatActivity {
    
    private EditText feedbackTitle;
    private EditText feedbackMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        
        feedbackTitle = findViewById(R.id.title1);
        feedbackMsg = findViewById(R.id.msg1);
        Button feedbackBtn = findViewById(R.id.btnSubmitFeedback);

        // init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_feedback = database.getReference("Feedback");
        
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Submitting feedback...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                // make sure the input fields are not empty
                if (feedbackTitle.getText().length() > 0 &&
                        feedbackMsg.getText().length() > 0 ) {

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        // display a loading dialog
                        final ProgressDialog mDialog = new ProgressDialog(UserFeedback.this);
                        mDialog.setMessage("Please wait...");
                        mDialog.show();

                        table_feedback.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    mDialog.dismiss();
                                    Feedback feedback = new Feedback(Common.currentUser.getName(),
                                            Common.currentUser.getEmail(),
                                            Common.currentUser.getPhoneNumber(),
                                            feedbackTitle.getText().toString(),
                                            feedbackMsg.getText().toString());
                                    table_feedback.child(Common.currentUser.getPhoneNumber()).setValue(feedback);
                                    Toast.makeText(UserFeedback.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        Toast.makeText(UserFeedback.this, "Please make sure you're connected to the Internet", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserFeedback.this, "Field(s) can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goBackHome(View view) {
        Intent home = new Intent(UserFeedback.this, Home.class);
        startActivity(home);
    }
}
