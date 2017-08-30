package com.yujotseng.expenselogger;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyEntryFragment extends Fragment {
    private static final String TAG = "NewEntryFragment";

    public final static String EXPENSE_ID = "id";
    
    private View view;
    private Button saveUpdateButton;
    private Button cancelButton;
    private EditText expenseNameUpdateInput;
    private DatabaseHandler databaseHandler;
    private Fragment fragment;
    private long _id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_entry_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        // Get UI
        expenseNameUpdateInput = (EditText) view.findViewById(R.id.expenseNameUpdateInput);
        saveUpdateButton = (Button) view.findViewById(R.id.saveUpdateButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        // Get HomeFragment
        fragment = getFragmentManager().findFragmentByTag("HomeFragment");
        if(fragment == null) {
            fragment = new HomeFragment();
        }

        // Set buttons onClickListeners
        saveUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update expense and return to HomeFragment
                saveUpdateButtonClicked();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return to HomeFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            _id = bundle.getLong(EXPENSE_ID);                   // Get ID
            Cursor cursor = databaseHandler.getExpense(_id);    // Get cursor from ID
            int nameIndex = cursor.getColumnIndex("_name");     // Get expense properties from ID
            String name = cursor.getString(nameIndex);
            expenseNameUpdateInput.setText(name);               // Populate EditViews with properties
        }
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void saveUpdateButtonClicked() {
        if (expenseNameUpdateInput.length() != 0) {
            databaseHandler.updateExpense(_id, expenseNameUpdateInput.getText().toString());
        } else {
            Toast.makeText(getActivity(),"You must put something in the text field!", Toast.LENGTH_SHORT).show();
        }
    }
}
