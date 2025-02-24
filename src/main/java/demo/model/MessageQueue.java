package demo.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Message {
    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    @Id
    @GeneratedValue
    private long id;
    private String text;
    private LocalDateTime timeCreated;
    private LocalDateTime timeFirstAccessed;
    private int numberOfReads;

    @ManyToMany(mappedBy = "messages")
    @JsonManagedReference
    private Set<MessageQueue> queues = new HashSet<>();

    public Message() {
        this.timeCreated = LocalDateTime.now();
    }

    public Message(String text) {
        this.text = text;
        this.timeCreated = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public LocalDateTime getTimeFirstAccessed() {
        return timeFirstAccessed;
    }

    public int getNumberOfReads() {
        return numberOfReads;
    }

    public void addQueue(MessageQueue queue) {
        this.queues.add(queue);
    }

    public void removeQueue(MessageQueue queue) {
        this.queues.remove(queue);
    }

    public boolean isOrphan() {
        return queues.isEmpty();
    }

    public void markAsRead() {
        if (this.timeFirstAccessed == null) {
            this.timeFirstAccessed = LocalDateTime.now();
        }
        this.numberOfReads++;
        logger.info("Message {} has been read {} times", id, numberOfReads);
    }
}