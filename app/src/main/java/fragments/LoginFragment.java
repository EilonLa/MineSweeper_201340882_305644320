package fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import activities.LoginActivity;
import db.DataRow;

/**
 * Created by eilon & dvir on 21/12/2016.
 */

public class LoginFragment extends Fragment {
    private DataRow lastGame;
    private Login_UI_Enabler ui;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lastGame = LoginActivity.database.getLastGameFromDB();
        setRetainInstance(true);
        ui = new Login_UI_Enabler((LoginActivity) getActivity(), lastGame);
    }

    class Login_UI_Enabler {
        private EditText nameText;
        private TextView welcome;
        private CheckBox easy;
        private CheckBox medium;
        private CheckBox hard;
        private Button start;
        private Button seeHighScores;
        private String defaultName = "Please enter name";

        public Login_UI_Enabler(final LoginActivity activity, DataRow lastGame) {
            nameText = (EditText) activity.findViewById(R.id.nameText);
            nameText.setHint(defaultName);
            easy = (CheckBox) activity.findViewById(R.id.checkbox_easy);
            medium = (CheckBox) activity.findViewById(R.id.checkbox_medium);
            hard = (CheckBox) activity.findViewById(R.id.checkbox_hard);
            start = (Button) activity.findViewById(R.id.btn_start);
            start.setBackgroundResource(R.mipmap.start);
            this.welcome = (TextView) activity.findViewById(R.id.welcome);
            this.welcome.setBackgroundResource(R.mipmap.ic_launcher);
            seeHighScores = (Button) activity.findViewById(R.id.btn_scores);
            seeHighScores.setBackgroundResource(R.mipmap.highscores);
            lastGame = LoginActivity.database.getLastGameFromDB();

            nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nameText.getCurrentTextColor() == Color.RED) {
                        nameText.setTextColor(Color.BLACK);
                        nameText.setText("");
                    }
                    if (nameText.getText().toString().compareTo(defaultName) == 0)
                        nameText.setText("");
                }
            });

            easy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    medium.setChecked(false);
                    hard.setChecked(false);
                    easy.setChecked(true);
                }
            });
            medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    easy.setChecked(false);
                    hard.setChecked(false);
                    medium.setChecked(true);
                }
            });
            hard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    easy.setChecked(false);
                    medium.setChecked(false);
                    hard.setChecked(true);
                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = nameText.getText().toString();
                    if (name.length() == 0 || name.compareToIgnoreCase(defaultName) == 0) {
                        nameText.setText(defaultName);
                        nameText.setTextColor(Color.RED);
                    } else {
                        int level = 0;
                        if (easy.isChecked())
                            level = 1;
                        if (medium.isChecked())
                            level = 2;
                        if (hard.isChecked())
                            level = 3;
                        if (level != 0) {
                            activity.startGame(level, name);
                        } else
                            Toast.makeText(getActivity(), "You must choose level!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            seeHighScores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment highScoresFragments = new HighScoresFragments();
                    activity.getFragmentManager().beginTransaction().replace(R.id.main_fragment, highScoresFragments).commit();
                    activity.getFragmentManager().beginTransaction().addToBackStack(null);
                }
            });
            if (lastGame != null) {
                nameText.setText(lastGame.getmName());
                switch (lastGame.getLevel()) {
                    case 1:
                        easy.callOnClick();
                        break;
                    case 2:
                        medium.callOnClick();
                        break;
                    case 3:
                        hard.callOnClick();
                }
            }
        }
    }
}
