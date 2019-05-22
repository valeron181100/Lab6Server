package Clothes;

import DataBaseWorks.DBConst;
import DataBaseWorks.iQuery;
import Enums.Color;
import Enums.Material;
import mainpkg.Main;
import org.json.JSONObject;

import java.io.Serializable;

public class Hat extends Clothes implements Serializable, iQuery {
    private int cylinder_height_sm;
    private int visor_length_sm;

    public Hat(int size, Color color, Material material,
                      String name, boolean for_man,
                      int cylinder_height_sm, int visor_length_sm){
        super(size, color, material, name, for_man);
        this.cylinder_height_sm = cylinder_height_sm;
        this.visor_length_sm = visor_length_sm;
    }

    public Hat(JSONObject object){
        super(
                object.getInt("size"), Color.valueOf(object.getString("color")),
                Material.valueOf(object.getString("material")), object.getString("name"),
                object.getBoolean("is_for_man")
        );

        this.cylinder_height_sm = object.getInt("cylinder_height_sm");
        this.visor_length_sm = object.getInt("visor_length_sm");
    }

    public int getCylinder_height_sm() {
        return cylinder_height_sm;
    }

    public int getVisor_length_sm() {
        return visor_length_sm;
    }

    @Override
    public int hashCode() {
        int prime = 12;
        int result = prime + 23;
        result = result * prime + cylinder_height_sm * (int)Math.pow(prime, 2);
        result = result * prime + visor_length_sm * (int)Math.pow(prime, 3);
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
        Hat other = (Hat) obj;
        if (cylinder_height_sm != other.getCylinder_height_sm())
            return false;
        if (visor_length_sm != other.getVisor_length_sm())
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
        return String.format("INSERT INTO hats VALUES (DEFAULT, %s, %s, %s, '%s', '%s', '%s', CAST(%s AS BIT));",
                cylinder_height_sm, visor_length_sm, getSize(), getColor().toString(), getMaterial().toString(), getName(), getIsForMan() ? 1 : 0);
    }

    @Override
    public String getDelSqlQuery() {
        return "DELETE FROM "+ DBConst.HATS_TABLE +" WHERE id=DEFAULT;";
    }

    public JSONObject getJson(){

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("cylinder_height_sm", this.cylinder_height_sm);
        jsonObject.put("visor_length_sm", this.visor_length_sm);

        jsonObject.put("size", super.getSize());
        jsonObject.put("color", super.getColor());
        jsonObject.put("material", super.getMaterial());
        jsonObject.put("name", super.getName());
        jsonObject.put("is_for_man", super.getIsForMan());

        return jsonObject;
    }
}
