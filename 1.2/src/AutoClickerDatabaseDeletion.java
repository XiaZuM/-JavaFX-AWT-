import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AutoClickerDatabaseDeletion {
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

            // 执行删除数据库操作
            System.out.println("Deleting database AutoClickerDB(正在删除数据库 AutoClickerDB)...");
            stmt = conn.createStatement();

            // 删除数据库
            String sqlDeleteDB = "DROP DATABASE IF EXISTS AutoClickerDB";
            stmt.executeUpdate(sqlDeleteDB);
            System.out.println("Database AutoClickerDB deleted successfully(数据库 AutoClickerDB 删除成功)");

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
        System.out.println("Database deletion completed.(数据库删除完成。)");
    }
}
