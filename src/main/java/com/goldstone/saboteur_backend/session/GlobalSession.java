package com.goldstone.saboteur_backend.session;

import com.goldstone.saboteur_backend.domain.board.Board;
import com.goldstone.saboteur_backend.domain.game.GameCardPool;
import com.goldstone.saboteur_backend.domain.game.GameRoom;
import com.goldstone.saboteur_backend.domain.game.GameTurnManager;
import com.goldstone.saboteur_backend.domain.game.GoldCardDeck;
import com.goldstone.saboteur_backend.domain.mapping.UserGameRole;
import com.goldstone.saboteur_backend.domain.user.User;
import com.goldstone.saboteur_backend.exception.BusinessException;
import com.goldstone.saboteur_backend.exception.code.error.CommonErrorCode;
import com.goldstone.saboteur_backend.service.game.GoldDistributionState;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class GlobalSession {
    // Key: User ID
    private final Map<UUID, User> userSession = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, GameRoom> gameRoomSession = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, Board> gameBoardSession = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, GameCardPool> gameCardPoolSession = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, GameTurnManager> turnManagerSessions = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, GoldCardDeck> goldDeckSession = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, List<UserGameRole>> roleAssignmentSession = new ConcurrentHashMap<>();
    // Key: Game Room ID, userId → socketId 매핑
    private final Map<UUID, UUID> userSocketIdMap = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, User> goldFinderSession = new ConcurrentHashMap<>();
    // Key: Game Room ID
    private final Map<UUID, GoldDistributionState> goldDistributionSession =
            new ConcurrentHashMap<>();

    private <T> T wrapperCall(Supplier<T> action) {
        try {
            return action.get();
        } catch (Exception e) {
            System.err.println("Exception in GlobalSession wrapperCall:\n" + e);
            throw new BusinessException(CommonErrorCode.FAILED_TO_MANIPULATE_GLOBAL_SESSION);
        }
    }

    public boolean addUserSession(User user) {
        this.wrapperCall(() -> this.userSession.put(user.getId(), user));
        return true;
    }

    public User getUserSession(UUID userId) {
        return this.wrapperCall(() -> this.userSession.get(userId));
    }

    public boolean addGameRoomSession(GameRoom gameRoom) {
        this.wrapperCall(() -> this.gameRoomSession.put(gameRoom.getId(), gameRoom));
        return true;
    }

    public GameRoom getGameRoomSession(UUID gameRoomId) {
        return this.wrapperCall(() -> this.gameRoomSession.get(gameRoomId));
    }

    public boolean addGameBoardSession(GameRoom gameRoom, Board board) {
        this.wrapperCall(() -> this.gameBoardSession.put(gameRoom.getId(), board));
        return true;
    }

    public void removeGameBoardSession(UUID gameRoomId) {
        this.wrapperCall(() -> this.gameBoardSession.remove(gameRoomId));
    }

    public Board getGameBoardSession(UUID gameRoomId) {
        return this.wrapperCall(() -> this.gameBoardSession.get(gameRoomId));
    }

    public boolean addGameCardPoolSession(UUID gameRoomId, GameCardPool cardPool) {
        this.wrapperCall(() -> this.gameCardPoolSession.put(gameRoomId, cardPool));
        return true;
    }

    public void removeGameCardPoolSession(UUID gameRoomId) {
        this.wrapperCall(() -> this.gameCardPoolSession.remove(gameRoomId));
    }

    public GameCardPool getGameCardPoolSession(UUID gameRoomId) {
        return this.wrapperCall(() -> this.gameCardPoolSession.get(gameRoomId));
    }

    public boolean addTurnManagerSession(UUID gameRoomId, GameTurnManager turnManager) {
        this.wrapperCall(() -> this.turnManagerSessions.put(gameRoomId, turnManager));
        return true;
    }

    public void removeTurnManagerSession(UUID gameRoomId) {
        this.wrapperCall(() -> this.turnManagerSessions.remove(gameRoomId));
    }

    public GameTurnManager getTurnManagerSession(UUID gameRoomId) {
        return this.wrapperCall(() -> this.turnManagerSessions.get(gameRoomId));
    }

    public boolean addGoldDeckSession(UUID gameRoomId, GoldCardDeck goldDeck) {
        this.wrapperCall(() -> this.goldDeckSession.put(gameRoomId, goldDeck));
        return true;
    }

    public GoldCardDeck getGoldDeckSession(UUID gameRoomId) {
        return this.wrapperCall(() -> this.goldDeckSession.get(gameRoomId));
    }

    public boolean addRoleAssignment(UUID gameRoomId, List<UserGameRole> roles) {
        return wrapperCall(() -> roleAssignmentSession.put(gameRoomId, roles) != null);
    }

    public List<UserGameRole> getRoleAssignment(UUID gameRoomId) {
        return wrapperCall(() -> roleAssignmentSession.get(gameRoomId));
    }

    public void addUserSocketId(UUID userId, UUID socketId) {
        userSocketIdMap.put(userId, socketId);
    }

    public UUID getSocketIdByUserId(UUID userId) {
        return userSocketIdMap.get(userId);
    }

    public void removeUserSocketId(UUID userId) {
        userSocketIdMap.remove(userId);
    }

    public void setGoldFinder(UUID gameRoomId, User user) {
        wrapperCall(() -> goldFinderSession.put(gameRoomId, user));
    }

    public User getGoldFinder(UUID gameRoomId) {
        return wrapperCall(() -> goldFinderSession.get(gameRoomId));
    }

    public void removeGoldFinder(UUID gameRoomId) {
        wrapperCall(() -> goldFinderSession.remove(gameRoomId));
    }

    public void setGoldDistributionState(UUID gameRoomId, GoldDistributionState state) {
        wrapperCall(() -> goldDistributionSession.put(gameRoomId, state));
    }

    public GoldDistributionState getGoldDistributionState(UUID gameRoomId) {
        return wrapperCall(() -> goldDistributionSession.get(gameRoomId));
    }

    public void removeGoldDistributionState(UUID gameRoomId) {
        wrapperCall(() -> goldDistributionSession.remove(gameRoomId));
    }
}
