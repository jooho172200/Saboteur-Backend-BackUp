package com.goldstone.saboteur_backend.service.gameRoom;

import com.corundumstudio.socketio.SocketIOClient;
import com.goldstone.saboteur_backend.domain.board.Board;
import com.goldstone.saboteur_backend.domain.game.*;
import com.goldstone.saboteur_backend.domain.mapping.UserGameRole;
import com.goldstone.saboteur_backend.domain.user.User;
import com.goldstone.saboteur_backend.domain.user.UserCardDeck;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.CreateGameRoomRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.JoinGameRoomRequestDto;
import com.goldstone.saboteur_backend.dtos.gameRoom.request.StartGameRequestDto;
import com.goldstone.saboteur_backend.exception.BusinessException;
import com.goldstone.saboteur_backend.exception.code.error.GameRoomErrorCode;
import com.goldstone.saboteur_backend.exception.code.error.UserErrorCode;
import com.goldstone.saboteur_backend.session.GlobalSession;
import com.goldstone.saboteur_backend.socketIo.SocketIoService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    @Autowired private final GlobalSession globalSession;
    private final SocketIoService socketIoService;

    public GameRoom getGameRoomById(UUID id) {
        GameRoom gameRoom = this.globalSession.getGameRoomSession(id);
        if (gameRoom == null) {
            throw new BusinessException(GameRoomErrorCode.GAME_ROOM_NOT_FOUND);
        }
        return gameRoom;
    }

    @Override
    public GameRoom createGameRoom(CreateGameRoomRequestDto dto) {
        User host = this.globalSession.getUserSession(dto.getUserId());
        if (host == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        GameRoom gameRoom = GameRoom.createGameRoomByHost(host);

        this.globalSession.addGameRoomSession(gameRoom);

        GoldCardDeck goldDeck = new GoldCardDeck();

        this.globalSession.addGoldDeckSession(gameRoom.getId(), goldDeck);

        return gameRoom;
    }

    @Override
    public GameRoom joinGameRoom(SocketIOClient client, JoinGameRoomRequestDto dto) {
        User user = this.globalSession.getUserSession(dto.getUserId());
        if (user == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
        }

        GameRoom gameRoom = this.getGameRoomById(dto.getGameRoomId());

        if (gameRoom.getSetting().getHost() == null) {
            System.out.println("[PROTOTYPE] If host is not set, set user as host.");
            System.out.println("[PROTOTYPE] User: " + gameRoom.getSetting().getHost());
            gameRoom.getSetting().setHost(user);
        }

        gameRoom.checkJoinGameRoom(user);

        gameRoom.addPlayer(user);
        client.joinRoom(gameRoom.getId().toString());

        return gameRoom;
    }

    @Override
    public GameRoom startGame(StartGameRequestDto dto) {
        GameRoom gameRoom = this.getGameRoomById(dto.getGameRoomId());
        gameRoom.canStartGame(dto.getUserId());
        gameRoom.startGame();

        // 첫 라운드 초기화
        initRound(gameRoom.getId());
        return gameRoom;
    }

    /** 라운드 초기화: 역할, 보드, 턴매니저, 카드풀, 카드 분배 등 */
    public void initRound(UUID gameRoomId) {
        GameRoom gameRoom = this.getGameRoomById(gameRoomId);

        // 1. 기존 세션 제거
        globalSession.removeGameBoardSession(gameRoomId);
        globalSession.removeTurnManagerSession(gameRoomId);
        globalSession.removeGameCardPoolSession(gameRoomId);
        globalSession.removeGoldFinder(gameRoomId);
        globalSession.removeGoldDistributionState(gameRoomId);

        // 2. 역할 분배
        GameRoleAssignment roleAssigner = new GameRoleAssignment();
        List<UserGameRole> roles = roleAssigner.assignRoles(gameRoom, gameRoom.getUserGameRooms());
        globalSession.addRoleAssignment(gameRoomId, roles);

        // 3. 각 플레이어에게 자신의 역할만 전송
        for (UserGameRole userGameRole : roles) {
            socketIoService.sendEventToUser(
                    userGameRole.getUser().getId(), "yourRole", userGameRole.getRole());
        }

        // 4. 보드, 턴매니저, 카드풀 새로 생성 및 세션 등록
        globalSession.addGameBoardSession(gameRoom, new Board());
        GameTurnManager turnManager = new GameTurnManager(gameRoom.getUserGameRooms());
        globalSession.addTurnManagerSession(gameRoomId, turnManager);
        GameCardPool newCardPool = GameCardPool.createDefaultPool(gameRoomId);
        globalSession.addGameCardPoolSession(gameRoomId, newCardPool);

        // 5. 카드 분배
        int cardPerPlayer = GameCardPool.getCardsPerPlayer(gameRoom.getUserGameRooms().size());
        Map<User, UserCardDeck> userCardDecks =
                newCardPool.assignCardsToUserDecks(gameRoom.getUserGameRooms(), cardPerPlayer);

        for (User user : userCardDecks.keySet()) {
            user.setCardDeck(userCardDecks.get(user));
        }
    }
}
