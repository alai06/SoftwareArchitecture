package demo.service;

import demo.model.MessageQueue;
import demo.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QueueService {

    @Autowired
    private QueueRepository queueRepository;

    // Récupérer toutes les files de messages
    public List<MessageQueue> findAllQueues() {
        return queueRepository.findAll();
    }

    // Trouver une file de messages par son ID
    public Optional<MessageQueue> findQueueById(long id) {
        return queueRepository.findById(id);
    }

    // Ajouter une nouvelle file de messages
    public MessageQueue saveQueue(MessageQueue queue) {
        return queueRepository.save(queue);
    }

    // Supprimer une file de messages
    public void deleteQueue(long id) {
        queueRepository.deleteById(id);
    }
}
