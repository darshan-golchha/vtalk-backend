package v.talk.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "user")
public class User {
	@Id
	private String userId;
	private String fullName;
	private Status status;
	private String roomCode;
	
	@Override
	public String toString() {
		return "{ userId: "+ userId +", fullName: "+ fullName +", status: "+ status+", roomCode: "+roomCode+"}";
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.userId.equals(obj);
	}
}
