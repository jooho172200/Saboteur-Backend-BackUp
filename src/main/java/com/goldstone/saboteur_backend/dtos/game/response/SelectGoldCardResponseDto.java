package com.goldstone.saboteur_backend.dtos.game.response;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectGoldCardResponseDto {
    private boolean success;
    private String message;
    private List<UUID> remainingGoldCardIds;
    private UUID nextPlayerUserId;
}
