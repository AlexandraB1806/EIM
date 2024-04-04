package ro.pub.cs.systems.eim.modelcolocviu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
public class ModelColocviuMainActivity extends AppCompatActivity {

    private EditText leftEditText, rightEditText;
    private Button navigateToSecondaryActivityButton, pressMeButton, pressMeTooButton;

    // ----------------------- C ------------------------
    private ActivityResultLauncher<Intent> startActivityForResultLauncher;

    // ----------------------- D.1 ------------------------
    private IntentFilter intentFilter;
    private Intent intent;

    // ----------------------- D.2 ------------------------
    private ModelColocviuBroadcastReceiver broadcastReceiver;

    private class PushedButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int numOfClicksLeft = Integer.parseInt(leftEditText.getText().toString());
            int numOfClicksRight = Integer.parseInt(rightEditText.getText().toString());

            if (view.getId() == R.id.press_me_button) {
                numOfClicksLeft++;

                leftEditText.setText(String.valueOf(numOfClicksLeft));

                // ----------------------- D.1 b ------------------------
                int numTotalClicks = numOfClicksLeft + numOfClicksRight;

                if (numTotalClicks >= Constants.THRESHOLD) {
                    intent = new Intent();
                    intent.putExtra(Constants.NUMBER_LEFT, numOfClicksLeft);
                    intent.putExtra(Constants.NUMBER_RIGHT, numOfClicksRight);

                    intent.setComponent(new ComponentName(Constants.SERVICE_PACKAGE, Constants.SERVICE_CLASS));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                }
                // ----------------------- D.1 b ------------------------
            } else if (view.getId() == R.id.press_me_too_button) {
                numOfClicksRight++;

                rightEditText.setText(String.valueOf(numOfClicksRight));

                // ----------------------- D.1 b ------------------------
                int numTotalClicks = numOfClicksLeft + numOfClicksRight;

                if (numTotalClicks >= Constants.THRESHOLD) {
                    intent = new Intent();
                    intent.putExtra(Constants.NUMBER_LEFT, numOfClicksLeft);
                    intent.putExtra(Constants.NUMBER_RIGHT, numOfClicksRight);

                    intent.setComponent(new ComponentName(Constants.SERVICE_PACKAGE, Constants.SERVICE_CLASS));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                }
                // ----------------------- D.1 b ------------------------
            } else if (view.getId() == R.id.button_to_secondary_activity) {
                // ----------------------- C ------------------------
                String numTotalClicks = String.valueOf(numOfClicksLeft + numOfClicksRight);

                Intent intent = new Intent("ro.pub.cs.systems.eim.modelcolocviu.intent.action.ModelColocviuSecondaryActivity");
                intent.putExtra(Constants.NUM_CLICKS, numTotalClicks);

                startActivityForResultLauncher.launch(intent);
                // ----------------------- C ------------------------
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_model_colocviu_main);

        PushedButtonListener pushedButtonListener = new PushedButtonListener();

        leftEditText = findViewById(R.id.left_edit_text);
        rightEditText = findViewById(R.id.right_edit_text);

        navigateToSecondaryActivityButton = findViewById(R.id.button_to_secondary_activity);
        navigateToSecondaryActivityButton.setOnClickListener(pushedButtonListener);

        pressMeButton = findViewById(R.id.press_me_button);
        pressMeButton.setOnClickListener(pushedButtonListener);

        pressMeTooButton = findViewById(R.id.press_me_too_button);
        pressMeTooButton.setOnClickListener(pushedButtonListener);

        // ----------------------- B ------------------------
        if (savedInstanceState == null) {
            leftEditText.setText(String.valueOf(0));
            rightEditText.setText(String.valueOf(0));
        } else {
            if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
                leftEditText.setText(savedInstanceState.getString(Constants.LEFT_COUNT));
            } else {
                leftEditText.setText(String.valueOf(0));
            }

            if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
                rightEditText.setText(savedInstanceState.getString(Constants.RIGHT_COUNT));
            } else {
                rightEditText.setText(String.valueOf(0));
            }
        }
        // ----------------------- B ------------------------


        // ----------------------- C ------------------------
        startActivityForResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Toast.makeText(getApplication(), "Ok", Toast.LENGTH_SHORT).show();
            } else if (result.getResultCode() == RESULT_CANCELED) {
                Toast.makeText(getApplication(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        // ----------------------- C ------------------------


        // ----------------------- D.2 ------------------------
        broadcastReceiver = new ModelColocviuBroadcastReceiver();

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_1);
        intentFilter.addAction(Constants.ACTION_2);
        intentFilter.addAction(Constants.ACTION_3);
        // ----------------------- D.2 ------------------------
    }

    // ----------------------- B ------------------------
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(Constants.LEFT_COUNT, leftEditText.getText().toString());
        savedInstanceState.putString(Constants.RIGHT_COUNT, rightEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
            leftEditText.setText(savedInstanceState.getString(Constants.LEFT_COUNT));
        } else {
            leftEditText.setText(String.valueOf(0));
        }

        if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
            rightEditText.setText(savedInstanceState.getString(Constants.RIGHT_COUNT));
        } else {
            rightEditText.setText(String.valueOf(0));
        }
    }

    // ----------------------- B ------------------------


    // ----------------------- D.1 ------------------------
    @Override
    protected void onDestroy() {
        stopService(intent);
        super.onDestroy();
    }
    // ----------------------- D.1 ------------------------



    // ----------------------- D.2 ------------------------
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
    // ----------------------- D.2 ------------------------
}