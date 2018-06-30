package com.garden.gardenorder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.garden.gardenorder.Common.Common;
import com.garden.gardenorder.Database.Database;
import com.garden.gardenorder.Model.Food;
import com.garden.gardenorder.Model.Order;
import com.garden.gardenorder.Model.Topping;
import com.garden.gardenorder.ViewHolder.ToppingViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FoodDetail extends AppCompatActivity {


    TextView food_name, food_price;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    RecyclerView toppingCheckBoxView;
    FirebaseRecyclerAdapter<Topping, ToppingViewHolder> toppingAdapter;
    RecyclerView.LayoutManager toppingLayoutManager;

    String foodId="";

    String toppings;

    ArrayList<String> checkedToppings = new ArrayList<>();

    //Firebase
    FirebaseDatabase database;
    DatabaseReference food;
    DatabaseReference topping;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Setting layout manager for the recyclerView
        toppingCheckBoxView = (RecyclerView) findViewById(R.id.food_topping);
        toppingLayoutManager = new LinearLayoutManager(this);
        toppingCheckBoxView.setLayoutManager(toppingLayoutManager);
        toppingCheckBoxView.addItemDecoration(new Divider(this));
        //Defining firebase instance and setting references
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");
        topping = database.getReference("Toppings");

        //Initialize view

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {

                toppings = toppingListString(checkedToppings);

                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        toppings
                ));

                Toast.makeText(FoodDetail.this, "Added to cart!", Toast.LENGTH_LONG).show();

            }
        });

        food_name = (TextView)findViewById(R.id.food_name);
        food_price = (TextView)findViewById(R.id.food_price);
        food_image = (ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food ID from Intent
        if(getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
            if (Common.isConnected(getBaseContext())) {
                getDetailFood(foodId);
            } else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void getDetailFood(final String foodId) {

        loadToppings(foodId);

        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //loadToppingList(foodId);
                currentFood = dataSnapshot.getValue(Food.class);

                //Set image
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());

                food_name.setText(currentFood.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadToppings(final String foodId){

        toppingAdapter = new FirebaseRecyclerAdapter<Topping, ToppingViewHolder>(Topping.class,
                R.layout.food_topping,
                ToppingViewHolder.class,
                topping.orderByChild("foodId").equalTo(foodId) ) {

            @Override protected void populateViewHolder(ToppingViewHolder viewHolder, final Topping model, int position) {

                viewHolder.checkBox.setText(model.getToppingName());

                //Listens to change in checkboxes
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isChecked()){
                            buttonView.setChecked(true);
                            checkedToppings.add(buttonView.getText().toString());
                        }
                        else {
                            buttonView.setChecked(false);
                            checkedToppings.remove(buttonView.getText().toString());
                        }
                    }
                });

            }
        };
        toppingAdapter.notifyDataSetChanged();
        toppingCheckBoxView.setAdapter(toppingAdapter);
    }

    public String toppingListString(ArrayList<String> toppings){
        StringBuilder sb = new StringBuilder();
        for (String s : toppings){
            sb.append(s);
            sb.append(",");
        }
        if(sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        else sb.append("None");
        return sb.toString();
    }
}