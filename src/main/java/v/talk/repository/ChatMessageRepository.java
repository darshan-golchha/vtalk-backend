package v.talk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import v.talk.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>{
	List<ChatMessage> findAllByRoomCode(String s);

	void deleteAllByRoomCode(String roomCode);
}
