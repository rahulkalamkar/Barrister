package com.singular.barrister.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import org.w3c.dom.Text;

/**
 * Created by rahul.kalamkar on 11/27/2017.
 */

public class EditProfileFragment extends Fragment {

    EditText edtFirstName, edtLastName, edtAddress;
    TextView txtEmailId, txtNumber;
    ProgressBar mProgressBar;
    RetrofitManager retrofitManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtFirstName = (EditText) getView().findViewById(R.id.EditTextFirstName);
        edtLastName = (EditText) getView().findViewById(R.id.EditTextLastName);
        edtAddress = (EditText) getView().findViewById(R.id.EditTextAddress);
        txtEmailId = (TextView) getView().findViewById(R.id.textViewEmailId);
        txtNumber = (TextView) getView().findViewById(R.id.textViewNumber);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBarEdit);

        setDataWithoutNetwork();
    }

    public EditProfileFragment() {
    }

    public void setDataWithoutNetwork() {
        UserPreferance userPreferance = new UserPreferance(getActivity());
        edtFirstName.setText(userPreferance.getFirst_name());
        edtLastName.setText(userPreferance.getLast_name());
        txtNumber.setText(userPreferance.getMobile());
        txtEmailId.setText(userPreferance.getEmail());
        edtAddress.setText(userPreferance.getAddress() != null ? userPreferance.getAddress() : "");
        mProgressBar.setVisibility(View.GONE);
    }

    public String getFirstName() {
        if (!TextUtils.isEmpty(edtFirstName.getText().toString())) {
            edtFirstName.setError(null);
            return edtFirstName.getText().toString();
        } else {
            edtFirstName.setError("Enter first name");
            return "";
        }
    }

    public String getLastName() {
        if (!TextUtils.isEmpty(edtLastName.getText().toString())) {
            edtFirstName.setError(null);
            edtLastName.setError(null);
            return edtLastName.getText().toString();
        } else {
            edtLastName.setError("Enter last name");
            return "";
        }
    }

    public String getAddress() {
        if (TextUtils.isEmpty(edtAddress.getText().toString())) {
            edtFirstName.setError(null);
            edtLastName.setError(null);
            edtAddress.setError("Enter address");
            return "";
        } else {
            edtFirstName.setError(null);
            edtLastName.setError(null);
            edtAddress.setError(null);
            return edtAddress.getText().toString();
        }
    }

}
