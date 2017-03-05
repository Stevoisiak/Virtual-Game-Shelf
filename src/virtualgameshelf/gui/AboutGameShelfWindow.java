package virtualgameshelf.gui;

import javafx.scene.control.Alert;

public class AboutGameShelfWindow extends Alert {
    // Display 'about' menu
    public AboutGameShelfWindow() {
        super(AlertType.INFORMATION);
        this.setTitle("About Virual Game Shelf");
        this.setHeaderText(null);

        // set window icon
        //Stage alertStage = (Stage) this.getDialogPane().getScene().getWindow();
        //alertStage.getIcons().add( new Image("resources/icons/about.png") );

        // replace the standard icon on the left
        //this.setGraphic( new ImageView( new Image("resources/icons/about.png", 64, 64, true, true) ) );

        // TODO: Use clickable hyperlinks for website credit
        this.setContentText("Virtual Game Shelf created by\n"
                + " -Morgan \"DrathianGull\" Abrams\n"
                + " -Arielle Tabuteau\n"
                + " -Steven \"Stevoisiak\" Vascellaro\n"
                + "\n"
                + "Icons courtesy of material.io\n"
                + "\n"
                + "Inspired by The Backloggery & My Game Collection");

    }
}
