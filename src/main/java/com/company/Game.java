package com.company;

import com.company.messages.GameMsg;
import com.company.messages.Move;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.Validator;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    private static final String SAVEFILE = "save.json";
    private static final Integer EIGHT = 8;
    private Player curPlayer;
    private Player playerFirst;
    private Player playerSecond;

    public Game(Player playerFirst, Player playerSecond) {
        this.playerFirst = playerFirst;
        this.playerSecond = playerSecond;
        this.curPlayer = playerFirst;
    }

    /**
     * обработка хода игрока
     */
    public void process(Move move) {
        if (curPlayer.equals(playerFirst)) {
            playerFirst.setStrength(playerFirst.getStrength() + move.getStrength());
        } else {
            playerSecond.setStrength(playerSecond.getStrength() + move.getStrength());
        }
        changeCurPlayer();
    }

    public void save() {
        GameMsg gameboard = new GameMsg(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            try (Writer writer = Files.newBufferedWriter(Paths.get(SAVEFILE))) {
                gson.toJson(gameboard, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logInfo() {
        System.out.println("Силы игрока 1 ");
        System.out.println(playerFirst.getStrength());

        System.out.println("Силы игрока 2 ");
        System.out.println(playerSecond.getStrength());
    }

    public void changeCurPlayer() {
        if (curPlayer.equals(playerFirst))
            curPlayer = playerSecond;
        else
            curPlayer = playerFirst;
    }

    public String getWinner() {
        int playerFirstPoints = 0;
        int playerSecondPoints = 0;
        if (playerFirst.getStrength().length() == 8 && playerSecond.getStrength().length() == 8) {
            for (int i = 0; i < 8; i++) {
                int playerFirstStrength = playerFirst.getStrength().charAt(i);
                int playerSecondStrength = playerSecond.getStrength().charAt(i);
                if (playerFirstStrength > playerSecondStrength)
                    playerFirstPoints += 1;
                else if (playerFirstStrength < playerSecondStrength)
                    playerSecondPoints += 1;
            }
            drawBattle(playerFirst.getStrength(), playerSecond.getStrength());
            if (playerFirstPoints > playerSecondPoints)
                return "1";
            else if (playerFirstPoints < playerSecondPoints)
                return "2";
            else return "3";
        } else
            return "0";
    }

    public static void drawBattle(String leftPlayer, String rightPlayer) {
        for (int i = 0; i < 8; i++) {
            System.out.println("     ( •_•)     (•_• )     \n" +
                    leftPlayer.charAt(i) + "    ( ง )ง     ୧( ୧ )     " + rightPlayer.charAt(i) + "\n" +
                    "     /︶\\     /︶\\     ");
        }
    }

    //
//    /**
//     * если с сейвом все нормально и он не загружен после конца игры то его данные присваиваем пользователям
//     */
    public boolean isSaveGood() throws ValidationException, FileNotFoundException {
        try {
            JSONObject jsonSchema = new JSONObject(
                    new JSONTokener(new FileReader("schema.json")));
            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(new FileReader("save.json")));
            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
            Validator validator = Validator.builder()
                    .build();
            validator.performValidation(schema, jsonSubject);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("save.json"));
            GameMsg gameMsg = gson.fromJson(reader, GameMsg.class);
            reader.close();
            if (!validation(gameMsg)) {
                System.out.println("Ошибка валидации");
                return false;
            }
            System.out.println("Валидация прошла успешно");
            return !isWinner(gameMsg);

        } catch (ValidationException e) {
            System.out.println("Ошибка валидации");
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Ошбика при обработке файла сохранения");
            return false;
        }
    }

    /**
     * загрузка сейва из файла
     */
    public void loadSave() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("save.json"));
            GameMsg gameMsg = gson.fromJson(reader, GameMsg.class);
            reader.close();
            if (gameMsg.getCurPlayer().equalsIgnoreCase("1"))
                this.curPlayer = playerFirst;
            else
                this.curPlayer = playerSecond;
            this.playerFirst.setStrength(gameMsg.getPlayerFirst());
            this.playerSecond.setStrength(gameMsg.getPlayerSecond());
            System.out.println("ИГРА БЫЛА ЗАГРУЖЕНА ИЗ СОХРАНЕНИЯ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //проверяем правильного ли формата пришли строки
    public boolean validation(GameMsg gameMsg) {
        Set<Character> strengths = Set.of('1', '2', '3', '4', '5', '6', '7', '8');

        AtomicBoolean validation = new AtomicBoolean(true);

        if (!List.of("1", "2").contains(gameMsg.getCurPlayer()))
            validation.set(false);
        for (Character c : gameMsg.getPlayerFirst().toCharArray()) {
            if (!strengths.contains(c))
                validation.set(false);
        }
        for (Character c : gameMsg.getPlayerSecond().toCharArray()) {
            if (!strengths.contains(c))
                validation.set(false);
        }
        return validation.get();
    }

    public boolean isWinner(GameMsg gameMsg) {
        return (gameMsg.getPlayerFirst().length() == 8 && gameMsg.getPlayerSecond().length() == 8);
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


}
