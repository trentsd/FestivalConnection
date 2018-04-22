package festival_package;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;


public class Controller_Main {

    private Stage primaryStage = new Stage();
    private AnchorPane rootPane;

    //the guid of the logged in User. Used for many queries


    @FXML
    ListView search_listview;

    @FXML
    ComboBox<String> search_dropdown = new ComboBox<>();

    @FXML
    Button search_button;

    @FXML
    Label description_pane;

    @FXML
    TextField search_field;

    ListProperty<String> listProperty = new SimpleListProperty<>();



    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(
                getClass().getResource(fxml));

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/festival_package/resources/Festival_Logo2.png"));
        primaryStage.setTitle(Database.fix_title(fxml));
        primaryStage.show();

    }

    @FXML
    public void initialize()
    {

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Festival");
        choices.add("User");

        search_dropdown.getItems().addAll(choices);

        search_dropdown.setValue("User");

        search_dropdown.setButtonCell(new ListCell<String>()
        {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER_RIGHT);
                }
            }

        });



        search_dropdown.setValue("User");
        try {
            Database.set_viewed_list(Database.User_Names);
            Database.refresh_users();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        listProperty.set(FXCollections.observableArrayList(Database.viewed_list));
        search_listview.itemsProperty().bind(listProperty);


        search_dropdown.setOnAction(
                event ->
                {
                    if(search_dropdown.getSelectionModel().getSelectedItem().equals("User"))
                    {
                        search_field.setText("");
                        Database.set_viewed_list(Database.User_Names);
                    }
                    else
                    {
                        search_field.setText("");
                        Database.set_viewed_list(Database.Festivals_Names);
                    }

                    try {
                        Database.refresh_users();
                    } catch (SQLException e) {
                        System.out.println("Empty");
                    } catch (ParseException e)
                    {
                        System.out.println("Parse failed");
                    }

                    listProperty.set(FXCollections.observableArrayList(Database.viewed_list));
                    search_listview.itemsProperty().bind(listProperty);
                }
        );

        search_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if(search_dropdown.getSelectionModel().getSelectedItem().equals("User"))
                {
                    description_pane.setText((Database.Users.get(Database.User_Names.indexOf(search_listview.getSelectionModel().getSelectedItem())).description()));
                }
                else
                {
                    description_pane.setText((Database.Festivals.get(Database.Festivals_Names.indexOf(search_listview.getSelectionModel().getSelectedItem())).description()));
                }
            }


        });



    }

    public void on_search_button(ActionEvent event) throws SQLException, ParseException {
        System.out.println("Fuuuuck!");

        Database.refresh_users();
        Database.set_viewed_list(Database.User_Names);

        listProperty.set(FXCollections.observableArrayList(Database.viewed_list));

        search_listview.itemsProperty().bind(listProperty);
    }


    public void on_friend_button(ActionEvent event) throws IOException
    {
        changeScene("Bookmarks_and_Friends.fxml");
        System.out.println("Friend adding");
    }


    public void on_favorite_button(ActionEvent event) throws IOException
    {
        changeScene("Bookmarks_and_Friends.fxml");
        System.out.println("Favorite adding");
    }

    public void on_advanced_button(ActionEvent event) throws IOException {
        changeScene("AdvancedSearch.fxml");
        System.out.println("advanced search");
    }

    public void add_friend_button(ActionEvent event) {

    }

}
