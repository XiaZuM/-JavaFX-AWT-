import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ClearTableData {
    // JDBC驱动和数据库URL
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=AutoClickerDB;encrypt=true;trustServerCertificate=true";

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

            // 创建 Statement 对象
            System.out.println("Creating statement(创建 Statement 对象)...");
            stmt = conn.createStatement();

            // 清空表中的数据
            System.out.println("Deleting data from table ClickDetails(正在从 ClickDetails 表中删除数据)...");
            String sqlDeleteData = "DELETE FROM ClickDetails";
            int rowsAffected = stmt.executeUpdate(sqlDeleteData);
            System.out.println(rowsAffected + " rows deleted from table (从 ClickDetails 表中成功删除了))");

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
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Database operation completed.(数据库操作完成。)");
    }
}
