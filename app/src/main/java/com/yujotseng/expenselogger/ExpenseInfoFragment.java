package com.yujotseng.expenselogger;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseInfoFragment extends Fragment {
    private static final String TAG = "ExpenseInfoFragment";

    public final static String EXPENSE_ID = "_id";
    
    private View view;
    private TextView expenseNameDetail;
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
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        // Get UI
        Button editButton = view.findViewById(R.id.editButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        expenseNameDetail = view.findViewById(R.id.expenseNameDetail);
        expenseDateDetail = view.findViewById(R.id.expenseDateDetail);
        expenseNoteDetail = view.findViewById(R.id.expenseNoteDetail);

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
                callback.passData(_id, false);
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
            int dateIndex = cursor.getColumnIndex("_date");
            String date = cursor.getString(dateIndex);
            int noteIndex = cursor.getColumnIndex("_note");
            String note;
            if (cursor.isNull(noteIndex)) {
                note = "";
            } else {
                note = cursor.getString(noteIndex);
            }

            expenseNameDetail.setText(name);                    // Populate TextViews with properties
            expenseDateDetail.setText(date);
            expenseNoteDetail.setText(note);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure host activity implements OnListItemSelectedListener interface, otherwise throw exception
        try {
            callback = (HomeFragment.PassDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListItemSelectedListener");
        }
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        view = null;
    }

    private void deleteButtonClicked() {
        databaseHandler.deleteExpense(_id);
    }
}
