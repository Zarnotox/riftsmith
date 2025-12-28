package com.riftsmith.datasyncer.infrastructure.riftbound;

import com.riftsmith.datasyncer.domain.riftbound.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {
}