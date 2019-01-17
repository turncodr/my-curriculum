package rocks.turncodr.mycurriculum.error;

/**
 * Message that will be displayed to the user.
 * It is NOT necessarily an error message.
 */
public class Message {

    /**
     * Message Types.
     */
    public enum Type {
        // Operation failed, here's why
        ERROR,
        // Operation succeeded, but not everything went as expected. Here's what happened
        WARNING,
        // Operation succeeded as expected
        SUCCESS
    }

    /**
     * Message in i18n.
     */
    private String message;

    private Type type;

    public Message(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
