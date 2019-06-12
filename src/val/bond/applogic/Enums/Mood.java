package val.bond.applogic.Enums;

public enum Mood {
    Calm,
    Dreamy,
    Energic,
    Good,
    Happy,
    Angry,
    Annoyed,
    Bad,
    Depressed,
    Stressed;
    public static Mood[] get_positive_values(){
        Mood[] result = new Mood[5];
        for(int i = 0; i <= 4; i ++){
            result[i] = Mood.values()[i];
        }
        return result;
    }
    public static Mood[] get_negative_values(){
        Mood[] result = new Mood[5];
        for(int i = 5; i < Mood.values().length; i ++){
            result[i] = Mood.values()[i];
        }
        return result;
    }
}
