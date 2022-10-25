package com.example.loops;

import static android.provider.Settings.System.DATE_FORMAT;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFormFragment extends Fragment {
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IngredientFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientFormFragment newInstance(String param1, String param2) {
        IngredientFormFragment fragment = new IngredientFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View formView = inflater.inflate(R.layout.fragment_ingredient_form, container, false);
        EditText dateInput = formView.findViewById(R.id.ingredientFormBestBeforeDateInput);
        bindDatePickerDialogToDateInput(dateInput);
        return formView;
    }

    private void bindDatePickerDialogToDateInput(EditText dateInput) {
        /*
        Android_coder, Datepicker: How to popup datepicker when click on edittext
        https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext,
        2022-09-24, Creative Commons Attribution-ShareAlike license
         */
        DatePickerDialog.OnDateSetListener onDateSetCallback = (view, year, month, day) -> {
            Calendar pickedDate = Calendar.getInstance();
            pickedDate.set(Calendar.YEAR, year);
            pickedDate.set(Calendar.MONTH,month);
            pickedDate.set(Calendar.DAY_OF_MONTH,day);

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            dateInput.setText(dateFormat.format(pickedDate.getTime()));
        };
        dateInput.setOnClickListener( (clickedView) -> {
            Calendar today = Calendar.getInstance();

            new DatePickerDialog(getActivity(), onDateSetCallback,
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }
}