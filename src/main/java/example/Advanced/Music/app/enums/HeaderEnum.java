package example.Advanced.Music.app.enums;
import lombok.Getter;

public enum HeaderEnum {
    CLIENT_MESSAGE_ID("x-client-message-id"), CLIENT_TIME("x-client-time");

    @Getter
    private final String label;

    private HeaderEnum(String label) {
        this.label = label;
    }
}