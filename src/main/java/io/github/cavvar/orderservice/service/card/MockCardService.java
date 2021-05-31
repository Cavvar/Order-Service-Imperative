package io.github.cavvar.orderservice.service.card;

import io.github.cavvar.orderservice.model.Card;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Alternative
@Priority(1)
@Singleton
public class MockCardService extends LiveCardService {
    @Override
    public Card getCard(URI card) {
        final List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, "1234-5678-9012-3456", "01/99", "123"));
        cards.add(new Card(2, "5678-1234-3456-9012", "05/34", "456"));
        cards.add(new Card(3, "9012-3456-5678-1234", "12/55", "789"));
        cards.add(new Card(4, "3456-9012-1234-5678", "07/17", "187"));
        final int randomIndex = (int) (Math.random() * 3);
        return cards.get(randomIndex);
    }
}