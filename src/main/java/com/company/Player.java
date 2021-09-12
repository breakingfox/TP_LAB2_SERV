package com.company;

import com.company.messages.Gameboard;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Map<String, Integer> cards;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Socket socket;

    public void waitAccept(ServerSocket serverSocket) throws IOException {
        socket = serverSocket.accept();
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        cards = new HashMap<>();
        cards.put("1", 3);
        cards.put("2", 2);
        cards.put("3", 1);
    }

    //отправка состояния игры от пользователя
    public void send(Game game) {
        Gson gson = new Gson();
        Gameboard gameboard = new Gameboard();
        gameboard.setCards(game.getBoardCards());
        Messaging.writeBytes(outputStream, gson.toJson(gameboard));
    }

    //тестовая отправка мапы в джейсоне
    public void send() {
        Gson gson = new Gson();
        Gameboard gameboard = new Gameboard();
        gameboard.setCards(cards);
        System.out.println(gson.toJson(gameboard));
        Messaging.writeBytes(outputStream, gson.toJson(gameboard));
    }


    public Player() {
        name = "keks";
    }

    public Player(String name) {
        this.name = name;
    }




    //положить карту на стол
    public void put(String type) {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getCards() {
        return cards;
    }

    public void setCards(Map<String, Integer> cards) {
        this.cards = cards;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
