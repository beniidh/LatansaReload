package com.c.dompetabata.Fragment.RiwayatTransaksi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c.dompetabata.Helper.utils;
import com.c.dompetabata.R;
import com.c.dompetabata.menuUtama.PaketData.PulsaPrabayar.TransaksiPending;

import java.util.ArrayList;

public class AdapterFragmentSaldoku extends RecyclerView.Adapter<AdapterFragmentSaldoku.ViewHolder> {
    Context context;
    ArrayList<ResponTransaksi.DataTransaksi> mData;


    public AdapterFragmentSaldoku(Context context, ArrayList<ResponTransaksi.DataTransaksi> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaksiku, parent, false);
        AdapterFragmentSaldoku.ViewHolder holder = new AdapterFragmentSaldoku.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResponTransaksi.DataTransaksi dataTransaksi = mData.get(position);
        holder.status.setText(dataTransaksi.getStatus());
        holder.harga.setText(dataTransaksi.getTotal_price());
        holder.transaksi.setText(dataTransaksi.getId());
        String tanggal = dataTransaksi.getUpdated_at();
        String tahun = tanggal.substring(0,4);
        String bulan = utils.convertBulan(tanggal.substring(5,7));
        String hari = tanggal.substring(8,10);
        holder.tanggal.setText(hari+" "+bulan+" "+tahun);
        holder.produk.setText(dataTransaksi.getProduct_name());

        holder.linearklik.setOnClickListener(v -> {


            Intent intent = new Intent(context, TransaksiPending.class);
            Bundle extras = new Bundle();
            extras.putString("transaksid",dataTransaksi.getId());
            extras.putString("code","saldo");
            intent.putExtras(extras);
            context.startActivity(intent);


        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView transaksi, tanggal, produk,harga,status;
        LinearLayout linearklik;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transaksi = itemView.findViewById(R.id.TransaksiFSSS);
            tanggal = itemView.findViewById(R.id.TanggalFSSS);
            harga = itemView.findViewById(R.id.HargaFSSS);
            linearklik = itemView.findViewById(R.id.lintesstS);
            produk = itemView.findViewById(R.id.ProdukFSSS);
            status = itemView.findViewById(R.id.StatusFSSS);

        }
    }
}
