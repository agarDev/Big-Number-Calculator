package info.mayuragarkar.calculator;

import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Mayur on 06/26/2016.
 */
public class DatabaseHandler {
    public static SQLiteDatabase calcDB = null;
    public static String tableName = "bigNumCalcHistory", dbName = "bigNumCalcHistory";

    /** Add each calc to the db */
    public static void addtoDBDec(BigDecimal fNum, BigDecimal sNum, String ansNum){
        calcDB.execSQL("INSERT INTO " + tableName + " VALUES(" + fNum + ", " + sNum + ", \"" + ansNum + "\")");
    }

    public static void addtoDBInt(BigInteger fNum, BigInteger sNum, String ansNum){
        calcDB.execSQL("INSERT INTO " + tableName + " VALUES(" + fNum + ", " + sNum + ", \"" + ansNum + "\")");
    }
}
