package scratch.cucumber.example.service;

import org.springframework.data.repository.CrudRepository;
import scratch.cucumber.example.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
