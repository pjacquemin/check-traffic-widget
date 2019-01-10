package compjacquemin.httpsgithub.checktrafficwidget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String HOME = "home";
    private static final String WORK = "work";
    public static final String SHARED_PREF_NAME = "check_traffic_widget";

    private String from;
    private TextView home_places_details_text;
    private TextView work_places_details_text;
    private SharedPreferences shared_pref;
    private SharedPreferences.Editor editor;
    private String home_address;
    private String work_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button open_autocomplete_for_home = (Button) findViewById(R.id.open_button);
        open_autocomplete_for_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity(HOME);
            }
        });

        home_places_details_text = (TextView) findViewById(R.id.home_place_details);
        work_places_details_text = (TextView) findViewById(R.id.work_place_details);
        shared_pref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = shared_pref.edit();
        home_address = shared_pref.getString("home_address", "");
        work_address = shared_pref.getString("work_address", "");
        home_places_details_text.setText(home_address);
        work_places_details_text.setText(work_address);

        Button open_autocomplete_for_work = (Button) findViewById(R.id.open_button2);
        open_autocomplete_for_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity(WORK);
            }
        });
    }

    private void openAutocompleteActivity(String from) {
        this.from = from;
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if(this.from == HOME) {
                    home_places_details_text.setText(formatPlaceDetails(getResources(), place.getName(),
                            place.getAddress()));
                    editor.putString("home_address", place.getAddress().toString());
                } else {
                    work_places_details_text.setText(formatPlaceDetails(getResources(), place.getName(),
                            place.getAddress()));
                    editor.putString("work_address", place.getAddress().toString());

                }
                editor.commit();
            }
        }
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, CharSequence address) {
        return Html.fromHtml(res.getString(R.string.place_details, name, address));
    }

}
