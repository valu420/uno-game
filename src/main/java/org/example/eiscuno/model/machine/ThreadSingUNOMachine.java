package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * ThreadSingUNOMachine is a class that implements Runnable to handle the machine player's action of calling "UNO".
 */
public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;

    /**
     * Constructs a new ThreadSingUNOMachine instance.
     *
     * @param cardsPlayer The list of cards held by the player.
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
    }

    /**
     * The run method continuously checks if the machine player has one card left and calls "UNO".
     */
    @Override
    public void run(){
        while (true){
            try {
                // Sleep for a random interval between 0 and 5 seconds.
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    /**
     * Checks if the machine player has only one card left and prints "UNO" if true.
     */
    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){
            System.out.println("UNO");
        }
    }
}