package com.campusconnect.ui;
import com.campusconnect.dao.ClaimDAO;import com.campusconnect.model.*;import com.campusconnect.service.Session;import com.campusconnect.service.ItemService;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;import java.util.List;
public class BrowseItemsFrame extends JFrame {
    private final ItemService service=new ItemService();private final ClaimDAO claimDAO=new ClaimDAO();private final DefaultTableModel model=UiStyle.model(new String[]{"ID","Title","Category","Description","Location","Type","Status","Reported By"});private final JTable table=new JTable(model);
    private final JTextField search=new JTextField(16),location=new JTextField(10);private final JComboBox<String> category=new JComboBox<>(new String[]{"All","Electronics","Accessories","Documents","Books","Clothing","Keys","Other"});private final JComboBox<String> type=new JComboBox<>(new String[]{"ALL","LOST","FOUND"});
    public BrowseItemsFrame(){setTitle("Browse Items");setSize(1120,620);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);setLayout(new BorderLayout(10,10));
        JLabel heading=new JLabel("Browse Lost & Found Items");heading.setFont(new Font("Arial",Font.BOLD,24));heading.setBorder(BorderFactory.createEmptyBorder(18,20,5,20));add(heading,BorderLayout.NORTH);
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));top.add(new JLabel("Title:"));top.add(search);top.add(new JLabel("Category:"));top.add(category);top.add(new JLabel("Location:"));top.add(location);top.add(new JLabel("Type:"));top.add(type);JButton find=UiStyle.primary("Search"),refresh=new JButton("Reset");top.add(find);top.add(refresh);
        UiStyle.styleTable(table);table.getColumnModel().getColumn(3).setPreferredWidth(190);JPanel center=new JPanel(new BorderLayout(8,8));center.setBorder(BorderFactory.createEmptyBorder(0,18,15,18));center.add(top,BorderLayout.NORTH);center.add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.RIGHT));JButton claim=UiStyle.primary("Claim Selected Found Item"),close=new JButton("Close");bottom.add(claim);bottom.add(close);center.add(bottom,BorderLayout.SOUTH);add(center,BorderLayout.CENTER);
        find.addActionListener(e->loadItems());refresh.addActionListener(e->{search.setText("");location.setText("");category.setSelectedIndex(0);type.setSelectedIndex(0);loadItems();});claim.addActionListener(e->claimSelected());close.addActionListener(e->dispose());loadItems();setVisible(true);
    }
    private void loadItems(){try{List<Item> items=service.searchItems(search.getText(),(String)category.getSelectedItem(),location.getText(),(String)type.getSelectedItem());model.setRowCount(0);for(Item i:items)model.addRow(new Object[]{i.getItemId(),i.getTitle(),i.getCategory(),i.getDescription(),i.getLocation(),i.getItemType(),i.getStatus(),i.getReportedBy()});}
        catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Search error",JOptionPane.ERROR_MESSAGE);}}
    private void claimSelected(){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select an item first.");return;}int id=(int)model.getValueAt(row,0);String itemType=(String)model.getValueAt(row,5),status=(String)model.getValueAt(row,6);
        if(!"FOUND".equals(itemType)||!"OPEN".equals(status)){JOptionPane.showMessageDialog(this,"Only OPEN found items can be claimed.");return;}
        if(Session.getCurrentUser()==null)return;String desc=JOptionPane.showInputDialog(this,"Describe proof/details to identify this item:","Submit Claim",JOptionPane.QUESTION_MESSAGE);if(desc==null)return;if(desc.isBlank()){JOptionPane.showMessageDialog(this,"Claim description is required.");return;}
        try{claimDAO.addClaim(new Claim(0,id,Session.getCurrentUser().getUserId(),desc.trim(),"PENDING",null));JOptionPane.showMessageDialog(this,"Claim submitted. You can track it in My Claims.");}
        catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Claim failed",JOptionPane.ERROR_MESSAGE);}}
}
