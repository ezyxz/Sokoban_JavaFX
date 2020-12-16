package sample.ui;
import javafx.scene.paint.Color;

/**
 * @author Xinyuan Zuo
 * This class is to convert color as String to color as javafx.scene.paint.Color
 */

public class BackgroundColor {
    private static Color m_select_color;

    public static Color GetSelect_color() {
        return m_select_color;
    }

    public static void SetSelect_color(String select_color) {
        switch (select_color) {
            case "RED":
                BackgroundColor.m_select_color = Color.web("0xFF4500",1.0);;
                break;
            case "YELLOW":
                BackgroundColor.m_select_color = Color.web("0xFFFF00",1.0);;
                break;
            case "BLUE":
                BackgroundColor.m_select_color = Color.web("0x00FFFF",1.0);;
                break;
            case "GREEN":
                BackgroundColor.m_select_color = Color.web("0x00FF00",1.0);;
                break;
            case "GRAY":
                BackgroundColor.m_select_color = Color.web("0x696969",1.0);;
                break;
            case "CHOCOLATE":
                BackgroundColor.m_select_color = Color.web("0xCD661D",1.0);;
                break;
            case "HOTPINK":
                BackgroundColor.m_select_color = Color.web("0xFF6EB4",1.0);;
                break;
                default:
                BackgroundColor.m_select_color = Color.BLACK;
        }
    }
}
