package com.example.core_service.model.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.base_module.repo.BaseRepository;
import com.example.core_service.model.entity.user.User;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
