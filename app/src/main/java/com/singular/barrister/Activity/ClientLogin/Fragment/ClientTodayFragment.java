package com.singular.barrister.Activity.ClientLogin.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singular.barrister.Activity.LandingScreen;
import com.singular.barrister.Activity.SubActivity.HearingDateActivity;
import com.singular.barrister.Adapter.TodaysCaseAdapter;
import com.singular.barrister.Custom.RecyclerViewPager;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Today.Query.TodayCaseQuery;
import com.singular.barrister.DisplayCaseActivity;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Model.Today.TodayResponse;
import com.singular.barrister.Preferance.UserPreferance;
import com.singular.barrister.R;
import com.singular.barrister.RetrofitManager.RetrofitManager;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;
import com.singular.barrister.Util.NetworkConnection;
import com.singular.barrister.Util.WebServiceError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul.kalamkar on 12/11/2017.
 */

public class ClientTodayFragment extends Fragment implements IDataChangeListener<IModel> {

    protected RecyclerViewPager mCustomRecyclerView;
    FrameLayout frameLayout;
    private ProgressBar progressBar;
    TextView errorTextView;
    private RetrofitManager retrofitManager;
    ArrayList<Case> caseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout = (FrameLayout) getView().findViewById(R.id.frameLayout1);
        mCustomRecyclerView = (RecyclerViewPager) getView().findViewById(R.id.viewpager);
        initRecycleView();
        progressBar = getView().findViewById(R.id.progressBar);
        errorTextView = getView().findViewById(R.id.textViewErrorText);
        retrofitManager = new RetrofitManager();
        caseList = new ArrayList<Case>();
        getCaseList();
    }

    public void getCaseList() {
        if (new NetworkConnection(getActivity()).isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            retrofitManager.getTodayCases(this, new UserPreferance(getActivity()).getToken());
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChanged() {

    }

    public void initRecycleView() {
        mCustomRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = mCustomRecyclerView.getChildCount();
                int width = mCustomRecyclerView.getChildAt(0).getWidth();
                int padding = (mCustomRecyclerView.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        mCustomRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            }
        });

        mCustomRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mCustomRecyclerView.getChildCount() < 3) {
                    if (mCustomRecyclerView.getChildAt(1) != null) {
                        if (mCustomRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mCustomRecyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mCustomRecyclerView.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mCustomRecyclerView.getChildAt(0) != null) {
                        View v0 = mCustomRecyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mCustomRecyclerView.getChildAt(2) != null) {
                        View v2 = mCustomRecyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }

    @Override
    public void onDataReceived(IModel response) {
        if (response != null && response instanceof TodayResponse) {
            TodayResponse todayResponse = (TodayResponse) response;
            if (todayResponse.getData().getCaseList() != null) {
                caseList.addAll(todayResponse.getData().getCaseList());
                ListAdapter todaysCaseAdapter = new ListAdapter(getActivity(), caseList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                        false);
                mCustomRecyclerView.setLayoutManager(linearLayoutManager);
                mCustomRecyclerView.setAdapter(todaysCaseAdapter);
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (caseList != null && caseList.size() > 0) {
                    TodayCaseQuery caseQuery = new TodayCaseQuery(getActivity());
                    caseQuery.addList(caseList);
                } else {
                    showError();
                }
            } else if (todayResponse.getError() != null && todayResponse.getError().getStatus_code() == 401) {
                Toast.makeText(getActivity(), "Your session is Expired", Toast.LENGTH_SHORT).show();
                new UserPreferance(getActivity()).logOut();
                Intent intent = new Intent(getActivity(), LandingScreen.class);
                startActivity(intent);
                getActivity().finish();
            } else
                showError();
        } else {
            showError();
        }
    }

    public void showError() {
        errorTextView.setText("There is no case for a day!");
        errorTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDataFailed(WebServiceError error) {

    }

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<Case> caseList;

        public ListAdapter(Context context, ArrayList<Case> caseList) {
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

                if (caseList.get(position).getClient() != null) {
                    if (caseList.get(position).getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                        todayViewHolder.txtClientOne.setText(caseList.get(position).getClient().getFirst_name() + " " + caseList.get(position).getClient()
                                .getLast_name() + " VS " + caseList.get(position).getPersons().get(0).getOpp_name());

                    } else {
                        todayViewHolder.txtClientOne.setText(caseList.get(position).getClient().getFirst_name() + " " + caseList.get(position).getClient()
                                .getLast_name() + " VS " + caseList.get(position).getPersons().get(1).getOpp_name());
                    }
                }
            /*     todayViewHolder.txtClientOne.setText(caseList.get(position).getClient().getFirst_name() + " " + caseList.get(position).getClient()
                    .getLast_name());
            if (caseList.get(position).getClient() != null) {
                if (caseList.get(position).getPersons().get(0).getType().equalsIgnoreCase("Client")) {
                    todayViewHolder.txtClientTwo.setText(caseList.get(position).getPersons().get(0).getOpp_name());

                } else {
                    todayViewHolder.txtClientTwo.setText(caseList.get(position).getPersons().get(1).getOpp_name());
                }
            }*/


                if (position % 2 == 0) {
                    if (position % 4 == 0) {
                        todayViewHolder.rltColor.setBackgroundResource(R.drawable.gradient_four);
                    } else
                        todayViewHolder.rltColor.setBackgroundResource(R.drawable.gradient_green);
                } else if (position % 3 == 0) {
                    if (position % 6 == 0) {
                        todayViewHolder.rltColor.setBackgroundResource(R.drawable.circle_five);
                    } else
                        todayViewHolder.rltColor.setBackgroundResource(R.drawable.gradient_blue);
                } else {
                    todayViewHolder.rltColor.setBackgroundResource(R.drawable.gradient_red);
                }

                todayViewHolder.txtNotes.setText("Notes : " + (caseList.get(position).getHearing() != null ? (caseList.get(position).getHearing().getCase_decision() != null ? caseList.get(position).getHearing().getCase_decision() : "") : ""));
                todayViewHolder.txtCNRNumber.setText("CNR : " + caseList.get(position).getCase_cnr_number());

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

            TextView txtClientOne, txtClientTwo, txtCourtName, txtAddress, txtViewHearing, txtCNRNumber, txtNotes;
            ImageView imageViewCall, imageViewMessage, imageViewEmail, imageViewWhatsApp;
            RelativeLayout rltColor;

            public TodayViewHolder(View itemView) {
                super(itemView);
                txtClientTwo = (TextView) itemView.findViewById(R.id.textViewClientTwo);
                txtClientOne = (TextView) itemView.findViewById(R.id.textViewClientOne);
                txtCourtName = (TextView) itemView.findViewById(R.id.textViewCourtName);
                txtAddress = (TextView) itemView.findViewById(R.id.textViewCourtAddress);
                txtViewHearing = (TextView) itemView.findViewById(R.id.txtViewAllHearings);

                rltColor = (RelativeLayout) itemView.findViewById(R.id.colorLayout);

                txtCNRNumber = (TextView) itemView.findViewById(R.id.textViewCNRNumber);
                txtNotes = (TextView) itemView.findViewById(R.id.textViewNotes);

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
            context.startActivity(Intent.createChooser(emailIntent, ""));
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
}
