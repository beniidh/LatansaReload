package com.c.dompetabata.Transfer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.c.dompetabata.Api.Api;
import com.c.dompetabata.Api.Value;
import com.c.dompetabata.Helper.GpsTracker;
import com.c.dompetabata.Helper.RetroClient;
import com.c.dompetabata.Helper.utils;
import com.c.dompetabata.R;
import com.c.dompetabata.TopUpSaldoku.BayarSales;
import com.c.dompetabata.TopUpSaldoku.ReqSaldoku;
import com.c.dompetabata.TopUpSaldoku.topup_saldoku_activity;
import com.c.dompetabata.sharePreference.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.muddzdev.styleabletoast.StyleableToast;
import com.oakkub.android.PinEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModalPinTransfer extends BottomSheetDialogFragment {

    GpsTracker gpsTracker;
    PinEditText pinpengajuan;
    TextView idCancelPengajuan;
    Button idPinPengajuanButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.modal_pintransfer, container, false);
        getLocation();

        pinpengajuan = v.findViewById(R.id.pinpengajuantransfer);
        idCancelPengajuan = v.findViewById(R.id.idCancelPengajuantransfer);
        idPinPengajuanButton = v.findViewById(R.id.idPinPengajuanButtontransfer);
        idCancelPengajuan.setOnClickListener(v12 -> dismiss());
        idPinPengajuanButton.setOnClickListener(v1 -> {

            if (pinpengajuan.length() < 6) {
                StyleableToast.makeText(getContext(), "Lengkapi PIN", Toast.LENGTH_SHORT, R.style.mytoast2).show();

            } else {

                String pinn = utils.hmacSha(Objects.requireNonNull(pinpengajuan.getText()).toString());
                String jumlahtransfer = getArguments().getString("nominalkonter");
                double pengajuan = Double.parseDouble(jumlahtransfer);
                sendTransfer(pinn, Value.getMacAddress(getContext()), getIPaddress(), getUserAgent(), gpsTracker.getLatitude(), gpsTracker.getLongitude(), pengajuan);
            }
        });

        return v;

    }

    private void sendTransfer(String pin, String mac_address, String ip_address, String user_agent, double latitude, double longitude, double amount) {

        Api api = RetroClient.getApiServices();
        String token = "Bearer " + Preference.getToken(getContext());
        String konterid = getArguments().getString("idkonter");
        Mtransfer mtransfer = new Mtransfer(mac_address, ip_address, user_agent, pin,konterid,latitude,longitude,amount);

        Call<Mtransfer> call = api.sendsaldoku(token, mtransfer);
        call.enqueue(new Callback<Mtransfer>() {
            @Override
            public void onResponse(Call<Mtransfer> call, Response<Mtransfer> response) {

                String code = response.body().getCode();

                if (code.equals("200")) {
                    StyleableToast.makeText(getContext(),"Pengiriman Berhasil",Toast.LENGTH_SHORT,R.style.mytoast).show();
                    transferKonter.transfer.finish();
                    dismiss();

                } else {
                    StyleableToast.makeText(getContext(), response.body().getError(), Toast.LENGTH_SHORT, R.style.mytoast2).show();
                }

            }

            @Override
            public void onFailure(Call<Mtransfer> call, Throwable t) {
                StyleableToast.makeText(getContext(), "Periksa Sambungan internet", Toast.LENGTH_SHORT, R.style.mytoast2).show();

            }
        });

    }


    private String getUserAgent() {

        String ua = new WebView(getContext()).getSettings().getUserAgentString();
        return ua;
    }

    private String getIPaddress() {

        String IP = utils.getIPAddress(true);
        return IP;
    }


    public void getLocation() {
        gpsTracker = new GpsTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

        } else {
            gpsTracker.showSettingsAlert();
        }
    }



}
