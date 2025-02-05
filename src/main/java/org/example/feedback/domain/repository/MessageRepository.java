package org.example.feedback.domain.repository;

import org.example.feedback.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    public List<Message> findByInboxId(UUID inboxId);

}
