package hu.unideb.inf.Controller;

import hu.unideb.inf.Dao.HighScore;
import hu.unideb.inf.Dao.HighScoreDAO;
import hu.unideb.inf.Dao.Score;
import hu.unideb.inf.View.Colors;
import hu.unideb.inf.View.ElementController;
import hu.unideb.inf.View.MyRectangle;
import hu.unideb.inf.View.MyText;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The Main class.
 */
public class Main extends Application{

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static Grid grid = new Grid();
    private static Pane appRoot = new Pane();
    private static Colors colors = new Colors();
    private ElementController elementController = new ElementController();

    /**
     * Actual score.
     */
    public static int score = 0;

    private MyRectangle[][] rectangles = new MyRectangle[Grid.SIZE][Grid.SIZE];
    private MyText[][] texts = new MyText[Grid.SIZE][Grid.SIZE];

    private static final int WINWIDTH = 600;
    private static final int WINHEIGHT= 600;

    private Pane initScreen(){
        appRoot.setPrefSize(WINWIDTH, WINHEIGHT);
        appRoot.getChildren().addAll(elementController.rectangle);

        for (int i = 0; i < grid.SIZE; i++){
            for (int j = 0; j < grid.SIZE; j++){
                rectangles[i][j] = new MyRectangle(100+i*100,100+j*100);
                texts[i][j] = new MyText(125+i*100,115+j*100,0);
                appRoot.getChildren().addAll(rectangles[i][j],texts[i][j]);
            }
        }
        updateTiles();
        appRoot.getChildren().addAll(elementController.failLabel, elementController.newgame, elementController.scoreLabel,
                                     elementController.scoreText, elementController.highscore, elementController.listView,
                                     elementController.back);

        return appRoot;
    }

    private void updateTiles(){
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                double rectCentX = rectangles[i][j].getTranslateX() + (15);
                rectangles[i][j].setFill(colors.colorMap.get(grid.tiles[i][j].getValue()));
                if (grid.tiles[i][j].getValue()==0){
                    texts[i][j].setText("");
                } else {
                    if(grid.tiles[i][j].getValue() >= 2 && grid.tiles[i][j].getValue() <= 8){
                        texts[i][j].setTranslateX(rectCentX+10);
                    } else if(grid.tiles[i][j].getValue() >= 16 && grid.tiles[i][j].getValue() <= 64){
                        texts[i][j].setTranslateX(rectCentX+2);
                    } else if(grid.tiles[i][j].getValue() >= 128 && grid.tiles[i][j].getValue() <1024){
                        texts[i][j].setTranslateX(rectCentX-8);
                    } else if (grid.tiles[i][j].getValue() >= 1024 ){
                        texts[i][j].setTranslateX(rectCentX-18);
                    }
                    texts[i][j].setText(String.valueOf(grid.tiles[i][j].getValue()));
                }
            }
        }
        elementController.scoreText.setText(String.valueOf(score));
    }

    private void newgame(){
        elementController.newgame.setOnAction(event -> {
            logger.info("New game.");
            grid.resetGrid();
            score=0;
            updateTiles();
            elementController.blurOFF(rectangles,texts);
            elementController.failLabel.setVisible(false);
            elementController.newgame.setVisible(false);
            elementController.highscore.setVisible(false);
        });
    }

    private void highScore(){
        elementController.highscore.setOnAction(event -> {
            elementController.blurON(rectangles,texts,1);
            elementController.failLabel.setVisible(false);
            elementController.newgame.setVisible(false);
            elementController.highscore.setVisible(false);
            elementController.listView.setVisible(true);
            elementController.back.setVisible(true);
            initData(score);
            elementController.refreshTable();
        });
    }

    private void back(){
        elementController.back.setOnAction(event -> {
            elementController.back.setVisible(false);
            elementController.blurON(rectangles,texts,0);
            elementController.failLabel.setVisible(true);
            elementController.newgame.setVisible(true);
            elementController.highscore.setVisible(true);
            elementController.listView.setVisible(false);
        });
    }

   /**
    * Save the score and the actual date to the database.
    * @param score Actual score.
    */
    public static void initData(int score) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);

        Score score1 = new Score(String.valueOf(score),formattedDateTime);
        HighScoreDAO higshScoreDAO = new HighScoreDAO();

        HighScore highScore = new HighScore();
        highScore = higshScoreDAO.getHighScores();

        for ( Score score2 : highScore.getHighscore()){
            if (String.valueOf(score2).equals(score) && dateTime.equals(score2.getDate())){

            } else {

            }
        }

        higshScoreDAO.addScore(score1);

    }

    @Override
    public void start(Stage stage) throws Exception{
        Scene scene = new Scene(initScreen());

        scene.setOnKeyPressed(event -> {
            if (grid.noPossibleMove()){
                logger.info("No possible moves.");
                elementController.blurON(rectangles,texts,0);
                elementController.failLabel.setVisible(true);
                elementController.newgame.setVisible(true);
                elementController.highscore.setVisible(true);
                newgame();
                highScore();
                back();
            } else {
                elementController.blurOFF(rectangles,texts);
                if (event.getCode() == KeyCode.LEFT){
                    logger.debug("LEFT");
                    grid.move(Direction.LEFT);
                    grid.generateNewTile();
                    updateTiles();
                } else if (event.getCode() == KeyCode.RIGHT){
                    logger.debug("RIGHT");
                    grid.move(Direction.RIGHT);
                    grid.generateNewTile();
                    updateTiles();
                } else if (event.getCode() == KeyCode.UP){
                    logger.debug("UP");
                    grid.move(Direction.UP);
                    grid.generateNewTile();
                    updateTiles();
                } else if (event.getCode() == KeyCode.DOWN){
                    logger.debug("DOWN");
                    grid.move(Direction.DOWN);
                    grid.generateNewTile();
                    updateTiles();
                }
            }
        });

        stage.setTitle("2048 game");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method.
     * @param args System args.
     */
    public static void main(String[] args) {
        logger.info("Game started.");
        launch(args);
    }
}
