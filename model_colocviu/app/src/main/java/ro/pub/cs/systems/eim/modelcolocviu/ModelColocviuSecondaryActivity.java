package ro.pub.cs.systems.eim.modelcolocviu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModelColocviuSecondaryActivity extends AppCompatActivity {

    private TextView numTotalClicksTextView;
    private Button okButton, cancelButton;

    private class PushedButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ok_button) {
                setResult(RESULT_OK);
                finish();
            } else if (view.getId() == R.id.cancel_button) {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_model_colocviu_secondary);

        PushedButtonListener pushedButtonListener = new PushedButtonListener();

        numTotalClicksTextView = findViewById(R.id.number_of_clicks_text_view);

        okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(pushedButtonListener);

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(pushedButtonListener);

        Intent intent = getIntent();
        if (intent != null) {
            String numTotalClicks = intent.getStringExtra(Constants.NUM_CLICKS);

            if (numTotalClicks != null) {
                numTotalClicksTextView.setText(numTotalClicks);
            }
        }
    }
}