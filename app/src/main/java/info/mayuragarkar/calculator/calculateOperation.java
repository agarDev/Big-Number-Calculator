package info.mayuragarkar.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import info.mayuragarkar.calculator.BigFunctions;
/**
 * Created by Mayur on 06/26/2016.
 */
public class calculateOperation {
    public static String operationAppliedString = "Operation Applied: ";

    public static ResultSet addOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        R.setMessage(operationAppliedString + "Addition");
        R.setValue(String.valueOf(firstNumber.add(secondNumber)));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        return R;
    }


    public static ResultSet subOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        R.setMessage(operationAppliedString + "Subtraction");
        R.setValue(String.valueOf(firstNumber.subtract(secondNumber)));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        return R;
    }


    public static ResultSet mulOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        R.setMessage(operationAppliedString + "Multiplication");
        R.setValue(String.valueOf(firstNumber.multiply(secondNumber)));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        return R;
    }

    public static ResultSet divOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        R.setMessage(operationAppliedString + "Division");
        R.setValue(String.valueOf(firstNumber.divide(secondNumber, 100, RoundingMode.HALF_UP)));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        return R;
    }


    public static ResultSet modOperation(BigInteger firstNumber, BigInteger secondNumber, ResultSet R){
        R.setMessage(operationAppliedString + "Mod");
        R.setValue(String.valueOf(firstNumber.mod(secondNumber)));
        DatabaseHandler.addtoDBInt(firstNumber, secondNumber, R.getValue());
        return R;
    }


    public static ResultSet modInverseOperation(BigInteger firstNumber, BigInteger secondNumber, ResultSet R){
        if (firstNumber.gcd(secondNumber).equals(BigInteger.ONE)) {
            R.setValue(String.valueOf(firstNumber.modInverse(secondNumber)));
            DatabaseHandler.addtoDBInt(firstNumber, secondNumber, R.getValue());
        } else {
            R.setValue("Given numbers are not prime to each other.");
        }
        R.setMessage(operationAppliedString + "Mod Inverse");
        return R;
    }

    public static ResultSet isPrimeOperation(BigInteger firstNumber, ResultSet R){
        if (firstNumber.isProbablePrime(250)) {
            R.setValue("Number is a Prime Number.");
        } else {
            R.setValue("Number is not a Prime Number.");
        }
        R.setMessage(operationAppliedString + "Is Prime ?");
        return R;
    }

    public static ResultSet compareOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        int resultFinal = firstNumber.compareTo(secondNumber);
        R.setValue((resultFinal == 0 ? "Both_Numbers_are_Equal" : (resultFinal > 0 ? "First_Number_is_Greater_than_Second_Number" : "Second_Number_is_Greater_than_First_Number")));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        R.setMessage(operationAppliedString + "Compare to");
        return R;
    }


    public static ResultSet lShiftOperation(BigInteger firstNumber, BigInteger secondNumber, ResultSet R){
            R.setValue(String.valueOf(firstNumber.shiftLeft(secondNumber.intValue())));
            DatabaseHandler.addtoDBInt(firstNumber, secondNumber, R.getValue());
            R.setMessage(operationAppliedString + "Left Shift");
        return R;
    }

    public static ResultSet rShiftOperation(BigInteger firstNumber, BigInteger secondNumber, ResultSet R){
        R.setValue(String.valueOf(firstNumber.shiftRight(secondNumber.intValue())));
        DatabaseHandler.addtoDBInt(firstNumber, secondNumber, R.getValue());
        R.setMessage(operationAppliedString + "Right Shift");
        return R;
    }


    public static ResultSet logOperation(BigInteger firstNumber, ResultSet R){
        R.setValue(String.valueOf(logBigInteger(firstNumber)));
        R.setMessage(operationAppliedString + "Logarithm");
        return R;
    }

    public static ResultSet squareOperation(BigInteger firstNumber, ResultSet R){
        R.setValue(String.valueOf(firstNumber.multiply(firstNumber)));
        R.setMessage(operationAppliedString + "Logarithm");
        return R;
    }

    public static ResultSet cubeOperation(BigInteger firstNumber, ResultSet R){
        R.setValue(String.valueOf((firstNumber.multiply(firstNumber)).multiply(firstNumber)));
        R.setMessage(operationAppliedString + "Logarithm");
        return R;
    }


    public static ResultSet factorialOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        R.setValue(String.valueOf(calculateFactorial(firstNumber, secondNumber)));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        R.setMessage(operationAppliedString + "Factorial");
        return R;
    }

    static BigDecimal calculateFactorial(BigDecimal factFirstNumber, BigDecimal factSecondNumber) {
        if (factFirstNumber.equals(BigDecimal.ONE)) {
            return factSecondNumber;
        }
        BigDecimal minusOne = factFirstNumber.subtract(BigDecimal.ONE);
        return calculateFactorial(minusOne, factSecondNumber.multiply(minusOne));
    }


    public static ResultSet powOperation(BigDecimal firstNumber, BigDecimal secondNumber, ResultSet R){
        final int SCALE = 10;
        R.setMessage(operationAppliedString + "Power");
        R.setValue(String.valueOf(BigFunctions.exp( BigFunctions.ln(firstNumber, SCALE).multiply(secondNumber),SCALE )));
        DatabaseHandler.addtoDBDec(firstNumber, secondNumber, R.getValue());
        return R;
    }




    /**
     * Computes the natural logarithm of a BigInteger. Works for really big
     * integers (practically unlimited)
     *
     * @param val Argument, positive integer
     * @return Natural logarithm, as in <tt>Math.log()</tt>
     */
    private static final double LOG2 = Math.log(2.0);
    public static double logBigInteger(BigInteger val) {
        int blex = val.bitLength() - 1022; // any value in 60..1023 is ok
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }
}
