package com.garden.gardenorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.garden.gardenorder.Common.Common;
import com.garden.gardenorder.Database.Database;
import com.garden.gardenorder.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn;
    Button btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRf = db.getReference("user");

        final SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("phone", null).isEmpty()){
            Intent i = new Intent(MainActivity.this, SignIn.class);
            startActivity(i);
        }
        else{

            userRf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = new User(dataSnapshot.child(sharedPreferences.getString("phone", null)).child("name").toString(), sharedPreferences.getString("phone", null));
                    Intent homeIntent = new Intent(MainActivity.this, Home.class);
                    Common.currentUser = user;
                    startActivity(homeIntent);
                    finish();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/WorkSans-Bold.ttf");
        txtSlogan.setTypeface(face);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });
    }
}
