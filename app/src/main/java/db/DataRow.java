package db;

/**
 * Created by אילון on 30/11/2016.
 */

public class DataRow {
    public static final int NUMBER_GEN = 1;
    public static int numberGen = NUMBER_GEN;
    private String mName;
    private int level;
    private int score;


    public DataRow(String name,int level, int score) {
        mName = name;
        this.score = score;
        this.level = level;
    }

    public DataRow(int id, String name,int level, int score) {
        mName = name;
        this.score = score;
    }

    public DataRow(DataRow other) {
        if (other != null) {
            this.level = other.getLevel();
            this.mName = other.getmName();
        }
    }

    public String getmName() {
        return mName;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

}
