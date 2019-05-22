package DataBaseWorks;

import Clothes.Costume;

import java.util.Collection;


public class DBController {

    private JDBCConnector connector;

    public DBController(JDBCConnector connector){
        this.connector = connector;
    }

    public void addCostumeToDB(Costume costume){
        costume.getInsertSQLQueries().forEach(query ->
                connector.execSQLUpdate(query));
    }

    public void addAllCostumesToDB(Collection<Costume> costumes){
        costumes.forEach(this::addCostumeToDB);
    }

    public void removeCostumeFromDB(Costume costume){
        costume.getDelSQLQueries().forEach(query ->
                connector.execSQLUpdate(query));
    }

    public void removeAllCostumesFromDB(Collection<Costume> costumes){
        costumes.forEach(this::removeCostumeFromDB);
    }
}
