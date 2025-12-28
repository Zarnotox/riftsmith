package com.riftsmith.datasyncer.domain.riftbound;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardEntity {

    @Id
    private UUID id;

    private String name;
}