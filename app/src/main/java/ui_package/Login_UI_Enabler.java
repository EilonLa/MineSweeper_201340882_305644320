package ui_package;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import db.DataRow;
import activities.HighScores;
import activities.LoginActivity;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

/**
 * Created by אילון on 26/11/2016.
 */

public class Login_UI_Enabler {
    private EditText nameText;
    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;
    private Button start;
    private Button seeHighScores;

    private String defaultName = "Please enter name";
    private LoginActivity loginActivity;

    public Login_UI_Enabler(final LoginActivity loginActivity, DataRow lastGame){
        this.loginActivity = loginActivity;
        nameText = (EditText) loginActivity.findViewById(R.id.nameText);
        nameText.setText(defaultName);
        easy     = (CheckBox) loginActivity.findViewById(R.id.checkbox_easy);
        medium   = (CheckBox) loginActivity.findViewById(R.id.checkbox_medium);
        hard     = (CheckBox) loginActivity.findViewById(R.id.checkbox_hard);
        start    = (Button)loginActivity.findViewById(R.id.btn_start);
        seeHighScores = (Button) loginActivity.findViewById(R.id.btn_scores);

        if (lastGame != null){
            nameText.setText(lastGame.getmName());
            switch (lastGame.getLevel()){
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
        nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameText.getCurrentTextColor() == Color.RED){
                nameText.setTextColor(Color.BLACK);
                nameText.setText("");

            }
                if (nameText.getText().toString().compareTo(defaultName)== 0)
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
                if (name.length() ==0){
                    nameText.setText("Please enter name!");
                    nameText.setTextColor(Color.RED);
                }
                else {
                    int level = 0;
                    if (easy.isChecked())
                        level = 1;
                    if (medium.isChecked())
                        level = 2;
                    if (hard.isChecked())
                        level = 3;

                    if (level != 0) {
                        loginActivity.startGame(level,name);
                    }
                }
            }
        });
        seeHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginActivity,HighScores.class);
                loginActivity.startActivity(i);
            }
        });
    }
    public void onCheckboxClicked(){

    }

}
