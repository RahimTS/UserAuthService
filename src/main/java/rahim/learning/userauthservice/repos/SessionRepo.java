package rahim.learning.userauthservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rahim.learning.userauthservice.models.Session;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {
    Session save(Session session);

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
}
