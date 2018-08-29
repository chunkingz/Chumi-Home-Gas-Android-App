package com.blogspot.chunkingz.eat_it2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.blogspot.chunkingz.eat_it2.Common.Common;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView txtName = findViewById(R.id.txtName);
        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtPhone = findViewById(R.id.txtPhone);
        TextView txtBirthday = findViewById(R.id.txtBirthday);
        TextView txtWedding = findViewById(R.id.txtWedding);

        String strName = Common.currentUser.getName();
        String strAddress = Common.currentUser.getAddress();
        String strEmail = Common.currentUser.getEmail();
        String strPhone = Common.currentUser.getPhoneNumber();
        String strBirthday = Common.currentUser.getBirthday();
        String strWedding = Common.currentUser.getWeddingAnniversary();

        txtName.setText(strName);
        txtAddress.setText(strAddress);
        txtEmail.setText(strEmail);
        txtPhone.setText(strPhone);
        txtBirthday.setText(strBirthday);
        txtWedding.setText(strWedding);
    }
}
