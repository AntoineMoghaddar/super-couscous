package model;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CousCousMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("JavaFX/login/rb_login.fxml"));
//        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}