package DataBaseWorks;

import Clothes.Costume;


public class DBController {

    private JDBCConnector connector;

    public DBController(JDBCConnector connector){
        this.connector = connector;
    }

    public void addCostumeToDB(Costume costume){
        costume.getInsertSQLQueries().forEach(query ->
                connector.execSQLUpdate(query));
    }

    public void removeCostumeFromDB(Costume costume){
        costume.getDelSQLQueries().forEach(query ->
                connector.execSQLUpdate(query));
    }

}
