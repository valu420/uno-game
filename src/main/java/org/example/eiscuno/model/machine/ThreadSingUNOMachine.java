package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * ThreadSingUNOMachine is a class that implements Runnable to handle the machine player's action of calling "UNO".
 */
public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private ThreadPlayMachine threadPlayMachine;
    private volatile boolean unoCalled;

    /**
     * Constructs a new ThreadSingUNOMachine instance.
     * @param cardsPlayer The list of cards held by the player.
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
        this.unoCalled = false;
    }

    /**
     * Sets the threadPlayMachine attribute.
     * @param threadPlayMachine The ThreadPlayMachine instance.
     */
    public void setThreadPlayMachine(ThreadPlayMachine threadPlayMachine) {
        this.threadPlayMachine = threadPlayMachine;
    }

    /**
     * The run method continuously checks if the machine player has one card left and calls "UNO".
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000); // Duerme por un intervalo aleatorio entre 0 y 5 segundos.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkUNO();
        }
    }

    /**
     * Checks if the machine player has only one card left and prints "UNO" if true.
     */
    public void checkUNO() {
        if (cardsPlayer.size() == 1 && !unoCalled) {
            System.out.println("UNO");
            unoCalled = true;
            if (threadPlayMachine != null) {
                threadPlayMachine.onUnoCalled();
            }
        }
    }
    /**
     * Checks if the "UNO" has been called by the human player.
     * @return true if "UNO" has been called, false otherwise
     */
    public boolean isUnoCalled() {
        return unoCalled;
    }
}
