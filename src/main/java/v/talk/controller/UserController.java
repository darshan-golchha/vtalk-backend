package v.talk.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import v.talk.model.ChatNotification;
import v.talk.model.User;
import v.talk.service.ChatRoomService;
import v.talk.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private ChatRoomService chatService;
	
	@PostMapping("/addUser")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User createdUser = service.saveUser(user); 
		return ResponseEntity.ok(createdUser);
	}
	
	@GetMapping("/deleteUser")
	public ResponseEntity<User> deleteUser(@RequestParam String id){
		return ResponseEntity.ok(service.deleteUser(id));
	}
	
	@MessageMapping("/user/connectUser")
	public ResponseEntity<User> userArrivalMessage(@Payload User user) {
		String destination = "/user/vtalk/messages/" + user.getRoomCode();
		messagingTemplate.convertAndSend(destination, ChatNotification.builder()
	            .roomCode(user.getRoomCode())
	            .timestamp(new Date())
	            .content("User "+user.getFullName()+" has joined.")
	            .build()
	    );
		return ResponseEntity.ok(user);
	}
	
	
	@MessageMapping("/user/disconnectUser")
	public User disconnect(@Payload User user) {
		String destination = "/user/vtalk/messages/" + user.getRoomCode();
		messagingTemplate.convertAndSend(destination, ChatNotification.builder()
	            .roomCode(user.getRoomCode())
	            .timestamp(new Date())
	            .content("User "+user.getFullName()+" has left the conversation.")
	            .build()
	    );
		service.disconnect(user);
		return user;
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> findConnectedUsers(){
		List<User> users = service.findConnectedUsers();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/deleteAllUsers")
	public ResponseEntity<String> deleteAllUsers(){
		service.deleteAll();
		return ResponseEntity.ok("Users Deleted Sucessfully");
	}
}
