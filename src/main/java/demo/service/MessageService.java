package demo.service;

import demo.model.Message;
import demo.model.MessageQueue;
import demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;  

    public List<Message> findAllByQueue(MessageQueue queue) {
        return messageRepository.findAllByQueue(queue);
    }

    public List<Message> searchMessages(MessageQueue queue, String query) {
        return messageRepository.findByTextContainingAndQueue(query, queue);
    }

    public boolean deleteMessageIfOrphan(long msgId, MessageQueue queue) {
        Message message = messageRepository.findById(msgId).orElse(null);
        if (message != null && message.isOrphan()) {
            messageRepository.delete(message);
            return true;
        }
        return false;
    }
    
    public Message getNextMessage(MessageQueue queue) {
        return messageRepository.findAllByQueue(queue).stream().findFirst().orElse(null);
    }

    public Message createMessage(Message message) { // ✅ Ajout de la méthode createMessage
        return messageRepository.save(message);
    }
}
