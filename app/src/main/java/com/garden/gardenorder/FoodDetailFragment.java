package com.garden.gardenorder;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDetailFragment extends Fragment {

    private static final String ARG_FOOD_ID = "Food ID";

    //UI elements

    Context context = getActivity().getApplicationContext();

    TextView food_name, food_price;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    RecyclerView toppingCheckBoxView;
    FirebaseRecyclerAdapter<Topping, ToppingViewHolder> toppingAdapter;
    RecyclerView.LayoutManager toppingLayoutManager;

    String toppings;

    ArrayList<String> checkedToppings = new ArrayList<>();

    //Firebase
    FirebaseDatabase database;
    DatabaseReference food;
    DatabaseReference topping;


    private String foodId;

    Food currentFood;


    public FoodDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param foodId Food ID
     * @return A new instance of fragment FoodDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodDetailFragment newInstance(String foodId) {
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOOD_ID, foodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_detail, container, false);

        //Setting layout manager for the recyclerView
        toppingCheckBoxView = (RecyclerView) rootView.findViewById(R.id.food_topping);
        toppingLayoutManager = new LinearLayoutManager(context);
        toppingCheckBoxView.setLayoutManager(toppingLayoutManager);
        toppingCheckBoxView.addItemDecoration(new Divider(context));
        //Defining firebase instance and setting references
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");
        topping = database.getReference("Toppings");

        //Initialize view

        numberButton = (ElegantNumberButton)rootView.findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)rootView.findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {

                toppings = toppingListString(checkedToppings);

                new Database(context).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        toppings
                ));

                Toast.makeText(context, "Added to cart!", Toast.LENGTH_LONG).show();

            }
        });

        food_name = (TextView)rootView.findViewById(R.id.food_name);
        food_price = (TextView)rootView.findViewById(R.id.food_price);
        food_image = (ImageView)rootView.findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food ID from Intent
        foodId = getArguments().getString("Food ID");
        if(!foodId.isEmpty())
        {
            if (Common.isConnected(getActivity().getApplicationContext())) {
                getDetailFood(foodId);
            } else
                Toast.makeText(getActivity().getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return rootView;

    }

    private void getDetailFood(final String foodId) {

        loadToppings(foodId);

        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //loadToppingList(foodId);
                currentFood = dataSnapshot.getValue(Food.class);

                //Set image
                Picasso.with(context).load(currentFood.getImage())
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


