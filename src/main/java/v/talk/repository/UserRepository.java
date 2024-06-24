package v.talk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import v.talk.model.Status;
import v.talk.model.User;


@Repository
public interface UserRepository extends MongoRepository<User, String>{

	List<User> findAllByStatus(Status online);

}
