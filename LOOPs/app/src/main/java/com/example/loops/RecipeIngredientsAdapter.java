package com.example.loops;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This is a class to represent a custom adapter to show a list of ingredients
 * in RecipeFragments
 */
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder>{
    private final RecyclerViewOnClickInterface recyclerViewOnClickInterface;
    IngredientCollection recipeIngredients;
    Context context;

    /**
     * Constructor of adapter.
     * @param recipeIngredients
     * @param context
     * @param recyclerViewOnClickInterface
     */
    public RecipeIngredientsAdapter(IngredientCollection recipeIngredients, Context context,
                                    RecyclerViewOnClickInterface recyclerViewOnClickInterface) {
        this.recipeIngredients = recipeIngredients;
        this.context = context;
        this.recyclerViewOnClickInterface = recyclerViewOnClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recyclerview_layout,
        parent,false);
        ViewHolder holder = new ViewHolder(view, recyclerViewOnClickInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeIngredientTitle.setText(recipeIngredients.getIngredients().get(position).getDescription());
        holder.recipeIngredientCount.setText(""+recipeIngredients.getIngredients().get(position).getAmount());
        holder.recipeIngredientUnit.setText(recipeIngredients.getIngredients().get(position).getUnit());
    }

    @Override
    public int getItemCount() {
        return recipeIngredients.getIngredients().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView recipeIngredientTitle;
        private TextView recipeIngredientCount;
        private TextView recipeIngredientUnit;

        public ViewHolder(@NonNull View itemView, RecyclerViewOnClickInterface recyclerViewOnClickInterface){
            super(itemView);
            recipeIngredientTitle = itemView.findViewById(R.id.recipeIngredientTitle);
            recipeIngredientCount = itemView.findViewById(R.id.recipeIngredientCount);
            recipeIngredientUnit = itemView.findViewById(R.id.recipeIngredientUnit);
            /* This part allows a click to get the position of a recyclerview
             * item and passes it on the recyclerViewOnClickInterface to be
             * process.
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewOnClickInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewOnClickInterface.OnItemClick(pos);
                        }
                    }
                }
            });
        }

    }


}
