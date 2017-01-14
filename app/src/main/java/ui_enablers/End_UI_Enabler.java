package ui_enablers;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cdv.minesweeper_eilonlaor_dvirtwina.R;

import activities.EndActivity;
import activities.GamePlayActivity;
import activities.LoginActivity;
import fragments.HighScoresFragments;

/**
 * Created by eilon & dvir on 30/11/2016.
 */

public class End_UI_Enabler {
    private ImageView imageView, fireworks1, fireworks2, fireworks3, blast;
    private AnimationDrawable fireWorksAnim;
    private int level;
    private String playerName;
    private TextView scoreText;
    private EndActivity activity;
    private int score;
    private boolean newHighScore;
    private Button seeHighScores;
    private Button playAgain;

    public End_UI_Enabler(final EndActivity activity, boolean newHighScore, int score) {
        this.activity = activity;
        this.level = activity.getLevel();
        this.playerName = activity.getPlayerName();
        this.scoreText = (TextView) activity.findViewById(R.id.finalScore);
        imageView = (ImageView) activity.findViewById(R.id.endImageView);
        this.newHighScore = newHighScore;
        this.score = score;
        setScoreToShow(score);
        showScore();
        seeHighScores = (Button) activity.findViewById(R.id.btn_scores_end);
        seeHighScores.setBackgroundResource(R.mipmap.highscores);
        seeHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment highScoresFragments = new HighScoresFragments();
                activity.getFragmentManager().beginTransaction().replace(R.id.endLayout, highScoresFragments).commit();
                activity.getFragmentManager().beginTransaction().addToBackStack(null);
            }
        });
        playAgain = (Button) activity.findViewById(R.id.btn_play_again);
        playAgain.setBackgroundResource(R.mipmap.play_again);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GamePlayActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("name", playerName);
                if (LoginActivity.lastLocation != null) {
                    intent.putExtra("lat", LoginActivity.lastLocation.getLatitude());
                    intent.putExtra("long", LoginActivity.lastLocation.getLongitude());
                } else {
                    intent.putExtra("lat", 0);
                    intent.putExtra("long", 0);
                }
                activity.startActivity(intent);
            }
        });
    }

    public static String setScoreToShow(int score) {
        int temp = score;
        StringBuilder stringBuilder = new StringBuilder("");
        if (score >= 1000) {
            stringBuilder.append(temp / 1000);
            stringBuilder.append(temp / 100);
            stringBuilder.append(":");
            stringBuilder.append((temp / 10) % 10);
            stringBuilder.append(temp & 10);
        }
        if (score >= 100 && score < 1000) {
            temp = score;
            stringBuilder.append('0');
            stringBuilder.append(temp / 100);
            stringBuilder.append(":");
            stringBuilder.append((temp / 10) % 10);
            stringBuilder.append(temp & 10);
        }
        if (score < 100 && score >= 10) {
            stringBuilder.append("00:" + score);
        }
        if (score < 10)
            stringBuilder.append("00:0" + score);

        return stringBuilder.toString();
    }

    public void showScore() {
        scoreText.setText(playerName + " your score is: " + setScoreToShow(score) + "!");
        if (newHighScore) {
            winAnimation();
        } else {
            loseAnimation();
        }
    }

    public void winAnimation() {
        imageView.setBackgroundResource(R.drawable.win);
        fireworks1 = (ImageView) activity.findViewById(R.id.fireworks1);
        fireworks2 = (ImageView) activity.findViewById(R.id.fireworks2);
        fireworks3 = (ImageView) activity.findViewById(R.id.fireworks3);
        fireworks1.setBackgroundResource(R.drawable.blue_fire);
        fireworks2.setBackgroundResource(R.drawable.red_fire);
        fireworks3.setBackgroundResource(R.drawable.green_fire);
        fireWorksAnim = (AnimationDrawable) fireworks1.getBackground();
        fireWorksAnim.start();
        fireWorksAnim = (AnimationDrawable) fireworks2.getBackground();
        fireWorksAnim.start();
        fireWorksAnim = (AnimationDrawable) fireworks3.getBackground();
        fireWorksAnim.start();
    }

    public void loseAnimation() {
        imageView.setBackgroundResource(R.drawable.lose);
        blast = (ImageView) activity.findViewById(R.id.blast);
        blast.setBackgroundResource(R.drawable.blast);
        fireWorksAnim = (AnimationDrawable) blast.getBackground();
        fireWorksAnim.start();
    }
}
