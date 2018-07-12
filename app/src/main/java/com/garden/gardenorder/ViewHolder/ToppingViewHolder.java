package com.garden.gardenorder.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import com.garden.gardenorder.R;

import java.util.List;

/**
 * Created by Nedim on 3/27/2018.
 */

public class ToppingViewHolder extends RecyclerView.ViewHolder{
    public static CheckBox checkBox;

    public ToppingViewHolder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.topping_view);
    }
}
