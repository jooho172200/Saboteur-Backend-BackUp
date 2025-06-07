package com.goldstone.saboteur_backend.service.card;

import com.goldstone.saboteur_backend.domain.board.Board;
import com.goldstone.saboteur_backend.domain.board.Cell;
import com.goldstone.saboteur_backend.domain.board.PathValidator;
import com.goldstone.saboteur_backend.domain.card.Card;
import com.goldstone.saboteur_backend.domain.card.PathCard;
import com.goldstone.saboteur_backend.domain.user.User;
import com.goldstone.saboteur_backend.dtos.card.request.PathCardRequest;
import com.goldstone.saboteur_backend.dtos.card.request.UseCardRequest;
import com.goldstone.saboteur_backend.dtos.card.response.UseCardResponse;
import com.goldstone.saboteur_backend.exception.BusinessException;
import com.goldstone.saboteur_backend.exception.code.error.BoardErrorCode;
import com.goldstone.saboteur_backend.exception.code.error.CardErrorCode;
import com.goldstone.saboteur_backend.service.board.BoardService;
import com.goldstone.saboteur_backend.session.GlobalSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathCardService {
    private final GlobalSession globalSession;
    private final BoardService boardService;

    public UseCardResponse use(UseCardRequest request) {
        User user = globalSession.getUserSession(request.getUserId());
        Board board = globalSession.getGameBoardSession(request.getRoomId());

        Card card =
                user.getCardDeck().getCards().stream()
                        .filter(c -> c.getId().equals(request.getCardId()))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(CardErrorCode.INVALID_CARD_ID));

        PathCard pathCard = (PathCard) card;

        int x = ((PathCardRequest) request).getTargetCellX();
        int y = ((PathCardRequest) request).getTargetCellY();
        Cell targetCell = board.getOrCreateCell(x, y);

        if (!targetCell.isEmptyCard()) {
            throw new BusinessException(BoardErrorCode.INVALID_PATH_PLACEMENT);
        }

        if (!(PathValidator.canPlacePathCard(board, targetCell, pathCard))) {
            throw new BusinessException(CardErrorCode.INVALID_PATH_CARD);
        }

        targetCell.setCard(pathCard);
        user.getCardDeck().useCard(pathCard);

        List<Cell> reachableGoals = boardService.getReachableGoals(board);
        if (!reachableGoals.isEmpty()) {
            globalSession.setGoldFinder(request.getRoomId(), user);
        }

        return new UseCardResponse(
                null, null, null, String.format("(%d, %d) 위치에 길카드가 놓였습니다.", x, y));
    }
}
