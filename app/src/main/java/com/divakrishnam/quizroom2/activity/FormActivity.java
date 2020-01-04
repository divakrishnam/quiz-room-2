package com.divakrishnam.quizroom2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.divakrishnam.quizroom2.R;
import com.divakrishnam.quizroom2.db.DatabaseClient;
import com.divakrishnam.quizroom2.db.MahasiswaDatabase;
import com.divakrishnam.quizroom2.entity.Mahasiswa;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATA_MAHASISWA = "datamahasiswa";

    private TextInputEditText etNpm, etNamaDepan, etNamaBelakang, etUsername, etPassword, etEmail, etNoTelepon;
    private Button btnSimpan, btnBatal;
    private RadioButton rbLaki, rbPerempuan;
    private Spinner spProdi;

    private MahasiswaDatabase db;

    private String mNpm, mNamaDepan, mNamaBelakang, mUsername, mPassword, mEmail, mNoTelepon, mJenkel, mProdi;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        db = DatabaseClient.getInstance(getApplicationContext()).getMahasiswaDatabase();

        etNpm = findViewById(R.id.et_npm);
        etNamaDepan = findViewById(R.id.et_namadepan);
        etNamaBelakang = findViewById(R.id.et_namabelakang);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etNoTelepon = findViewById(R.id.et_notelepon);
        rbLaki = findViewById(R.id.rb_laki);
        rbPerempuan = findViewById(R.id.rb_perempuan);
        spProdi = findViewById(R.id.sp_prodi);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnBatal = findViewById(R.id.btn_batal);

        btnSimpan.setOnClickListener(this);
        btnBatal.setOnClickListener(this);

        Mahasiswa mahasiswa = (Mahasiswa) getIntent().getSerializableExtra(DATA_MAHASISWA);

        if (mahasiswa != null) {
            mUserId = mahasiswa.getUserId();
            mNpm = mahasiswa.getNpm();
            mNamaDepan = mahasiswa.getNamaDepan();
            mNamaBelakang = mahasiswa.getNamaBelakang();
            mUsername = mahasiswa.getUsername();
            mPassword = mahasiswa.getPassword();
            mEmail = mahasiswa.getEmail();
            mNoTelepon = mahasiswa.getNoTelepon();
            mJenkel = mahasiswa.getJenisKelamin();
            mProdi = mahasiswa.getProgramStudi();

            etNpm.setText(mNpm);
            etNamaDepan.setText(mNamaDepan);
            etNamaBelakang.setText(mNamaBelakang);
            etUsername.setText(mUsername);
            etPassword.setText(mPassword);
            etEmail.setText(mEmail);
            etNoTelepon.setText(mNoTelepon);

            if(mJenkel.equals("L")){
                rbLaki.setChecked(true);
            }else if (mJenkel.equals("P")){
                rbPerempuan.setChecked(true);
            }
        }

        spinnerProdi();
    }

    @SuppressLint("StaticFieldLeak")
    private void ubah(final Mahasiswa mahasiswa) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return db.mahasiswaDao().update(mahasiswa);
            }

            @Override
            protected void onPostExecute(Integer status) {
                if (status > 0) {
                    Toast.makeText(getApplicationContext(), "Data tersimpan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Data gagal tersimpan", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void simpan(final Mahasiswa mahasiswa) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setNpm(mahasiswa.getNpm());
                mhs.setNamaDepan(mahasiswa.getNamaDepan());
                mhs.setNamaBelakang(mahasiswa.getNamaBelakang());
                mhs.setUsername(mahasiswa.getUsername());
                mhs.setPassword(mahasiswa.getPassword());
                mhs.setEmail(mahasiswa.getEmail());
                mhs.setJenisKelamin(mahasiswa.getJenisKelamin());
                mhs.setNoTelepon(mahasiswa.getNoTelepon());
                mhs.setProgramStudi(mahasiswa.getProgramStudi());

                return db.mahasiswaDao().insert(mahasiswa);
            }

            @Override
            protected void onPostExecute(Long status) {
                if (status > 0) {
                    Toast.makeText(getApplicationContext(), "Data tersimpan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Data gagal tersimpan", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void simpanMahasiswa() {
        String npm = etNpm.getText().toString();
        String namaDepan = etNamaDepan.getText().toString();
        String namaBelakang = etNamaBelakang.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        String noTelepon = etNoTelepon.getText().toString();
        String jenkel = "";
        if (rbLaki.isChecked()){
            jenkel = "L";
        }else if (rbPerempuan.isChecked()){
            jenkel = "P";
        }
        String prodi = spProdi.getSelectedItem().toString();

        if (!npm.isEmpty() && !namaDepan.isEmpty() && !namaBelakang.isEmpty() && !username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !noTelepon.isEmpty() && !jenkel.isEmpty() && !prodi.isEmpty()) {

            if (mUserId != 0 && mNpm != null && mNamaDepan != null && mNamaBelakang != null && mUsername != null && mPassword != null && mEmail != null && mNoTelepon != null && mJenkel != null && mProdi != null) {
                Mahasiswa mahasiswa = new Mahasiswa(mUserId, npm, namaDepan, namaBelakang, username, password, email, jenkel, noTelepon, prodi);
                ubah(mahasiswa);
            } else {
                Mahasiswa mahasiswa = new Mahasiswa(npm, namaDepan, namaBelakang, username, password, email, jenkel, noTelepon, prodi);
                simpan(mahasiswa);
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Kolom belum terisi", Toast.LENGTH_LONG).show();
        }
    }

    private void spinnerProdi() {
        List<String> list = new ArrayList<>();
        list.add("Teknik Informatika");
        list.add("Manajemen Bisnis");
        list.add("Manajemen Logistik");
        list.add("Akuntansi");
        list.add("Transportasi");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdi.setAdapter(dataAdapter);
        if (mProdi != null){
            int spinnerPosition = dataAdapter.getPosition(mProdi);
            spProdi.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnSimpan) {
            simpanMahasiswa();
        } else if (view == btnBatal) {
            finish();
        }
    }
}
