package com.example.loops;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * This class provides a generic layout to all collection fragments.
 */
public abstract class GenericCollectionLayout extends Fragment {
    protected TextView collectionTitle;
    protected Button addButton;
    protected ListView collectionView;
    protected Spinner sortOptionSpinner;

    /**
     * Initialize all the components in the generic collection.
     * @param view (View)
     */
    protected void bindComponents(View view) {
        collectionTitle = view.findViewById(R.id.generic_collection_title);
        addButton = view.findViewById(R.id.add_item_to_collection_btn);
        collectionView = view.findViewById(R.id.generic_collection_view);
        sortOptionSpinner = view.findViewById(R.id.sort_option_spinner);
    }
}
