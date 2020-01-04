package com.divakrishnam.quizroom2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.divakrishnam.quizroom2.R;
import com.divakrishnam.quizroom2.adapter.MahasiswaAdapter;
import com.divakrishnam.quizroom2.db.DatabaseClient;
import com.divakrishnam.quizroom2.db.MahasiswaDatabase;
import com.divakrishnam.quizroom2.entity.Mahasiswa;
import com.divakrishnam.quizroom2.util.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pbMahasiswa;
    private TextView tvPesan;
    private RecyclerView rvMahasiswa;

    private MahasiswaAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MahasiswaDatabase db;

    private SharedPrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = SharedPrefManager.getInstance(getApplicationContext());

        if (!pref.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        pbMahasiswa = findViewById(R.id.pb_mahasiswa);
        tvPesan = findViewById(R.id.tv_pesan);
        rvMahasiswa = findViewById(R.id.rv_mahasiswa);

        db = DatabaseClient.getInstance(getApplicationContext()).getMahasiswaDatabase();

        loadMahasiswa();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadMahasiswa() {
        showLoading(true);

        new AsyncTask<Void, Void, List<Mahasiswa>>(){
            @Override
            protected List<Mahasiswa> doInBackground(Void... voids) {
                return db.mahasiswaDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Mahasiswa> mahasiswas) {
                if (mahasiswas != null && !mahasiswas.isEmpty()){
                    showMessage(false, "");
                }else{
                    showMessage(true, "Data tidak ada");
                }
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rvMahasiswa.setLayoutManager(mLayoutManager);
                mAdapter = new MahasiswaAdapter(mahasiswas, getApplicationContext());
                rvMahasiswa.setAdapter(mAdapter);
            }
        }.execute();

        showLoading(false);
    }

    private void showMessage(Boolean state, String message){
        if (state) {
            tvPesan.setText(message);
            tvPesan.setVisibility(View.VISIBLE);
        } else {
            tvPesan.setVisibility(View.GONE);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            pbMahasiswa.setVisibility(View.VISIBLE);
        } else {
            pbMahasiswa.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout){
            pref.logout();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        return true;
    }
}
