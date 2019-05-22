package Clothes;

import DataBaseWorks.DBConst;
import DataBaseWorks.iQuery;
import Enums.Color;
import Enums.Material;
import mainpkg.Main;
import org.json.JSONObject;

import java.io.Serializable;

public class Underwear extends Clothes implements Serializable, iQuery {
    private int sex_lvl;

    public Underwear(int size, Color color, Material material,
                      String name, boolean for_man, int sex_lvl){
        super(size, color, material, name, for_man);
        this.sex_lvl = sex_lvl;
    }

    public Underwear(JSONObject object){
        super(
                object.getInt("size"), Color.valueOf(object.getString("color")),
                Material.valueOf(object.getString("material")), object.getString("name"),
                object.getBoolean("is_for_man")
        );

        this.sex_lvl = object.getInt("sex_lvl");
    }

    public int getSex_lvl() {
        return sex_lvl;
    }

    @Override
    public int hashCode() {
        int prime = 14;
        int result = prime + 23;
        result = result * prime + sex_lvl * (int)Math.pow(prime, 2);
        result = result * prime + (getIsForMan() ? (int)Math.pow(prime, 3) : 0);
        result = result * prime + getSize() * (int)Math.pow(prime, 4);
        result = result * prime + getColor().ordinal() * (int)Math.pow(prime, 5);
        result = result * prime + Main.strHashCode(getName());
        result = result * prime + getMaterial().ordinal()  * (int)Math.pow(prime, 6);
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
        Underwear other = (Underwear) obj;
        if (sex_lvl != other.getSex_lvl())
            return false;
        ////Clothes' Fields
        if (getName().intern() != other.getName().intern())
            return false;
        if (getMaterial() != other.getMaterial())
            return false;
        if (getColor() != other.getColor())
            return false;
        if (getSize() != other.getSize())
            return false;
        if (getIsForMan() != other.getIsForMan())
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public String getInsertSqlQuery() {
        return String.format("INSERT INTO underwear VALUES (DEFAULT, %s, %s, '%s', '%s', '%s', CAST(%s AS BIT));",
                sex_lvl, getSize(), getColor().toString(), getMaterial().toString(), getName(), getIsForMan() ? 1 : 0);
    }

    @Override
    public String getDelSqlQuery() {
        return "DELETE FROM "+ DBConst.UNDERWEAR_TABLE +" WHERE id=DEFAULT;";
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("sex_lvl", this.sex_lvl);

        jsonObject.put("size", super.getSize());
        jsonObject.put("color", super.getColor());
        jsonObject.put("material", super.getMaterial());
        jsonObject.put("name", super.getName());
        jsonObject.put("is_for_man", super.getIsForMan());

        return jsonObject;
    }
}
