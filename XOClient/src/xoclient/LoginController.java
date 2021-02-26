package xoclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static xoclient.XOClient.client;

/**
 * FXML Controller class
 *
 * @author Aalaa Habib
 */
public class LoginController implements Initializable {

    String playerName = "";
    String playerPassword = "";
    Validation valid = new Validation();
    @FXML
    public TextField userName;
    public TextField password;
    public Button loginBtn;
    public Button registerBtn;
    public Label errorLb;
    public Label passError;
    boolean login = false;

    Stage primaryStage;

    public LoginController(Stage _primaryStage) {
        this.primaryStage = _primaryStage;
    }

    public void clear() {
        errorLb.setText("");
        passError.setText("");
    }

    public boolean validation() {
        boolean check_name, check_pass;
        clear();
        check_name = valid.isValid_userName(userName.getText());
        check_pass = valid.isValid_password(password.getText());
        if (!check_name || !check_pass) {
            if (!check_name) {
                errorLb.setText("Name must be charachters only [3,20] digit");
            }
            if (!check_pass) {
                passError.setText("Password must be 8 digit at least upTo 20");
            }
            return false;
        }
        return true;
    }

    public String sendData_login() {
        String Data;
        playerName = userName.getText();
        playerPassword = password.getText();
        Data = "#login" + "," + playerName + "," + playerPassword;
        return Data;
    }

    public String sendData_register() {
        String Data;
        playerName = userName.getText();
        playerPassword = password.getText();
        Data = "#register" + "," + playerName + "," + playerPassword;
        return Data;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void min(MouseEvent event) {
        // System.err.println("min");
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @FXML
    private void max(MouseEvent event) {
        //Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        //s.setFullScreen(true);
    }

    @FXML
    private void close(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.close();
    }

    @FXML
    private void login(ActionEvent event) {
        if (client == null) {
            client = new ClientThread("127.0.0.1", 4433, this);
        }
        if (validation()) {
            client.sendMsg(sendData_login());
            login = true;
        }
    }

    @FXML
    private void register() {
        if (client == null) {
            client = new ClientThread("127.0.0.1", 4433, this);
        }
        if (validation()) {
            client.sendMsg(sendData_register());
            login = false;
        }
    }

    public void authorizeResult(String msg) {
        if (msg.equals("#done")) {
            successLogin();
        } else if (msg.equals("#no")) {
            if (login) {
                failedLogin();
            } else {
                failedRegister();
            }

        }
    }

    public void successLogin() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Athentication");
        alert.setHeaderText("Login Info");
        alert.setContentText("Logged Successfully ");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                    // Create a controller instance
                    DashboardController dashboardController = new DashboardController(primaryStage, playerName);
                    // Set it in the FXMLLoader
                    loader.setController(dashboardController);
                    primaryStage.setTitle("XO Dashboard");
                    Scene scene = new Scene((Parent) loader.load());
                    primaryStage.setScene(scene);
//                    primaryStage.show();

                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void failedLogin() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Athentication");
        alert.setHeaderText("Login Failed");
        alert.setContentText("Wrong Username or Password !");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    public void failedRegister() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Registration");
        alert.setHeaderText("Registration Failed");
        alert.setContentText("Username Exists, Try Another One !");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    public void playWithAi() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SinglePlayer.fxml"));
        // Create a controller instance
        SinglePlayerController singlePlayerController = new SinglePlayerController(primaryStage);
        // Set it in the FXMLLoader
        loader.setController(singlePlayerController);
        primaryStage.setTitle("XO Dashboard");
        Scene scene = new Scene((Parent) loader.load());
        primaryStage.setScene(scene);
    }
}
