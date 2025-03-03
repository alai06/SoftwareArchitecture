package demo.repository;

import demo.model.MessageQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueRepository extends JpaRepository<MessageQueue, Long> {
}
