package io.github.cavvar.orderservice.service.item;

import io.github.cavvar.orderservice.model.Item;

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
        itemList.add(new Item(1, "Blue Sock", 4, 1.5));
        itemList.add(new Item(2, "Red Sock", 2, 2.5));
        itemList.add(new Item(3, "Green Sock", 2, 9.5));
        itemList.add(new Item(4, "Yellow Sock", 2, 5.5));
        return itemList;
    }
}