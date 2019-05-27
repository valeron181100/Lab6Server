package NetStuff.DataBaseWorks;

import Clothes.*;
import Enums.Color;
import Enums.Material;
import NetStuff.Net.User;
import mainpkg.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void addCostumeToDB(Costume costume, User user){
        costume.getInsertSQLQueries().forEach(query ->
                connector.execSQLUpdate(query.replace("USER" , user.getLogin())));
    }

    public void addAllCostumesToDB(Collection<Costume> costumes, User user){
        costumes.forEach(p -> addCostumeToDB(p,user));
    }

    public void removeCostumeFromDB(Costume costume){
        costume.getDelSQLQueries().forEach(query ->
                connector.execSQLUpdate(query));
    }

    public void addUserToDB(User user){
        connector.execSQLUpdate(user.getInsertSqlQuery());
    }

    public void removeUserFromDB(User user){
        connector.execSQLUpdate(user.getDelSqlQuery());
    }

    public void removeAllCostumesFromDB(Collection<Costume> costumes){
        costumes.forEach(this::removeCostumeFromDB);
    }

    public ArrayList<Costume> getCostumesFromDB() throws SQLException {
        Pair<PreparedStatement, ResultSet> pair = connector.execSQLQuery("SELECT * FROM costumes;");
        ResultSet set = pair.getValue();
        //Получили id-шники костюмов
        ArrayList<Integer> costumeKeys = new ArrayList<>();
        while(set.next()){
            costumeKeys.add(set.getInt("id"));
        }
        pair.getKey().close();

        //Получили костюмы
        ArrayList<Costume> costumes = new ArrayList<>();
        ArrayList<String> consts = new ArrayList<>(Arrays.asList(DBConst.TOPCLOTHES_TABLE, DBConst.DOWNCLOTHES_TABLE,
                DBConst.HATS_TABLE, DBConst.SHOES_TABLE, DBConst.UNDERWEAR_TABLE));
        for (int key : costumeKeys) {
            Costume costume = new Costume();
            for (String tableName : consts){
                Pair<PreparedStatement, ResultSet> tablePair = connector.execSQLQuery("SELECT * FROM " + tableName + " WHERE id=" + key +";");
                ResultSet tableSet = tablePair.getValue();
                while(tableSet.next()) {
                    int size = tableSet.getInt("size");
                    Color color = Color.valueOf(tableSet.getString("color"));
                    Material material = Material.valueOf(tableSet.getString("material"));
                    String name = tableSet.getString("name");
                    boolean isForMan = tableSet.getBoolean("is_for_man");
                    switch (tableName) {
                        case DBConst.TOPCLOTHES_TABLE:
                            TopClothes topClothes = new TopClothes(size, color, material, name, isForMan,
                                    tableSet.getInt("hand_sm_length"), tableSet.getBoolean("is_hood"),
                                    tableSet.getInt("growth_sm"));
                            costume.setTopClothes(topClothes);
                            break;
                        case DBConst.DOWNCLOTHES_TABLE:
                            DownClothes downClothes = new DownClothes(size, color, material, name, isForMan,
                                    tableSet.getInt("leg_length_sm"),
                                    tableSet.getInt("diametr_leg_sm"));
                            costume.setDownClothes(downClothes);
                            break;
                        case DBConst.HATS_TABLE:
                            Hat hat= new Hat(size, color, material, name, isForMan,
                                    tableSet.getInt("cylinder_height_sm"),
                                    tableSet.getInt("visor_length_sm"));
                            costume.setHat(hat);
                            break;
                        case DBConst.SHOES_TABLE:
                            Shoes shoes = new Shoes(size, color, material, name, isForMan,
                                    tableSet.getBoolean("is_shoelaces"),
                                    Material.valueOf(tableSet.getString("outsole_material")));
                            costume.setShoes(shoes);
                            break;
                        case DBConst.UNDERWEAR_TABLE:
                            Underwear underwear = new Underwear(size, color, material, name, isForMan,
                                    tableSet.getInt("sex_lvl"));
                            costume.setUnderwear(underwear);
                            break;
                    }
                }
            }
            costumes.add(costume);
        }

        return costumes;
    }
}
