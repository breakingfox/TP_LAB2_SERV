package com.company.messages;

import com.company.Game;

import java.util.Map;

public class Gameboard {
    private String curPlayer;
    private String lastCard;
    private String boardCard;
    private Map<String, Integer> cards;
    private Map<String, Integer> playerFirstCards;
    private Map<String, Integer> playerSecondCards;

    public Gameboard() {
    }

    public Gameboard(Game game) {
        this.setPlayerFirstCards(game.getPlayerFirst().getCards());
        this.setPlayerSecondCards(game.getPlayerSecond().getCards());
        this.setCurPlayer(game.getCurPlayer().getName());
        if (game.getLastCard() != null)
            this.setLastCard(game.getLastCard());
        if (game.getBoardCard() != null)
            this.setBoardCard(game.getBoardCard());
        this.setCards(game.getBoardCards());
    }

    public boolean isWinner() {
        boolean isPlayerFirstWin = true;
        boolean isPlayerSecondWin = true;
        for (Integer number : playerFirstCards.values()) {
            if (number > 0) {
                isPlayerFirstWin = false;
                break;
            }
        }
        for (Integer number : playerSecondCards.values()) {
            if (number > 0) {
                isPlayerSecondWin = false;
                break;
            }
        }
        return (isPlayerFirstWin || isPlayerSecondWin);
    }

    public String getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(String curPlayer) {
        this.curPlayer = curPlayer;
    }

    public String getBoardCard() {
        return boardCard;
    }

    public void setBoardCard(String boardCard) {
        this.boardCard = boardCard;
    }

    public Map<String, Integer> getPlayerFirstCards() {
        return playerFirstCards;
    }

    public void setPlayerFirstCards(Map<String, Integer> playerFirstCards) {
        this.playerFirstCards = playerFirstCards;
    }

    public Map<String, Integer> getPlayerSecondCards() {
        return playerSecondCards;
    }

    public void setPlayerSecondCards(Map<String, Integer> playerSecondCards) {
        this.playerSecondCards = playerSecondCards;
    }


    public String getLastCard() {
        return lastCard;
    }

    public void setLastCard(String lastCard) {
        this.lastCard = lastCard;
    }

    public Map<String, Integer> getCards() {
        return cards;
    }

    public void setCards(Map<String, Integer> cards) {
        this.cards = cards;
    }


}
