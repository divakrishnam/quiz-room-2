package com.divakrishnam.quizroom2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.divakrishnam.quizroom2.R;
import com.divakrishnam.quizroom2.entity.Mahasiswa;

public class DetailActivity extends AppCompatActivity {


    public static final String DATA_MAHASISWA = "DetailMahasiswa";

    private TextView tvNpm, tvNama, tvUsername, tvPassword, tvEmail, tvJenkel, tvNoTelepon, tvProdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Mahasiswa mahasiswa = (Mahasiswa) getIntent().getSerializableExtra(DATA_MAHASISWA);

        tvNpm = findViewById(R.id.tv_npm);
        tvNama = findViewById(R.id.tv_nama);
        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
        tvEmail = findViewById(R.id.tv_email);
        tvJenkel = findViewById(R.id.tv_jenkel);
        tvNoTelepon = findViewById(R.id.tv_notelepon);
        tvProdi = findViewById(R.id.tv_prodi);

        tvNpm.setText(mahasiswa.getNpm());
        tvNama.setText(mahasiswa.getNamaDepan()+" "+mahasiswa.getNamaBelakang());
        tvUsername.setText(mahasiswa.getUsername());
        tvPassword.setText(mahasiswa.getPassword());
        tvEmail.setText(mahasiswa.getEmail());
        tvJenkel.setText(mahasiswa.getJenisKelamin());
        tvNoTelepon.setText(mahasiswa.getNoTelepon());
        tvProdi.setText(mahasiswa.getProgramStudi());
    }
}
