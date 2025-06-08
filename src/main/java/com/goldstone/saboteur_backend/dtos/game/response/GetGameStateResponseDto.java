package com.goldstone.saboteur_backend.dtos.game.response;

import com.goldstone.saboteur_backend.domain.card.Card;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetGameStateResponseDto {
    private UUID currentPlayerId;
    private String currentPlayerName;
    private Map<UUID, Integer> playerCardCounts;
    private int cardPoolRemaining;
    private List<Card> myCards; // 내 손패 카드 id 목록
}
