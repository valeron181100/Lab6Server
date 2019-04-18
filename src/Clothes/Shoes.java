package Clothes;

import Enums.Color;
import Enums.Material;
import org.json.JSONObject;

import java.io.Serializable;

public class Shoes extends Clothes implements Serializable {
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
        int result = 1;
        int shoelaces = is_shoelaces ? 1 : 0;
        int forman = getIsForMan() ? 1 : 0;
        result = prime * result + shoelaces + forman +
                forman + getSize() + getColor().toString().length() +
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
