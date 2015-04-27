package se.chalmers.group8.communication.http;

/**
 * Used to set a property of HTTP requests.
 */
public class RequestPropertyPair {
    private String field;
    private String newValue;

    public RequestPropertyPair(String field, String newValue) {
        this.field = field;
        this.newValue = newValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
