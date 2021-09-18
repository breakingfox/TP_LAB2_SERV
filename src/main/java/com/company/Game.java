package com.company;

import com.company.messages.Gameboard;
import com.company.messages.Move;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private static final Integer ZERO = 0;
    private Player curPlayer;
    private String lastCard;
    private String boardCard;
    private Player playerFirst;
    private Player playerSecond;
    private Map<String, Integer> boardCards;

    public Game(Player playerFirst, Player playerSecond) {
        this.playerFirst = playerFirst;
        this.playerSecond = playerSecond;
        boardCards = new HashMap<>();
        boardCards.put("1", 0);
        boardCards.put("2", 0);
        boardCards.put("3", 0);
    }

    /**
     * подсветка хода игрока
     */
    //TODO добавить проверку по нику
    public void process(Move move) {
        if (move.isBelieve()) {
            lastCard = move.getCard();
            if (move.getToldCard() != null)
                boardCard = move.getToldCard();
            //увеличиваем число карт на столе
            int count = boardCards.getOrDefault(move.getCard(), 0);
            boardCards.put(move.getCard(), count + 1);
            //TODO проверка какой пользователь сходил
            if (curPlayer.equals(playerFirst))
                playerFirst.decreaseCards(move.getCard());
            else
                playerSecond.decreaseCards(move.getCard());

        } else {
            //TODO проверка какой пользователь сходил
            if (move.getCard().equalsIgnoreCase(boardCard)) {
                if (curPlayer.equals(playerFirst))
                    playerFirst.increaseCards(boardCards);
                else
                    playerSecond.increaseCards(boardCards);
            } else {
                if (curPlayer.equals(playerFirst))
                    playerSecond.increaseCards(boardCards);
                else
                    playerFirst.increaseCards(boardCards);
            }
            emptyBoard();

        }
        changeCurPlayer();
    }

    /**
     * обнуление карт на столе
     */
    public void emptyBoard() {
        boardCards.replaceAll((type, number) -> ZERO);
    }

    public void logInfo() {
        System.out.println("карты на столе");
        boardCards.forEach((key, value) -> {
            System.out.println("масть " + key + " количество  " + value);
        });
        System.out.println("карты игрока 1 ");
        playerFirst.getCards().forEach((key, value) -> {
            System.out.println("масть " + key + " количество  " + value);
        });
        System.out.println("карты игрока 2 ");
        playerSecond.getCards().forEach((key, value) -> {
            System.out.println("масть " + key + " количество  " + value);
        });
    }

    public void changeCurPlayer() {
        if (curPlayer.equals(playerFirst))
            curPlayer = playerSecond;
        else
            curPlayer = playerFirst;
    }

    public String getWinner() {
        boolean isPlayerFirstWin = true;
        boolean isPlayerSecondWin = true;
        for (Integer number : playerFirst.getCards().values()) {
            if (number > 0) {
                isPlayerFirstWin = false;
                break;
            }
        }
        for (Integer number : playerSecond.getCards().values()) {
            if (number > 0) {
                isPlayerSecondWin = false;
                break;
            }
        }
        if (isPlayerFirstWin)
            return "1";
        else if (isPlayerSecondWin)
            return "2";
        else return "0";
    }

    public Map<String, Integer> getBoardCards() {
        return boardCards;
    }

    public void setBoardCards(Map<String, Integer> boardCards) {
        this.boardCards = boardCards;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    public Player getPlayerFirst() {
        return playerFirst;
    }

    public void setPlayerFirst(Player playerFirst) {
        this.playerFirst = playerFirst;
    }

    public Player getPlayerSecond() {
        return playerSecond;
    }

    public void setPlayerSecond(Player playerSecond) {
        this.playerSecond = playerSecond;
    }

    public String getLastCard() {
        return lastCard;
    }

    public void setLastCard(String lastCard) {
        this.lastCard = lastCard;
    }

    public String getBoardCard() {
        return boardCard;
    }

    public void setBoardCard(String boardCard) {
        this.boardCard = boardCard;
    }

}
