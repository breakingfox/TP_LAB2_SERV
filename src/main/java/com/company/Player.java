package com.company;

import com.company.messages.Gameboard;
import com.company.messages.Move;
import com.company.messages.Position;
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

    public Player() {
        name = "test_name";
        cards = new HashMap<>();
        cards.put("1", 3);
        cards.put("2", 2);
        cards.put("3", 1);
    }

    public Player(String name) {
        this.name = name;
        cards = new HashMap<>();
        cards.put("1", 3);
        cards.put("2", 2);
        cards.put("3", 1);
    }

    public void waitAccept(ServerSocket serverSocket) throws IOException {
        socket = serverSocket.accept();
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    //отправка состояния игры от пользователя
    public void send(Game game) {
        Gson gson = new Gson();
        Gameboard gameboard = new Gameboard(game);
        Messaging.writeBytes(outputStream, gson.toJson(gameboard));
    }

    //отправка состояния игры от пользователя
    public void sendPosition(String position) {
        Gson gson = new Gson();
        Position name = new Position(position);
        Messaging.writeBytes(outputStream, gson.toJson(name));
    }

    //получение хода с клиента
    public Move readIfActive(String activePlayer) {
        if (name.equalsIgnoreCase(activePlayer)) {
            String message = Messaging.readBytes(inputStream);
            Gson gson = new Gson();

            /** логируем ходы игроков*/
            logInfo(gson.fromJson(message, Move.class));

            return gson.fromJson(message, Move.class);
        }
        return null;
    }

    public void logInfo(Move move) {
        System.out.print("Игрок: " + name);
        if (move.isBelieve())
            System.out.print(" поверил другому игроку \n");
        else if (move.getToldCard() != null) {
            System.out.println("Назвал карту: " + move.getToldCard() + "Положил карту: " + move.getCard());
        } else
            System.out.println("Положил карту: " + move.getCard());
    }

    public void decreaseCards(String type) {
        int count = cards.getOrDefault(type, 0);
        cards.put(type, count - 1);
    }

    public void increaseCards(Map<String, Integer> boardCards) {
        boardCards.forEach((type, number) -> {
            int count = cards.getOrDefault(type, 0);
            cards.put(type, count + number);
        });

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
