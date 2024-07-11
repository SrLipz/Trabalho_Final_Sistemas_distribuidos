import java.io.Serializable;

public class Player implements Serializable {
    private String Name;
    private int Score = 0;

    public Player(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public void AddScore(int ScoreToAdd){
        this.Score += ScoreToAdd;
    }
}
