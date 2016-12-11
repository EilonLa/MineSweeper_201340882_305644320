package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import db.DBOperator;
import db.DataRow;
import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import ui_package.Login_UI_Enabler;

/**
 * Created by אילון on 26/11/2016.
 */

public class LoginActivity extends AppCompatActivity {

    DBOperator database;
    DataRow lastGame;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        database = new DBOperator(this);//sets the connection to the sqllite db
        lastGame = database.getLastGameFromDB();
        if (lastGame == null)//if no last game will create db table, return null
            lastGame = database.getLastGameFromDB();

        new Login_UI_Enabler(this, lastGame);
    }

    public void startGame (int level, String name){
        Intent intent = new Intent(getApplicationContext(), GamePlayActivity.class);
        intent.putExtra("level", level);
        intent.putExtra("name",name);
        startActivity(intent);
    }
}
