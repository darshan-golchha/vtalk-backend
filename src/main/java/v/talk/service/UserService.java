package v.talk.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import v.talk.model.Status;
import v.talk.model.User;
import v.talk.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	private Random random = new Random();
	
	public User saveUser(User user) {
		if (user.getStatus() != Status.JOINED) {
			user.setStatus(Status.ONLINE);
		}
		if(user.getUserId() == null) {
			user.setUserId(generateUniqueID());
		}
		repository.save(user);
		return user;
	}
	
	public void disconnect(User user) {
		var storedUser = repository.findById(user.getUserId())
				.orElse(null);
		if (storedUser != null) {
			repository.delete(user);
			user.setRoomCode(null);
			user.setStatus(Status.ONLINE);
			repository.save(user);
		}
	}
	
	public List<User> findConnectedUsers(){
		return repository.findAll();
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
		var exists = repository.findById(id);
		if (exists.isEmpty()) {
			return true;
		}
		return false;
	}

	private String generateRandomID() {
		return 100_000_000 + random.nextInt(900_000_000)+ "";
	}

	public User deleteUser(String id) {
		var storedUser = repository.findById(id)
				.orElse(null);
		if (storedUser != null) {
			repository.delete(storedUser);
		}
		return storedUser;
	}
	
	public User getUser(String id) {
		return repository.findById(id).get();
	}
	
	public void deleteAll() {
		repository.deleteAll();
	}
}
