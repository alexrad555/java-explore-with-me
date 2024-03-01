package ru.practicum.stats.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.server.entity.Hit;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

}
