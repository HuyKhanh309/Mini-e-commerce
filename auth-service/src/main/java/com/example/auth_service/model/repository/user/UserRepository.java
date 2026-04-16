package com.example.auth_service.model.repository.user;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

import com.example.auth_service.model.entity.user.User;
import com.example.base_module.repo.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<User, UUID>{

	Optional<User> findByUsername(String username);
}
