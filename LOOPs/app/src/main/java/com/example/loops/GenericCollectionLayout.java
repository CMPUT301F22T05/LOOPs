package com.example.loops;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * This class provides a generic layout to all collection fragments.
 */
public abstract class GenericCollectionLayout extends Fragment {
    //protected TextView collectionTitle;
    protected ListView collectionView;
    protected Spinner sortOptionSpinner;
    protected ImageButton sortOrderButton;
    protected boolean isAscendingOrder = true;

    /**
     * Initialize all the components in the generic collection.
     * @param view (View)
     */
    protected void bindComponents(View view) {
        //collectionTitle = view.findViewById(R.id.generic_collection_title);
        collectionView = view.findViewById(R.id.generic_collection_view);
        sortOptionSpinner = view.findViewById(R.id.sort_option_spinner);
        sortOrderButton = view.findViewById(R.id.sort_order_button);

        //toggle between ascending or descending
        sortOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAscendingOrder) {
                    sortOrderButton.setImageDrawable(ContextCompat.
                            getDrawable(getActivity(), android.R.drawable.arrow_down_float));
                    isAscendingOrder = false;
                    sortCollection(sortOptionSpinner);
                }
                else {
                    sortOrderButton.setImageDrawable(ContextCompat.
                            getDrawable(getActivity(), android.R.drawable.arrow_up_float));
                    isAscendingOrder = true;
                    sortCollection(sortOptionSpinner);
                }
            }
        });
    }

    abstract protected void sortCollection(AdapterView<?> parent);
}
