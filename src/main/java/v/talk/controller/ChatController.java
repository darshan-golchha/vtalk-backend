package v.talk.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import v.talk.model.ChatMessage;
import v.talk.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	@Autowired
	private ChatMessageService chatService;
	
	
	
	@MessageMapping("/chat")
	public void processMessage(@Payload ChatMessage chatMessage) {
	    try {
	    	
	    	ChatMessage savedMsg = chatService.save(chatMessage);
		    chatService.send(savedMsg);
	    } catch(Exception e) {
	    	return;
	    }
	}
	
	
	
	@GetMapping("/chats")
	public ResponseEntity<List<ChatMessage>> findChatMessages(
			@RequestParam String roomcode
	){
		return ResponseEntity.ok(chatService.findChatMessages(roomcode));
	}
	
	@GetMapping("/deleteAllChats")
	public ResponseEntity<String> deleteAllChats(){
		chatService.deleteAllChats();
		return ResponseEntity.ok("Successfully Deleted All Previous Chats");
	}
	
	
	
}
