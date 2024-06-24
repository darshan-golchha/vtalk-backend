package v.talk.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import v.talk.model.ChatRoom;
import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>{
	Optional<ChatRoom> findByRoomCode(String roomCode);

	List<ChatRoom> findByLabelIgnoreCaseContaining(String search);

	List<ChatRoom> findByCategoryIgnoreCaseContaining(String search);
}
