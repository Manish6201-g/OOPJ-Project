package com.campusconnect.ui;
import com.campusconnect.dao.ClaimDAO;import com.campusconnect.service.Session;import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;import java.util.List;
public class AdminPanelFrame extends JFrame {
    private final ClaimDAO dao=new ClaimDAO();private final DefaultTableModel model=UiStyle.model(new String[]{"Claim ID","Item","Student","Email","Claim Details","Status","Date"});private final JTable table=new JTable(model);
    public AdminPanelFrame(){if(Session.getCurrentUser()==null||!"ADMIN".equals(Session.getCurrentUser().getRole())){JOptionPane.showMessageDialog(null,"Admin access only.");dispose();return;}
        setTitle("Admin Panel | Claim Management");setSize(1150,560);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);setLayout(new BorderLayout(10,10));JLabel title=new JLabel("Claim Management");title.setFont(new Font("Arial",Font.BOLD,25));title.setBorder(BorderFactory.createEmptyBorder(18,20,10,20));add(title,BorderLayout.NORTH);UiStyle.styleTable(table);add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT));JButton approve=UiStyle.primary("Approve"),reject=new JButton("Reject"),refresh=new JButton("Refresh"),close=new JButton("Close");actions.add(approve);actions.add(reject);actions.add(refresh);actions.add(close);add(actions,BorderLayout.SOUTH);
        approve.addActionListener(e->decide("APPROVED"));reject.addActionListener(e->decide("REJECTED"));refresh.addActionListener(e->load());close.addActionListener(e->dispose());load();setVisible(true);
    }
    private void load(){try{model.setRowCount(0);List<Object[]> rows=dao.getAllClaimsForAdmin();for(Object[] r:rows)model.addRow(r);}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}}
    private void decide(String status){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a claim first.");return;}int id=(int)model.getValueAt(row,0);if(!"PENDING".equals(model.getValueAt(row,5))){JOptionPane.showMessageDialog(this,"This claim has already been reviewed.");return;}
        int confirm=JOptionPane.showConfirmDialog(this,"Are you sure you want to "+status.toLowerCase()+" this claim?","Confirm",JOptionPane.YES_NO_OPTION);if(confirm!=JOptionPane.YES_OPTION)return;
        try{dao.updateClaimStatus(id,status);JOptionPane.showMessageDialog(this,"Claim "+status.toLowerCase()+".");load();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Update failed",JOptionPane.ERROR_MESSAGE);}}
}
