package Clothes;

import DataBaseWorks.DBConst;
import DataBaseWorks.iQuery;
import Enums.Color;
import Enums.Material;
import mainpkg.Main;
import org.json.JSONObject;

import java.io.Serializable;

public class TopClothes extends Clothes implements Serializable, iQuery {
    private int hand_sm_length;
    private boolean is_hood;
    private int growth_sm;

    public TopClothes(int size, Color color, Material material,
                      String name, boolean for_man, int hand,
                      boolean hood, int growthSm){
        super(size, color, material, name, for_man);
        this.growth_sm = growthSm;
        this.hand_sm_length = hand;
        this.is_hood = hood;
    }

    public TopClothes(JSONObject object){
        super(
                object.getInt("size"), Color.valueOf(object.getString("color")),
                Material.valueOf(object.getString("material")), object.getString("name"),
                object.getBoolean("is_for_man")
        );

        this.hand_sm_length = object.getInt("hand_sm_length");
        this.is_hood = object.getBoolean("is_hood");
        this.growth_sm= object.getInt("growth_sm");
    }

    public int getGrowth_sm() {
        return growth_sm;
    }

    public int getHand_sm_length() {
        return hand_sm_length;
    }

    public boolean getIsHood(){
        return is_hood;
    }

    @Override
    public int hashCode() {
        int prime = 15;
        int result = prime + 23;
        result = result * prime + growth_sm * (int)Math.pow(prime, 2);
        result = result * prime + hand_sm_length * (int)Math.pow(prime, 3);
        result = result * prime + (is_hood ? (int)Math.pow(prime, 4) : 0);
        result = result * prime + (getIsForMan() ? (int)Math.pow(prime, 5):0);
        result = result * prime + getSize() * (int)Math.pow(prime, 6);
        result = result * prime + getColor().ordinal() * (int)Math.pow(prime, 7);
        result = result * prime + Main.strHashCode(getName());
        result = result * prime + getMaterial().ordinal()  * (int)Math.pow(prime, 8);
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
        TopClothes other = (TopClothes) obj;
        if (is_hood != other.getIsHood())
            return false;
        if (hand_sm_length != other.getHand_sm_length())
            return false;
        if (growth_sm != other.getGrowth_sm())
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
        return String.format("INSERT INTO top_clothes VALUES (DEFAULT, %s, CAST(%s AS BIT), %s, %s, '%s', '%s', '%s', CAST(%s AS BIT));",
                hand_sm_length, is_hood ? 1 : 0, growth_sm, getSize(), getColor().toString(), getMaterial().toString(), getName(), getIsForMan() ? 1 : 0);
    }

    @Override
    public String getDelSqlQuery() {
        return "DELETE FROM "+ DBConst.TOPCLOTHES_TABLE +" WHERE id=DEFAULT;";
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("hand_sm_length", this.hand_sm_length);
        jsonObject.put("is_hood", this.is_hood);
        jsonObject.put("growth_sm", this.growth_sm);

        jsonObject.put("size", super.getSize());
        jsonObject.put("color", super.getColor());
        jsonObject.put("material", super.getMaterial());
        jsonObject.put("name", super.getName());
        jsonObject.put("is_for_man", super.getIsForMan());

        return jsonObject;
    }
}
