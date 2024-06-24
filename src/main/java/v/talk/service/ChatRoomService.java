package v.talk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import v.talk.model.ChatMessage;
import v.talk.model.ChatRoom;
import v.talk.model.Status;
import v.talk.model.User;
import v.talk.repository.ChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	
	@Autowired
	private ChatMessageService chatService;

    private Random random = new Random();
	
//	public Optional<String> getChatRoomId(
//			String senderId,
//			String recipientId,
//			boolean createNewRoomIfNotExists
//	) {
//		return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
//				.map(ChatRoom::getId)
//				.or(() -> {
//					if (createNewRoomIfNotExists) {
//						var chatId = createChatId(senderId, recipientId);
//						return Optional.of(chatId);
//					}
//					return Optional.empty();
//				});
//	}
//
//	private String createChatId(String senderId, String recipientId) {
//		var chatId = String.format("%s_%s",senderId,recipientId);
//		ChatRoom senderRecipient = ChatRoom.builder()
//				.id(chatId)
//				.roomCode(recipientId)
//				.build();
//		
//		ChatRoom reciepientSender = ChatRoom.builder()
//				.chatId(chatId)
//				.senderId(recipientId)
//				.recipientId(senderId)
//				.build();
//		chatRoomRepository.save(senderRecipient);
//		chatRoomRepository.save(reciepientSender);
//		return chatId;
//	}
	
	@Autowired
    private final ChatRoomRepository repository;
	
	public ChatRoom createRoom(String label, String category) {
		if (label ==null || label.isBlank()) {
			return null;
		}
		ChatRoom room = new ChatRoom();
		room.setId(generateUniqueID());
		room.setRoomCode(generateRoomCode());
		room.setLabel(label);
		room.setUsers(new ArrayList<>());
		room.setCategory(category);
		repository.save(room);
		return room;
	}
	
	public User addUserToRoom(ChatRoom room, User user) {
		ChatRoom roomNew = room;
		repository.deleteById(room.getId());
		if (room.getUsers() != null) {
			List<User> users = roomNew.getUsers();
			users.add(user);
			roomNew.setUsers(users);
		} else {
			List<User> users = new ArrayList<>();
			users.add(user);
			roomNew.setUsers(users);
		}
		user.setRoomCode(room.getRoomCode());
		user.setStatus(Status.JOINED);
		repository.save(roomNew);
		return user;
	}
	
	public User removeUserFromRoom(ChatRoom room, User user) {
		ChatRoom roomNew = room;
		repository.deleteById(room.getId());
		if (room.getUsers() != null) {
			List<User> users = roomNew.getUsers();
			for (int i=0; i<users.size();i++) {
				if (users.get(i).getUserId().equals(user.getUserId())) {
					users.remove(i);
				}
			}
			roomNew.setUsers(users);
			if (users.isEmpty()) {
				roomNew.setUsers(new ArrayList<>());
			}
		} else {
			roomNew.setUsers(new ArrayList<>());
		}
		user.setRoomCode(null);
		user.setStatus(Status.ONLINE);
		repository.save(roomNew);
		return user;
	}
	
	public boolean dissolveRoom(ChatRoom room) {
		// Checking that there are no users in that room
		List<User> users = room.getUsers();
		if (users.isEmpty()) {
			chatService.deleteRoomChats(room.getRoomCode());
			repository.deleteById(room.getId());
			return true;
		}
		return false;
	}
	
	public List<ChatMessage> getRoomChats(ChatRoom room){
		return chatService.findChatMessages(room.getRoomCode());
	}
	
	public List<ChatRoom> getAvailableRooms(){
		return repository.findAll();
	}

	public ChatRoom getRoom(String roomCode) {
		return repository.findByRoomCode(roomCode).get();
	}
	
	private static final String PREFIX = "VT";

    public String generateRoomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder roomCode;
        boolean isUnique;
        do {
            roomCode = new StringBuilder(PREFIX);
            for (int i = PREFIX.length(); i < 9; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                roomCode.append(randomChar);
            }
            isUnique = checkRoomCodeUniqueness(roomCode.toString());
        } while (!isUnique);

        return roomCode.toString();
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

    private boolean checkRoomCodeUniqueness(String roomCode) {
        Optional<ChatRoom> exists = repository.findByRoomCode(roomCode);
        return exists.isEmpty();
    }

    private boolean checkIDUniqueness(String id) {
        Optional<ChatRoom> exists = repository.findById(id);
        return exists.isEmpty();
    }

    private String generateRandomID() {
        return 100_000_000 + random.nextInt(900_000_000) + "";
    }

	public void deleteAll() {
		repository.deleteAll();
	}
	
	public List<User> getActiveUsers(String roomCode) {
		return repository.findByRoomCode(roomCode).get().getUsers();
	}

	public List<ChatRoom> searchRooms(String search) {

        return repository.findByLabelIgnoreCaseContaining(search);
    }
	
}
