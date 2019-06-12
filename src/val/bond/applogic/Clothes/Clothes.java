package val.bond.applogic.Clothes;

import val.bond.applogic.Enums.Color;
import val.bond.applogic.Enums.Material;

import java.io.Serializable;

public abstract class Clothes implements Serializable {
    private int size;
    private Color color;
    private Material material;
    private String name;
    private boolean is_for_man;

    Clothes(int size, Color color, Material material, String name, boolean for_man){
        this.size = size;
        this.color = color;
        this.material = material;
        this.name = name;
        this.is_for_man = for_man;
    }

    public Clothes(){
        this.size = 5;
        this.color = Color.White;
        this.material = Material.Chlopoc;
        this.name = "Shirt";
        this.is_for_man = true;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public boolean getIsForMan(){
        return is_for_man;
    }

    @Override
    public int hashCode() {
        int prime = 16;
        int result = 1;
        result = prime * result + getSize() + getColor().toString().length() +
                getName().length() + getMaterial().toString().length();
        return result;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
