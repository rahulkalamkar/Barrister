package com.singular.barrister.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.singular.barrister.R;

/**
 * Created by rahul.kalamkar on 11/27/2017.
 */

public class ImportantLinkFragment extends Fragment {

    public TextInputLayout txtILWebNameError, txtILWebsiteError;
    public TextInputEditText edtWebName, edtWebSite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.important_link, container, false);
    }

    public ImportantLinkFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtILWebNameError = (TextInputLayout) getView().findViewById(R.id.editTextWebSiteNameError);
        txtILWebsiteError = (TextInputLayout) getView().findViewById(R.id.editTextWebSiteUrlError);

        edtWebName = (TextInputEditText) getView().findViewById(R.id.editTextWebSiteName);
        edtWebSite = (TextInputEditText) getView().findViewById(R.id.editTextWebSiteUrl);
    }

    public boolean checkValues() {
        if (TextUtils.isEmpty(edtWebName.getText().toString())) {
            txtILWebNameError.setError("enter website name");
            txtILWebsiteError.setError(null);
            return false;
        } else if (TextUtils.isEmpty(edtWebSite.getText().toString())) {
            txtILWebNameError.setError(null);
            txtILWebsiteError.setError("enter website url");
            return false;
        } else {
            return true;
        }
    }
}
