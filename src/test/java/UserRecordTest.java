import org.junit.Test;
import sample.dataoperation.ScoreRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserRecordTest {
    /**
     * @Author Xinyuan Zuo
     * @throws IOException
     * This is simulate when a player's time cost is better than every player in HighScoreList
     * if the  ScoreRecord.Write_rank() method write his score to the top of the list
     * by testing if the HighScoreList first line is equal to the Sting which is written
     */
    @Test
    public void HighScoreListRecordTest() throws IOException {
        int level=1;
        String Test_user_info= new String("JunitTest/1/1"); //In this game, the user info is record
                                                                     // like "UserName/Time/Moves"
        String[] info_arr = Test_user_info.split("/");
        ScoreRecord.Write_rank(level,info_arr);
        BufferedReader br = new BufferedReader(new FileReader("data/HighScoreList_level"+level+".txt"));
        String first_line=br.readLine();
        br.close();
        assert (first_line.equals(Test_user_info));
    }

    /**
     * @author Xinyuan Zuo
     * @throws IOException
     * This is simulate if a player's time and moves don't meet top 10 requirement
     * and the HighScoreList is 10 user info
     * if his record would be record
     * by simulating writing 20 player with the same score
     */
    @Test
    public void HighScoreListRecordTest2() throws IOException {
        int level=3;
        String Test_user_info= new String("JunitTest2/999/999");
        String[] info_arr = Test_user_info.split("/");
        for (int i = 0; i < 20; i++) {
            ScoreRecord.Write_rank(level,info_arr);
        }
        int user_count=0;                 //count how many lines in HighScoreList
        BufferedReader br = new BufferedReader(new FileReader("data/HighScoreList_level"+level+".txt"));
        String[] lines_arr = new String[10];
        String lines;
        while ((lines=br.readLine())!= null){
            lines_arr[user_count]=lines;
            user_count++;
        }
        br.close();
        assert (user_count==10);         //Judge if lines in HighScoreList is 10

    }

}
