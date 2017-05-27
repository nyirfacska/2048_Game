package hu.unideb.inf.Dao;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *The HighScore class creates a list which contains {@link Score}.
 */

@XmlRootElement(name = "highscores")
public class HighScore {
    private ArrayList<Score> highscore;


    /**
     * Creates an empty instance of {@code HighScore}.
     */
    public HighScore() {
        this.highscore = new ArrayList<Score>();
    }

    /**
     * Sorted the scores and get the first five high scores.
     * @return highscore score.
     */
    public ArrayList<Score> getHighscore() {
        Collections.sort(highscore, (hs1, hs2) -> {
            return Integer.parseInt(hs2.getScore()) - Integer.parseInt(hs1.getScore());
        });
        if (highscore.size() > 5) {
            for (int i = 0; i < highscore.size(); i++) {
                highscore.subList(5, highscore.size()).clear();
            }
        }
        return highscore;
    }

    /**
     * Add a new {@link Score} to the highscore list.
     *
     * @param score Score.
     */

    public void addScore(Score score){
        highscore.add(score);
    }
}
