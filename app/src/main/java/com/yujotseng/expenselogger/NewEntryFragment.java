package com.yujotseng.expenselogger;

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

public class NewEntryFragment extends Fragment {
    private static final String TAG = "NewEntryFragment";
    
    private View view;
    private Button saveButton;
    private EditText expenseName;
    private DatabaseHandler databaseHandler;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_entry_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        saveButton = (Button) view.findViewById(R.id.saveButton);

        expenseName = (EditText) view.findViewById(R.id.expenseName);

        fragment = getFragmentManager().findFragmentByTag("HomeFragment");

        if(fragment == null) {
            fragment = new HomeFragment();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add expense to database and return to HomeFragment
                saveButtonClicked();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();
            }
        });

        return view;
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void saveButtonClicked() {
        if (expenseName.length() != 0) {
            Expense expense = new Expense(expenseName.getText().toString());
            if (expense != null) {
                databaseHandler.addExpense(expense);
            } else {
                Toast.makeText(getActivity(),"Object null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(),"You must put something in the text field!", Toast.LENGTH_SHORT).show();
        }
    }
}
