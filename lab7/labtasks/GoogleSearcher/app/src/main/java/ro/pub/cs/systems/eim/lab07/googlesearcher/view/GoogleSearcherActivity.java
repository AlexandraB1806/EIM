package ro.pub.cs.systems.eim.lab07.googlesearcher.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.pub.cs.systems.eim.lab07.googlesearcher.R;
import ro.pub.cs.systems.eim.lab07.googlesearcher.general.Constants;
import ro.pub.cs.systems.eim.lab07.googlesearcher.network.GoogleSearcherAsyncTask;

public class GoogleSearcherActivity extends AppCompatActivity {

    private EditText keywordEditText;
    private WebView googleResultsWebView;
    private Button searchGoogleButton;

    private SearchGoogleButtonClickListener searchGoogleButtonClickListener = new SearchGoogleButtonClickListener();
    private class SearchGoogleButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // obtain the keyword from keywordEditText
            // signal an empty keyword through an error message displayed in a Toast window
            // split a multiple word (separated by space) keyword and link them through +
            // prepend the keyword with "search?q=" string
            // start the GoogleSearcherAsyncTask passing the keyword
            StringBuilder keyword = new StringBuilder(keywordEditText.getText().toString());

            if (keyword.length() == 0) {
                Toast.makeText(getApplication(), Constants.EMPTY_KEYWORD_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            } else {
                String[] keywords = keyword.toString().split(" ");

                // ex: keyword = "android developers"
                // http://search.brave.com/search?q=android+developers
                keyword = new StringBuilder(Constants.SEARCH_PREFIX + keywords[0]);

                for (int i = 1; i < keywords.length; i++) {
                    keyword.append("+").append(keywords[i]);
                }
            }

            new GoogleSearcherAsyncTask(googleResultsWebView).execute(keyword.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_searcher);

        keywordEditText = (EditText)findViewById(R.id.keyword_edit_text);
        googleResultsWebView = (WebView)findViewById(R.id.google_results_web_view);

        searchGoogleButton = (Button)findViewById(R.id.search_google_button);
        searchGoogleButton.setOnClickListener(searchGoogleButtonClickListener);
    }
}
