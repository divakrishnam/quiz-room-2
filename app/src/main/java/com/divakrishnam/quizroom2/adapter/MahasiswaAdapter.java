package com.divakrishnam.quizroom2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.divakrishnam.quizroom2.R;
import com.divakrishnam.quizroom2.activity.DetailActivity;
import com.divakrishnam.quizroom2.activity.FormActivity;
import com.divakrishnam.quizroom2.db.DatabaseClient;
import com.divakrishnam.quizroom2.db.MahasiswaDatabase;
import com.divakrishnam.quizroom2.entity.Mahasiswa;
import com.divakrishnam.quizroom2.util.SharedPrefManager;

import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>{

    private List<Mahasiswa> mList;
    private Context mContext;

    public MahasiswaAdapter(List<Mahasiswa> list, Context context) {
        mList = list;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa, parent, false);
        MahasiswaViewHolder mViewHolder = new MahasiswaViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, final int position) {
        final Mahasiswa mahasiswa = mList.get(position);

        holder.tvNpm.setText(mahasiswa.getNpm());
        holder.tvNama.setText(mahasiswa.getNamaDepan()+" "+mahasiswa.getNamaBelakang());
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.DATA_MAHASISWA, mahasiswa);
                mContext.startActivity(intent);
            }
        });
        holder.cvItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return menuDialog(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MahasiswaViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNpm, tvNama;
        private CardView cvItem;

        MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cv_item);
            tvNpm = itemView.findViewById(R.id.tv_npm);
            tvNama = itemView.findViewById(R.id.tv_nama);
        }
    }

    private boolean menuDialog(View view, final int position){
        CharSequence[] menu = {"Edit", "Delete"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Pilih Aksi")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                editData(position);
                                break;
                            case 1:
                                if (SharedPrefManager.getInstance(mContext).getUserId() == mList.get(position).getUserId()){
                                    Toast.makeText(mContext, "Tidak bisa menghapus data yang sedang dipakai", Toast.LENGTH_SHORT).show();
                                }else{
                                    deleteData(position);
                                }
                        }
                    }
                });
        dialog.create();
        dialog.show();
        return true;
    }

    private void editData(int position) {
        Intent intent = new Intent(mContext, FormActivity.class);
        intent.putExtra(FormActivity.DATA_MAHASISWA, mList.get(position));
        mContext.startActivity(intent);
    }

    private void deleteData(int position) {
        delete(position);
        Toast.makeText(mContext, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")
    private void delete(final int position){
        final MahasiswaDatabase db = DatabaseClient.getInstance(mContext).getMahasiswaDatabase();
        new AsyncTask<Void, Void, Integer>(){
            @Override
            protected Integer doInBackground(Void... voids) {
                return db.mahasiswaDao().delete(mList.get(position));
            }

            @Override
            protected void onPostExecute(Integer status) {
                if(status > 0){
                    Toast.makeText(mContext, "Data terhapus", Toast.LENGTH_SHORT).show();
                    mList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mList.size());
                }else{
                    Toast.makeText(mContext, "Data gagal terhapus", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

}
