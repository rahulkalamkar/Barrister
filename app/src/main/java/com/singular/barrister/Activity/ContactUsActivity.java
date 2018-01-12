package com.singular.barrister.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singular.barrister.R;
import com.singular.barrister.WebActivity;

public class ContactUsActivity extends AppCompatActivity {

    RelativeLayout email;
    LinearLayout callFirstNumber, callSecondNumber;
    TextView Web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.menu_contact_us));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_contact_us));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        callFirstNumber = (LinearLayout) findViewById(R.id.textViewFirstNumber);
        callSecondNumber = (LinearLayout) findViewById(R.id.textViewSecondNumber);
        email = (RelativeLayout) findViewById(R.id.relativeLayoutEmail);
        Web = (TextView) findViewById(R.id.website);

        Web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeb();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        callSecondNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+918980899345");
            }
        });

        callFirstNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+919724283616");
            }
        });
    }


    public void showWeb() {
        Bundle bundle = new Bundle();
        bundle.putString("Name", "Barrister");
        bundle.putString("Url", "www.barristerdiary.com");
        Intent i = new Intent(getApplicationContext(), WebActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        startActivity(intent);
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "barristerapp@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
