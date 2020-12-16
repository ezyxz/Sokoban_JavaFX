package sample.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.dataoperation.ScoreRecord;
import sample.dataoperation.UserStatistic;
import sample.scene.APP;
import sample.scene.Main;
import sample.ui.BackgroundColor;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Xinyuan Zuo
 * This controller class implements the START screen all the button metonds
 */
public class Controller implements Initializable {
    private String m_str_select_color;
    @FXML
    private AnchorPane m_start_anPane;
    @FXML
    private Button m_start_button;
    @FXML
    private Label m_start_home_label;
    @FXML
    private ComboBox m_colors_combobox;
    @FXML
    private Label m_start_userName_Label;
    @FXML
    private Label m_start_colorSelect_Label;
    @FXML
    private TextField m_start_userName_TextField;
    @FXML
    private Button m_start_viewUser_button;
    @FXML
    private Button m_music_button;
    private int music_status=1;

    @FXML
    private void start_button_action() throws Exception {
        String inputText=m_start_userName_TextField.getText();
        if (m_str_select_color==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("ALERT");
            alert.headerTextProperty().set("Select A Color");
            alert.show();

        }else if (inputText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("ALERT");
            alert.headerTextProperty().set("Input UserName");
            alert.show();
        } else{
            UserStatistic.SetUserName(inputText);
            BackgroundColor.SetSelect_color(m_str_select_color);
            System.out.println(m_str_select_color);
            Main.Close_main();
            Stage gameStage = new Stage();
            APP app = new APP();
            app.start(gameStage);
        }
    }
    @FXML
    private void color_combobox_action() throws Exception {
        Object obj_color=m_colors_combobox.getValue();
        m_str_select_color=(String) obj_color;
        if (obj_color.equals("RED")){
            m_start_anPane.setStyle("-fx-background-color:#FF4500;");
        }else if (obj_color.equals("YELLOW")){
            m_start_anPane.setStyle("-fx-background-color:#FFFF00;");
        }else if (obj_color.equals("BLUE")){
            m_start_anPane.setStyle("-fx-background-color:#00FFFF;");
        }else if (obj_color.equals("GREEN")){
            m_start_anPane.setStyle("-fx-background-color:#00FF00;");
        }else if (obj_color.equals("GRAY")){
            m_start_anPane.setStyle("-fx-background-color:#696969;");
        }else if (obj_color.equals("CHOCOLATE")){
            m_start_anPane.setStyle("-fx-background-color:#CD661D;");
        }else if (obj_color.equals("HOTPINK")){
            m_start_anPane.setStyle("-fx-background-color:#FF6EB4;");
        }
        else {
            m_start_anPane.setStyle("-fx-background-color:#000000;");
        }


    }
    @FXML
    private void start_view_button() throws IOException {
        Map<String, Integer> user_map = ScoreRecord.Read();
        StringBuffer sbf = new StringBuffer();
        for (String key : user_map.keySet()){
            String str = "UserName: "+key+"  Level: "+user_map.get(key);
            sbf.append(str);
            sbf.append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("ALERT");
        alert.headerTextProperty().set("User Record");
        alert.setContentText(sbf.toString());
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] colors = {"RED", "YELLOW", "BLUE", "GREEN", "GRAY", "CHOCOLATE", "HOTPINK", "BLACK"};
        m_colors_combobox.setItems(FXCollections.observableArrayList(colors));
    }
    @FXML
    private void start_music(){
        if (music_status==0){
            music_status=1;
            Image image = new Image(this.getClass().getResource("/img/on.png").toString(),25,25,false,false);
            m_music_button.setGraphic(new ImageView(image));
            System.out.println("Music on");
            Main.GetPlayer().play();
        }else{
            music_status=0;
            System.out.println("Music off");
            System.out.println(this.getClass().getResource(""));
            Image image = new Image(this.getClass().getResource("/img/off.png").toString(),25,25,false,false);
            m_music_button.setGraphic(new ImageView(image));
            Main.GetPlayer().stop();
        }
    }
}
