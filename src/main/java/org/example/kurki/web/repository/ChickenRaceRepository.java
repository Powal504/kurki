package org.example.kurki.web.repository;

import org.example.kurki.web.model.ChickenRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChickenRaceRepository extends JpaRepository<ChickenRace, Long> {
}
