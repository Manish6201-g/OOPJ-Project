package com.campusconnect.interfaces;
import com.campusconnect.model.Item;
import java.util.List;
public interface Searchable {
    List<Item> searchByTitle(String title);
    List<Item> searchByCategory(String category);
    List<Item> searchByLocation(String location);
}
