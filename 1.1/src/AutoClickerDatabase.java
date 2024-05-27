import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AutoClickerDatabase {
    // JDBC驱动和数据库URL
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=master;encrypt=true;trustServerCertificate=true";

    // 数据库的用户名和密码
    static final String USER = "sa";
    static final String PASS = "xzm020424";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开连接
            System.out.println("Connecting to database(连接到数据库)...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行创建数据库操作
            System.out.println("Creating database AutoClickerDB(正在创建数据库 AutoClickerDB)...");
            stmt = conn.createStatement();

            // 创建数据库
            String sqlCreateDB = "CREATE DATABASE AutoClickerDB";
            stmt.executeUpdate(sqlCreateDB);
            System.out.println("Database AutoClickerDB created successfully(数据库 AutoClickerDB 创建成功)");

            // 切换到新创建的数据库
            System.out.println("Switching to database AutoClickerDB(切换到数据库 AutoClickerDB)...");
            conn.setCatalog("AutoClickerDB");

            // 执行创建表格操作
            System.out.println("Creating table ClickDetails(正在创建表格 ClickDetails)...");
            String sqlCreateTable = "CREATE TABLE ClickDetails " +
                    "(Id INT PRIMARY KEY IDENTITY(1,1), " +
                    "ClickTime DATETIME NOT NULL, " +
                    "ClickDelay INT NOT NULL, " +
                    "ClickCount INT NOT NULL, " +
                    "MaxClicks INT NOT NULL, " +
                    "RunTime INT NOT NULL, " +
                    "XCoordinate INT NOT NULL, " +
                    "YCoordinate INT NOT NULL)";
            stmt.executeUpdate(sqlCreateTable);
            System.out.println("Table ClickDetails created successfully(表格 ClickDetails 创建成功)");

        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Database operation completed.(数据库操作完成。)");
    }
}
