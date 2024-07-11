package com.example.onlinebanksystem;

import com.example.onlinebanksystem.GcashApp.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ControlManager implements Initializable {
    //global Variables Start
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserAuthentication currentUser;
    private UserAuthentication searchedUser;
    private ControlManager controlManager23;
    private StringBuilder error;
    @FXML private Circle circle_MinimiseShadow;
    @FXML private Circle circle_CloseShadow;
    //end

    //Register Variables Start
    @FXML private Label fNameAsterisk,lNameAsterisk,EmailAsterisk,mNumberAsterisk,PinAsterisk,cPinAsterisk,errorLabel;
    @FXML private TextField txtField_fname,txtField_Mname,txtField_Lname,txtField_Email,txtField_MobileNum;
    @FXML private TextField RegtxtField_Pin,RegtxtField_PinConfirm;
    @FXML private PasswordField RegPasstxtField_Pin,RegPasstxtField_PinConfirm;
    @FXML private ImageView regHiddenPass,regViewPass,regHiddenPassConfirm,regViewPassConfirm;
    //end

    //Login Variables Start
    @FXML private ImageView hiddenPass,viewPass;
    @FXML private TextField txtField_Mnumber,txtField_Pin;
    @FXML private PasswordField passtxtField_Pin;
    //end

    //Cash Transfer Variables Start
    @FXML private TextField txtFieldSearchNumber,PINtxtField;
    @FXML private VBox cashTransferAmountPanel;
    @FXML private Label msgError,cashINBalanceView;
    @FXML private Button cashTransferNext,cashTransferSend;
    @FXML private Label accName,accNumber,AmountTotransfer,totalAmountToTransfer,errorBalance;
    @FXML private CheckBox confirmCheckbox;
    @FXML private HBox pinPanel,numPanel;
    //end

    //Cash In Variables Start
    @FXML private TextField CashInAmountTxtField;
    @FXML private Label accountName,accountNumber;
    //end

    //UserDashboard Start
    @FXML private Label userBalance,userBalanceHIDE;
    @FXML private ImageView hiddenBalance,viewBalance;
    //end

    //Transaction Variables Start
    ObservableList<Transaction> observableList = FXCollections.observableArrayList();
    private String[] menu = {"All","User ID","Transaction ID"};
    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, Integer> colID;
    @FXML private TableColumn<Transaction, Float> colAmount;
    @FXML private TableColumn<Transaction, String> colName;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, Integer> colTransferToID;
    @FXML private TableColumn<Transaction, Integer> colTransferFromID;
    @FXML private ChoiceBox<String> filterBox;
    @FXML private TextField filterTextfield;
    @FXML private FlowPane searchPane;
    //end

    //Change PIN Variables Start
    @FXML private TextField oldPINtxtField,newPINtxtField,newPINConfirmtxtField;
    //end

    //Global Methods Start

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML protected void togglePasswordVisibility(boolean showPassword) {
        if (showPassword) {
            passtxtField_Pin.setVisible(false);
            txtField_Pin.setText(passtxtField_Pin.getText());
            txtField_Pin.setVisible(true);
            hiddenPass.setVisible(false);
            viewPass.setVisible(true);
        } else {
            passtxtField_Pin.setVisible(true);
            passtxtField_Pin.setText(txtField_Pin.getText());
            txtField_Pin.setVisible(false);
            viewPass.setVisible(false);
            hiddenPass.setVisible(true);
        }
    }

    @FXML protected void onMouseClickToHiddenPassBtn() {
        togglePasswordVisibility(false);
    }

    @FXML protected void onMouseClickToViewPassBtn() {
        togglePasswordVisibility(true);
    }

    @FXML protected void onMouseClickToView_Register_PassBtn() {
        toggleVisibility(RegtxtField_Pin, RegPasstxtField_Pin, regHiddenPass, regViewPass, true);
    }

    @FXML protected void onMouseClickToHidden_Register_PassBtn() {
        toggleVisibility(RegtxtField_Pin, RegPasstxtField_Pin, regHiddenPass, regViewPass, false);
    }

    @FXML protected void onMouseClickToView_Register_Confirm_PassBtn() {
        toggleVisibility(RegtxtField_PinConfirm, RegPasstxtField_PinConfirm, regHiddenPassConfirm, regViewPassConfirm, true);
    }

    @FXML protected void onMouseClickToHidden_Register_Confirm_PassBtn() {
        toggleVisibility(RegtxtField_PinConfirm, RegPasstxtField_PinConfirm, regHiddenPassConfirm, regViewPassConfirm, false);
    }

    public void setControlManager(ControlManager currentControlManager){
        this.controlManager23 = currentControlManager;
    }

    private void toggleVisibility(TextField textField, PasswordField passwordField, ImageView hiddenIcon, ImageView viewIcon, boolean isVisible) {
        passwordField.setVisible(!isVisible);
        textField.setText(passwordField.getText());
        textField.setVisible(isVisible);
        hiddenIcon.setVisible(!isVisible);
        viewIcon.setVisible(isVisible);
    }

    @FXML protected void onMouseClickToToggleBalanceBtn() {
        boolean isViewBalanceVisible = viewBalance.isVisible();
        userBalance.setVisible(!isViewBalanceVisible);
        userBalanceHIDE.setVisible(isViewBalanceVisible);
        viewBalance.setVisible(!isViewBalanceVisible);
        hiddenBalance.setVisible(isViewBalanceVisible);
    }

    public void close(ActionEvent actionEvent)throws IOException{
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void addTextFieldListener(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (textField.getText().length() > maxLength) {
                textField.setText(textField.getText().substring(0, maxLength));
            }
        });
    }

    private void addFloatTextFieldListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove non-digit and non-decimal point characters, including specified symbols
            newValue = newValue.replaceAll("[^\\d.]", "");

            // Split the new value at the decimal point
            String[] parts = newValue.split("\\.");

            // Check if the value before the decimal point is all zeros
            if (parts.length > 0 && parts[0].matches("0+")) {
                parts[0] = "0";
            } else {
                // Limit digits before the decimal point to 6
                if (parts.length > 0 && parts[0].length() > 6) {
                    parts[0] = parts[0].substring(0, 6);
                }
            }

            // Limit digits after the decimal point to 2
            if (parts.length > 1 && parts[1].length() > 2) {
                parts[1] = parts[1].substring(0, 2);
            }

            // Join the parts back together
            StringBuilder formattedValue = new StringBuilder();

            // Check if there is a part before the decimal point
            if (parts.length > 0 && !parts[0].isEmpty()) {
                formattedValue.append(parts[0]);
            }

            // Check if there is a part after the decimal point
            if (parts.length > 1) {
                formattedValue.append('.').append(parts[1]);
            } else if (newValue.endsWith(".")) {
                // If the new value ends with a dot, ensure it is preserved
                formattedValue.append('.');
            }

            // Update the text field with the formatted value
            textField.setText(formattedValue.toString());
        });
    }

    public void displayTransferStep1Content(ControlManager controlManager) {
        addTextFieldNumberListener(controlManager.txtFieldSearchNumber, 10 ,controlManager);
        addFloatTextFieldTransferAmountListener(controlManager.CashInAmountTxtField,controlManager);
    }

    private void addFloatTextFieldTransferAmountListener(TextField textField, ControlManager controlManager) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove non-digit and non-decimal point characters, including specified symbols
            newValue = newValue.replaceAll("[^\\d.]", "");

            // Split the new value at the decimal point
            String[] parts = newValue.split("\\.");

            // Check if the value before the decimal point is all zeros
            if (parts.length > 0 && parts[0].matches("0+")) {
                parts[0] = "0";
            } else {
                // Limit digits before the decimal point to 6
                if (parts.length > 0 && parts[0].length() > 6) {
                    parts[0] = parts[0].substring(0, 6);
                }
            }

            // Limit digits after the decimal point to 2
            if (parts.length > 1 && parts[1].length() > 2) {
                parts[1] = parts[1].substring(0, 2);
            }

            // Join the parts back together
            StringBuilder formattedValue = new StringBuilder();

            // Check if there is a part before the decimal point
            if (parts.length > 0 && !parts[0].isEmpty()) {
                formattedValue.append(parts[0]);
            }

            // Check if there is a part after the decimal point
            if (parts.length > 1) {
                formattedValue.append('.').append(parts[1]);
            } else if (newValue.endsWith(".")) {
                // If the new value ends with a dot, ensure it is preserved
                formattedValue.append('.');
            }

            boolean isValidNumber = false;
            try {
                if((!newValue.equals("0"))){
                    if ((!newValue.isEmpty())) {
                        if(Float.parseFloat(newValue) <= CheckBalance.checkBalance(controlManager.currentUser.getUserId())){

                            isValidNumber = true;
                            controlManager.errorBalance.setVisible(false);
                            controlManager.cashINBalanceView.setVisible(true);
                        }
                        else{
                            controlManager.errorBalance.setVisible(true);
                            controlManager.errorBalance.setText("You do not have enough balance.");
                            controlManager.cashINBalanceView.setVisible(false);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // Not a valid number
            }


            // Enable or disable the button based on the validity of the number
            textField.setText(formattedValue.toString());

            controlManager.cashTransferNext.setVisible(isValidNumber);
        });
    }

    public static String formatCurrency(double number) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        String formattedNumber = formatter.format(number);
        return formattedNumber;
    }

    public void addTextFieldNumberListener(TextField textField, int maxLength,ControlManager controlManager) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Restrict input to digits only
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            // Limit the length of input
            if (textField.getText().length() > maxLength) {
                textField.setText(textField.getText().substring(0, maxLength));
            }

            // Check if the mobile number exists in the database
            if (textField.getText().length() == maxLength) {
                setSearchedUser(UserAuthentication.SearchNumberAccount(textField.getText()));
                //System.out.println(searchedUser.getUserId());
                if (searchedUser != null) {
                    if(currentUser.getNumber().equals(textField.getText())){
                        controlManager.msgError.setVisible(true);
                        controlManager.msgError.setText("You cannot transfer To Yourself.");
                    }else{
                        controlManager.cashTransferAmountPanel.setVisible(true);
                        controlManager.cashINBalanceView.setText("Available Balance: " +
                                "\u20B1" +" "+ formatCurrency(CheckBalance.checkBalance(currentUser.getUserId())));
                    }
                } else {
                    controlManager.msgError.setVisible(true);
                    controlManager.msgError.setText("Mobile number does not exist.");
                }
            }else
            {
                controlManager.msgError.setVisible(false);
                controlManager.cashTransferAmountPanel.setVisible(false);
            }
        });
    }

    @FXML protected void onMouseEnterToMinimizeBtn() {circle_MinimiseShadow.setVisible(true);}

    @FXML protected void onMouseExitToMinimizeBtn() {circle_MinimiseShadow.setVisible(false);}

    @FXML protected void onMouseEnterToCloseBtn() {circle_CloseShadow.setVisible(true);}

    @FXML protected void onMouseExitToCloseBtn() {circle_CloseShadow.setVisible(false);}

    @FXML protected void onMouseClickToCloseBtn() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText("Are you sure want to exit?");
        if(alert.showAndWait().get() == ButtonType.OK){
            System.exit(0);
        }
    }

    public static boolean isValidName(String name) {return name != null && !name.trim().isEmpty();}

    public static boolean isValidEmail(String email) {return email != null && Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matcher(email).matches();}

    public static boolean isValidPhone(String phone) {return phone != null && Pattern.compile("^[0-9]{10}$").matcher(phone).matches();}

    public static boolean isValidPin(String pin, String confirmPin) {return pin.equals(confirmPin) && pin != null && Pattern.compile("^[0-9]{4}$").matcher(pin).matches();}

    public static void notif(String msg, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(msg);

        if(alert.showAndWait().get() == ButtonType.OK){

        }
    }

    private void addTextFieldListenerForFilter(TextField textField, ChoiceBox choiceBox,ControlManager controlManager) {

        final int[] selection = new int[1];

        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(choiceBox.getSelectionModel().getSelectedIndex() == 0)
                {
                    //viewAll
                    textField.setText("");
                    selection[0] = choiceBox.getSelectionModel().getSelectedIndex();
                    controlManager.searchPane.setVisible(false);
                    observableList.clear();
                    ArrayList<Transaction> transactionlist =
                            Transaction.getTransactionHistory(controlManager.currentUser.getUserId(),0,selection[0]+1);
                    observableList.addAll(transactionlist);
                    controlManager.tableView.setItems(observableList);
                }
                else {
                    selection[0] = choiceBox.getSelectionModel().getSelectedIndex();
                    controlManager.searchPane.setVisible(true);
                }
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                if (newValue != null && !newValue.isEmpty()) {
                    //view by userId or transaction Id
                    ArrayList<Transaction> transactionlist =
                            Transaction.getTransactionHistory(controlManager.currentUser.getUserId(), Integer.parseInt(textField.getText()), selection[0] + 1);
                    if (!transactionlist.isEmpty()) {
                        observableList.addAll(transactionlist);
                        controlManager.tableView.setItems(observableList);
                    } else {
                        observableList.clear();
                    }
                } else {
                    //view all
                    observableList.clear();
                    ArrayList<Transaction> transactionlist =
                            Transaction.getTransactionHistory(controlManager.currentUser.getUserId(), 0, 1);
                    observableList.addAll(transactionlist);
                    controlManager.tableView.setItems(observableList);
                }
            }
        });
    }

    public void setCurrentUser(UserAuthentication currentUser){
        this.currentUser = currentUser;
    }

    public void setSearchedUser(UserAuthentication User){
        this.searchedUser = User;
    }

    public void AnotherTransaction()throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification");
        alert.setHeaderText("Another Transaction?");

        if(alert.showAndWait().get() == ButtonType.OK){
            openCashTransferStep1Form();
        }

    }
    //end

    //Login Methods Start
    public void Login(ActionEvent event) throws IOException {
        if(!passtxtField_Pin.isVisible()){passtxtField_Pin.setText(txtField_Pin.getText());}

        String user = txtField_Mnumber.getText();
        String pass = passtxtField_Pin.getText();
        if (isValidPhone(user)){
            currentUser = UserAuthentication.Login(user,pass);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Invalid Input!");
            if(alert.showAndWait().get() == ButtonType.OK){

            }
        }
        if(!(currentUser == null)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("User ID: " + currentUser.getUserId() +" successfully logged In!");

            if(alert.showAndWait().get() == ButtonType.OK){
                switchUserDashboardForm(event,currentUser);
            }
        }
    }

    public void displayLoginContent(ControlManager controlManager) {
        addTextFieldListener(controlManager.txtField_Mnumber, 10);
        addTextFieldListener(controlManager.txtField_Pin, 4);
        addTextFieldListener(controlManager.passtxtField_Pin, 4);
    }

    public void switchLoginForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("frm_Login.fxml"));
        root = loader.load();
        ControlManager controlManager = loader.getController();
        controlManager.displayLoginContent(controlManager);
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    //end

    //Register Methods Start
    public void Register(ActionEvent actionEvent) throws IOException {
        hideAsterisk();
        String name,email,number,pin,confirmPin;
        error = new StringBuilder();
        if(!RegPasstxtField_Pin.isVisible()){RegPasstxtField_Pin.setText(RegtxtField_Pin.getText());}
        if(!RegPasstxtField_PinConfirm.isVisible()){RegPasstxtField_PinConfirm.setText(RegtxtField_PinConfirm.getText());}

        name = txtField_fname.getText() + " " + txtField_Mname.getText() + " " + txtField_Lname.getText();
        email = txtField_Email.getText();
        number = txtField_MobileNum.getText();
        pin = RegPasstxtField_Pin.getText();
        confirmPin = RegPasstxtField_PinConfirm.getText();

        if(validateInputs(name,email,number,pin,confirmPin)){

            if(UserAuthentication.Register(name,email,number,pin) == true){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("You Have Successfully Register!");

                if(alert.showAndWait().get() == ButtonType.OK) {
                    switchLoginForm(actionEvent);
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Invalid Input!");

            if(alert.showAndWait().get() == ButtonType.OK){
                errorLabel.setVisible(true);
                errorLabel.setText(error.toString());
            }
        }
    }

    public boolean validateInputs(String name, String email, String phone, String pin, String confirmPin) {
        boolean validInput = true;

        validInput = isValidName(name) ? validInput : showAsterisk(fNameAsterisk,"Invalid Name");
        validInput = isValidEmail(email) ? validInput : showAsterisk(EmailAsterisk,"Invalid Email (Ex.example@example.com)");
        validInput = isValidPhone(phone) ? validInput : showAsterisk(mNumberAsterisk,"Invalid Number");
        validInput = isValidPin(pin, confirmPin) ? validInput : showPinAsterisks(PinAsterisk, cPinAsterisk,"Pin Not Match/Invalid Pin");

        return validInput;
    }

    private boolean showAsterisk(Label label,String msg) {
        label.setVisible(true);
        error.append(msg + "\n");
        return false;
    }

    private void hideAsterisk() {
        fNameAsterisk.setVisible(false);
        EmailAsterisk.setVisible(false);
        mNumberAsterisk.setVisible(false);
        PinAsterisk.setVisible(false);
        cPinAsterisk.setVisible(false);

    }

    private boolean showPinAsterisks(Label pinLabel, Label confirmPinLabel,String msg) {
        pinLabel.setVisible(true);
        confirmPinLabel.setVisible(true);
        error.append(msg + "\n");
        return false;
    }

    public void displayRegisterContent(ControlManager controlManager) {
        addTextFieldListener(controlManager.txtField_MobileNum, 10);
        addTextFieldListener(controlManager.RegtxtField_Pin, 4);
        addTextFieldListener(controlManager.RegPasstxtField_Pin, 4);
        addTextFieldListener(controlManager.RegtxtField_PinConfirm, 4);
        addTextFieldListener(controlManager.RegPasstxtField_PinConfirm, 4);

    }

    public void switchRegisterForm(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("frm_Register.fxml"));
        root = loader.load();
        ControlManager controlManager = loader.getController();
        controlManager.displayRegisterContent(controlManager);
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    //end

    //UserDashboard Start
    public void Logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logging out!");
        alert.setHeaderText("Are you sure want to Log out?");
        if(alert.showAndWait().get() == ButtonType.OK){
            currentUser = null;
            switchLoginForm(event);
        }
    }

    public void switchUserDashboardForm(ActionEvent actionEvent,UserAuthentication cUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("frm_userDashboard.fxml"));
        root = loader.load();
        ControlManager controlManager1 = loader.getController();
        controlManager1.setCurrentUser(cUser);
        controlManager1.setControlManager(controlManager1);
        controlManager1.displayUserDashboardContent(controlManager1);
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void displayUserDashboardContent(ControlManager controlManager) {
        controlManager.accountName.setText("Account Name: " + controlManager.currentUser.getName());
        controlManager.accountNumber.setText("Account Number: " + controlManager.currentUser.getNumber());
        controlManager.userBalance.setText("\u20B1" +" "+ formatCurrency(CheckBalance.checkBalance(controlManager.currentUser.getUserId())));

    }
    //end

    //Cash In Start

    public void openCashInForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frm_CashIn.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene1 = new Scene(root1);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene1.getStylesheets().add(css);
        ControlManager controlManager1 = fxmlLoader.getController();
        controlManager1.setControlManager(this.controlManager23);
        controlManager1.setCurrentUser(currentUser);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene1);
        stage.centerOnScreen();
        stage.show();
        displayUserCashINContent(controlManager1);
    }

    public void cashIn()throws IOException{
        if(!(CashInAmountTxtField.getText().isEmpty()) && !(CashInAmountTxtField.getText().equals("0"))){
            float amount = Float.parseFloat(CashInAmountTxtField.getText());
            if(CashIn.cashIn(currentUser.getUserId(),amount)){
                alertCashin(amount);
            }
            else {
                notif("Your Balance Exceeds the limit!", Alert.AlertType.ERROR);
            }
        }
        else{
            notif("Invalid Input!", Alert.AlertType.ERROR);
        }
    }

    public void alertCashin(float amount){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText("You've Enter " +amount + " Amount Into Your Account Successfully!");

        if(alert.showAndWait().get() == ButtonType.OK){
            controlManager23.userBalance.setText("\u20B1" +" "+  CheckBalance.checkBalance(currentUser.getUserId()));
        }
    }

    public void displayUserCashINContent(ControlManager controlManager) {
        addFloatTextFieldListener(controlManager.CashInAmountTxtField);
        controlManager.accountName.setText("Account Name: " + controlManager.currentUser.getName());
        controlManager.accountNumber.setText("Account Number: " + controlManager.currentUser.getNumber());
    }

    //end

    //CashTransfer Start
    public void verifyPIN()throws IOException{
        //  System.out.println(currentUser.getPIN());

        if(currentUser.getPIN().equals(PINtxtField.getText())){
            notif("PIN Verified!", Alert.AlertType.CONFIRMATION);
            pinPanel.setVisible(false);
            numPanel.setVisible(true);

            System.out.println(PINtxtField.getText());
        }
        else{
            notif("Invalid PIN!", Alert.AlertType.ERROR);
        }
    }

    public void openCashTransferStep1Form()throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frm_CashTransfer_Number.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene1 = new Scene(root1);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene1.getStylesheets().add(css);
        ControlManager controlManager1 = fxmlLoader.getController();
        controlManager1.setControlManager(this.controlManager23);
        controlManager1.setCurrentUser(currentUser);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene1);
        stage.centerOnScreen();
        stage.show();
        displayTransferStep1Content(controlManager1);
    }

    public void openCashTransferStep2Form(ActionEvent actionEvent)throws IOException{
        close(actionEvent);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frm_CashTransfer.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene1 = new Scene(root1);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene1.getStylesheets().add(css);
        ControlManager controlManager1 = fxmlLoader.getController();
        controlManager1.setControlManager(this.controlManager23);
        controlManager1.setCurrentUser(currentUser);
        controlManager1.setSearchedUser(UserAuthentication.SearchNumberAccount(txtFieldSearchNumber.getText()));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene1);
        stage.centerOnScreen();
        stage.show();
        displayTransferStep2Content(controlManager1);
    }

    private void displayTransferStep2Content(ControlManager controlManager1) {
        //System.out.println(txtFieldSearchNumber.getText());

        controlManager1.AmountTotransfer.setText("\u20B1" +" "+ formatCurrency(Double.parseDouble(CashInAmountTxtField.getText())));

        controlManager1.totalAmountToTransfer.setText(formatCurrency(Double.parseDouble(CashInAmountTxtField.getText())));

        controlManager1.accName.setText(controlManager1.searchedUser.getName());

        controlManager1.accNumber.setText(controlManager1.searchedUser.getNumber());

        controlManager1.userBalance.setText("\u20B1" +" "+ formatCurrency(CheckBalance.checkBalance(controlManager1.currentUser.getUserId())));

    }

    public void transferCash(ActionEvent actionEvent)throws IOException{
        System.out.println("transfer: " + searchedUser.getUserId());

        System.out.println("current: " +currentUser.getUserId());

        System.out.println("amount: " + totalAmountToTransfer.getText());

        String cleanedStr = totalAmountToTransfer.getText().replace(",", "");

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        float amount = Float.parseFloat(cleanedStr);
        String name = searchedUser.getName();
        int account_ID = currentUser.getUserId();
        String date = formattedDateTime;
        int transferToID = searchedUser.getUserId();
        int transferFromID = currentUser.getUserId();

        CashTransfer cashTransfer = new CashTransfer(amount,name,account_ID,date,transferToID,transferFromID);

        if(CashTransfer.transferCash(cashTransfer)){

            notif("Transaction Complete!", Alert.AlertType.INFORMATION);
            controlManager23.userBalance.setText("\u20B1" +" "+  CheckBalance.checkBalance(currentUser.getUserId()));
            AnotherTransaction();
            close(actionEvent);
        }
        else{
            notif("Transaction Failed!", Alert.AlertType.ERROR);
            controlManager23.userBalance.setText("\u20B1" +" "+  CheckBalance.checkBalance(currentUser.getUserId()));
            close(actionEvent);
        }




    }

    public void confirmTransfer(ActionEvent event)throws IOException{
        if(confirmCheckbox.isSelected()){
            cashTransferSend.setVisible(true);
        }
        else{
            cashTransferSend.setVisible(false);
        }
    }

    //end

    //Transaction Start
    public void openTransactionHistory(ActionEvent actionEvent)throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frm_Transaction.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene1 = new Scene(root1);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene1.getStylesheets().add(css);
        ControlManager controlManager1 = fxmlLoader.getController();
        controlManager1.setControlManager(this.controlManager23);
        controlManager1.setCurrentUser(currentUser);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene1);
        stage.centerOnScreen();
        stage.show();
        initializeTableView(controlManager1);
    }
    public void initializeTableView(ControlManager controlManager1)throws IOException {
        controlManager1.filterBox.getItems().addAll(menu);
        if (!controlManager1.filterBox.getItems().isEmpty()) {
            controlManager1.filterBox.getSelectionModel().selectFirst();
        }

        controlManager1.colID.setCellValueFactory(new PropertyValueFactory<>("TransactionID"));
        controlManager1.colAmount.setCellValueFactory(new PropertyValueFactory<>("TransactionAmount"));
        controlManager1.colName.setCellValueFactory(new PropertyValueFactory<>("TransactionName"));
        controlManager1.colDate.setCellValueFactory(new PropertyValueFactory<>("TransactionDate"));
        controlManager1.colTransferToID.setCellValueFactory(new PropertyValueFactory<>("TransactionTransferToID"));
        controlManager1.colTransferFromID.setCellValueFactory(new PropertyValueFactory<>("TransactionTransferFromID"));

        observableList.clear();
        ArrayList<Transaction> transactionlist =
                Transaction.getTransactionHistory(controlManager1.currentUser.getUserId(),0,1);
        observableList.addAll(transactionlist);
        controlManager1.tableView.setItems(observableList);

        addTextFieldListenerForFilter(controlManager1.filterTextfield , controlManager1.filterBox,controlManager1);


    }

    //end

    //Change PIN Method Start
    public void ChangePIN(ActionEvent event) throws IOException {
        if(oldPINtxtField.getText().equals(currentUser.getPIN())){

            if(newPINtxtField.getText().equals(newPINConfirmtxtField.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Are you sure you want to Change PIN?");
                alert.setContentText("You'll be Logout Upon Confirmation.");

                if(alert.showAndWait().get() == ButtonType.OK){
                    DatabaseManager.changeUserPin(currentUser.getUserId(),newPINConfirmtxtField.getText());
                    currentUser = null;
                    switchLoginForm(event);
                }
            }
            else{
                notif("New PIN Does Not Match!", Alert.AlertType.ERROR);
            }

        }
        else{
            notif("Old PIN is Invalid!", Alert.AlertType.ERROR);
        }
    }

    public void OpenChangePINForm()throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frm_changePIN.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene1 = new Scene(root1);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene1.getStylesheets().add(css);
        ControlManager controlManager1 = fxmlLoader.getController();
        controlManager1.setControlManager(this.controlManager23);
        controlManager1.setCurrentUser(currentUser);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene1);
        stage.centerOnScreen();
        stage.show();
        addTextFieldListener(controlManager1.oldPINtxtField,4);
        addTextFieldListener(controlManager1.newPINtxtField,4);
        addTextFieldListener(controlManager1.newPINConfirmtxtField,4);
    }

}