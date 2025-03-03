package demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private String text;

    @ManyToOne
    @JsonBackReference
    private MessageQueue queue;

    public Message() {}

    public Message(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageQueue getQueue() {
        return queue;
    }

    public void setQueue(MessageQueue queue) {
        this.queue = queue;
    }
}