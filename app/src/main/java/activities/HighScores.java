package activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import ui_package.HighScores_UI_Enabler;

/**
 * Created by אילון on 01/12/2016.
 */

public class HighScores extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
        new HighScores_UI_Enabler((this));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
