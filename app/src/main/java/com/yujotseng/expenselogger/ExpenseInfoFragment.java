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
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseInfoFragment extends Fragment {
    private static final String TAG = "ExpenseInfoFragment";

    public final static String EXPENSE_ID = "id";
    public final static String EXPENSE_NAME = "name";
    
    private View view;
    private Button editButton;
    private Button deleteButton;
    private TextView expenseNameDetail;
    private DatabaseHandler databaseHandler;
    private Fragment fragment;
    private long _id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expense_info_layout, container, false);

        // Instantiate database
        databaseHandler = new DatabaseHandler(getActivity(), null, null, 1);

        editButton = (Button) view.findViewById(R.id.editButton);
        deleteButton = (Button) view.findViewById(R.id.deleteButton);

        expenseNameDetail = (TextView) view.findViewById(R.id.expenseNameDetail);

        fragment = getFragmentManager().findFragmentByTag("HomeFragment");

        if(fragment == null) {
            fragment = new HomeFragment();
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete expense and return to HomeFragment
                deleteButtonClicked();
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
            expenseNameDetail.setText(bundle.getString(EXPENSE_NAME));
            _id = bundle.getLong(EXPENSE_ID);
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
