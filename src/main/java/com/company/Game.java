package com.company;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private String curPlayer;
    private Player playerFirst;
    private Player playerSecond;

    public Map<String, Integer> getBoardCards() {
        return boardCards;
    }

    public void setBoardCards(Map<String, Integer> boardCards) {
        this.boardCards = boardCards;
    }

    private Map<String, Integer> boardCards;

    public Game() {
        boardCards = new HashMap<>();
        boardCards.put("yeah",228);
    }

    public Game(Player playerFirst, Player playerSecond) {
        this.playerFirst = playerFirst;
        this.playerSecond = playerSecond;
    }


}
