package com.singular.barrister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.SubActivity.HearingDateActivity;
import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.R;
import com.singular.barrister.Util.NetworkConnection;

import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 12/5/2017.
 */

public class TodaysCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Case> caseList;

    public TodaysCaseAdapter(Context context, ArrayList<Case> caseList) {
        this.context = context;
        this.caseList = caseList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_case_item, parent, false);
        return new TodayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TodayViewHolder) {
            TodayViewHolder todayViewHolder = (TodayViewHolder) holder;
            todayViewHolder.txtClientOne.setText(caseList.get(position).getClient().getFirst_name() + " " + caseList.get(position).getClient()
                    .getLast_name());
            if (caseList.get(position).getClient() != null) {
                if (caseList.get(position).getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                    todayViewHolder.txtClientTwo.setText(caseList.get(position).getPersons().get(0).getOpp_name());

                } else {
                    todayViewHolder.txtClientTwo.setText(caseList.get(position).getPersons().get(1).getOpp_name());
                }
            }

            todayViewHolder.txtCourtName.setText(caseList.get(position).getCourt().getCourt_name());
            todayViewHolder.txtAddress.setText(getAddress(caseList.get(position).getCourt()));
        }
    }

    public String getAddress(CourtData aCaseDetail) {
        String address = "";
        if (aCaseDetail.getSubdistrict() != null && aCaseDetail.getSubdistrict().getName() != null) {
            address = aCaseDetail.getSubdistrict().getName() + ", ";
        }


        if (aCaseDetail.getDistrict() != null && aCaseDetail.getDistrict().getName() != null) {
            address = address + aCaseDetail.getDistrict().getName() + ", ";
        }


        if (aCaseDetail.getState() != null && aCaseDetail.getState().getName() != null) {
            address = address + aCaseDetail.getState().getName();
        }
        return address;
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    public class TodayViewHolder extends RecyclerView.ViewHolder {

        TextView txtClientOne, txtClientTwo, txtCourtName, txtAddress, txtViewHearing;
        ImageView imageViewCall, imageViewMessage, imageViewEmail, imageViewWhatsApp;

        public TodayViewHolder(View itemView) {
            super(itemView);
            txtClientTwo = (TextView) itemView.findViewById(R.id.textViewClientTwo);
            txtClientOne = (TextView) itemView.findViewById(R.id.textViewClientOne);
            txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
            txtAddress = (TextView) itemView.findViewById(R.id.textViewCourtAddress);
            txtViewHearing = (TextView) itemView.findViewById(R.id.txtViewAllHearings);

            imageViewCall = (ImageView) itemView.findViewById(R.id.ic_call);
            imageViewMessage = (ImageView) itemView.findViewById(R.id.ic_messge);
            imageViewEmail = (ImageView) itemView.findViewById(R.id.ic_mail);
            imageViewWhatsApp = (ImageView) itemView.findViewById(R.id.ic_whatsapp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Case", caseList.get(getAdapterPosition()));
                    Intent intent = new Intent(context, DisplayCaseActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            txtViewHearing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Id", caseList.get(getAdapterPosition()).getId());
                    Intent intent1 = new Intent(context, HearingDateActivity.class);
                    intent1.putExtras(bundle);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            });

            imageViewCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = "";
                    phone = caseList.get(getAdapterPosition()).getClient().getCountry_code() + "" + caseList.get(getAdapterPosition()).getClient().getMobile();
                    if (!TextUtils.isEmpty(phone)) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            imageViewMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = "";
                    phone = caseList.get(getAdapterPosition()).getClient().getCountry_code() + "" + caseList.get(getAdapterPosition()).getClient().getMobile();
                    sendSMS(phone);
                }
            });

            imageViewWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = "";
                    phone = caseList.get(getAdapterPosition()).getClient().getMobile();
                    openWhatsApp(phone);
                }
            });

            imageViewEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (caseList.get(getAdapterPosition()).getClient().getEmail() != null) {
                        sendEmail(caseList.get(getAdapterPosition()).getClient().getEmail());
                    } else
                        sendEmail("");
                }
            });
        }
    }
    public void sendEmail(String emailId) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailId, null));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(Intent.createChooser(emailIntent,""));
    }

    private void openWhatsApp(String phone) {
        String smsNumber = phone;
        try {
            Uri uri = Uri.parse("smsto:" + smsNumber);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("sms_body", smsNumber);
            i.setPackage("com.whatsapp");
            context.startActivity(i);
        } catch (Exception e) {
            Toast.makeText(context, "Whats app not found", Toast.LENGTH_SHORT).show();
        }

    }
    public void sendSMS(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {

                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(number)));
                sendIntent.setType("text/plain");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                context.startActivity(sendIntent);

            } catch (Exception e) {
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                context.startActivity(sendIntent);
            }
        } else {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("address", number);
            context.startActivity(smsIntent);
        }
    }
}
