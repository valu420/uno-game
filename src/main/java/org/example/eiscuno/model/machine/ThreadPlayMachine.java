package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private Player humanPlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUno gameUno;
    private ThreadPlayMachine threadPlayMachine;
    private Deck deck;


    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = deck;
        this.gameUno = gameUno;
        this.threadPlayMachine = threadPlayMachine;
        this.humanPlayer = humanPlayer;

    }

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable(){
        Card topCard = table.getCurrentCardOnTheTable();
        String currentColor = topCard.getColor();
        String currentValue = topCard.getValue();
        Card playableCard = machinePlayer.findPlayableCard(currentColor, currentValue);

        String cardColor = "NONE";
        String cardValue = "FOUR_WILD_DRAW";
        Card fourCard = machinePlayer.findPlayableCard(cardColor, cardValue);
        if (fourCard != null) {
            table.addCardOnTheTable(fourCard);
            tableImageView.setImage(fourCard.getImage());
            machinePlayer.getCardsPlayer().remove(fourCard);
        }
        else {
            if (playableCard != null) {
                table.addCardOnTheTable(playableCard);
                tableImageView.setImage(playableCard.getImage());
                machinePlayer.getCardsPlayer().remove(playableCard);
                isWildCards(playableCard, humanPlayer);
            } else {
                gameUno.eatCard(machinePlayer, 1);

            }
            unoMachine();
            System.out.println("\n Cartas de la máquina: ");
            machinePlayer.printCardsPlayer();
        }
    }

    public void unoMachine() {
        if (machinePlayer.getCardsPlayer().size() == 1) {
        }
    }
    public void isWildCards(Card card, Player player){
        if (card.getValue() == "SKIP"){
            run();
            System.out.println("\nLa maquina utilizo una carta de Skip.\n");
        } else if (card.getValue() =="RESERVE") {
            run();
            setHasPlayerPlayed(false);
            System.out.println("\nLa maquina utilizo una carta de Reverse.\n");
        } else if (card.getValue() =="TWO_WILD_DRAW") {
            gameUno.eatCard(player, 2);
            System.out.println("\nLa maquina utilizo  un TWO_WILD_DRAW, " +player.getTypePlayer()+ " comio 2 cartas\n");
        } else if (card.getValue() =="WILD") {

        }else if (card.getValue() == "FOUR_WILD_DRAW" || card.getValue() =="WILD") {
        }
        else {
            setHasPlayerPlayed(true);
        }
    }
    public void fourWild(){
        String currentColor = "NONE";
        String currentValue = "FOUR_WILD_DRAW";
        Card playableCard = machinePlayer.findPlayableCard(currentColor, currentValue);


    }
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}