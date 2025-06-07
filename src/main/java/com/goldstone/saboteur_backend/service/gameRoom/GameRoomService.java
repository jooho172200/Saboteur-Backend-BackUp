package com.goldstone.saboteur_backend.service.gameRoom;

import com.corundumstudio.socketio.SocketIOClient;
import com.goldstone.saboteur_backend.domain.game.GameRoom;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.CreateGameRoomRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.JoinGameRoomRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.StartGameRequestDto;
import java.util.UUID;

public interface GameRoomService {
    GameRoom getGameRoomById(UUID gameRoomId);

    GameRoom createGameRoom(CreateGameRoomRequestDto dto);

    GameRoom joinGameRoom(SocketIOClient client, JoinGameRoomRequestDto dto);

    GameRoom startGame(StartGameRequestDto dto);
}
