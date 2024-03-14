package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @Query("select u from User u where u.deletionDate is null")
    List<User> findAll();

    @Override
    @Query("select u from User u where u.deletionDate is null")
    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);

    @Override
    @Query("select u from User u where u.deletionDate is null and u.id = ?1")
    Optional<User> findById(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.deletionDate = current_timestamp() where u.id = ?1")
    void softDelete(Long userId);

    @Query("select u from User u where u.deletionDate is null and u.id in (?1)")
    Page<User> findByIdList(List<Long> ids, Pageable pageable);
}
