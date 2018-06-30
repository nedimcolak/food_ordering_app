package com.garden.gardenorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.garden.gardenorder.Common.Common;
import com.garden.gardenorder.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtNameSignUp, edtPhoneSignUp;
    Button signUpbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtNameSignUp = (MaterialEditText)findViewById(R.id.nameSignUp);
        edtPhoneSignUp = (MaterialEditText)findViewById(R.id.phoneSignUp);
        signUpbtn = (Button)findViewById(R.id.btnSignUp);

        //Startanje Firebasea
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnected(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please wait");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //provjeravamo da li user postoji u bazi
                            if (dataSnapshot.child(edtPhoneSignUp.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Korisnik već postoji",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtNameSignUp.getText().toString(), edtPhoneSignUp.getText().toString());
                                table_user.child(edtPhoneSignUp.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Registracija uspješna", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else
                    Toast.makeText(SignUp.this, "Nešto se sjebalo", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
