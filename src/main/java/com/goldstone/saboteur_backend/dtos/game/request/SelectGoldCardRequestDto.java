package com.goldstone.saboteur_backend.dtos.game.request;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectGoldCardRequestDto {
    private UUID gameRoomId;
    private UUID userId;
    private UUID selectedGoldCardId;
}
