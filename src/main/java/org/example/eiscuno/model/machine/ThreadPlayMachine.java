package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Player humanPlayer;
    private Table table;
    private Deck deck;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Deck deck, Player humanPlayer, Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.humanPlayer = humanPlayer;
        this.deck = deck;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
    }

    public void run() {
        boolean hasMachinePlayedCard = false;
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(!hasMachinePlayedCard){
                    Card card = chooseRandomCard();
                    if (this.table.isValidCard(card)){
                        if (card.getValue().equals("W")){
                            card.setColor(chooseRandomColor());
                            System.out.println("El color de la partida ha sido cambiado a: "+card.getColor());
                        }
                        if(card.getValue().equals("+4")){
                            humanPlayer.drawCards(deck, 4);
                            card.setColor(chooseRandomColor());
                            System.out.println("El color de la partida ha sido cambiado a: "+card.getColor());
                        }
                        if(card.getValue().equals("+2")){
                            humanPlayer.drawCards(deck, 2);
                        }
                        putCardOnTheTable(card);
                        hasPlayerPlayed = false;
                        hasMachinePlayedCard = true;
                    }
                }
            }
        }
    }

    private void putCardOnTheTable(Card card){
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
    }

    private String chooseRandomColor(){
        String color = "";
        int numColor = (int) ((Math.random() * 4)+1);
        color = switch (numColor) {
            case 1 -> "RED";
            case 2 -> "BLUE";
            case 3 -> "GREEN";
            case 4 -> "YELLOW";
            default -> color;
        };
        return color;
    }

    private Card chooseRandomCard(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        return machinePlayer.getCard(index);
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
