package ru.ddc.consultationsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ddc.consultationsservice.entity.Slot;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>, JpaSpecificationExecutor<Slot> {

    @Query("select (count(s) > 0) from Slot s where s.specialistId = ?1 and " +
            "(s.beginAt < ?2 and ?2 < s.endAt or " +
            "s.beginAt < ?3 and ?3 < s.endAt or " +
            "s.beginAt = ?2 and s.endAt = ?3 or " +
            "s.beginAt > ?2 and s.endAt < ?3)")
    boolean isDatesOverlaps(UUID specialistId, LocalDateTime beginAt, LocalDateTime endAt);
}