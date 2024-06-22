package org.example.eiscuno.model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class PlayerView implements Observer{
    private GridPane gridPaneCardsPlayer;
    private Player player;

    public PlayerView(GridPane gridPane, Player player) {
        this.gridPaneCardsPlayer = gridPane;
        this.player = player;
    }

    @Override
    public void update() {
        gridPaneCardsPlayer.getChildren().clear();
        ArrayList<Card> cards = player.getCardsPlayer();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            ImageView cardImageView = card.getCard();
            gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

}
