package v.talk.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chat")
public class ChatMessage {
	@Id
	private String id;
	private String fullName;
	private String senderId;
	private String roomCode;
	private String content;
	private Date timestamp;
	private String filePath;
}
