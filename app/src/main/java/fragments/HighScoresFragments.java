package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import java.util.ArrayList;

import activities.EndActivity;
import activities.LoginActivity;
import db.DataRow;
import ui_enablers.End_UI_Enabler;

/**
 * Created by eilon & dvir on 20/12/2016.
 */

public class HighScoresFragments extends Fragment {
    HighScores_UI_Enabler ui;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ui = new HighScores_UI_Enabler(getActivity());
    }
}

class HighScores_UI_Enabler {
    private Activity activity;
    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;
    private Button back;
    private ArrayList<DataRow> topTenRows;
    private LinearLayout table;
    private FloatingActionButton goToMapsView;
    private LoginFragment loginFragment;


    public HighScores_UI_Enabler(final Activity activity) {
        this.activity = activity;
        this.easy = (CheckBox) activity.findViewById(R.id.checkbox_easy_scores);
        this.medium = (CheckBox) activity.findViewById(R.id.checkbox_medium_scores);
        this.hard = (CheckBox) activity.findViewById(R.id.checkbox_hard_scores);
        this.back = (Button) activity.findViewById(R.id.btn_back);
        table = (LinearLayout) activity.findViewById(R.id.scoreTable_scores);
        back.setBackgroundResource(R.mipmap.back);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medium.setChecked(false);
                hard.setChecked(false);
                easy.setChecked(true);
                topTenRows = LoginActivity.database.getTopTen(1);
                setTable(topTenRows);
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setChecked(false);
                hard.setChecked(false);
                medium.setChecked(true);
                topTenRows = LoginActivity.database.getTopTen(2);
                setTable(topTenRows);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setChecked(false);
                medium.setChecked(false);
                hard.setChecked(true);
                topTenRows = LoginActivity.database.getTopTen(3);
                setTable(topTenRows);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity instanceof LoginActivity) {
                    loginFragment = new LoginFragment();
                    activity.getFragmentManager().beginTransaction().replace(R.id.main_fragment, loginFragment).commit();
                }
                else if (activity instanceof EndActivity){
                    activity.onBackPressed();
                }
            }
        });
        goToMapsView = (FloatingActionButton) activity.findViewById(R.id.floatingMapActionButton);
        goToMapsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, MyMapFragment.class));
            }
        });
        easy.callOnClick();
    }

    public void setTable(ArrayList<DataRow> rowsData) {
        table.removeAllViews();
        if (rowsData != null && rowsData.size() > 0) {
            table.setPadding(5, 40, 5, 0);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(table.getWidth(), 2);
            LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(400, 100);

            int i = 1;
            for (DataRow row : rowsData) {
                View separate1 = new View(activity);
                separate1.setLayoutParams(viewParams);
                separate1.setBackgroundColor(0xFF909090);
                table.addView(separate1);
                LinearLayout rows = new LinearLayout(activity);
                rows.setOrientation(LinearLayout.HORIZONTAL);
                TextView tempCell = new TextView(activity);
                tempCell.setLayoutParams(cellParams);
                tempCell.setText("" + (i++) + ".");
                tempCell.setPadding(5, 0, 200, 0);
                rows.addView(tempCell);
                TextView tempCell2 = new TextView(activity);
                tempCell2.setLayoutParams(cellParams);
                tempCell2.setText(row.getmName());
                tempCell2.setPadding(0, 0, 200, 0);
                rows.addView(tempCell2);
                TextView tempCell3 = new TextView(activity);
                tempCell3.setLayoutParams(cellParams);
                tempCell3.setText(String.valueOf(End_UI_Enabler.setScoreToShow(row.getScore())));
                tempCell3.setPadding(0, 0, 0, 5);
                rows.addView(tempCell3);
                table.addView(rows);
                View separate2 = new View(activity);
                separate2.setLayoutParams(viewParams);
                separate2.setBackgroundColor(0xFF909090);
                table.addView(separate2);
            }
        }
    }
}
