package val.bond.applogic.NetStuff.Net;

public enum TransferCommandID {

    REMOVE(1),
    SHOW(2),
    ADD_IF_MAX(3),
    LOAD(4),
    INFO(5),
    IMPORT(6),
    ADD(7),
    START(8),
    EXIT(9),
    CHANGE_DEF_FILE_PATH(10),
    HELP(11),
    EMPTY_TP(666),
    OK(0),
    ERROR(-1),
    ADI_FE(601),
    CheckingConnectionTP(101);

    private int id;

    TransferCommandID(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
