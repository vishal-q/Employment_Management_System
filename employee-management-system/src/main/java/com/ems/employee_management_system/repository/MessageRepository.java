package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByToUserIdOrderBySentAtDesc(String toUserId);
    List<Message> findByFromUserIdOrderBySentAtDesc(String fromUserId);
    List<Message> findByFromRoleOrderBySentAtDesc(String fromRole);
    List<Message> findByReplyToIdOrderBySentAtAsc(String replyToId);
    long countByToUserIdAndReadByRecipientFalse(String toUserId);
    long countByFromRoleAndReadByRecipientFalse(String fromRole);
}
