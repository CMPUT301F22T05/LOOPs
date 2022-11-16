package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.loops.adapters.MealPlansViewAdapter;
import com.example.loops.modelCollections.MealPlanCollection;

public class MealPlanHomePageFragment extends Fragment {
    private MealPlanCollection mealPlans;
    private MealPlansViewAdapter viewAdapter;
    //private ListView mealPlansView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealPlans = ((MainActivity)getActivity()).getMealPlans();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan_home_page, container, false);
        viewAdapter = new MealPlansViewAdapter(getActivity(), mealPlans.getMealPlans());
        ListView mealPlansView = view.findViewById(R.id.meal_plan_collection);
        mealPlansView.setAdapter(viewAdapter);
        return view;
    }
}