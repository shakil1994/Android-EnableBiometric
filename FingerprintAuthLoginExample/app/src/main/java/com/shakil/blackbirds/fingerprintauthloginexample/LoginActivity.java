package com.shakil.blackbirds.fingerprintauthloginexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Now Let's select our button and msg text
        TextView msg_txt = findViewById(R.id.txt_msg);
        AppCompatButton login_btn = findViewById(R.id.login_btn);

        // Create the BiometricManager and let's check if our user can use the fingerprint sensor or not
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){ // We will switch some constant to check different possibility
            case BiometricManager.BIOMETRIC_SUCCESS: // this is mean that we can use the biometric sensor
                msg_txt.setText("You can use the fingerprint sensor to login");
                msg_txt.setTextColor(Color.parseColor("#FAFAFA"));
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE: // this is mean that the device don't have a fingerprint sensor
                msg_txt.setText("The device don't have a fingerprint sensor");
                login_btn.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_txt.setText("The biometric sensors is currently unavailable");
                login_btn.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg_txt.setText("Your device don't have any fingerprint saved, Please check your security settings");
                login_btn.setVisibility(View.GONE);
                break;
        }

        // Now that we have checked if we are able or not to use the biometric sensors let's start create our
        // biometric dialog box

        // First we need to create an executor
        Executor executor = ContextCompat.getMainExecutor(this);

        // Now we need to create the biometric prompt callback
        // this will give us the result of the authentication and if we can login or not
        // just type this line and your biometric prompt will be generated automatically
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            // this method is called when there is an error while the authentication
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // this method is called when the authentication is a success
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginActivity.this, "Login Success!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            // this method is called if we have failed the authentication
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        // Now let's create our Biometric Dialog
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login")
                .setDescription("Use your fingerprint to login to your app")
                .setNegativeButtonText("Cancel")
                .build();

        //Now everything is ready, all what we have to do is to call the dialog when the user press login button
        login_btn.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });


    }
}