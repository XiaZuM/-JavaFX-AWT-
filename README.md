# 基于JavaFX和AWT的自动点击器
它可以模拟鼠标点击操作并记录点击详细信息到数据库中


1.1：

AutoClicker.java

使用的技术和工具：

  JavaFX： 用于构建用户界面的框架，提供了按钮、文本框等控件以及布局管理器。
  AWT（Abstract Window Toolkit）： 用于创建Robot对象，实现模拟鼠标点击的功能。
  Java数据库连接（JDBC）： 用于与数据库进行交互，将点击详细信息保存到数据库中。
  
实现的功能：

  界面设计： 使用JavaFX创建了一个用户界面，包括标签、文本框和按钮，用于设置点击间隔、点击次数和运行时间，并显示当前的状态。
  自动点击功能： 当用户点击“启动”按钮时，程序开始自动进行鼠标点击操作，根据用户设置的点击间隔、点击次数和运行时间执行相应的操作。
  数据记录： 每次鼠标点击后，程序将点击详细信息（包括点击时间、点击间隔、点击次数、最大点击次数、运行时间以及鼠标坐标）保存到数据库中。
  快捷键支持： 用户可以使用空格键作为快捷键来启动/停止自动点击器。
  异常处理： 对可能出现的异常进行了处理，例如解析用户输入时可能抛出的NumberFormatException和数据库操作可能抛出的SQLException。



AutoClickerDatabase.java

用于创建一个名为 "AutoClickerDB" 的数据库以及其中的一个名为 "ClickDetails" 的表格。让我来解释一下它的实现：

导入和定义数据库连接信息：

通过 import java.sql.Connection, import java.sql.DriverManager, import java.sql.SQLException, import java.sql.Statement 导入了 JDBC 相关的类。
定义了数据库驱动程序的类名 JDBC_DRIVER 和数据库的连接 URL DB_URL。这里使用的是 SQL Server 数据库的连接 URL。
定义了数据库的用户名 USER 和密码 PASS。
定义 AutoClickerDatabase 类：

这是主类，包含了 main 方法，程序的入口点。
在 main 方法中：

使用 Class.forName(JDBC_DRIVER) 注册 JDBC 驱动程序。
通过 DriverManager.getConnection(DB_URL, USER, PASS) 建立与数据库的连接。
创建 Statement 对象以执行 SQL 语句。
使用 stmt.executeUpdate(sqlCreateDB) 创建名为 "AutoClickerDB" 的数据库。
使用 conn.setCatalog("AutoClickerDB") 切换到新创建的数据库。
使用 stmt.executeUpdate(sqlCreateTable) 创建名为 "ClickDetails" 的表格，包含了 Id（自增主键）、ClickTime（点击时间）、ClickDelay（点击延迟）、ClickCount（点击次数）、MaxClicks（最大点击次数）、RunTime（运行时间）、XCoordinate（X 坐标）和 YCoordinate（Y 坐标）字段。
异常处理：

使用了 try-catch 块来捕获可能出现的异常，分别处理 SQL 异常和其他异常。
在 finally 块中关闭了数据库连接和 Statement 对象，确保资源的正确释放。
输出信息：

在各个步骤中通过 System.out.println 输出了相应的信息，用于提示操作的执行情况。
