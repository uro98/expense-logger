package com.yujotseng.expenselogger;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class ExpenseInfoFragment extends Fragment {
    private static final String TAG = "ExpenseInfoFragment";

    public final static String EXPENSE_ID = "_id";
    
    private View view;
    private TextView expenseCategoryDetail;
    private TextView expenseAmountDetail;
    private TextView expenseDateDetail;
    private TextView expenseNoteDetail;
    private DatabaseHandler databaseHandler;
    private Fragment homeFragment;
    private Fragment modifyEntryFragment;
    private long _id;
    private HomeFragment.PassDataListener callback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expense_info_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity());

        // Get UI
        Button editButton = view.findViewById(R.id.editButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        expenseCategoryDetail = view.findViewById(R.id.expenseCategoryDetail);
        expenseAmountDetail = view.findViewById(R.id.expenseAmountDetail);
        expenseDateDetail = view.findViewById(R.id.expenseDateDetail);
        expenseNoteDetail = view.findViewById(R.id.expenseNoteDetail);

        // Set UI
        expenseNoteDetail.setMovementMethod(new ScrollingMovementMethod());

        // Get Fragments
        homeFragment = getFragmentManager().findFragmentByTag("HomeFragment");
        if(homeFragment == null) {
            homeFragment = new HomeFragment();
        }

        modifyEntryFragment = getFragmentManager().findFragmentByTag("ModifyEntryFragment");
        if(modifyEntryFragment == null) {
            modifyEntryFragment = new ModifyEntryFragment();
        }

        // Set buttons onClickListeners
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete expense and return to HomeFragment
                deleteButtonClicked();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, homeFragment);
                transaction.commit();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.passID(_id, false);
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

            // Get expense properties from ID
            int categoryIndex = cursor.getColumnIndex("_category");
            String category = cursor.getString(categoryIndex);
            int dateIndex = cursor.getColumnIndex("_date");
            String date = cursor.getString(dateIndex);
            int noteIndex = cursor.getColumnIndex("_note");
            String note;
            if (cursor.isNull(noteIndex)) {
                note = "";
            } else {
                note = cursor.getString(noteIndex);
            }
            int amountIndex = cursor.getColumnIndex("_amount");
            long amountInCents = cursor.getLong(amountIndex);
            double amountModified = (double) amountInCents / 100;
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            String amount = numberFormat.format(amountModified);

            // Populate TextViews with properties
            expenseCategoryDetail.setText(category);
            expenseAmountDetail.setText(amount);
            expenseDateDetail.setText(date);
            expenseNoteDetail.setText(note);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure host activity implements PassDataListener interface, otherwise throw exception
        try {
            callback = (HomeFragment.PassDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PassDataListener");
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void deleteButtonClicked() {
        databaseHandler.deleteExpense(_id);
    }
}
