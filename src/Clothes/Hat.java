package Clothes;

import Enums.Color;
import Enums.Material;
import org.json.JSONObject;

import java.io.Serializable;

public class Hat extends Clothes implements Serializable {
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
        int result = 1;
        int forman = getIsForMan() ? 1 : 0;
        result = prime * result + cylinder_height_sm + visor_length_sm +
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
