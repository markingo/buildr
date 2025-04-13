package com.markingo.buildr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.markingo.buildr.R;
import com.markingo.buildr.ui.MainActivity;

/**
 * LoginActivity where users can sign in to their account
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        initializeUI();

        // Set up click listeners
        setupListeners();
    }

    private void initializeUI() {
        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        tilPassword = findViewById(R.id.til_password);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> loginUser());
        
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        
        tvForgotPassword.setOnClickListener(v -> {
            // Show dialog to input email for password reset
            // For simplicity, this is not implemented in this example
            Toast.makeText(LoginActivity.this, "Password reset feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        // Clear previous errors
        tilEmail.setError(null);
        tilPassword.setError(null);
        
        // Get input values
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        
        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.field_required));
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.field_required));
            return;
        }
        
        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        
        // Attempt to sign in
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, getString(R.string.login_success), 
                                Toast.LENGTH_SHORT).show();
                        
                        // Navigate to main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        String errorMessage = "";
                        if (task.getException() != null) {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                errorMessage = "Account not found. Please check your email or register.";
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMessage = "Invalid credentials. Please check your email and password.";
                            } else {
                                errorMessage = task.getException().getMessage();
                            }
                        }
                        
                        Toast.makeText(LoginActivity.this, 
                                getString(R.string.login_failed, errorMessage),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
} 