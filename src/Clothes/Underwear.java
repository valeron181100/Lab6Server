package Clothes;

import Enums.Color;
import Enums.Material;
import org.json.JSONObject;

import java.io.Serializable;

public class Underwear extends Clothes implements Serializable {
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
        int result = 1;
        result = prime * result + sex_lvl + getSize() +
                getColor().toString().length() +
                getName().length() + getMaterial().toString().length();
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
