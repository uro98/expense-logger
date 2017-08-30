package com.yujotseng.expenselogger;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class NewEntryFragment extends Fragment {
    private static final String TAG = "NewEntryFragment";
    
    private View view;
    private Button saveButton;
    private Button expenseDateInputButton;
    private EditText expenseNameInput;
    private TextView expenseDateInput;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatabaseHandler databaseHandler;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_entry_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        // Get UI
        expenseNameInput = (EditText) view.findViewById(R.id.expenseNameInput);
        expenseDateInput = (TextView) view.findViewById(R.id.expenseDateInput);
        expenseDateInputButton = (Button) view.findViewById(R.id.expenseDateInputButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        // Get HomeFragment
        fragment = getFragmentManager().findFragmentByTag("HomeFragment");
        if(fragment == null) {
            fragment = new HomeFragment();
        }

        // Set buttons onClickListener
        expenseDateInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                // Get current year, month and day
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

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

        // Set date listener
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++; // since MONTH starts from 0
                String date = day + "/" + month + "/" + year;
                expenseDateInput.setText(date);
            }
        };

        return view;
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void saveButtonClicked() {
        if (expenseNameInput.length() != 0) {
            Expense expense = new Expense(expenseNameInput.getText().toString(), expenseDateInput.getText().toString());
            databaseHandler.addExpense(expense);
        } else {
            Toast.makeText(getActivity(),"You must put something in the text field!", Toast.LENGTH_SHORT).show();
        }
    }
}
