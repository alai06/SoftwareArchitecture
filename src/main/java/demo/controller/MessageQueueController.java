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
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/queues")
public class MessageQueueController {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageQueueController.class);

    @Autowired
    private QueueService queueService;
    
    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<Collection<MessageQueue>> getQueues(@RequestParam(value = "startWith", defaultValue = "") String prefix) {
        return new ResponseEntity<>(queueService.findByPrefix(prefix), HttpStatus.OK);
    }
    

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable("id") long id) {
    return queueService.findQueueById(id)
            .map(q -> new ResponseEntity<>(messageService.findAllByQueue(q), HttpStatus.OK))
            .orElseGet(() -> ResponseEntity.notFound().build());
}


@GetMapping("/{id}/messages/search")
public ResponseEntity<List<Message>> searchMessages(@PathVariable("id") long id, @RequestParam("query") String query) {
    return queueService.findQueueById(id)
        .<ResponseEntity<List<Message>>>map(q -> ResponseEntity.ok(messageService.searchMessages(q, query)))
        .orElseGet(() -> ResponseEntity.notFound().build());
}


    @DeleteMapping("/{id}/messages/{msgId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") long id, @PathVariable("msgId") long msgId) {
        Optional<MessageQueue> queue = queueService.findQueueById(id);
        if (queue.isPresent() && messageService.deleteMessageIfOrphan(msgId, queue.get())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}/messages/next")
public ResponseEntity<Message> getNextMessage(@PathVariable("id") long id) {
    return queueService.findQueueById(id)
        .<ResponseEntity<Message>>map(q -> {  // ✅ Ajoute une conversion explicite en `ResponseEntity<Message>`
            Message message = messageService.getNextMessage(q);
            if (message == null) {
                return ResponseEntity.noContent().build();
            }
            message.markAsRead();
            logger.info("Message {} a été récupéré à {}", message.getId(), LocalDateTime.now());
            return ResponseEntity.ok(message);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());  // ✅ Assure un `ResponseEntity<Message>`
}


}