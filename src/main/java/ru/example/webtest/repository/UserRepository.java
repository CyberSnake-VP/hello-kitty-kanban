package ru.example.webtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.example.webtest.entity.User;
import ru.example.webtest.entity.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRoleInOrderById(Collection<UserRole> role);
    Optional<User> findByEmailIgnoreCase(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User SET role = :role WHERE id = :id")
    void updateRole(long id, @Param("role") UserRole newRole);
}
