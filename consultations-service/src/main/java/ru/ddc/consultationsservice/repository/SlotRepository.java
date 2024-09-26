package ru.ddc.consultationsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ddc.consultationsservice.entity.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
}