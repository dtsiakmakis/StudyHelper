package com.example.ashvi.studyhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button button;
    private Button goRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText)findViewById(R.id.login_email_input);
        password = (EditText)findViewById(R.id.login_password_input);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        button = (Button)findViewById(R.id.login);
        goRegister = findViewById(R.id.goRegister);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button){
                    LoginUser();
                }
            }
        });
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == goRegister){
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
            }
        });
    }
    public void LoginUser() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if ((Email.equals("")) && (Password.equals(""))) {
            Toast.makeText(MainActivity.this, "Please fill out your email and password",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = mAuth.getCurrentUser();
                                finish();
                                startActivity(new Intent(getApplicationContext(),
                                        ProfileActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "couldn't login",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}