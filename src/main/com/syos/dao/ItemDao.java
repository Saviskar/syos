package main.com.syos.dao;

import main.com.syos.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Optional<Item> findByCode(String itemCode);
    List<Item> findAll();
    void save(Item item);
    void update(Item item);
    void delete(String itemCode);
}
