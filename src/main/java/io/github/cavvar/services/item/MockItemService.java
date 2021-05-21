package io.github.cavvar.services.item;

import io.github.cavvar.models.Item;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Alternative
@Priority(1)
@Singleton
public class MockItemService extends LiveItemService {
    @Override
    public List<Item> getItems(URI items) {
        final List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(5, "Blue Sock", 5, 5.5));
        itemList.add(new Item(6, "Red Sock", 3, 2.5));
        itemList.add(new Item(7, "Green Sock", 2, 9.5));
        itemList.add(new Item(8, "Yellow Sock", 1, 0.5));
        return itemList;
    }
}
