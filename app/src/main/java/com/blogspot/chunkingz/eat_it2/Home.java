package com.blogspot.chunkingz.eat_it2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Model.GasPrice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText date;
    private EditText time;
    private EditText cylinderSize2;
    private TextView priceTextView;
    Button submit;
    private TextView price;
    private TextView textArea;
    private String format;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private final Integer homePriceOfGas = 384;
    private final Integer officePriceOfGas = 352;
    private String cartDate;
    private String cartTime;
    private String cartSize;
    private String cartPrice;
    private String cartDelivery;
    private String cartComment;

    String homePriceFirebase, officePriceFirebase;

    private FirebaseDatabase database;
    private DatabaseReference table_gas_price;  // for the gas cylinder price

    private void selectedDeliveryMethod() {

        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = findViewById(selectedId);

//        Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
        radioButton.getText();

    }

    //this method is called when the plus button is clicked
    public void increment(View view){
        if (cylinderSize2.getText().length() == 0){
            price.setText("0");
            Toast.makeText(this, "Type in a value...", Toast.LENGTH_SHORT).show();
        } else {
            Float intCylinderSize2 = Float.parseFloat(cylinderSize2.getText().toString());
            if ((intCylinderSize2 >= 0) && (intCylinderSize2 <= 1000)) {
                intCylinderSize2++;
                if (intCylinderSize2 > 1000) {
                    Toast.makeText(this, "This shouldnt be greater than 1000", Toast.LENGTH_SHORT).show();
                } else {
                    String intCylinderSizeIncreased = intCylinderSize2.toString();
                    cylinderSize2.setText(intCylinderSizeIncreased);
                    // update price function
                    display(intCylinderSize2);
                }
            } else {
                Toast.makeText(this, "Kindly pick a size lower than 1000kg", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //this method is called when the minus button is pressed.
    public void decrement(View view){
        if (cylinderSize2.getText().length() == 0){
            price.setText("0");
            Toast.makeText(this, "Type in a value...", Toast.LENGTH_SHORT).show();
        } else {
            Float intCylinderSize2 = Float.parseFloat(cylinderSize2.getText().toString());
            if ((intCylinderSize2 >= 0) && (intCylinderSize2 <= 1000)) {
                intCylinderSize2--;
                if (intCylinderSize2 < 0) {
                    Toast.makeText(this, "This shouldnt be less than zero.", Toast.LENGTH_SHORT).show();
                } else {
                    String intCylinderSizeDecreased = intCylinderSize2.toString();
                    cylinderSize2.setText(intCylinderSizeDecreased);
                    // update price function
                    display(intCylinderSize2);
                }
            } else {
                Toast.makeText(this, "Kindly pick a size higher than 0kg", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*this method displays the given quantity value on the screen*/
    private void display(Float number){

        selectedDeliveryMethod();

        switch (radioButton.getText().toString()) {
            case "Home Service":
                number *= Integer.parseInt(homePriceFirebase);
                break;
            case "Come to our office":
                number *= Integer.parseInt(officePriceFirebase);
                break;
            default:
                return;
        }

//        number *= homePriceOfGas;
            String intCylinderSize2ToString = number.toString();
            price.setText(intCylinderSize2ToString);

    }

    private void selectedTimeFormat(int hr) {
        if(hr == 0){
            format = "AM";
        } else if(hr == 12){
            format = "PM";
        } else if(hr > 12){
            format = "PM";
        } else {
            format = "AM";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = findViewById(R.id.homeDatePicker);
        time = findViewById(R.id.homeTimePicker);
        radioGroup = findViewById(R.id.radioGroup);
        cylinderSize2 = findViewById(R.id.kgText2);
        price = findViewById(R.id.kgsum);
        textArea = findViewById(R.id.textArea);
        priceTextView = findViewById(R.id.priceTextView);

        // init Firebase
        table_gas_price = FirebaseDatabase.getInstance().getReference("Gas_Price");

        // display a loading dialog
        final ProgressDialog mDialog = new ProgressDialog(Home.this);
        mDialog.setMessage("Fetching current price, Please wait...");
        mDialog.show();

        // try to check for gas price values in the db
        table_gas_price.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Price").exists()){
                    mDialog.dismiss();
                    homePriceFirebase = dataSnapshot.child("Price").child("home").getValue().toString();
                    officePriceFirebase = dataSnapshot.child("Price").child("office").getValue().toString();
                    if(dataSnapshot.child("Price").child("home").exists() && dataSnapshot.child("Price").child("office").exists()){
                        priceTextView.setVisibility(View.VISIBLE);
                        priceTextView.setText("Price:\nHome = N"+homePriceFirebase +"  Office = N"+officePriceFirebase);
                    } else {
                        Toast.makeText(Home.this, "home and office price don't exist", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    mDialog.dismiss();
                    Toast.makeText(Home.this, "Price db doesnt exist, contact admin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });

        // init paper
        Paper.init(this);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d, m, y;
                Calendar calendar = Calendar.getInstance();
                d = calendar.get(Calendar.DAY_OF_MONTH);
                m = calendar.get(Calendar.MONTH);
                y = calendar.get(Calendar.YEAR);
                DatePickerDialog pickerDialog = new DatePickerDialog(
                        Home.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {
                                mm += 1;
                                date.setText(dd +"/"+ mm +"/"+ yyyy );
                            }
                        },
                        y, m, d);
                pickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hr, min;
                Calendar calendar = Calendar.getInstance();
                hr = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);

                selectedTimeFormat(hr);

                TimePickerDialog pickerDialog = new TimePickerDialog(
                        Home.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hr, int min) {
                                selectedTimeFormat(hr);
                                time.setText(hr +":"+ min +" "+ format);
                            }
                        },
                        hr, min, false);
                pickerDialog.show();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Snackbar.make(view, "Checking Cart... Press Submit when done.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                // first check if user has selected both date and time
                if ( !(date.getText().toString().isEmpty()) && !(time.getText().toString().isEmpty())) {
                    if (cylinderSize2.getText().length() == 0) {
                        price.setText("0");
                        Toast.makeText(Home.this, "Kindly fill out fields marked with *...", Toast.LENGTH_SHORT).show();
                    } else {
                        ProgressDialog mDialog = new ProgressDialog(Home.this);
                        mDialog.setMessage("Please wait...");
                        mDialog.show();

                        Float intCylinderSize2 = Float.parseFloat(cylinderSize2.getText().toString());

                        if ((intCylinderSize2 > 0) && (intCylinderSize2 <= 1000)) {

                            selectedDeliveryMethod();

                            Float cySize = intCylinderSize2;
                            switch (radioButton.getText().toString()) {
                                case "Home Service":
                                    intCylinderSize2 *= Integer.parseInt(homePriceFirebase);
                                    break;
                                case "Come to our office":
                                    intCylinderSize2 *= Integer.parseInt(officePriceFirebase);
                                    break;
                                default:
                                    return;
                            }
                            String intCylinderSizeIncreased = intCylinderSize2.toString();
                            price.setText(intCylinderSizeIncreased);

                            Intent cartCheckout = new Intent(Home.this, Cart.class);

                            // assign values to variables, so that we can init the intent values.
                            cartDate = date.getText().toString();
                            cartTime = time.getText().toString();
                            cartSize = cylinderSize2.getText().toString();
                            cartPrice = price.getText().toString();
                            cartDelivery = radioButton.getText().toString();
                            cartComment = textArea.getText().toString();

                            // send the values from this activity to the next which is the cart activity, for checkout.
                            cartCheckout.putExtra("cartDate", cartDate);
                            cartCheckout.putExtra("cartTime", cartTime);
                            cartCheckout.putExtra("cartSize", cartSize);
                            cartCheckout.putExtra("cartPrice", cartPrice);
                            cartCheckout.putExtra("cartDelivery", cartDelivery);
                            cartCheckout.putExtra("cartComment", cartComment);

                            startActivity(cartCheckout);

                            mDialog.dismiss();
                        } else {
                            mDialog.dismiss();

                            price.setText("0");
                            Toast.makeText(Home.this, "Kindly pick a cylinder size between 1kg and 1000kg", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(Home.this, "Kindly fill in the date/time...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name and email for current user in nav drawer
        View headerView = navigationView.getHeaderView(0);
        TextView txtFullName = headerView.findViewById(R.id.nav_header_fullname);
        txtFullName.setText(Common.currentUser.getName());
        TextView txtEmail = headerView.findViewById(R.id.nav_header_email);
        txtEmail.setText(Common.currentUser.getEmail());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact_us) {
            Intent contactUs = new Intent(Home.this, ContactUs2.class);
            startActivity(contactUs);
//            return true;
        } else if (id == R.id.action_about) {
            Intent about = new Intent(Home.this, About.class);
            startActivity(about);
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle the users action in the nav drawer
        switch (id) {
            case R.id.nav_person:

                Intent userProfile = new Intent(Home.this, UserProfile.class);
                startActivity(userProfile);

                break;
            case R.id.nav_orders:
                Intent ordersIntent = new Intent(Home.this, OrderStatus.class);
                startActivity(ordersIntent);

                break;
            case R.id.nav_change_pass:
                showChangePasswordDialog();

                break;
            case R.id.nav_log_out:

                // Delete remember user and password
                Paper.book().destroy();

                // exit the app
                Intent mainActivity = new Intent(Home.this, MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
//            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_feedback:

                Intent userFeedback = new Intent(Home.this, UserFeedback.class);
                startActivity(userFeedback);

                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("Please Fill All Information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_password_layout, null);

        final MaterialEditText pwd1 = findViewById(R.id.pass1); // old password
        MaterialEditText pwd2 = findViewById(R.id.pass2); // new password
        MaterialEditText pwd3 = findViewById(R.id.pass3); // new password again

        alertDialog.setView(layout_pwd);

        // buttons for the alert dialog
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // change password here
                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(Home.this);
                alertDialog1.show();

                // check old password
/*                if (pwd1.getText().toString().equals()){
                    // check new password and repeat password
                }else {
                    Toast.makeText(Home.this, "Old Password doesnt match up", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
