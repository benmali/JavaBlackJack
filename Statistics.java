package blackjackgame;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Statistics implements Parcelable {
    private ArrayList<Boolean> stats;
    public Statistics(ArrayList <Boolean> stats){
        this.stats = stats;
    }
    public Statistics(){
        this.stats = new ArrayList<>();
    }
    public String getStats(){
       float numberOfGames = this.stats.size();
        float numberOfWins = 0;
        float stats;
        for( boolean value: this.stats){
            if(value){
                numberOfWins++;
            }
        }
        stats = numberOfWins/numberOfGames;
        String statsToShow = String.valueOf(stats*100);
        String games = numberOfWins + "/" + numberOfGames;
        String message = "Win percentage is " + statsToShow+ "  " + games;
        return message;
    }
    public void addGameResult(boolean result){
        this.stats.add(result);
    }
    public void deleteStats(){
        this.stats = new ArrayList<Boolean>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.stats);
    }

    protected Statistics(Parcel in) {
        this.stats = new ArrayList <Boolean>();
        in.readList(this.stats, Boolean.class.getClassLoader());
    }
    public static final Parcelable.Creator<Statistics> CREATOR = new Parcelable.Creator<Statistics>() {
        @Override
        public Statistics createFromParcel(Parcel source) {
            return new Statistics(source);
        }

        @Override
        public Statistics[] newArray(int size) {
            return new Statistics[size];
        }
    };
}
