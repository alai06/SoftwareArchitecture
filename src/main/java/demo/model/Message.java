package demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private long id;
    private String text;

    @ManyToOne
    @JsonBackReference
    private MessageQueue queue;
    
    private LocalDateTime timeCreated;
    
    private LocalDateTime timeFirstAccessed;
    
    private int numberOfReads;

    public Message() {
        this.timeCreated = LocalDateTime.now();
        this.numberOfReads = 0;
    }

    public Message(long id, String text) {
        this.timeCreated = LocalDateTime.now();
        this.numberOfReads = 0;
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

    public void markAsRead() {
        if (this.timeFirstAccessed == null) {
            this.timeFirstAccessed = LocalDateTime.now();
        }
        this.numberOfReads++;
    }
    
    public boolean isOrphan() {
        return queue == null;
    }
    
    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }
    
}