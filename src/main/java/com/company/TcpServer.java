package com.company;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    private static final int DEFAULT_PORT = 11122;


    public static void main(String[] args) throws IOException {

        int port = DEFAULT_PORT;


        /* Создаем серверный сокет на полученном порту */
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            Player player = new Player();
            player.waitAccept(serverSocket);
            Game game = new Game();


            while (true) {
                //отправили сообщение
                player.send(game);
                //получаем сообщение с клиента
                Poop poop = player.read();
                System.out.println(poop.poop);
            }

        } catch (IOException e) {
            System.out.println("Порт занят: " + port);
            System.exit(-1);
        }
        serverSocket.accept();
    }
}
