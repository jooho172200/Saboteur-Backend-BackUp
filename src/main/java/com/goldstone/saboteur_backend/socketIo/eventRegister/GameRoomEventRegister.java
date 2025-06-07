package com.goldstone.saboteur_backend.socketIo.eventRegister;

import com.corundumstudio.socketio.SocketIOServer;
import com.goldstone.saboteur_backend.domain.game.GameRoom;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.GetGameRoomUsersRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.JoinGameRoomRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.StartGameRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.response.GameRoomInfoResponseDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.response.GetGameRoomUsersResponseDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.response.JoinGameRoomResponseDto;
import com.goldstone.saboteur_backend.exception.BusinessException;
import com.goldstone.saboteur_backend.exception.code.error.ErrorCode;
import com.goldstone.saboteur_backend.exception.responseDto.ErrorResponse;
import com.goldstone.saboteur_backend.service.gameRoom.GameRoomService;
import com.goldstone.saboteur_backend.socketIo.SocketIoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameRoomEventRegister implements SocketEventRegister {
    private final GameRoomService gameRoomService;
    private final SocketIoService socketIoService;

    @Override
    public void registerEvents(SocketIOServer server) {
        // 게임 룸 참여 이벤트
        server.addEventListener(
                "joinGameRoom",
                JoinGameRoomRequestDto.class,
                (client, data, ackSender) -> {
                    try {
                        GameRoom gameRoom = this.gameRoomService.joinGameRoom(client, data);

                        client.sendEvent("gameRoomJoined", JoinGameRoomResponseDto.from(gameRoom));
                    } catch (Exception e) {
                        if (e instanceof BusinessException) {
                            ErrorCode errorCode = ((BusinessException) e).getErrorCode();
                            client.sendEvent("errorEvent", new ErrorResponse(errorCode));
                        } else {
                            client.sendEvent("errorEvent", ErrorResponse.internalServerError());
                        }
                    }
                });

        // 게임 시작 이벤트
        server.addEventListener(
                "startGame",
                StartGameRequestDto.class,
                (client, data, ackSender) -> {
                    try {
                        GameRoom gameRoom = this.gameRoomService.startGame(data);

                        this.socketIoService.sendBroadCast(
                                gameRoom.getId(),
                                "gameStarted",
                                GameRoomInfoResponseDto.from(gameRoom));
                    } catch (Exception e) {
                        if (e instanceof BusinessException) {
                            ErrorCode errorCode = ((BusinessException) e).getErrorCode();
                            client.sendEvent("errorEvent", new ErrorResponse(errorCode));
                        } else {
                            client.sendEvent("errorEvent", ErrorResponse.internalServerError());
                        }
                    }
                });

        // 게임 룸에 참여한 유저 목록 조회 이벤트
        server.addEventListener(
                "getGameRoomUsers",
                GetGameRoomUsersRequestDto.class,
                (client, data, ackSender) -> {
                    try {
                        GameRoom gameRoom =
                                this.gameRoomService.getGameRoomById(data.getGameRoomId());

                        this.socketIoService.sendBroadCast(
                                gameRoom.getId(),
                                "gameRoomUsers",
                                GetGameRoomUsersResponseDto.of(
                                        gameRoom, gameRoom.getUserGameRooms()));
                    } catch (Exception e) {
                        if (e instanceof BusinessException) {
                            ErrorCode errorCode = ((BusinessException) e).getErrorCode();
                            client.sendEvent("errorEvent", new ErrorResponse(errorCode));
                        } else {
                            client.sendEvent("errorEvent", ErrorResponse.internalServerError());
                        }
                    }
                });
    }
}
