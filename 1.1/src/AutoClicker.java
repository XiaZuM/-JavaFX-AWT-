import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutoClicker extends Application {
    private Robot robot;
    private boolean autoClickerRunning = false;
    private int clickDelay = 1000;
    private int clickCount = 0;
    private int maxClicks = Integer.MAX_VALUE; // 默认最大点击次数为无限
    private int runTime = Integer.MAX_VALUE; // 默认运行时间为无限
    private Thread autoClickerThread;

    private Label statusLabel;
    private TextField delayTextField;
    private TextField countTextField;
    private TextField timeTextField; // 添加时间输入文本框
    private Button stopButton;

    // 数据库连接参数
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=AutoClickerDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "xzm020424";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("XiaZuM - 自动点击器");

        // 创建一个标签显示状态
        statusLabel = new Label("状态：未启动");

        // 创建一个文本框用于设置点击间隔
        delayTextField = new TextField("1000");
        delayTextField.setPromptText("点击间隔（毫秒）");

        // 创建一个文本框用于设置点击次数
        countTextField = new TextField();
        countTextField.setPromptText("点击次数");

        // 创建一个文本框用于设置运行时间
        timeTextField = new TextField();
        timeTextField.setPromptText("运行时间（秒）");

        // 创建启动按钮
        Button startButton = new Button("启动");
        startButton.setOnAction(e -> startAutoClicker());

        // 创建停止按钮
        stopButton = new Button("停止");
        stopButton.setDisable(true);
        stopButton.setOnAction(e -> stopAutoClicker());

        // 创建一个水平布局用于放置按钮
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(startButton, stopButton);

        // 创建一个垂直布局用于放置控件
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(statusLabel, delayTextField, countTextField, timeTextField, buttonBox);

        // 创建场景并设置布局
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);

        // 创建 Robot 实例
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // 设置快捷键
        KeyCombination kc = new KeyCodeCombination(KeyCode.SPACE);
        Runnable rn = () -> {
            if (autoClickerRunning) {
                stopAutoClicker();
            } else {
                startAutoClicker();
            }
        };
        scene.getAccelerators().put(kc, rn);

        // 关闭窗口时退出程序
        primaryStage.setOnCloseRequest(event -> {
            stopAutoClicker(); // 停止自动点击器
            Platform.exit(); // 关闭程序
        });

        // 显示主舞台
        primaryStage.show();
    }

    private void startAutoClicker() {
        autoClickerRunning = true;
        statusLabel.setText("状态：运行中");
        delayTextField.setDisable(true);
        countTextField.setDisable(true);
        timeTextField.setDisable(true);
        stopButton.setDisable(false); // 启动时启用停止按钮

        // 获取文本框中设置的点击间隔、点击次数和运行时间
        try {
            clickDelay = Integer.parseInt(delayTextField.getText());
            String maxClicksInput = countTextField.getText();
            if (!maxClicksInput.isEmpty()) {
                maxClicks = Integer.parseInt(maxClicksInput);
            }
            String runTimeInput = timeTextField.getText();
            if (!runTimeInput.isEmpty()) {
                runTime = Integer.parseInt(runTimeInput) * 1000; // 转换为毫秒
            }
        } catch (NumberFormatException e) {
            // Handle parsing error if needed
        }

        // 创建一个线程执行自动点击任务
        autoClickerThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (autoClickerRunning && clickCount < maxClicks && (System.currentTimeMillis() - startTime) < runTime) {
                // 获取当前鼠标坐标
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                int x = mouseLocation.x;
                int y = mouseLocation.y;

                // 模拟鼠标左键点击
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                // 更新点击计数
                clickCount++;

                // 保存点击详细信息到数据库
                saveClickDetailsToDatabase(x, y);

                // 等待指定时间
                try {
                    Thread.sleep(clickDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 点击次数达到最大值或运行时间到达指定时间或停止按钮被点击时停止自动点击器
            stopAutoClicker();
        });
        autoClickerThread.start();
    }

    private void stopAutoClicker() {
        autoClickerRunning = false;
        Platform.runLater(() -> {
            statusLabel.setText("状态：已停止");
            delayTextField.setDisable(false);
            countTextField.setDisable(false);
            timeTextField.setDisable(false);
            stopButton.setDisable(true); // 停止时禁用停止按钮
        });
        clickCount = 0; // 重置点击计数
        runTime = Integer.MAX_VALUE; // 重置运行时间
    }

    private void saveClickDetailsToDatabase(int x, int y) {
        // 将点击详细数据保存到数据库中
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ClickDetails (ClickTime, ClickDelay, ClickCount, MaxClicks, RunTime, XCoordinate, YCoordinate) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setInt(2, clickDelay);
            pstmt.setInt(3, clickCount);
            pstmt.setInt(4, maxClicks);
            pstmt.setInt(5, runTime);
            pstmt.setInt(6, x);
            pstmt.setInt(7, y);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
