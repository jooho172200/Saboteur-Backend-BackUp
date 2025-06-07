package com.goldstone.saboteur_backend.dtos.gameRoom.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetGameRoomUsersRequestDto {
    private UUID gameRoomId;
}
