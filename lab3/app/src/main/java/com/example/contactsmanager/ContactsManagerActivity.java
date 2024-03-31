package com.example.contactsmanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private Button infoButton, saveButton, cancelButton;
    private EditText nameEditText, phoneEditText, emailEditText, addressEditText;
    private EditText jobTitleEditText, companyEditText, websiteEditText, imEditText;

    // !!! Al doilea container este cuprins intr-un LinearLayout care are
    // "visibility" = invisibile
    private LinearLayout additionalInformation;


    // Clasa abstracta de tip Intent, utila pentr inlocuirea
    // metodei deprecated startActivityForResult()
    private ActivityResultLauncher<Intent> startActivityForResultLauncher;


    // Implementare clasa asculator (clasa interna):
    // implements: View.OnClickListener
    // @Override: onClick(View v)
    private class PushedButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.details_button) {
                // Daca Butonul este "Show Additional Fields"
                // => ne uitam dupa textul butonului !!!
                if (infoButton.getText().equals(Constants.SHOW_INFO)) {
                    // Ne focusam pe al doilea container ce facem.
                    // In cazul nostru, il facem VIZIBIL !!!
                    additionalInformation.setVisibility(View.VISIBLE);

                    // Schimbam textul butonului !!!
                    infoButton.setText(Constants.HIDE_INFO);
                } else {
                    // Butonul este "Hide Additional Fields"
                    additionalInformation.setVisibility(View.GONE);

                    // Schimbam textul butonului !!!
                    infoButton.setText(Constants.SHOW_INFO);
                }
            } else if (view.getId() == R.id.save) {
                // butonul save.
                // Lanseaza in executie aplicatia Android nativa pt stocarea
                // unui contact in agenda telefonica

                // Mai intai, preluam informatiile din controalele grafice
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String jobTitle = jobTitleEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String website = websiteEditText.getText().toString();
                String im = imEditText.getText().toString();

                // Setam ACTIUNEA si TIPUL pentru intentie
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                // Informatiile care se doresc a fi completate sunt atasate in
                // campul extra al intentiei: (cheie, valoare)
                if (!name.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                }
                if (!phone.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                }
                if (!email.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                }
                if (!address.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                }
                if (!jobTitle.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                }
                if (!company.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                }

                // Pun un ArrayList in Intent
                ArrayList<ContentValues> contactData = new ArrayList<>();

                if (!website.isEmpty()) {
                    ContentValues websiteRow = new ContentValues();
                    websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                    contactData.add(websiteRow);
                }

                if (!im.isEmpty()) {
                    ContentValues imRow = new ContentValues();
                    imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                    contactData.add(imRow);
                }

                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

                // Se lanseaza in executie aplicatia nativa pentru gestiunea agendei telefonice

                // Activitatea copil rezultata este independenta de activitatea parinte
                // => parintele NU este notificat cu privire la terminarea activitatii copil
                // startActivity(intent);

                // sol:
                startActivityForResultLauncher.launch(intent);
            } else if (view.getId() == R.id.cancel) {
                // Se transmite inapoi rezultatul
                setResult(RESULT_CANCELED);

                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Incarcarea interfetei grafice
        setContentView(R.layout.activity_contacts_manager);

        // Instantiere clasa interna
        PushedButtonListener pushedButtonListener = new PushedButtonListener();

        // Butoanele Show Additional Details / Hide Additional Details:
        infoButton = findViewById(R.id.details_button);
        infoButton.setOnClickListener(pushedButtonListener);

        // Butonul Save
        saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(pushedButtonListener);

        // Butonul Cancel
        cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(pushedButtonListener);

        // Restul elementelor grafice
        nameEditText = findViewById(R.id.name_field);
        phoneEditText = findViewById(R.id.phone_number_field);
        emailEditText = findViewById(R.id.email_field);
        addressEditText = findViewById(R.id.address_field);

        jobTitleEditText = findViewById(R.id.job_title);
        companyEditText = findViewById(R.id.company);
        websiteEditText = findViewById(R.id.website_field);
        imEditText = findViewById(R.id.im_field);

        // Al doilea container
        additionalInformation = findViewById(R.id.additional_information);

        startActivityForResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
           setResult(result.getResultCode());
           finish();
        });

        // Dupa ce am modificat in fisierul Manifest ca activitatea sa poata
        // fi rulata doar prin intermediul unei intentii, trb verificata
        // intentia cu care activitatea este pornita
        Intent intent = getIntent();
        if (intent != null) {
            // Daca intentia nu este nula, se preia info din extra
            String phoneNumber = intent.getStringExtra("com.example.contactsmanager.PHONE_NUMBER_KEY");

            if (phoneNumber != null) {
                phoneEditText.setText(phoneNumber);
            } else {
                Toast.makeText(this, "ERROR IN SETTING THE PHONE NUMBER", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
