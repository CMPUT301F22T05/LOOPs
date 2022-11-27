package com.example.loops.userPreferencesFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.loops.GenericCollectionLayout;
import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.database.Database;
import com.example.loops.database.UserPreferenceAttribute;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple editor to read, delete, and edit user options like ingredient category,
 * recipe category, and storage locations
 */
public class OptionsEditorFragment extends GenericCollectionLayout {
    private ArrayList<String> options;
    private OptionsType optionsType;

    private ArrayAdapter<String> optionsAdapter;
    private Button addOptionButton;
    private Button saveOptionButton;

    /**
     * Specifies the option to manipulate
     * INGREDIENT_CATEGORY - manipulate user's ingredient categories
     * RECIPE_CATEGORY - manipulate user's recipe categories
     * STORAGE_LOCATION - manipulate user's storage locations
     */
    public enum OptionsType {
        INGREDIENT_CATEGORY,
        RECIPE_CATEGORY,
        STORAGE_LOCATION
    }

//    public OptionsEditorFragment() {
//        // Required empty public constructor
//    }

    /**
     * Makes request to the database for result
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new ArrayList<>();
        optionsType = OptionsEditorFragmentArgs.fromBundle(getArguments())
                .getOptionType();
        switch (optionsType) {
            case INGREDIENT_CATEGORY:
                ((MainActivity)getActivity()).setActionBarTitle("Modify Ingredient Categories");
                Database.getInstance().getUserPreferencesAttribute(
                    UserPreferenceAttribute.IngredientCategory,
                    (result) -> {
                        options.addAll(result);
                        if (optionsAdapter != null)
                            optionsAdapter.notifyDataSetChanged();
                });
                break;
            case RECIPE_CATEGORY:
                ((MainActivity)getActivity()).setActionBarTitle("Modify Recipe Categories");
                Database.getInstance().getUserPreferencesAttribute(
                    UserPreferenceAttribute.RecipeCategory,
                    (result) -> {
                        options.addAll(result);
                        if (optionsAdapter != null)
                            optionsAdapter.notifyDataSetChanged();
                });
                break;
            case STORAGE_LOCATION:
                ((MainActivity)getActivity()).setActionBarTitle("Modify Storage Locations");
                Database.getInstance().getUserPreferencesAttribute(
                        UserPreferenceAttribute.StorageLocation,
                        (result) -> {
                            options.addAll(result);
                            if (optionsAdapter != null)
                                optionsAdapter.notifyDataSetChanged();
                        });
                break;
            default:
                throw new Error("Given option type is not implemented");
        }
    }

    /**
     * Creates the view of the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_options_editor, container, false);
        bindComponents(fragmentView);
        return fragmentView;
    }

    /**
     * Sets the adapter and on click item behavior.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        optionsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, options);
        collectionView.setAdapter(optionsAdapter);
        populateSortSpinnerOptions();

        collectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openOptionPromptForEdit(position);
            }
        });

        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionPromptForEdit(-1);
            }
        });

        saveOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (optionsType) {
                    case INGREDIENT_CATEGORY:
                        Database.getInstance().setUserPreferencesAttribute(
                                UserPreferenceAttribute.IngredientCategory, options
                        );
                        break;
                    case RECIPE_CATEGORY:
                        Database.getInstance().setUserPreferencesAttribute(
                                UserPreferenceAttribute.RecipeCategory, options
                        );
                        break;
                    case STORAGE_LOCATION:
                        Database.getInstance().setUserPreferencesAttribute(
                                UserPreferenceAttribute.StorageLocation, options
                        );
                        break;
                    default:
                        throw new Error("Given option type is not implemented");
                }
                Navigation.findNavController(getView()).popBackStack();
            }
        });
    }

    /**
     * Opens a dialog to prompt for editing, deleting, (or doing nothing) on the option
     * @param position position of item. If -1, it interprets it as adding a new item
     */
    private void openOptionPromptForEdit(int position) {
        String option = "";
        if (position != -1)
            option = optionsAdapter.getItem(position).toString();

        // Create view for the prompt
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View optionEditorView = inflater.inflate(R.layout.dialog_option_editor_prompt, null);
        EditText nameInput = optionEditorView.findViewById(R.id.dialog_option_name_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle( (position == -1 ? "Add Option" : "Edit "+ option ) )
            .setView(optionEditorView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;     // Do nothing on cancel
                }
            });
        if (position != -1) {
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    options.remove(position);
                    optionsAdapter.notifyDataSetChanged();
                }
            });
        }
        AlertDialog optionPrompt = builder.create();

        // We implement the positive button on click listener separately to prevent the
        // dialog closing on button click automatically. We want to control when it closes.
        final String lambdaOption = option;
        optionPrompt.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (position != -1)
                    nameInput.setText(lambdaOption);

                Button button = optionPrompt.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newName = nameInput.getText().toString();
                        if ( newName.trim().equals("") ) {
                            return;
                        }
                        else if ( position == -1 && ! options.contains(newName) ) {
                            options.add(newName);
                            optionsAdapter.notifyDataSetChanged();
                            optionPrompt.dismiss();
                        }
                        else if (position != -1) {
                            options.set(position, newName);
                            optionsAdapter.notifyDataSetChanged();
                            optionPrompt.dismiss();
                        }
                        else {
                            // Error message to user
                        }
                    }
                });
            }
        });
        optionPrompt.show();
    }

    /**
     * Populate the sort spinner with sort options
     */
    private void populateSortSpinnerOptions() {
        ArrayAdapter<CharSequence> sortOptionArrayAdapter;
        sortOptionArrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.user_preferences_sort_option, android.R.layout.simple_spinner_item);
        sortOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptionSpinner.setAdapter(sortOptionArrayAdapter);
    }

    /**
     * Binds the widget compoents of the view to class attributes
     * @param view (View)
     */
    @Override
    protected void bindComponents(View view) {
        super.bindComponents(view);
        addOptionButton = view.findViewById(R.id.add_option_button);
        saveOptionButton = view.findViewById(R.id.save_option_button);
    }

    /**
     * Sort the options
     * @param parent
     */
    protected void sortCollection(AdapterView<?> parent) {
        if (parent.getSelectedItem().toString().equals(getString(R.string.empty_sort_option))) {
            return;
        }
        if (isAscendingOrder) {
            if (parent.getSelectedItem().toString().equals("Alphabetically")) {
                Collections.sort(options);
            }
        }
        else {
            if (parent.getSelectedItem().toString().equals("Alphabetically")) {
                Collections.sort(options);
                Collections.reverse(options);
            }
        }
        optionsAdapter.notifyDataSetChanged();
    }
}