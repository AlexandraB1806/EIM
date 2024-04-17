package ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.R;
import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.network.FTPServerCommunicationAsyncTask;

public class FTPServerWelcomeMessageActivity extends AppCompatActivity {

    private final ButtonClickListener buttonClickListener = new ButtonClickListener();
    private EditText FTPServerAddressEditText;
    private EditText FTPServerPortEditText;
    private TextView welcomeMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpserver_welcome_message);

        FTPServerAddressEditText = (EditText) findViewById(R.id.ftp_server_address_edit_text);
        FTPServerPortEditText = (EditText) findViewById(R.id.port_server_edit_text);

        Button displayWelcomeMessageButton = (Button) findViewById(R.id.display_welcome_message_button);
        displayWelcomeMessageButton.setOnClickListener(buttonClickListener);

        welcomeMessageTextView = (TextView) findViewById(R.id.welcome_message_text_view);
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            FTPServerCommunicationAsyncTask ftpServerCommunicationAsyncTask = new FTPServerCommunicationAsyncTask(welcomeMessageTextView);
            // For example you can use: ftp.gnu.org as FTP server address and 21 as FTP server port
            ftpServerCommunicationAsyncTask.execute(FTPServerAddressEditText.getText().toString(), FTPServerPortEditText.getText().toString());
        }

    }
}
