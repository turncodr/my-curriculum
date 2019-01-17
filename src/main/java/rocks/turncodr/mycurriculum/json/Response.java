package rocks.turncodr.mycurriculum.json;

import rocks.turncodr.mycurriculum.error.Message;

import java.util.List;

/**
 * Response object for Ajax-Requests.
 */
public class Response {

    /**
     * This can be whatever you want it to be.
     */
    private Object data;
    /**
     * Messages that should be displayed to the User.
     */
    private List<Message> messages;

    private String redirectTo;

    public Response(Object data, List<Message> messages) {
        this.data = data;
        this.messages = messages;
    }

    public Response(Object data, List<Message> messages, String redirectTo) {
        this.data = data;
        this.messages = messages;
        this.redirectTo = redirectTo;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
}
