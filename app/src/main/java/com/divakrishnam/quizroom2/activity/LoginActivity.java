package com.divakrishnam.quizroom2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.divakrishnam.quizroom2.R;
import com.divakrishnam.quizroom2.db.DatabaseClient;
import com.divakrishnam.quizroom2.db.MahasiswaDatabase;
import com.divakrishnam.quizroom2.entity.Mahasiswa;
import com.divakrishnam.quizroom2.util.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etUsername, etPassword;
    private Button btnLogin, btnRegister;

    private MahasiswaDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = DatabaseClient.getInstance(getApplicationContext()).getMahasiswaDatabase();

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin){
            loginMahasiswa();
        }else if (view == btnRegister){
            startActivity(new Intent(getApplicationContext(), FormActivity.class));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void login(final String username, final String password) {
        new AsyncTask<Void, Void, Mahasiswa>(){
            @Override
            protected Mahasiswa doInBackground(Void... voids) {
                return db.mahasiswaDao().login(username, password);
            }

            @Override
            protected void onPostExecute(Mahasiswa mahasiswa) {
                if (mahasiswa != null){
                    Toast.makeText(getApplicationContext(), "Berhasil login", Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).mahasiswaLogin(mahasiswa.getUserId());
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Username dan Password salah", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void loginMahasiswa() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()){
            login(username, password);
        }else{
            Toast.makeText(getApplicationContext(), "Username dan Password harus diisi", Toast.LENGTH_LONG).show();
        }
    }
}
