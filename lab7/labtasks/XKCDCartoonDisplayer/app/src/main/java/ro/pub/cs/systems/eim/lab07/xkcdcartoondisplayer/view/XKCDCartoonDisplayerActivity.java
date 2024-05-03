package ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.R;
import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.general.Constants;
import ro.pub.cs.systems.eim.lab07.xkcdcartoondisplayer.network.XKCDCartoonDisplayerAsyncTask;

public class XKCDCartoonDisplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xkcd_cartoon_displayer);

        TextView xkcdCartoonTitleTextView = findViewById(R.id.xkcd_cartoon_title_text_view);
        ImageView xkcdCartoonImageView = findViewById(R.id.xkcd_cartoon_image_view);
        TextView xkcdCartoonUrlTextView = findViewById(R.id.xkcd_cartoon_url_text_view);

        Button previousButton = findViewById(R.id.previous_button);
        Button nextButton = findViewById(R.id.next_button);

        new XKCDCartoonDisplayerAsyncTask(xkcdCartoonTitleTextView, xkcdCartoonImageView, xkcdCartoonUrlTextView, previousButton, nextButton).execute(Constants.XKCD_INTERNET_ADDRESS);
    }
}
