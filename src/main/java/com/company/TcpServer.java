package com.company;

import com.company.messages.Move;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServer {
    private static final int DEFAULT_PORT = 11122;


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("Запуск сервера");
            Player playerFirst = new Player("1");
            Player playerSecond = new Player("2");
            playerFirst.waitAccept(serverSocket);
            playerFirst.sendPosition("1");
            playerSecond.waitAccept(serverSocket);
            playerSecond.sendPosition("2");
            Game game = new Game(playerFirst, playerSecond);
            if (game.isSaveGood()) {
                game.loadSave();
            }
            //основной цикл программы с игрой
            while (!playerFirst.getSocket().isClosed() && !playerSecond.getSocket().isClosed()) {
                game.logInfo();
                //отправили сообщение

                playerFirst.send(game);
                playerSecond.send(game);
                System.out.println("Отправили игрокам текущее состояние игры");
                //получаем сообщение с клиента
                Move move = playerFirst.readIfActive(game.getCurPlayer().getName());
                if (move == null)
                    game.process(playerSecond.readIfActive(game.getCurPlayer().getName()));
                else
                    game.process(move);
                String winner = game.getWinner();
                game.save();
                if (winner.equalsIgnoreCase("1") || winner.equalsIgnoreCase("2")) {
                    System.out.println("Победил игрок : " + winner);
                    playerFirst.send(game);
                    playerSecond.send(game);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Порт занят: " + DEFAULT_PORT);
            System.exit(-1);
        } catch (NullPointerException e) {
            System.out.println("\nПользователи отключились от игры");
        }
//        serverSocket.accept();
    }


}
