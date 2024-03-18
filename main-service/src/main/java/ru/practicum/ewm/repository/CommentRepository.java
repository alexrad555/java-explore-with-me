package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Comment;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    @Query("select c from Comment c where c.deletionDate is null")
    List<Comment> findAll();

    @Override
    @Query("select c from Comment c where c.deletionDate is null")
    Page<Comment> findAll(Pageable pageable);

    @Override
    @Query("select c from Comment c where c.deletionDate is null and c.id = ?1")
    Optional<Comment> findById(Long id);

    @Transactional
    @Modifying
    @Query("update Comment c set c.deletionDate = current_timestamp() where c.id = ?1")
    void softDelete(Long commentId);

    @Query("select c from Comment c where c.deletionDate is null and c.event.id = ?1")
    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

    @Query("select c from Comment c where c.deletionDate is null and c.author.id = ?1")
    Page<Comment> findAllByAuthorId(Long authorId, Pageable pageable);

    Page<Comment> findAllByPublishDateIsNullAndDeletionDateIsNull(Pageable pageable);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);

    Page<Comment> findAllByPublishDateIsNullAndDeletionDateIsNullAndEventId(Long eventId, Pageable pageable);

    Page<Comment> findAllByPublishDateIsNotNullAndDeletionDateIsNullAndEventId(Long eventId, Pageable pageable);
}
