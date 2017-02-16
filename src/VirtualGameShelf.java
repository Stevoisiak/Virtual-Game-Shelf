package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VirtualGameShelf extends Application {
    public static void main(String[] args) {
        // Automatic VM reset, thanks to Joseph Rachmuth.
        try {
            launch(args);
            System.exit(0);
        }
        catch (Exception error) {
            error.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setTitle("Virtual Game Shelf");

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        mainStage.show();
    }
}
