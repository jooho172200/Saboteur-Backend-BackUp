package com.goldstone.saboteur_backend.dtos.userGameRoom.response;

import com.goldstone.saboteur_backend.domain.mapping.UserGameRoom;
import com.goldstone.saboteur_backend.dtos.user.response.UserInfoResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGameRoomInfoResponseDto {
    private final UserInfoResponseDto user;

    public static UserGameRoomInfoResponseDto of(UserGameRoom userGameRoom) {
        return UserGameRoomInfoResponseDto.builder()
                .user(UserInfoResponseDto.from(userGameRoom.getUser()))
                .build();
    }
}
