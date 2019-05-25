package Clothes;

import NetStuff.DataBaseWorks.DBConst;
import NetStuff.DataBaseWorks.iQuery;
import Enums.Color;
import Enums.Material;
import mainpkg.Main;
import org.json.JSONObject;

import java.io.Serializable;

public class Shoes extends Clothes implements Serializable, iQuery {
    private boolean is_shoelaces;
    private Material outsole_material;

    public Shoes(int size, Color color, Material material,
                      String name, boolean for_man,
                      boolean is_shoelaces, Material outsole_material){
        super(size, color, material, name, for_man);
        this.is_shoelaces = is_shoelaces;
        this.outsole_material = outsole_material;
    }

    public Shoes(JSONObject object){
        super(
                object.getInt("size"), Color.valueOf(object.getString("color")),
                Material.valueOf(object.getString("material")), object.getString("name"),
                object.getBoolean("is_for_man")
        );

        this.outsole_material = Material.valueOf(object.getString("outsole_material"));
        this.is_shoelaces = object.getBoolean("is_shoelaces");
    }

    public Material getOutsole_material() {
        return outsole_material;
    }

    public boolean getIsShoelaces(){
        return is_shoelaces;
    }

    @Override
    public int hashCode() {
        int prime = 13;
        int result = prime + 23;
        result = result * prime + (getIsForMan() ? (int)Math.pow(prime, 2):0);
        result = result * prime + outsole_material.ordinal()  * (int)Math.pow(prime, 3);
        result = result * prime + (getIsForMan() ? (int)Math.pow(prime, 4):0);
        result = result * prime + getSize() * (int)Math.pow(prime, 5);
        result = result * prime + getColor().ordinal() * (int)Math.pow(prime, 6);
        result = result * prime + Main.strHashCode(getName());
        result = result * prime + getMaterial().ordinal()  * (int)Math.pow(prime, 7);
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
        Shoes other = (Shoes) obj;
        if (is_shoelaces != other.getIsShoelaces())
            return false;
        if (outsole_material != other.getOutsole_material())
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
        return String.format("INSERT INTO shoes VALUES (DEFAULT, '%s', CAST(%s AS BIT), %s, '%s', '%s', '%s', CAST(%s AS BIT));",
                outsole_material.toString(), is_shoelaces ? 1 : 0, getSize(), getColor().toString(), getMaterial().toString(), getName(), getIsForMan() ? 1 : 0);
    }

    @Override
    public String getDelSqlQuery() {
        return "DELETE FROM "+ DBConst.SHOES_TABLE +" WHERE id=DEFAULT;";
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("is_shoelaces", this.is_shoelaces);
        jsonObject.put("outsole_material", this.outsole_material);

        jsonObject.put("size", super.getSize());
        jsonObject.put("color", super.getColor());
        jsonObject.put("material", super.getMaterial());
        jsonObject.put("name", super.getName());
        jsonObject.put("is_for_man", super.getIsForMan());


        return jsonObject;
    }
}
