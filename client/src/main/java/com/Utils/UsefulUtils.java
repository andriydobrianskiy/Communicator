package com.Utils;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.mainLogin.Main;
import com.mainPage.NotFulled.DictionaryPropertiesNotfulled;
import com.mainPage.NotFulled.ProductAdd.ProductSearch.ProductSearch;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.google.jhsheets.filtered.tablecolumn.AbstractFilterableTableColumn;
import org.apache.commons.beanutils.BeanUtils;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UsefulUtils {
   public static TrayNotification tray = new TrayNotification();
    public static final String IMAGES = "/images/";
    public static void installCopyPasteHandler(TableView<?> table) {

        // install copy/paste keyboard handler
        table.setOnKeyPressed(new TableKeyEventHandler());

    }
    public static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCompination.match(keyEvent)) {

                if( keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    copySelectionToClipboard( (TableView<?>) keyEvent.getSource());

                    System.out.println("Selection copied to clipboard");

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }

        }


    }

  /*  public static boolean setFieldsByModel(List<? extends Control> fields, Object model) { // заповнення полів вікна
        fields.forEach(field -> {

            Object value = null;

            try {
                value = BeanUtils.getProperty(model, field.getId());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            //    log.log(Level.SEVERE, "objectinit exception: "  + e);
                throw new IllegalStateException(e);
            }

            try {
                if (field instanceof TextField) {
                    ((TextField) field).setText(String.valueOf(value));
                } else if (field instanceof TextArea) {
                    ((TextArea) field).setText(String.valueOf(value));
                } else if (field instanceof DatePicker) {
                    ((DatePicker) field).setValue(stringToDateTimeConverter(String.valueOf(value)));
                } else if (field instanceof JFXTimePicker) {
                    ((JFXTimePicker) field).setValue(localTimeConverter(String.valueOf(value)));
                } else if (field instanceof ComboBox) {
                    List<ProductSearch> list = ((ComboBox<ProductSearch>) field).getItems();
                    ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(String.valueOf(value), (ObservableList<? extends DictionaryPropertiesNotfulled>) list));
                } else if (field instanceof CheckBox){
                    ((CheckBox) field).setSelected(Integer.parseInt(String.valueOf(value)) > 0);
                }
            } catch (Exception e) {
            //    log.log(Level.SEVERE, "setfields by model exception: " + e);
                throw new IllegalStateException(e);
            }
        });

        return true;
    }*/
    public static LocalTime localTimeConverter(String time) {
        if(time == null) return (LocalTime) null;

        LocalTime localTime = localDateTimeConverter(time).toLocalTime();

        return localTime;
    }
    public static LocalDateTime localDateTimeConverter(String time) {
        if(time == null) return (LocalDateTime) null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);

        return localDateTime;
    }
    public static void copySelectedCell(TableView tableView) {
        tableView.setOnKeyPressed(eventKey -> {
            KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
            if (copyKeyCodeCompination.match(eventKey)) {

                if (eventKey.getSource() instanceof TableView) {

                    UsefulUtils.copySelectionToClipboard((TableView<?>) eventKey.getSource());


                    // event is handled, consume it
                    eventKey.consume();

                }
            }
        });
    }

    public static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = 1;

        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            Object cell = (Object) table.getColumns().get(col).getCellData(row);

            // null-check: provide empty string for nulls
            if (cell == null) {
                cell = "";


                // determine whether we advance in a row (tab) or a column
                // (newline).
                if (prevRow == row) {

                    clipboardString.append('\t');

                } else if (prevRow != -1) {

                    clipboardString.append('\n');

                }
            }
            // create string from cell
            String text = cell.toString();

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);

    }


    public static Node getProgressIndicator() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxHeight(50);
        progressIndicator.setMaxWidth(50);

        return progressIndicator;
    }

    public static void searchCombination(KeyEvent eventKey, TableView tableView) {
        KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY);
        if (copyKeyCodeCompination.match(eventKey)) {

            if (eventKey.getSource() instanceof TableView) {

                TablePosition position = tableView.getFocusModel().getFocusedCell();

                ((AbstractFilterableTableColumn)position.getTableColumn()).getFilterEditor().getFilterMenu().show(
                        (Node) tableView,
                        MouseInfo.getPointerInfo().getLocation().getX(),
                        MouseInfo.getPointerInfo().getLocation().getY()
                );


                // event is handled, consume it
                eventKey.consume();

            }
        }
    }



    public static Object filter(String criteria, ObservableList<? extends DictionaryPropertiesNotfulled> list) { // фільтр list'y java 8
        /*List<? extends DictionaryInterface> result = list.stream()
                .filter(line -> line.toString().equals(criteria))
                .collect(Collectors.toList());
        return result.get(0);*/

        List<? extends DictionaryPropertiesNotfulled> result = list.stream()
                .filter(line -> line.getID().equals(criteria))
                .collect( Collectors.toList());

        return (result.isEmpty() ? null : result.get(0));
    }

    public static void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка!");
        alert.setHeaderText("Помилка!");
        alert.setContentText(message);

        alert.showAndWait();
    }



    public static void showCustomDialogDown (String message){
        tray.setNotificationType(NotificationType.CUSTOM);
        tray.setTitle("Сповіщення");
        String s = message;
        tray.setMessage(s);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.millis(5000));
        tray.setRectangleFill(Color.valueOf("#39b5ff"));
        tray.setImage(new Image("/sample/resources/images/chat.png"));
        
    }

    public static void showConfirmDialogDown (String message){
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setTitle("Підтвердження");
        String s = message;
        tray.setMessage(s);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.millis(4000));
       // tray.setRectangleFill(Color.valueOf("#4183D7"));
    }

    public static void showErrorDialogDown (String message){
        tray.setNotificationType(NotificationType.ERROR);
        tray.setTitle("Помилка");
        String s = message;
        tray.setMessage(s);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.millis(4000));
        // tray.setRectangleFill(Color.valueOf("#4183D7"));
    }

    public static void showSuccessful(String message){
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.setTitle("Communicator");
        String s = message;
        tray.setMessage(s);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.millis(4000));
        // tray.setRectangleFill(Color.valueOf("#4183D7"));
    }


    public static ButtonType showConfirmDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження");
        String s = message;
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();


        return result.get();

    }
    public static void showInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(true);
        alert.setTitle("Інформація");
        alert.setHeaderText("Інформація");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static String dateTimeConverter(DatePicker time) { // перетворення часу в формат datetime sql
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return formatter.format(time.getValue()) + " 00:00:00";
    }

    public static void fadeTransition(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(400));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public static LocalDate stringToDateTimeConverter(String time) {
        if(time == null) return (LocalDate)null;

        time = time.substring(0, 10); // вирізаю неактуальну годину щоб занести в DatePicker

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(time, formatter);

        return localDate;
    }
    public static String createSQLString(String value) {
        StringBuilder builder = new StringBuilder();

        builder.append('\'' + value + '\'');

        return builder.toString();
    }


    public static double getVisualScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }
    public static double getVisualScreenHeight() {
        return Screen.getPrimary().getVisualBounds().getHeight();
    }

     public static boolean isReachableByPing(String host) {
        try {

            // Start a new Process
            Process process = Runtime.getRuntime().exec("ping -" + ( System.getProperty("os.name").toLowerCase().startsWith("windows") ? "n" : "c" ) + " 1 " + host);

            //Wait for it to finish
            process.waitFor();

            //Check the return value
            return process.exitValue() == 0;

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, null, ex);
            return false;
        }
    }

    public static final String getBasePathForClass(Class<?> classs) {

        // Local variables
        File file;
        String basePath = "";
        boolean failed = false;

        // Let's give a first try
        try {
            file = new File(classs.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

            basePath = ( file.isFile() || file.getPath().endsWith(".jar") || file.getPath().endsWith(".zip") ) ? file.getParent() : file.getPath();
        } catch (URISyntaxException ex) {
            failed = true;
            Logger.getLogger(classs.getName()).log(Level.WARNING, "Cannot firgue out base path for class with way (1): ", ex);
        }

        // The above failed?
        if (failed)
            try {
                file = new File(classs.getClassLoader().getResource("").toURI().getPath());
                basePath = file.getAbsolutePath();

                // the below is for testing purposes...
                // starts with File.separator?
                // String l = local.replaceFirst("[" + File.separator +
                // "/\\\\]", "")
            } catch (URISyntaxException ex) {
                Logger.getLogger(classs.getName()).log(Level.WARNING, "Cannot firgue out base path for class with way (2): ", ex);
            }

        // fix to run inside Eclipse
        if (basePath.endsWith(File.separator + "lib") || basePath.endsWith(File.separator + "bin") || basePath.endsWith("bin" + File.separator)
                || basePath.endsWith("lib" + File.separator)) {
            basePath = basePath.substring(0, basePath.length() - 4);
        }
        // fix to run inside NetBeans
        if (basePath.endsWith(File.separator + "build" + File.separator + "classes")) {
            basePath = basePath.substring(0, basePath.length() - 14);
        }
        // end fix
        if (!basePath.endsWith(File.separator))
            basePath += File.separator;

        return basePath;
    }
    public static Image getImageFromResourcesFolder(String imageName) {
        return new Image(UsefulUtils.class.getResourceAsStream(IMAGES + imageName));
    }

    public static boolean checkEmptyRequiredFields(List<? extends Control> listComboBox) { // перевірка на обов'язкові поля
        boolean isEmptyField = false;

        try {
            for (Control elem : listComboBox) {
                String value = "";

                if (elem instanceof JFXComboBox) {
                    value =
                            ((JFXComboBox) elem).getSelectionModel().isEmpty() ? "" : (((JFXComboBox) elem).getValue().toString());
                    elem.setStyle("-fx-background-color: rgba(155, 232, 247, 0.3)");
                } else if (elem instanceof JFXTextField || elem instanceof TextField) {
                    value = ((TextField) elem).getText();
                    elem.setStyle("-fx-background-color: white");
                }else if(elem instanceof JFXTextArea || elem instanceof TextArea ){
                    value = ((TextArea) elem).getText();
                    elem.setStyle("-fx-background-color: white");
                }
                if (elem instanceof ChoiceBox) {
                    value =
                            ((ChoiceBox) elem).getSelectionModel().isEmpty() ? "" : (((ChoiceBox) elem).getValue().toString());
                    elem.setStyle("-fx-background-color: rgba(155, 232, 247, 0.3)");
                } else if (elem instanceof JFXTextField || elem instanceof TextField) {
                    value = ((TextField) elem).getText();
                    elem.setStyle("-fx-background-color: white");
                }

                if (value.isEmpty()) { // red color of required textfield
                    elem.setStyle(("-fx-background-color: rgba(255,0,0,0.21);" +
                            "    -fx-background-radius: 6, 5;\n" +
                            "    -fx-background-insets: 0, 1;\n" +
                            "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n" +
                            "    -fx-text-fill: #395306;"));

                    isEmptyField = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEmptyField;
    }
}
