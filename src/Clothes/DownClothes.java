package Clothes;

import DataBaseWorks.*;
import Enums.Color;
import Enums.Material;
import mainpkg.Main;
import org.json.JSONObject;

import java.io.Serializable;

public class DownClothes extends Clothes implements Serializable, iQuery {
    private int leg_length_sm;
    private int diametr_leg_sm;

    public DownClothes(int size, Color color, Material material,
                      String name, boolean for_man, int leg_length_sm,
                      int diametr_leg_sm){
        super(size, color, material, name, for_man);
        this.leg_length_sm = leg_length_sm;
        this.diametr_leg_sm = diametr_leg_sm;
    }

    public DownClothes(JSONObject object){
        super(
                object.getInt("size"), Color.valueOf(object.getString("color")),
                Material.valueOf(object.getString("material")), object.getString("name"),
                object.getBoolean("is_for_man")
        );

        this.diametr_leg_sm = object.getInt("diametr_leg_sm");
        this.leg_length_sm = object.getInt("leg_length_sm");
    }

    public int getDiametr_leg_sm() {
        return diametr_leg_sm;
    }

    public int getLeg_length_sm() {
        return leg_length_sm;
    }

    @Override
    public int hashCode() {
        int prime = 11;
        int result = prime + 23;
        result = result * prime + leg_length_sm * (int)Math.pow(prime, 2);
        result = result * prime + diametr_leg_sm * (int)Math.pow(prime, 3);
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
        DownClothes other = (DownClothes) obj;
        if (leg_length_sm != other.getLeg_length_sm())
            return false;
        if (diametr_leg_sm != other.getDiametr_leg_sm())
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
        String result = String.format("INSERT INTO down_clothes VALUES (DEFAULT, %s, %s, %s, '%s', '%s', '%s', CAST(%s AS BIT));",
                leg_length_sm, diametr_leg_sm, getSize(), getColor().toString(), getMaterial().toString(), getName(), getIsForMan() ? 1 : 0);
        return result;
    }

    @Override
    public String getDelSqlQuery() {
        return "DELETE FROM "+ DBConst.DOWNCLOTHES_TABLE +" WHERE id=DEFAULT;";
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("leg_length_sm", this.leg_length_sm);
        jsonObject.put("diametr_leg_sm", this.diametr_leg_sm);

        jsonObject.put("size", super.getSize());
        jsonObject.put("color", super.getColor());
        jsonObject.put("material", super.getMaterial());
        jsonObject.put("name", super.getName());
        jsonObject.put("is_for_man", super.getIsForMan());

        return jsonObject;
    }
}
