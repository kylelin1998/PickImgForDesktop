package code.ui;

import code.config.Config;
import code.config.I18nEnum;
import code.util.ProgramUtil;
import javafx.scene.control.Alert;
import javafx.stage.Window;

public class MessageUI {

    public static void warning(String message) {
        warning(null, message);
    }
    public static void warning(Window window, String message) {
        ProgramUtil.activate(Config.MetaData.ProcessName);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(I18nEnum.Title.getText());
        alert.setContentText(message);

        alert.initOwner(window);

        alert.showAndWait();
    }
    public static void info(String message) {
        info(null, message);
    }
    public static void info(Window window, String message) {
        ProgramUtil.activate(Config.MetaData.ProcessName);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(I18nEnum.Title.getText());
        alert.setContentText(message);

        alert.initOwner(window);

        alert.showAndWait();
    }

}
