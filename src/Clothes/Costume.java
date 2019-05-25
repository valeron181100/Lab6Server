package Clothes;

import NetStuff.DataBaseWorks.DBConst;
import Enums.Color;
import Enums.Material;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class Costume implements Comparable, Serializable {
    private TopClothes topClothes;
    private DownClothes downClothes;
    private Shoes shoes;
    private Hat hat;
    private Underwear underwear;
    private OffsetDateTime initDate;

    public Costume(TopClothes top, DownClothes down, Shoes shoes, Hat hat, Underwear under){
        topClothes = top;
        downClothes = down;
        this.shoes = shoes;
        this.hat = hat;
        underwear = under;
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    public Costume(){
        topClothes = new TopClothes(50, Color.White, Material.Chlopoc, "T-Shirt", true, 60, false, 170);
        downClothes = new DownClothes(50, Color.Black, Material.Chlopoc, "Trousers", true, 70, 40);
        this.shoes = new Shoes(38, Color.White, Material.Leather, "Sneackers", true, true, Material.Rubber);
        this.hat = new Hat(50, Color.White, Material.Len, "BaseballHat", true, 15, 20);
        underwear = new Underwear(30, Color.Black, Material.Chlopoc, "Panties", true, 100);
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    public Costume(JSONObject object){
        this.topClothes = new TopClothes(object.getJSONObject("topClothes"));
        this.downClothes = new DownClothes(object.getJSONObject("downClothes"));
        this.shoes = new Shoes(object.getJSONObject("shoes"));
        this.hat = new Hat(object.getJSONObject("hat"));
        this.underwear = new Underwear(object.getJSONObject("underwear"));
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    public TopClothes getTopClothes() {
        return topClothes;
    }

    public void setTopClothes(TopClothes topClothes) {
        this.topClothes = topClothes;
    }

    public DownClothes getDownClothes() {
        return downClothes;
    }

    public void setDownClothes(DownClothes downClothes) {
        this.downClothes = downClothes;
    }

    public Shoes getShoes() {
        return shoes;
    }

    public void setShoes(Shoes shoes) {
        this.shoes = shoes;
    }

    public Hat getHat() {
        return hat;
    }

    public void setHat(Hat hat) {
        this.hat = hat;
    }

    public Underwear getUnderwear() {
        return underwear;
    }

    public OffsetDateTime getInitDate() {
        return initDate;
    }

    public void setUnderwear(Underwear underwear) {
        this.underwear = underwear;
    }

    public void get_undressed(){
        this.topClothes = null;
        this.underwear = null;
        this.shoes = null;
        this.downClothes = null;
        this.hat = null;
    }

    public void change_look(TopClothes top, DownClothes down, Shoes shoes, Hat hat, Underwear under){
        topClothes = top;
        downClothes = down;
        this.shoes = shoes;
        this.hat = hat;
        underwear = under;
    }

    @Override
    public int hashCode() {
        int prime = 10;
        int result = prime + 23;
        result = result * prime + downClothes.hashCode();
        result = result * prime + hat.hashCode();
        result = result * prime + shoes.hashCode();
        result = result * prime + topClothes.hashCode();
        result = result * prime + underwear.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Costume other = (Costume) obj;
        if (!topClothes.equals(other.getTopClothes()))
            return false;
        if (!downClothes.equals(other.getDownClothes()))
            return false;
        if (!underwear.equals(other.getUnderwear()))
            return false;
        if (!hat.equals(other.getHat()))
            return false;
        if (!shoes.equals(other.getShoes()))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Costume № " + Math.abs(this.hashCode() % 7);
    }

    @Override
    public int compareTo(Object o) {
        return this.hashCode() - ((Costume)o).hashCode();
    }


    public ArrayList<String> getInsertSQLQueries(){
        ArrayList<String> queries = new ArrayList<>(6);
        queries.add("INSERT INTO costumes VALUES ("+ this.hashCode() +", '" + this.toString() + "', '" + initDate.toString() + "');");
        queries.add(downClothes.getInsertSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(hat.getInsertSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(shoes.getInsertSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(topClothes.getInsertSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(underwear.getInsertSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        return queries;
    }

    public ArrayList<String> getDelSQLQueries(){
        ArrayList<String> queries = new ArrayList<>(6);
        queries.add("DELETE FROM "+ DBConst.COSTUME_TABLE +" WHERE id=" + this.hashCode() + ";");
        queries.add(downClothes.getDelSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(hat.getDelSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(shoes.getDelSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(topClothes.getDelSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        queries.add(underwear.getDelSqlQuery().replace("DEFAULT", String.valueOf(this.hashCode())));
        return queries;
    }

    public JSONObject getJson(){
        JSONObject mainJson = new JSONObject();

        mainJson.put("topClothes", this.topClothes.getJson());
        mainJson.put("downClothes", this.downClothes.getJson());
        mainJson.put("shoes", this.shoes.getJson());
        mainJson.put("hat", this.hat.getJson());
        mainJson.put("underwear", this.underwear.getJson());

        return mainJson;
    }
}
