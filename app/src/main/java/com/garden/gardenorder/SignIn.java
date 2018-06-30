package com.garden.gardenorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.garden.gardenorder.Common.Common;
import com.garden.gardenorder.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText edtPhone;
    Button btnSignIn;
    CheckBox chkRemember;
    String user = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        chkRemember = (CheckBox) findViewById(R.id.ckbRemember);

        SharedPreferences spUser = getSharedPreferences(user, Context.MODE_PRIVATE);
        final SharedPreferences.Editor spEditor = spUser.edit();


        //Startanje Firebasea
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnected(getBaseContext())) {


                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Molimo sačekajte");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //provjeravamo da li user postoji u bazi
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                                chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (buttonView.isChecked()){
                                            if (edtPhone!=null){
                                                spEditor.putString("phone", edtPhone.getText().toString());
                                                spEditor.apply();
                                            }else {
                                                spEditor.clear();
                                            }
                                        }
                                    }
                                });


                                //Prikupljanje podataka o korisniku iz baze
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                // set Phone, odnosno broj stola (trenutno)
                                user.setPhone(edtPhone.getText().toString());
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Netačno unesen broj stola", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else
                    Toast.makeText(SignIn.this, "Nema internet konekcije", Toast.LENGTH_SHORT).show();
                    return;
            }
        });

    }
}