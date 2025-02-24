package demo.controller;

import demo.model.Message;
import demo.model.MessageQueue;
import demo.service.MessageService;
import demo.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/queues")
public class MessageQueueController {
    
    @Autowired
    private QueueService queueService;
    
    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<Collection<MessageQueue>> getQueues(@RequestParam(value = "startWith", defaultValue = "") String prefix) {
        return new ResponseEntity<>(queueService.findByPrefix(prefix), HttpStatus.OK);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<Collection<Message>> getMessages(@PathVariable("id") String id) {
        return queueService.findQueueById(id)
                .map(q -> new ResponseEntity<>(messageService.findAllByQueue(q), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/messages/search")
    public ResponseEntity<Collection<Message>> searchMessages(@PathVariable("id") String id, @RequestParam("query") String query) {
        return queueService.findQueueById(id)
                .map(q -> new ResponseEntity<>(messageService.searchMessages(q, query), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}/messages/{msgId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") String id, @PathVariable("msgId") long msgId) {
        Optional<MessageQueue> queue = queueService.findQueueById(id);
        if (queue.isPresent() && messageService.deleteMessageIfOrphan(msgId, queue.get())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}/messages/next")
public ResponseEntity<Message> getNextMessage(@PathVariable("id") String id) {
    return queueService.findQueueById(id)
        .map(q -> {
            Message message = messageService.getNextMessage(q);
            message.markAsRead();  // Incrémente les lectures
            logger.info("Message {} a été récupéré à {}", message.getId(), LocalDateTime.now());
            return new ResponseEntity<>(message, HttpStatus.OK);
        })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

}