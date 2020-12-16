package sample.dataoperation;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * @author Xinyuan Zuo
 * This class is to record some info when the game is running
 */
public class UserStatistic {
    private static Instant m_Baseline;
    private static String m_userName;
    private static long time_total_consumption;
    private static int current_score;
    private static int moves_total_consumption;
    private static int moves_consumption;
    private static int level;
    private static long time_consumption;

    public static int GetLevel() {
        return level;
    }

    public static void SetUserName(String userName) {
        UserStatistic.m_userName = userName;
    }

    public static String GetUserName() {
        return m_userName;
    }

    public static  void Initialize_score () {
        time_total_consumption=0;
        current_score=0;
        level=1;
    }

    public static int GetCurrent_score() {
        return current_score;
    }

    public static void  AddCurrent_score(int add_score) {
        current_score += add_score;
    }

    public static void Start_time (){
        m_Baseline = Instant.now();
    }

    public static long GetTime_cost(){
        Instant inst3 = Instant.now();
        time_consumption= Duration.between(m_Baseline, inst3).getSeconds();
        time_total_consumption+=time_consumption;
        return time_consumption;
    }

    public static long GetTime_total_consumption() {
        return time_total_consumption;
    }

    public static int GetMoves_total_consumption() {
        return moves_total_consumption;
    }

    public static int GetMoves_consumption() {
        return moves_consumption;
    }

    public static void AddMoves_consumption(){
        moves_consumption++;
        moves_total_consumption++;
    }
    public static void Init_consumption(){
        moves_consumption=0;
        m_Baseline = Instant.now();

    }

    public static void AddLevel() {
        level++;
    }

    /**
     * @author Xinyuan Zuo
     * @throws IOException
     * This method is run when user pass a level to info user his info
     * and write his grade into HighScoreList if he exceeds the top ten players
     */
    public static void set_print_write_Score() throws IOException {
        UserStatistic.AddCurrent_score(1);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("ALERT");
        if (level==3){
            alert.headerTextProperty().set("Congratulations! Cleared!");
        }else{
            alert.headerTextProperty().set("INFORMATION");
        }
        alert.setContentText("UserName :"+UserStatistic.GetUserName()+"\nCurrent Level :"+UserStatistic.GetCurrent_score()
                +"\nMoves :"+UserStatistic.GetMoves_consumption()+"  Total Moves:"+UserStatistic.GetMoves_total_consumption()
                +"\nTimeCost :"+UserStatistic.GetTime_cost()+"S"
                +"  Total TimeCost :"+UserStatistic.GetTime_total_consumption()+"S"
        );
        alert.show();

        String[] new_user_info = new String[3];
        new_user_info[0]=m_userName;
        new_user_info[1]=time_consumption+"";
        new_user_info[2]=moves_consumption+"";
        ScoreRecord.Write_rank(UserStatistic.GetLevel(),new_user_info);
        UserStatistic.Init_consumption();
        UserStatistic.Start_time();
    }


}
