package v.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import v.talk.model.ChatRoom;
import v.talk.model.User;
import v.talk.service.ChatRoomService;
import v.talk.service.UserService;

@Controller
@RequiredArgsConstructor
public class RoomController {
	
	@Autowired
	private ChatRoomService roomService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoom>> getRooms(){
		return ResponseEntity.ok(roomService.getAvailableRooms());
	}
	
	@GetMapping("/searchRooms")
	public ResponseEntity<List<ChatRoom>> getSearchRooms(@RequestParam String label) {
	    return ResponseEntity.ok(roomService.searchRooms(label));
	}
	
	@GetMapping("/createRoom")
	public ResponseEntity<ChatRoom> createRoom(@RequestParam String label, @RequestParam String category) {
		ChatRoom room = roomService.createRoom(label, category);
		return ResponseEntity.ok(room);
	}
	
	@GetMapping("/joinRoom")
	public ResponseEntity<User> joinRoom(@RequestParam String roomcode, @RequestParam String userid){
		User user = userService.deleteUser(userid);
		user = roomService.addUserToRoom(roomService.getRoom(roomcode), user);
		userService.saveUser(user);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/leaveRoom")
	public ResponseEntity<User> leaveRoom(@RequestParam String roomcode, @RequestParam String userid){
		User user = userService.deleteUser(userid);
		user = roomService.removeUserFromRoom(roomService.getRoom(roomcode), user);
		userService.saveUser(user);
//		 Checking if all the users have left the room and dissolving accordingly
//		roomService.dissolveRoom(roomService.getRoom(roomcode));
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/deleteAllRooms")
	public ResponseEntity<String> deleteAllRooms() {
		roomService.deleteAll();
		return ResponseEntity.ok("Sucessfully Removed All Rooms");
	}
	
	@GetMapping("/roomLabel")
	public ResponseEntity<String> getRoomLabel(@RequestParam String roomcode){
		return ResponseEntity.ok(roomService.getRoom(roomcode).getLabel());
	}
	
	@GetMapping("/ActiveUsers")
	public ResponseEntity<List<User>> getRoomUsers(@RequestParam String roomcode){
		return ResponseEntity.ok(roomService.getActiveUsers(roomcode));
	}
	
	
	
}
