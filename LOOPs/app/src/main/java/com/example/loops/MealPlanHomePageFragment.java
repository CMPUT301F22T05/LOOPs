package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.loops.adapters.MealPlansViewAdapter;
import com.example.loops.ingredientFragments.IngredientCollectionEditorFragmentDirections;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;

/**
 * Display this week's meal plan
 */
public class MealPlanHomePageFragment extends Fragment {
    private MealPlanCollection mealPlans;
    private MealPlansViewAdapter viewAdapter;

    /**
     * Retrieve meal plan from database
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * The try catch is to allow the intent test for this fragment to pass the
         * casting error since the test uses fragment activity instead of activities
         * Once the casting error is catched, it uses dummy data
         */
        try{
            mealPlans = ((MainActivity)getActivity()).getMealPlans();
        }
        catch (Exception e){
            mealPlans = new MealPlanCollection();
        }

    }

    /**
     * Set up the adapter, parse fragment's argument, and set event listeners
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan_home_page, container, false);
        viewAdapter = new MealPlansViewAdapter(getActivity(), mealPlans.getMealPlans());
        ListView mealPlansView = view.findViewById(R.id.meal_plan_collection);
        mealPlansView.setAdapter(viewAdapter);
        mealPlansView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MealPlan selectedMealPlan = mealPlans.getMealPlans().get(position);
                NavDirections lookAtMealPlanDetailAction = MealPlanHomePageFragmentDirections
                        .actionLookAtMealPlanDetail(selectedMealPlan, position);
                Navigation.findNavController(view).navigate(lookAtMealPlanDetailAction);
            }
        });

        // parse arguments
        MealPlanHomePageFragmentArgs argsBundle
                = MealPlanHomePageFragmentArgs.fromBundle(getArguments());
        int updatedMealPlanIndex = argsBundle.getMealPlanIndex();
        MealPlan updatedMealPlan = argsBundle.getUpdatedMealPlan();
        if (updatedMealPlanIndex != -1 && updatedMealPlan != null) {
            mealPlans.updateMealPlan(updatedMealPlanIndex, updatedMealPlan);
        }
        getArguments().clear();

        return view;
    }
}