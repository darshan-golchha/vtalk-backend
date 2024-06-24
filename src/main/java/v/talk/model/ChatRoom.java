package v.talk.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "room")
public class ChatRoom {
    
    @Id
    private String id;
    private String roomCode;
    private String label;
    private List<User> users;

    // Adding categorization of chat rooms
    private String category;
    
}