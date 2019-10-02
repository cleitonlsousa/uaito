package com.uaito.repository;

import com.uaito.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findByCreated(Long created);

    Optional<Tournament> findByIdAndCreated(Long id, Long created);

}
