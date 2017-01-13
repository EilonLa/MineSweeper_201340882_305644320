package db;


/**
 * Created by אילון on 30/11/2016.
 */

public class DataRow {
    private String mName;
    private int level;
    private int score;
    private String lat;
    private String longt;

    public DataRow(String name, int level, int score, double lat, double longT) {
        mName = name;
        this.score = score;
        this.level = level;
        this.lat = String.valueOf(lat);
        this.longt = String.valueOf(longT);
    }

    public DataRow(int id, String name,int level, int score, double lat, double longT) {
        mName = name;
        this.score = score;
        this.level = level;
        this.lat = String.valueOf(lat);
        this.longt = String.valueOf(longT);
    }

    public DataRow(DataRow other) {
        if (other != null) {
            this.level = other.getLevel();
            this.mName = other.getmName();
            this.lat = other.getLatidude();
            this.longt = other.getLongtitude();
        }
    }
    public String getLongtitude() {
        return longt;
    }

    public String getLatidude() {
        return lat;
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
