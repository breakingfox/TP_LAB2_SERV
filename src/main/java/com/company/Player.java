package com.company;

import com.company.messages.GameMsg;
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


    private String strength;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Socket socket;

    public Player() {
        strength = "";
    }

    public Player(String name) {
        this.name = name;
        strength = "";
    }

    public void waitAccept(ServerSocket serverSocket) throws IOException {
        socket = serverSocket.accept();
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    //отправка состояния игры от пользователя
    public void send(Game game) {
        Gson gson = new Gson();
        GameMsg message = new GameMsg(game);
        Messaging.writeBytes(outputStream, gson.toJson(message));
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
        System.out.println("Добавил силу " + move.getStrength() + "в свою армию");
    }




    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
