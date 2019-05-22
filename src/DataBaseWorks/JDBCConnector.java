package DataBaseWorks;

import java.sql.*;

public class JDBCConnector {

    private final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASS = "va181100";

    private Connection connection;

    public JDBCConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        String costumes_table = String.format("CREATE TABLE IF NOT EXISTS %s(\n" +
                "                         %s INT,\n" +
                "                         %s VARCHAR,\n" +
                "                         PRIMARY KEY(id)\n" +
                ");", DBConst.COSTUME_TABLE, DBConst.TABLES_ID, DBConst.COSTUMES_NAME);

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
        execSQLUpdate(costumes_table);
        execSQLUpdate(downClothes_table);
        execSQLUpdate(topClothes_table);
        execSQLUpdate(hats_table);
        execSQLUpdate(shoes_table);
        execSQLUpdate(underwear_table);
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet execSQLQuery(String query){
        try (PreparedStatement statement = connection.
                prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean execSQLUpdate(String query){
        try (PreparedStatement statement = connection.
                prepareStatement(query)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}

