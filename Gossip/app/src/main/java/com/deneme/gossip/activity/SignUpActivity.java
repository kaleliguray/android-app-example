package com.deneme.gossip.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deneme.gossip.R;
import com.deneme.gossip.dialog.WaitDialog;
import com.deneme.gossip.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = SignUpActivity.class.getName();

    private TextInputEditText firstNameTextInputLayout;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameTextInputLayout;
    private TextInputEditText lastNameEditText;
    private TextInputEditText usernameTextInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private Button signupButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
        registerViewsEvents();
    }

    private void initializeViews(){
        firstNameTextInputLayout = findViewById(R.id.first_name_text_input_layout);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameTextInputLayout = findViewById(R.id.last_name_text_input_layout);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        usernameTextInputLayout = findViewById(R.id.username_text_input_layout);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        passwordEditText = findViewById(R.id.password_edit_text);
        signupButton = findViewById(R.id.sign_up_button);

    }

    private void registerViewsEvents(){
        signupButton.setOnClickListener(v -> {
            boolean errorExists = false;
            if (firstNameEditText.getText().toString().trim().length() == 0) {
                firstNameTextInputLayout.setError("First name cannot be empty");
                errorExists |= true;
            } else {
                firstNameTextInputLayout.setError(null);
            }
            if (lastNameEditText.getText().toString().trim().length() == 0) {
                lastNameTextInputLayout.setError("Last name cannot be empty");
                errorExists |= true;
            } else {
                lastNameTextInputLayout.setError(null);
            }
            if (usernameEditText.getText().toString().trim().length() == 0) {
                usernameTextInputLayout.setError("Username cannot be empty");
                errorExists |= true;
            } else {
                usernameTextInputLayout.setError(null);
            }
            if (errorExists |= passwordEditText.getText().toString().trim().length() == 0) {
                passwordTextInputLayout.setError("Password cannot be empty");
                errorExists |= true;
            } else {
                passwordTextInputLayout.setError(null);
            }
            if (Boolean.FALSE.equals(errorExists)) {
                WaitDialog waitDialog = new WaitDialog(SignUpActivity.this);
                waitDialog.show();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(username,password)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            if (task.isSuccessful()) {
                                User user = new User(firstNameEditText.getText().toString(),lastNameEditText.getText().toString(),new Date(),true);
                                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(user)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(SignUpActivity.this, task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }else {
                                waitDialog.dismiss();
                                Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });

    }

}

















