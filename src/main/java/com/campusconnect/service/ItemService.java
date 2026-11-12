package com.campusconnect.service;
import com.campusconnect.dao.ItemDAO;import com.campusconnect.model.Item;import com.campusconnect.exceptions.InvalidItemException;import java.util.List;
public class ItemService {
    private final ItemDAO dao=new ItemDAO();
    public boolean reportItem(Item item)throws InvalidItemException{
        if(item==null)throw new InvalidItemException("Item cannot be empty.");
        if(item.getTitle()==null||item.getTitle().isBlank())throw new InvalidItemException("Item title is required.");
        if(item.getLocation()==null||item.getLocation().isBlank())throw new InvalidItemException("Location is required.");
        if(item.getReportedBy()<=0)throw new InvalidItemException("Please login before reporting an item.");
        return dao.addItem(item);
    }
    public List<Item> searchByTitle(String title){return dao.searchByTitle(title);}
    public List<Item> searchByCategory(String category){return dao.searchByCategory(category);}
    public List<Item> searchByLocation(String location){return dao.searchByLocation(location);}
    public List<Item> searchItems(String title,String category,String location,String type){return dao.searchItems(title,category,location,type);}
    public int getTotalItems(){return dao.getTotalItems();}public int getLostItemsCount(){return dao.getLostItemsCount();}public int getFoundItemsCount(){return dao.getFoundItemsCount();}
}
