package sample.dataoperation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreRecord {
    /**
     * @author Xinyuan Zuo
     * @param UserName
     * @param Score
     * This method is to write a line including username and his score with the style(xxx/20)
     * to ScoreList.txt in data directory
     */

    public static void Write(String UserName, int Score) {
        FileWriter fw = null;
        try {
            File f=new File("data/ScoreList.txt");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(UserName+"/"+Score);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Xinyuan Zuo
     * @return Map<UserName,Score>
     * @throws IOException
     * This method is to read a every line in ScoreList.txt in data directory
     * and store to a map with key(username) and value(score)
     */

    public static Map<String, Integer> Read() throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader("data/ScoreList.txt"));
        Map<String, Integer> map = new HashMap<>();
        String s = null;
        while((s = br.readLine())!=null){
            String[] info = s.split("/");
            String UserName=info[0];
            if (UserName.isEmpty())
                break;
            Integer Score = Integer.valueOf(info[1]);
            if (map.containsKey(UserName)){
                map.put(UserName+"_Duplicate",Score);
            }else{
                map.put(UserName,Score);
            }

        }
        br.close();

        return map;
    }

    /**
     * @author Xinyuan Zuo
     * @param level
     * @return StringBuffer with all info in the file
     * @throws IOException
     * This method is to read info in specific level list text file in data like HighScoreList_level1.txt
     * in these file the data is witten by the syntax (UserName/Time/Moves)
     */

    public static StringBuffer Read_rank(int level) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data/HighScoreList_level"+level+".txt"));
        StringBuffer  m_info_user = new StringBuffer();
        String s = null;
        while((s = br.readLine())!=null){
            String[] info = s.split("/");
            m_info_user.append("User: "+info[0]+" Time: "+info[1]+" Moves: "+info[2]+"\n");
        }
        br.close();
        return m_info_user;
    }


    /**
     * @author Xinyuan Zuo
     * This inner class is to record User information
     */
    public static class UserInfo{
        private String Username;
        private int time;
        private int Moves;

        public UserInfo(String username, int time, int moves) {
            Username = username;
            this.time = time;
            Moves = moves;
        }

        public int getTime() {
            return time;
        }

        public int getMoves() {
            return Moves;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "Username='" + Username + '\'' +
                    ", time=" + time +
                    ", Moves=" + Moves +
                    '}';
        }
    }

    /**
     * @author Xinyuan Zuo
     * @param fileName
     * This method is to clear a file
     */
    private static void clearInfoForFile(String fileName) {
        File file =new File(fileName);;
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @author Xinyuan Zuo
     * @param level
     * @param new_user_info
     * @throws IOException
     * This method is to write the username whose grade is in top10
     * by read all the info in existed file and clear it then judge which rank the new user is
     * finally write all to the file
     */
    public static void Write_rank(int level,String[] new_user_info) throws IOException {
        ArrayList<UserInfo> m_userinfo_array = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("data/HighScoreList_level"+level+".txt"));
        String s = null;

        while((s = br.readLine())!=null){
            String[] info = s.split("/");
            String username=info[0];
            if (username=="")
                break;
            int time_cost=Integer.parseInt(info[1]);
            int moves=Integer.parseInt(info[2]);
            m_userinfo_array.add( new UserInfo (username,time_cost,moves));
        }

        String username=new_user_info[0];
        int time_cost=Integer.parseInt(new_user_info[1]);
        int moves=Integer.parseInt(new_user_info[2]);
        UserInfo m_user_info = new UserInfo(username,time_cost,moves);

        if (m_userinfo_array.size()<10 ||
                m_userinfo_array.get(9).getTime()>=time_cost ||
                (m_userinfo_array.get(9).getTime()==time_cost && m_userinfo_array.get(9).getMoves()>moves)){
            //insert
            int i;
            for ( i=0;i<=9;i++){
                if (i>=m_userinfo_array.size())
                    break;
                if (m_userinfo_array.get(i).getTime()>time_cost) {
                    break;
                }else if (m_userinfo_array.get(i).getTime()==time_cost && m_userinfo_array.get(i).getMoves()>moves){
                    break;
                }
            }
            m_userinfo_array.add(i,m_user_info);
            if (m_userinfo_array.size()>10)
                m_userinfo_array.remove(10);
        }else {
            return;
        }
        Write_array(level,m_userinfo_array);
//        for (UserInfo m_print:m_userinfo_array){
//            System.out.println(m_print);
//        }
    }

    /**
     * @author Xinyuan Zuo
     * @param level
     * @param m_userinfo
     * This method is to write a list of UserInfo to the file
     */

    public static void Write_array(int level,ArrayList<UserInfo> m_userinfo) {
        clearInfoForFile("data/HighScoreList_level"+level+".txt");
        FileWriter fw = null;
        try {
            File f=new File("data/HighScoreList_level"+level+".txt");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        for (UserInfo m_print:m_userinfo){
            String input=m_print.Username+"/"+m_print.time+"/"+m_print.Moves;
            pw.println(input);
        }

        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
