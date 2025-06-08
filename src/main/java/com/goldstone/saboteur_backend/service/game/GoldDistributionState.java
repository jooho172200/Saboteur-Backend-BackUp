package com.goldstone.saboteur_backend.service.game;

import com.goldstone.saboteur_backend.domain.card.GoldCard;
import com.goldstone.saboteur_backend.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GoldDistributionState {
    public Queue<User> minerQueue;
    public List<GoldCard> availableGoldCards;
    public ArrayList<User> distributionMiners;
}
