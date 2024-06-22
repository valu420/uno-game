package org.example.eiscuno.model.player;

import org.example.eiscuno.model.Observable;
import org.example.eiscuno.model.Observer;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Uno game.
 */

public class Player extends Observable implements IPlayer {
        private ArrayList<Card> cardsPlayer;
        private String typePlayer;
    private List<Observer> observers = new ArrayList<>();

        public Player(String typePlayer) {
            this.cardsPlayer = new ArrayList<Card>();
            this.typePlayer = typePlayer;
        }

        @Override
        public void addCard(Card card) {
            cardsPlayer.add(card);
            notifyObservers();
        }

        @Override
        public ArrayList<Card> getCardsPlayer() {
            return cardsPlayer;
        }

        @Override
        public void removeCard(int index) {
            cardsPlayer.remove(index);
            notifyObservers();
        }

        @Override
        public Card getCard(int index) {
            return cardsPlayer.get(index);
        }

        public String getTypePlayer() {
            return typePlayer;
        }

        @Override
        public void drawCards(Deck deck, int numberOfCards) {
            for (int i = 0; i < numberOfCards; i++) {
                addCard(deck.takeCard());
            }
        }
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

        public boolean findPlayableCard(String currentColor, String currentValue) {
            for (Card card : cardsPlayer) {
                if (card.getColor().equals(currentColor) || card.getValue().equals(currentValue)
                        || card.getValue().equals("W") || card.getValue().equals("+4")) {
                    return true;
                }
            }
            return false;
        }

        public void printCardsPlayer() {
            for (Card card : this.cardsPlayer) {
                System.out.println(card);
            }
        }
    }
