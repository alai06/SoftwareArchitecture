package demo.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class MessageQueue {
    
    @Id
    @GeneratedValue
    private long id;
    
    private String name;

    @ManyToMany
    @JsonBackReference
    private Set<Message> messages = new HashSet<>();

    public MessageQueue() {}

    public MessageQueue(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }
}
