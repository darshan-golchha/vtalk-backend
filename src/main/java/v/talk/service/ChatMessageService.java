package v.talk.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import v.talk.model.ChatMessage;
import v.talk.model.ChatRoom;
import v.talk.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	
	@Autowired
	private ChatMessageRepository chatRepo;
	
	private Random random = new Random();
	
	private final SimpMessagingTemplate messagingTemplate;
	
//	public ChatMessage save(ChatMessage chatMessage) {
//		var chatId = chatService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
//		chatMessage.setChatId(chatId.get());
//		chatRepo.save(chatMessage);
//		return chatMessage;
//	}
//	
//	public List<ChatMessage> findChatMessages(String senderId, String recipientId){
//		var chatId = chatService.getChatRoomId(senderId, recipientId, false);
//		return chatId.map(chatRepo::findByChatId).orElse(new ArrayList<>());
//		
//	}
	
	public ChatMessage save(ChatMessage chatMessage) {
		chatMessage.setId(generateUniqueID());
		chatRepo.save(chatMessage);
		return chatMessage;
	}
	
	public List<ChatMessage> findChatMessages(String roomCode){
		return chatRepo.findAllByRoomCode(roomCode);
	}
	
	
	private String generateUniqueID() {
        String id;
        boolean isUnique;
        do {
            id = generateRandomID();
            isUnique = checkIDUniqueness(id);
        } while (!isUnique);
        return id;
    }

    private boolean checkIDUniqueness(String id) {
        Optional<ChatMessage> exists = chatRepo.findById(id);
        return exists.isEmpty();
    }

    private String generateRandomID() {
        return 100_000_000 + random.nextInt(900_000_000) + "";
    }

	public void deleteAllChats() {
		chatRepo.deleteAll();
	}
	
	public void deleteRoomChats(String roomCode) {
		chatRepo.deleteAllByRoomCode(roomCode);
	}
	
	public void send(ChatMessage savedMsg) {

	    String topicDestination = "/user/vtalk/messages/" + savedMsg.getRoomCode();

	    messagingTemplate.convertAndSend(topicDestination, ChatMessage.builder()
	            .id(savedMsg.getId())
	            .senderId(savedMsg.getSenderId())
	            .fullName(savedMsg.getFullName())
	            .roomCode(savedMsg.getRoomCode())
	            .timestamp(savedMsg.getTimestamp())
	            .content(savedMsg.getContent())
	            .build()
	    );

	}
}
