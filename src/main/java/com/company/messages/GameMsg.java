package com.company.messages;

import com.company.Game;

public class GameMsg {


    private String curPlayer;
    private String playerFirst;
    private String playerSecond;

    public GameMsg(Game game) {
        this.curPlayer = game.getCurPlayer().getName();
        this.playerFirst = game.getPlayerFirst().getStrength();
        this.playerSecond = game.getPlayerSecond().getStrength();
    }

    public String getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(String curPlayer) {
        this.curPlayer = curPlayer;
    }

    public String getPlayerFirst() {
        return playerFirst;
    }

    public void setPlayerFirst(String playerFirst) {
        this.playerFirst = playerFirst;
    }

    public String getPlayerSecond() {
        return playerSecond;
    }

    public void setPlayerSecond(String playerSecond) {
        this.playerSecond = playerSecond;
    }
}
