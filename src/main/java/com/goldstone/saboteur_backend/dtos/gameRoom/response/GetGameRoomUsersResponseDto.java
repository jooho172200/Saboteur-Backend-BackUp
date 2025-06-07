package com.goldstone.saboteur_backend.dtos.gameRoom.response;

import com.goldstone.saboteur_backend.domain.game.GameRoom;
import com.goldstone.saboteur_backend.domain.mapping.UserGameRoom;
import com.goldstone.saboteur_backend.dtos.userGameRoom.response.UserGameRoomInfoResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetGameRoomUsersResponseDto {
    private final String gameRoomId;
    private final UserGameRoomInfoResponseDto[] users;

    public static GetGameRoomUsersResponseDto of(
            GameRoom gameRoom, List<UserGameRoom> userGameRooms) {
        return GetGameRoomUsersResponseDto.builder()
                .gameRoomId(gameRoom.getId().toString())
                .users(
                        userGameRooms.stream()
                                .map(UserGameRoomInfoResponseDto::of)
                                .toArray(UserGameRoomInfoResponseDto[]::new))
                .build();
    }
}
