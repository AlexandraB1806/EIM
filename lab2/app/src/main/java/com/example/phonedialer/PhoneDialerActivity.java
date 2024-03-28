package com.example.phonedialer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class PhoneDialerActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private ImageButton callButton, hangupButton, backspaceButton;
    private Button genericButton;

    private class PushedButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.backspace_button) {
                // buton corectie

                // In cazul in care nr de telefon NU este vid, se sterge ultimul caracter.
                String phoneNumber = phoneNumberEditText.getText().toString();

                if (!phoneNumber.isEmpty()) {
                    phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                    phoneNumberEditText.setText(phoneNumber);
                }
            } else if (view.getId() == R.id.outgoing_call_button) {
                // buton primire apel

                // Trebuie solicitata permisiunea de efectuare a apelului telefonic
                // la momentul rularii
                if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            PhoneDialerActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            Constants.PERMISSION_REQUEST_CALL_PHONE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumberEditText.getText().toString()));
                    startActivity(intent);
                }
            } else if (view.getId() == R.id.missed_call_button) {
                // buton oprire apel

                // Se inchide activitatea
                finish();
            } else {
                // buton generic: 0-9, *, #

                // Pe masura ce se tasteaza un nr de telefon, se adauga caractere la numarul deja format
                phoneNumberEditText.setText(phoneNumberEditText.getText().append(((Button)view).getText()));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Incarcarea interfetei grafice
        setContentView(R.layout.activity_phone_dialer);

        // Instantiere clasa interna
        PushedButtonListener pushedButtonListener = new PushedButtonListener();

        // Obtinerea referintelor elementelor din interfata grafica: findViewById
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);

        callButton = findViewById(R.id.outgoing_call_button);
        callButton.setOnClickListener(pushedButtonListener);

        hangupButton = findViewById(R.id.missed_call_button);
        hangupButton.setOnClickListener(pushedButtonListener);

        backspaceButton = findViewById(R.id.backspace_button);
        backspaceButton.setOnClickListener(pushedButtonListener);

        for (int index = 0; index < Constants.buttonIds.length; index++) {
            genericButton = findViewById(Constants.buttonIds[index]);
            genericButton.setOnClickListener(pushedButtonListener);
        }
    }
}
