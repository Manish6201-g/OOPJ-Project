package com.campusconnect.ui;
import java.awt.*;import javax.swing.*;import javax.swing.table.DefaultTableModel;
final class UiStyle {
    static final Color NAVY=new Color(25,35,55), BLUE=new Color(35,75,130), BG=new Color(245,247,250), MUTED=new Color(110,120,135);
    private UiStyle(){}
    static JButton primary(String text){JButton b=new JButton(text);b.setBackground(BLUE);b.setForeground(Color.WHITE);b.setFocusPainted(false);return b;}
    static void styleTable(JTable table){table.setRowHeight(28);table.setFont(new Font("Arial",Font.PLAIN,13));table.getTableHeader().setFont(new Font("Arial",Font.BOLD,13));table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);}
    static DefaultTableModel model(String[] cols){return new DefaultTableModel(cols,0){@Override public boolean isCellEditable(int r,int c){return false;}};}
}
