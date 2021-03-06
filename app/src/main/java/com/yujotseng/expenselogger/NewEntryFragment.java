package com.yujotseng.expenselogger;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class NewEntryFragment extends Fragment {
    private static final String TAG = "NewEntryFragment";

    public static final String DATE = "_date";
    
    private View view;
    private TextView expenseCategoryInput;
    private EditText expenseAmountInput;
    private TextView expenseDateInput;
    private EditText expenseNoteInput;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatabaseHandler databaseHandler;
    private Fragment fragment;
    //private ListView categoryListView;
    private AlertDialog.Builder showBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_entry_layout, container, false);

        // Instantiate database
        databaseHandler = DatabaseHandler.getInstance(getActivity());

        // Get UI
        expenseCategoryInput = (TextView) view.findViewById(R.id.expenseCategoryInput);
        expenseAmountInput = (EditText) view.findViewById(R.id.expenseAmountInput);
        expenseDateInput = (TextView) view.findViewById(R.id.expenseDateInput);
        expenseNoteInput = (EditText) view.findViewById(R.id.expenseNoteInput);
        Button saveButton = (Button) view.findViewById(R.id.saveButton);

        // Get HomeFragment
        fragment = getFragmentManager().findFragmentByTag("HomeFragment");
        if(fragment == null) {
            fragment = new HomeFragment();
        }

        // Set onClickListeners
        expenseCategoryInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });

        expenseDateInput.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String calendarDate = bundle.getString(DATE);
            expenseDateInput.setText(calendarDate);
        } else {
            expenseDateInput.setText((new HomeFragment()).getTodayDate());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    private void saveButtonClicked() {
        // If category and amount is not empty
        if (expenseCategoryInput.getText().toString().length() != 0 && expenseAmountInput.length() != 0) {
            double expenseAmountInputDouble = Double.parseDouble(expenseAmountInput.getText().toString());
            double expenseAmountInputRounded = Math.round(expenseAmountInputDouble * 100.0) / 100.0;        // Round to 2 decimal places
            long expenseAmountInputInCents = (long) (expenseAmountInputRounded * 100);                      // Store amount in cents
            Expense expense = new Expense(
                    expenseCategoryInput.getText().toString(),
                    expenseAmountInputInCents,
                    expenseDateInput.getText().toString(),
                    expenseNoteInput.getText().toString());
            databaseHandler.addExpense(expense);
        } else {
            Toast.makeText(getActivity(), "You must select a category and enter an amount!", Toast.LENGTH_LONG).show();
        }
    }

    private void showCategoryDialog() {
        showBuilder = new AlertDialog.Builder(getActivity());

        showBuilder.setTitle("Select a Category");
        showBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });
        showBuilder.setPositiveButton("+ New Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Create another dialog
                AlertDialog.Builder newBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater newLayoutInflater = getActivity().getLayoutInflater();
                View newCategoryView = newLayoutInflater.inflate(R.layout.new_category_dialog_layout, null);
                newBuilder.setView(newCategoryView);

                final EditText newCategoryInput = (EditText) newCategoryView.findViewById(R.id.newCategoryInput);

                newBuilder.setTitle("Add New Category");
                newBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Make sure category is not empty, add category and return
                        if (newCategoryInput.getText().toString().length() != 0) {
                            databaseHandler.addCategory(newCategoryInput.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), "Please enter a category name!", Toast.LENGTH_LONG).show();
                        }
                        dialogInterface.dismiss();
                    }
                });
                newBuilder.show();
            }
        });

        populateCategoryListView();

        showBuilder.show();
    }

    private void populateCategoryListView() {
        // Get categories
        Cursor cursor = databaseHandler.getCategory();

        // Store categories in an ArrayList
        ArrayList<String> categoryArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(0);
                categoryArrayList.add(categoryName);
            } while (cursor.moveToNext());
        }

        // Create and set ArrayAdapter
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, categoryArrayList);
        showBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String categoryName = arrayAdapter.getItem(i);
                expenseCategoryInput.setText(categoryName);
                dialogInterface.dismiss();
            }
        });
    }
}
