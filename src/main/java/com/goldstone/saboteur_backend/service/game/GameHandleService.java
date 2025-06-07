package com.goldstone.saboteur_backend.service.game;

import com.corundumstudio.socketio.SocketIOClient;
import com.goldstone.saboteur_backend.dtos.game.request.*;
import com.goldstone.saboteur_backend.dtos.game.response.GetGameStateResponseDto;
import com.goldstone.saboteur_backend.dtos.game.response.NextTurnResponseDto;
import com.goldstone.saboteur_backend.dtos.game.response.PlayCardResponseDto;
import com.goldstone.saboteur_backend.dtos.game.response.SelectGoldCardResponseDto;

public interface GameHandleService {
    PlayCardResponseDto playCard(SocketIOClient client, PlayCardRequestDto dto) throws Exception;

    NextTurnResponseDto nextTurn(SocketIOClient client, NextTurnRequestDto dto) throws Exception;

    GetGameStateResponseDto getGameState(SocketIOClient client, GetGameStateRequestDto dto)
            throws Exception;

    PlayCardResponseDto discardCard(SocketIOClient client, DiscardCardRequestDto dto)
            throws Exception;

    SelectGoldCardResponseDto selectGoldCard(SocketIOClient client, SelectGoldCardRequestDto dto)
            throws Exception;
}
