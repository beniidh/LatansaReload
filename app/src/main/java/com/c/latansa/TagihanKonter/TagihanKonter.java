package com.c.latansa.TagihanKonter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;

import com.c.latansa.Api.Api;
import com.c.latansa.Helper.RetroClient;
import com.c.latansa.R;
import com.c.latansa.sharePreference.Preference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagihanKonter extends AppCompatActivity {

RecyclerView recyclerView;
ArrayList<ResponTagihanKonter.mData> mdata = new ArrayList<>();
AdapterTagihanKonter adapterTagihanKonter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan_konter);
        String color = Integer.toHexString(getResources().getColor(R.color.green, null)).toUpperCase();
        String color2 = "#" + color.substring(1);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='" + color2 +"'><b>Tagihan Konter <b></font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        recyclerView = findViewById(R.id.ReyTagihanKonter);

        adapterTagihanKonter = new AdapterTagihanKonter(getApplicationContext(), mdata);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterTagihanKonter);
        getDataTagihan();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataTagihan();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getDataTagihan(){

        String token = "Bearer "+ Preference.getToken(getApplicationContext());
        Api api = RetroClient.getApiServices();
        Call<ResponTagihanKonter> call = api.getTagihanSales(token);
        call.enqueue(new Callback<ResponTagihanKonter>() {
            @Override
            public void onResponse(Call<ResponTagihanKonter> call, Response<ResponTagihanKonter> response) {

                String code = response.body().getCode();
                if(code.equals("200")){

                    mdata = response.body().getData();
                    adapterTagihanKonter = new AdapterTagihanKonter(getApplicationContext(),mdata);
                    recyclerView.setAdapter(adapterTagihanKonter);
                }

            }

            @Override
            public void onFailure(Call<ResponTagihanKonter> call, Throwable t) {

            }
        });

    }
}