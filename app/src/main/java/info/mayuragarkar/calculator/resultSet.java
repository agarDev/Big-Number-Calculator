package info.mayuragarkar.calculator;

/**
 * Created by Mayur on 06/26/2016.
 */
public class ResultSet {
    String message;
    String value;

    public String getMessage() {
        return message;
    }

    public ResultSet(){}

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultSet(String message, String value) {
        this.message = message;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
