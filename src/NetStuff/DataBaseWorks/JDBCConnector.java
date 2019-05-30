package NetStuff.DataBaseWorks;

import mainpkg.LoadingPrinter;
import mainpkg.Main;
import mainpkg.Pair;

import java.sql.*;

public class JDBCConnector extends DBConfigs {

    private final String DB_URL = "jdbc:postgresql://"+dbHost+":"+dbPort+"/" + dbName;
    private final String USER = dbUser;
    private final String PASS = dbPassword;


    private static final FileLogger logger = new FileLogger();

    private Connection connection;

    public JDBCConnector() {
        LoadingPrinter loadingPrinter = new LoadingPrinter();
        boolean firstCreation = true;
        while(true) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                loadingPrinter.stop();
                System.out.println("\nСоединение с БД установлено!");
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Ошибка JDBC драйвер не найден!");
                System.exit(1);
            } catch (SQLException e) {
                if (e.getSQLState().equals("08001")) {
                    if(firstCreation) {
                        System.out.println("Ошибка: невозможно подключиться к серверу БД, так как сервер не доступен!");
                        System.out.println("Попытка подключения:");
                        new Thread(loadingPrinter::printLoadingLine).start();
                        firstCreation = false;
                    }
                }
            }
        }
        String costumes_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.COSTUME_TABLE, DBConst.TABLES_ID, DBConst.COSTUMES_NAME, DBConst.COSTUMES_INITDATE, DBConst.COSTUMES_USER);

        String topClothes_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s INT,\n" +
                "                         %s BIT NOT NULL,\n" +
                "                         %s INT,\n" +
                "\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s BIT NOT NULL,\n" +
                "                         \n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.TOPCLOTHES_TABLE, DBConst.TABLES_ID, DBConst.TOPCLOTHES_HANDLENGTH, DBConst.TOPCLOTHES_ISHOOD, DBConst.TOPCLOTHES_GROWTH,
                        DBConst.TABLES_SIZE, DBConst.TABLES_COLOR, DBConst.TABLES_MATERIAL, DBConst.TABLES_NAME, DBConst.TABLES_ISFORMAN);

        String downClothes_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s INT,\n" +
                "                         %s INT,\n" +
                "\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s BIT NOT NULL,\n" +
                "                         \n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.DOWNCLOTHES_TABLE, DBConst.TABLES_ID, DBConst.DOWNCLOTHES_LEGLENGTH, DBConst.DOWNCLOTHES_LEGDIAMETR,
                DBConst.TABLES_SIZE, DBConst.TABLES_COLOR, DBConst.TABLES_MATERIAL, DBConst.TABLES_NAME, DBConst.TABLES_ISFORMAN);

        String hats_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s INT,\n" +
                "                         %s INT,\n" +
                "\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s BIT NOT NULL,\n" +
                "                         \n" +
                "                         PRIMARY KEY(id)\n" +
                ");",DBConst.HATS_TABLE, DBConst.TABLES_ID,DBConst.HATS_CYLINDERHEIGHT, DBConst.HATS_VISORLENGTH,
                DBConst.TABLES_SIZE, DBConst.TABLES_COLOR, DBConst.TABLES_MATERIAL, DBConst.TABLES_NAME, DBConst.TABLES_ISFORMAN);

        String shoes_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s BIT NOT NULL,\n" +
                "\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s BIT NOT NULL,\n" +
                "                         \n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.SHOES_TABLE, DBConst.TABLES_ID, DBConst.SHOES_OUTSOLE_MATERIAL, DBConst.SHOES_ISSHOELACES,
                DBConst.TABLES_SIZE, DBConst.TABLES_COLOR, DBConst.TABLES_MATERIAL, DBConst.TABLES_NAME, DBConst.TABLES_ISFORMAN);

        String underwear_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s INT,\n" +
                "\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s BIT NOT NULL,\n" +
                "                         \n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.UNDERWEAR_TABLE, DBConst.TABLES_ID, DBConst.UNDERWEAR_SEXLVL,
                DBConst.TABLES_SIZE, DBConst.TABLES_COLOR, DBConst.TABLES_MATERIAL, DBConst.TABLES_NAME, DBConst.TABLES_ISFORMAN);


        String users_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         %s VARCHAR,\n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.USERS_TABLE, DBConst.TABLES_ID, DBConst.USERS_LOGIN, DBConst.USERS_PASSWORD, DBConst.USERS_EMAIL, DBConst.USERS_ADDRESS);

        execSQLUpdate(costumes_table);
        execSQLUpdate(downClothes_table);
        execSQLUpdate(topClothes_table);
        execSQLUpdate(hats_table);
        execSQLUpdate(shoes_table);
        execSQLUpdate(underwear_table);
        execSQLUpdate(users_table);
        int k =0;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Executing a query and returning resultSet. After calling this method and processing resultSet, you must call PreparedStatement.close() method.
     * @param query SQL query
     * @return Pair of PreparedStatement and ResultSet
     */
    public Pair<PreparedStatement, ResultSet> execSQLQuery(String query){
            try {
                PreparedStatement statement = connection.
                        prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                return new Pair<>(statement, resultSet);
            } catch (SQLException e) {
                if (e.getSQLState().equals("08001") || e.getSQLState().equals("08006")) {
                    resetConnection();
                    return execSQLQuery(query);
                }
                else {
                    logger.log(e.getMessage() + "\nSqlState = " + e.getSQLState());
                    return null;
                }
            }
    }

    private boolean resetConnection(){
        LoadingPrinter loadingPrinter = new LoadingPrinter();
        boolean firstCreation = true;
        while(true) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                loadingPrinter.stop();
                System.out.println("\nСоединение с БД установлено!");
                return true;
            } catch (ClassNotFoundException e) {
                System.err.println("Ошибка JDBC драйвер не найден!");
                System.exit(1);
            } catch (SQLException e) {
                if (e.getSQLState().equals("08001")) {
                    if(firstCreation) {
                        new Thread(loadingPrinter::printLoadingLine).start();
                        firstCreation = false;
                    }
                }
            }
        }
    }

    public boolean execSQLUpdate(String query){
        try (PreparedStatement statement = connection.
                prepareStatement(query)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("08001") || e.getSQLState().equals("08006")) {
                resetConnection();
                return execSQLUpdate(query);
            }
            else {
                logger.log(e.getMessage() + "\nSqlState = " + e.getSQLState());
                return false;
            }
        }
    }
}

