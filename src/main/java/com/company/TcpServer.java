package com.company;

import com.company.messages.Gameboard;
import com.company.messages.Move;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServer {
    private static final int DEFAULT_PORT = 11122;


    public static void main(String[] args) throws IOException {

        int port = DEFAULT_PORT;


        /** Создаем серверный сокет на полученном порту */
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SERVER STARTED");
            Player playerFirst = new Player("1");
            Player playerSecond = new Player("2");
            playerFirst.waitAccept(serverSocket);
            playerFirst.sendPosition("1");
            playerSecond.waitAccept(serverSocket);
            playerSecond.sendPosition("2");

            /** пока что имя пользователя это его номер порта */

            Game game = new Game(playerFirst, playerSecond);
            game.setCurPlayer(playerFirst);
            //основной цикл программы с игрой
            System.out.println(game.getCurPlayer().getName());

            while (!playerFirst.getSocket().isClosed() && !playerSecond.getSocket().isClosed()) {
                game.logInfo();
                //отправили сообщение
                playerFirst.send(game);
                playerSecond.send(game);
                System.out.println("ОТПРАВИЛИ ИГРОКАМ");
                //получаем сообщение с клиента
                Move move = playerFirst.readIfActive(game.getCurPlayer().getName());
                if (move == null)
                    game.process(playerSecond.readIfActive(game.getCurPlayer().getName()));
                else
                    game.process(move);
                String winner = game.getWinner();
                if (winner.equalsIgnoreCase("1") || winner.equalsIgnoreCase("2")) {
                    System.out.println("Победил игрок : " + winner);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Порт занят: " + port);
            System.exit(-1);
        }
        serverSocket.accept();
    }


}
