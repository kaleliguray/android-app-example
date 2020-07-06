package com.deneme.gossip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deneme.gossip.R;
import com.deneme.gossip.dialog.WaitDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.channels.AlreadyConnectedException;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameTextInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        registerViewsEvents();
    }

    private void initializeViews(){
        usernameTextInputLayout = findViewById(R.id.username_text_input_layout);
        usernameEditText = findViewById(R.id.username_edit_text);
        usernameTextInputLayout = findViewById(R.id.password_text_input_layout);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.sign_up_text_view);


    }

    private void registerViewsEvents(){
        loginButton.setOnClickListener(v -> {
            boolean errorExists = false;
            if (usernameEditText.getText().toString().trim().length() == 0) {
                usernameTextInputLayout.setError("Username cannot be empty");
                errorExists = true;
            } else {
                usernameTextInputLayout.setError(null);
            }
            if (passwordEditText.getText().toString().trim().length() == 0) {
                passwordTextInputLayout.setError("Password cannot be empty");
                errorExists = true;
            } else {
            //    passwordTextInputLayout.setError(null);
            }
            if (Boolean.FALSE.equals(errorExists)){
                WaitDialog waitDialog = new WaitDialog(LoginActivity.this);
                waitDialog.show();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                FirebaseAuth.getInstance().signInWithEmailAndPassword(username,password)
                        .addOnCompleteListener(task -> {
                            waitDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });
        signUpTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,SignUpActivity.class)));
    }

}

















