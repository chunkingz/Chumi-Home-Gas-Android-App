package com.blogspot.chunkingz.eat_it2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Database.Database;
import com.blogspot.chunkingz.eat_it2.Model.Order;
import com.blogspot.chunkingz.eat_it2.Model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private String cartDate, cartTime, cartSize, cartPrice, cartDelivery, cartComment, userEmail, userAddress, usernameString, userPhone;

    private DatabaseReference requests;

    // URL that the orders email goto
    String URL_REQUEST = "http://firebase.chumihome.com/request.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // How the Firebase request is made.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        // init the vars
        TextView checkoutDate = findViewById(R.id.datetxt);
        TextView checkoutTime = findViewById(R.id.timetxt);
        TextView checkoutSize = findViewById(R.id.sizetxt);
        TextView checkoutPrice = findViewById(R.id.pricetxt);
        TextView checkoutDelivery = findViewById(R.id.deliverytxt);
        TextView checkoutComment = findViewById(R.id.extracommentstxt);
        TextView username = findViewById(R.id.usernametxt);

        // auto set some values from the Common>User class
        userEmail =  Common.currentUser.getEmail();
        userAddress =  Common.currentUser.getAddress();
        usernameString = Common.currentUser.getName();
        userPhone = Common.currentUser.getPhoneNumber();

        // fetch the values from the previous activity
        cartDate = getIntent().getExtras().getString("cartDate");
        cartTime = getIntent().getExtras().getString("cartTime");
        cartSize = getIntent().getExtras().getString("cartSize");
        cartPrice = getIntent().getExtras().getString("cartPrice");
        cartDelivery = getIntent().getExtras().getString("cartDelivery");
        cartComment = getIntent().getExtras().getString("cartComment");

        // set the values for the cart view
        checkoutDate.setText("Date of Delivery: "+cartDate);
        checkoutTime.setText("Time of Delivery: "+cartTime);
        checkoutSize.setText("Size of Cylinder: "+cartSize+" kg");
        checkoutPrice.setText("Total Amount Payable: N "+cartPrice);
        checkoutDelivery.setText("Method of Delivery: "+cartDelivery);
        checkoutComment.setText("Extra Comments: "+cartComment);
        username.setText(usernameString);
    }

    public void submit(View view) {

        new Database(getBaseContext()).addToCart(new Order(
                cartDate,
                cartTime,
                cartSize,
                cartPrice,
                cartDelivery,
                cartComment
        ));

//        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();

        // set the users request based on parameters provided. into the request class constructor.
        Request request = new Request(
                usernameString,
                userPhone,
                userAddress,
                userEmail,
                cartDate,
                cartTime,
                cartSize,
                cartPrice,
                cartDelivery,
                cartComment,
                null
        );

        // submit to firebase
        // using system.currentTimeMillis as the key
        requests.child(String.valueOf(System.currentTimeMillis()))
                .setValue(request);

        // Delete cart
        new Database(getBaseContext()).cleanCart();
        Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();

        // php mysql stuffs
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
//                                Toast.makeText(Cart.this, "PHP/MySql success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(Cart.this, "MySql Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Cart.this, "MySql Error: "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", usernameString);
                params.put("phone", userPhone);
                params.put("address", userAddress);
                params.put("email", userEmail);
                params.put("date", cartDate);
                params.put("time", cartTime);
                params.put("size", cartSize);
                params.put("price", cartPrice);
                params.put("delivery", cartDelivery);
                params.put("extras", cartComment);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Intent thanks = new Intent(Cart.this, ThankYou.class);
        startActivity(thanks);
        finish();


        //Send Email intent
        /*Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.setType("message/rfc822");
        sendEmail.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kingstonfortune@gmail.com"});
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "New order from Gas App");
        sendEmail.putExtra(Intent.EXTRA_TEXT   , "Hello Admin,\n\nYou have a new order.\n"+
                "\nCustomer name: "+usernameString+".\n"+
                "\nDate of Delivery: "+cartDate+".\n"+
                "\nTime of Delivery: "+cartTime+".\n"+
                "\nSize of Cylinder: "+cartSize+".\n"+
                "\nTotal Amount Payable: "+cartPrice+".\n"+
                "\nMethod of Delivery: "+cartDelivery+".\n"+
                "\nExtra Comments: "+cartComment+".\n"+
                "\n\n\nKindly treat as urgent.\nRegards.");
        try {
            startActivity(Intent.createChooser(sendEmail, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Cart.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }*/

        /*Intent thanks = new Intent(Cart.this, ThankYou.class);
        startActivity(thanks);
        finish();*/
    }

    public void goBackToHome(View view) {
        Intent home = new Intent(Cart.this, Home.class);
        startActivity(home);
    }
}
