package festival_package;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class Controller_Login {

    //Used for getting the login screen's window in order to close it at successful login
    @FXML
    AnchorPane login_anchor;

    @FXML
    TextField username_field;

    @FXML
    TextField password_field;

    @FXML
    Button login_button;

    @FXML
    Button create_account_button;

    @FXML
    Text login_error_text;

    /**
     * Sets error text to invisible.
     */
    @FXML
    public void initialize(){login_error_text.setVisible(false);}


    /**
     * Uses authenticate(String, String) helper method in Database.java to query database.
     * Sets login_error_text to visible if empty textfield or the given username is not in the ResultSet of the query.
     * TODO: Add password authentication
     * If username is present in database, stores guid and launches main window, passing username and guid to
     * main window using launchMainWindow(String, String)
     * @param event
     * @throws SQLException
     */
    public void on_login_button(ActionEvent event) throws SQLException{

        Stage stage =(Stage) login_anchor.getScene().getWindow();
        String username = username_field.getText();

        if (username.equals("")){
            login_error_text.setVisible(true);
        }
        else{
            String guid = Database.authenticate(username, password_field.getText());
            if (guid.equals("")){
                login_error_text.setVisible(true);
            }
            else{
                try {
                    launchMainWindow(username, guid);
                    stage.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        System.out.println("[" + username + "] Logging in...");

    }

    /**
     * Launches the main window for the program.
     * Creates a Controller_Main object so the guid can be stored in the userGuid field in Controller_Main.java.
     * Sets the title of the main window to display the user's username.
     * @param username
     * @param guid THE USER'S GUID, STORED IN THE userGuid FIELD IN CONTROLLER_MAIN.JAVA
     *                  USING Controller_Main.initData(String) METHOD
     * @throws IOException
     */
    public void launchMainWindow(String username, String guid) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_window.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        stage.getIcons().add(new Image("file:src/festival_package/resources/Festival_Logo2.png"));
        stage.setTitle("Festival Connection - Welcome " + username);

        Database.cur_user_guid = guid;
        Database.cur_user_name = username;


        stage.show();
    }




    public void on_create_account_button(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create_account_window.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        stage.getIcons().add(new Image("file:src/festival_package/resources/Festival_Logo2.png"));
        stage.setTitle("Create Account");

        Controller_CreateAccount controller = loader.<Controller_CreateAccount>getController();

        stage.show();
    }
}
