package com.company;

import com.company.messages.Gameboard;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    private static final String SAVEFILE = "save.json";
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
        this.curPlayer = playerFirst;
        boardCards = new HashMap<>();
        boardCards.put("1", 0);
        boardCards.put("2", 0);
        boardCards.put("3", 0);
    }

    /**
     * подсветка хода игрока
     */
    public void process(Move move) {
        if (move.isBelieve()) {
            lastCard = move.getCard();
            if (move.getToldCard() != null)
                boardCard = move.getToldCard();
            //увеличиваем число карт на столе
            int count = boardCards.getOrDefault(move.getCard(), 0);
            boardCards.put(move.getCard(), count + 1);
            if (curPlayer.equals(playerFirst))
                playerFirst.decreaseCards(move.getCard());
            else
                playerSecond.decreaseCards(move.getCard());

        } else {
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

    public void save() {
        Gameboard gameboard = new Gameboard(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            try (Writer writer = Files.newBufferedWriter(Paths.get(SAVEFILE))) {
                gson.toJson(gameboard, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * если с сейвом все нормально и он не загружен после конца игры то его данные присваиваем пользователям
     */
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
            Gameboard gameboard = gson.fromJson(reader, Gameboard.class);
            reader.close();
            if (!validation(gameboard)) {
                System.out.println("VALIDATION ERROR");
                return false;
            }
            System.out.println("SUCCESSFUL VALIDATION");
            return !gameboard.isWinner();

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            e.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .forEach(System.out::println);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
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
            Gameboard gameboard = gson.fromJson(reader, Gameboard.class);
            reader.close();
            if (gameboard.getCurPlayer().equalsIgnoreCase("1"))
                this.curPlayer = playerFirst;
            else
                this.curPlayer = playerSecond;
            this.playerFirst.setCards(gameboard.getPlayerFirstCards());
            this.playerSecond.setCards(gameboard.getPlayerSecondCards());
            this.boardCards = gameboard.getCards();
            this.lastCard = gameboard.getLastCard();
            this.boardCard = gameboard.getBoardCard();
            System.out.println("ИГРА БЫЛА ЗАГРУЖЕНА ИЗ СОХРАНЕНИЯ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validation(Gameboard gameboard) {
        List<String> types = List.of("1", "2");
        List<String> typeCard = List.of("1", "2", "3");
        AtomicBoolean validation = new AtomicBoolean(true);
        if (!types.contains(gameboard.getCurPlayer()) || !typeCard.contains(gameboard.getBoardCard()) || !typeCard.contains(gameboard.getLastCard()))
            return false;
        gameboard.getCards().forEach((cardType, number) -> {
            if (!typeCard.contains(cardType)) {
                validation.set(false);
            }
        });
        gameboard.getPlayerFirstCards().forEach((cardType, number) -> {
            if (!typeCard.contains(cardType)) {
                validation.set(false);
            }
        });
        gameboard.getPlayerSecondCards().forEach((cardType, number) -> {
            if (!typeCard.contains(cardType)) {
                validation.set(false);
            }
        });
        return validation.get();
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
