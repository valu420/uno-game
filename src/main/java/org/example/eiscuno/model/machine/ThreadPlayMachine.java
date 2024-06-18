package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
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

    private Card chooseRandomCard(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        return machinePlayer.getCard(index);
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
