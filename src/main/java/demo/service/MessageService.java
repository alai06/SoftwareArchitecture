package demo.service;

import demo.model.Message;
import demo.model.MessageQueue;
import demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAllByQueue(MessageQueue queue) {
        return messageRepository.findAllByQueue(queue);
    }

    public List<Message> searchMessages(MessageQueue queue, String query) {
        return messageRepository.findByTextContainingAndQueue(query, queue);
    }
    
    public boolean deleteMessageIfOrphan(long msgId, MessageQueue queue) {
        Message message = messageRepository.findById(msgId);
        if (message != null) {
            // Ne pas supprimer si le message n'a jamais été lu
            if (message.getTimeFirstAccessed() == null) {
                logger.warn("Tentative de suppression du message {} qui n'a jamais été lu", message.getId());
                return false;
            }
            
            message.removeQueue(queue);
            if (message.isOrphan()) {
                long timeToDelete = Duration.between(message.getTimeCreated(), LocalDateTime.now()).toMinutes();
                logger.info("Message {} supprimé après {} minutes", message.getId(), timeToDelete);
                messageRepository.delete(message);
                return true;
            }
        }
        return false;
    }
}