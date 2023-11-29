package com.example.lab7;

import java.net.URL;
import java.util.ResourceBundle;

import backend.BankManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Controller {
    BankManager bankManager = BankManager.getInstance();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Thread, Integer> indexColumn;

    @FXML
    private TextArea log;

    @FXML
    private TextField maximumWithdrawal;

    @FXML
    private TableColumn<Thread, String > nameColumn;

    @FXML
    private Button pauseButton;

    @FXML
    private TableColumn<Thread, Integer> priorityColumn;

    @FXML
    private Button resumeButton;

    @FXML
    private Button runAppButton;

    @FXML
    private TableColumn<Thread, Thread.State> stateColumn;

    @FXML
    private TextField threadIndex;

    @FXML
    private TextField threadsNumber;

    @FXML
    private TableView<Thread> threadsTable;

    @FXML
    private TextField totalBankMoney;

    private static ObservableList<Thread> threadObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        initializeTable();
        var timeline = initializeTimeLineForTableUpdate();
        clearLogFile();
        var logUpdateTimeline = initializeTimelineForLogUpdate();

        runAppButton.setOnAction(event -> {
            int numberOfThreads = Integer.parseInt(threadsNumber.getText());
            int bankMoney = Integer.parseInt(totalBankMoney.getText());
            int maxWithdrawal = Integer.parseInt(maximumWithdrawal.getText());

            bankManager.initializeBank(bankMoney);
            bankManager.setThreadsNumber(numberOfThreads);
            bankManager.setMaxWithdrawal(maxWithdrawal);

            bankManager.RunThreads();

            threadObservableList.addAll(bankManager.threads);

            threadsTable.setItems(threadObservableList);

            timeline.play();
            logUpdateTimeline.play();
        });

        pauseButton.setOnAction(event -> {
            int threadIdx = Integer.parseInt(threadIndex.getText());

            bankManager.clients.get(threadIdx).suspendThread();
        });

        resumeButton.setOnAction(event -> {
            int threadIdx = Integer.parseInt(threadIndex.getText());

            bankManager.clients.get(threadIdx).resumeThread();
        });
    }

    private void initializeTable(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        indexColumn.setCellValueFactory(cellData -> {
            int index = bankManager.threads.indexOf(cellData.getValue());
            return new SimpleIntegerProperty(index).asObject();
        });
    }

    private Timeline initializeTimeLineForTableUpdate(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            threadObservableList.clear();
            threadObservableList.addAll(bankManager.threads);

            threadsTable.refresh();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        return timeline;
    }

    private Timeline initializeTimelineForLogUpdate(){
        Timeline logUpdateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateLogFromFile();
        }));

        logUpdateTimeline.setCycleCount(Timeline.INDEFINITE);

        return logUpdateTimeline;
    }


    private void updateLogFromFile() {
        try {
            String logContent = Files.readString(Path.of("log.txt"));

            log.setText(logContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearLogFile() {
        try {
            Path logFilePath = Path.of("log.txt");

            Files.write(logFilePath, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void StopThreads(){
        for (Thread t : threadObservableList){
            t.interrupt();
        }
    }

}
