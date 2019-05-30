package NetStuff.DataBaseWorks;

import Clothes.*;
import Enums.Color;
import Enums.Material;
import NetStuff.Net.User;
import mainpkg.Main;
import mainpkg.Pair;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;


public class DBController {

    private JDBCConnector connector;

    public DBController(JDBCConnector connector){
        this.connector = connector;
    }

    public void addCostumeToDB(Costume costume){
        costume.getInsertSQLQueries().forEach(query ->
                connector.execSQLUpdate(query.replace("DEFAULT", String.valueOf(costume.hashCode()))));
    }

    public void addAllCostumesToDB(Collection<Costume> costumes){
        costumes.forEach(this::addCostumeToDB);
    }

    public void addCostumeToDB(Costume costume, User user){
        costume.getInsertSQLQueries().forEach(query ->
                connector.execSQLUpdate(query.replace("USER" , user.getLogin()).replace("DEFAULT",
                        String.valueOf(costume.hashCode() * 10 + user.hashCode() * 100)
                        )));
    }

    public void addAllCostumesToDB(Collection<Costume> costumes, User user){
        costumes.forEach(p -> addCostumeToDB(p,user));
    }

    public void removeCostumeFromDB(Costume costume){
        costume.getDelSQLQueries().forEach(query ->
                connector.execSQLUpdate(query));
    }

    public void removeAllCostumesFromDB(Collection<Costume> costumes){
        costumes.forEach(this::removeCostumeFromDB);
    }

    public ArrayList<Pair<Costume,String>> getCostumesFromDB() throws SQLException {
        Pair<PreparedStatement, ResultSet> pair = connector.execSQLQuery("SELECT * FROM costumes;");
        ResultSet set = pair.getValue();
        //Получили id-шники костюмов
        ArrayList<Pair<Integer,String>> costumeKeys = new ArrayList<>();
        while(set.next()){
            costumeKeys.add(new Pair<>(set.getInt("id"), set.getString(DBConst.COSTUMES_USER)));
        }
        pair.getKey().close();

        //Получили костюмы
        ArrayList<Pair<Costume,String>> costumes = new ArrayList<>();
        ArrayList<String> consts = new ArrayList<>(Arrays.asList(DBConst.TOPCLOTHES_TABLE, DBConst.DOWNCLOTHES_TABLE,
                DBConst.HATS_TABLE, DBConst.SHOES_TABLE, DBConst.UNDERWEAR_TABLE));
        for (Pair<Integer,String> mainPair : costumeKeys) {
            Costume costume = new Costume();
            int key = mainPair.getKey();
            String login = mainPair.getValue();
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
            costumes.add(new Pair<>(costume, login));
        }
        return costumes;
    }

    public void addUserToDB(User user){
        connector.execSQLUpdate(user.getInsertSqlQuery());
    }

    public void addUserToDB(User user, SocketAddress address){
        connector.execSQLUpdate(user.getInsertSqlQuery().replace("ADDRESS", ((InetSocketAddress)address).getAddress().toString() + "|" + ((InetSocketAddress)address).getPort()));
    }

    public void removeUserFromDB(User user){
        connector.execSQLUpdate(user.getDelSqlQuery());
    }

    public void sinchronizeDB(){
        connector.execSQLUpdate("TRUNCATE costumes;");
        connector.execSQLUpdate("TRUNCATE down_clothes;");
        connector.execSQLUpdate("TRUNCATE hats;");
        connector.execSQLUpdate("TRUNCATE shoes;");
        connector.execSQLUpdate("TRUNCATE top_clothes;");
        connector.execSQLUpdate("TRUNCATE underwear;");

        Main.getObjectsHashSet().forEach(p -> addCostumeToDB(p.getKey(),new User(p.getValue(),"")));
    }

    public List<Pair<User, SocketAddress>> getAllUsersFromDB() throws SQLException {
        Pair<PreparedStatement, ResultSet> resultPair = connector.execSQLQuery("SELECT * FROM users;");
        ResultSet set = resultPair.getValue();
        List<Pair<User, SocketAddress>> users = new ArrayList<>();
        while(set.next()){
            Pair<User, SocketAddress> pair = new Pair<>(
                    new User(set.getString("login"), set.getString("password"), set.getString("email")),
                    new InetSocketAddress(set.getString(DBConst.USERS_ADDRESS).split("\\|")[0], Integer.parseInt(set.getString(DBConst.USERS_ADDRESS).split("\\|")[1]))
            );
            users.add(pair);
        }
        resultPair.getKey().close();
        return users;
    }

    public boolean isUserExistsInDB(User user){
        Pair<PreparedStatement, ResultSet> resultPair = connector.execSQLQuery("SELECT users.login FROM users;");
        ResultSet set = resultPair.getValue();
        try {
            if (set.next()) {
                if (set.getString("login").equals(user.getLogin())) {
                    return true;
                }
            }
        }catch (SQLException e){}
        return false;
    }

    public boolean isUserCorrectInDB(User user) throws SQLException{
        Pair<PreparedStatement, ResultSet> resultPair = connector.execSQLQuery("SELECT users.login, users.password FROM users;");
        ResultSet set = resultPair.getValue();
        try {
            if (set.next()) {
                if (set.getString("login").equals(user.getLogin()) && set.getString("password").equals(user.getPassword())) {
                    return true;
                }
            }
        }catch (SQLException e){}
        return false;
    }
}
