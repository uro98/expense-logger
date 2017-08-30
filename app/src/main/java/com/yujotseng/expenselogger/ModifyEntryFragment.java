package com.yujotseng.expenselogger;

import android.app.DatePickerDialog;
import android.database.Cursor;
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

public class ModifyEntryFragment extends Fragment {
    private static final String TAG = "NewEntryFragment";

    public final static String EXPENSE_ID = "id";
    
    private View view;
    private Button expenseDateUpdateInputButton;
    private Button saveUpdateButton;
    private Button cancelButton;
    private EditText expenseNameUpdateInput;
    private TextView expenseDateUpdateInput;
    private EditText expenseNoteUpdateInput;
    private DatabaseHandler databaseHandler;
    private Fragment fragment;
    private long _id;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_entry_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        // Get UI
        expenseNameUpdateInput = (EditText) view.findViewById(R.id.expenseNameUpdateInput);
        expenseDateUpdateInput = (TextView)  view.findViewById(R.id.expenseDateUpdateInput);
        expenseDateUpdateInputButton = (Button) view.findViewById(R.id.expenseDateUpdateInputButton);
        expenseNoteUpdateInput = (EditText) view.findViewById(R.id.expenseNoteUpdateInput);
        saveUpdateButton = (Button) view.findViewById(R.id.saveUpdateButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        // Get HomeFragment
        fragment = getFragmentManager().findFragmentByTag("HomeFragment");
        if(fragment == null) {
            fragment = new HomeFragment();
        }

        // Set buttons onClickListeners
        expenseDateUpdateInputButton.setOnClickListener(new View.OnClickListener() {
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

        // Set date listener
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++; // since MONTH starts from 0
                String date = day + "/" + month + "/" + year;
                expenseDateUpdateInput.setText(date);
            }
        };

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
            int dateIndex = cursor.getColumnIndex("_date");
            String date = cursor.getString(dateIndex);
            int noteIndex = cursor.getColumnIndex("_note");
            String note;
            if (cursor.isNull(noteIndex)) {
                note = "";
            } else {
                note = cursor.getString(noteIndex);
            }

            expenseNameUpdateInput.setText(name);               // Populate EditViews with properties
            expenseDateUpdateInput.setText(date);
            expenseNoteUpdateInput.setText(note);
        }
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void saveUpdateButtonClicked() {
        if (expenseNameUpdateInput.length() != 0) {
            databaseHandler.updateExpense(
                    _id,
                    expenseNameUpdateInput.getText().toString(),
                    expenseDateUpdateInput.getText().toString(),
                    expenseNoteUpdateInput.getText().toString());
        } else {
            Toast.makeText(getActivity(),"You must put something in the text field!", Toast.LENGTH_SHORT).show();
        }
    }
}
