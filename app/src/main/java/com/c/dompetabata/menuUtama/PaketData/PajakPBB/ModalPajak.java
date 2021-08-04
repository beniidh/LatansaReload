package com.c.dompetabata.menuUtama.PaketData.PajakPBB;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c.dompetabata.Api.Api;
import com.c.dompetabata.Helper.RetroClient;
import com.c.dompetabata.R;
import com.c.dompetabata.menuUtama.PaketData.AngsuranKredit.AdapterAngsuran;
import com.c.dompetabata.menuUtama.PaketData.AngsuranKredit.ModelAngsuran;
import com.c.dompetabata.menuUtama.PaketData.TV.AdapterTV;
import com.c.dompetabata.menuUtama.PaketData.TV.ResponTV;
import com.c.dompetabata.sharePreference.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModalPajak extends BottomSheetDialogFragment {

    RecyclerView recyclerView;
    AdapterPajak adapterPajak;
    ArrayList<ModelPajak> modelPajaks = new ArrayList<>();
    Button pilih,tutup;
    SearchView search;

    private BottomSheetListenerProduksms bottomSheetListenerProduksms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.modal_layout_pajak, container, false);

        recyclerView = v.findViewById(R.id.ReyPajak);
        adapterPajak = new AdapterPajak(getContext(), modelPajaks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterPajak);

        String idd = getArguments().getString("id");
        getProduk(idd);

        pilih = v.findViewById(R.id.pilihPajak);
        tutup = v.findViewById(R.id.tutupPajak);

        pilih.setOnClickListener(v1 -> {


            String nameid[][] = adapterPajak.getNameid();

            String namee = nameid[0][0];
            String id = nameid[0][1];

            bottomSheetListenerProduksms.onButtonClick(namee,id);
            dismiss();
        });

        tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        search = v.findViewById(R.id.search_pajak);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.onActionViewExpanded();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapterPajak.getFilter().filter(newText);
                return false;
            }
        });




        return v;
    }

    public interface BottomSheetListenerProduksms {
        void onButtonClick(String name, String id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetListenerProduksms = (ModalPajak.BottomSheetListenerProduksms) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement bottomsheet Listener");
        }
    }

    private void getProduk(String id) {

        String token = "Bearer " + Preference.getToken(getContext());
        Api api = RetroClient.getApiServices();
        Call<ResponPajak> call = api.getProdukPajak(token, id);
        call.enqueue(new Callback<ResponPajak>() {
            @Override
            public void onResponse(Call<ResponPajak> call, Response<ResponPajak> response) {

                modelPajaks = response.body().getData();
                adapterPajak = new AdapterPajak(getContext(), modelPajaks);
                recyclerView.setAdapter(adapterPajak);

            }

            @Override
            public void onFailure(Call<ResponPajak> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
