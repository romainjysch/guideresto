package ch.hearc.ig.guideresto.persistence;

public class DAOUtils {

    public static boolean charToBoolean(String value){
        return value.equalsIgnoreCase("T");
    }

    public static String booleanToString(Boolean value){
        if(value)
            return "T";
        else{
            return "F";
        }
    }

}
