package com.campusconnect.ui;
import com.campusconnect.dao.UserDAO;import com.campusconnect.model.User;import javax.swing.*;import java.awt.*;
public class RegisterFrame extends JFrame {
    private final JTextField name=new JTextField(),email=new JTextField();private final JPasswordField pass=new JPasswordField(),confirm=new JPasswordField();private final UserDAO dao=new UserDAO();
    public RegisterFrame(){setTitle("CampusConnect | Register");setSize(460,560);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);setResizable(false);
        JPanel p=new JPanel();p.setBackground(UiStyle.BG);p.setBorder(BorderFactory.createEmptyBorder(25,48,25,48));p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        JLabel title=new JLabel("Create Student Account");title.setFont(new Font("Arial",Font.BOLD,24));title.setAlignmentX(CENTER_ALIGNMENT);p.add(title);
        addField(p,"Full Name",name);addField(p,"Email Address",email);addField(p,"Password (minimum 8 characters)",pass);addField(p,"Confirm Password",confirm);
        JButton create=UiStyle.primary("Create Account");create.setMaximumSize(new Dimension(350,42));create.setAlignmentX(CENTER_ALIGNMENT);create.addActionListener(e->register());
        JButton back=new JButton("Back to Login");back.setAlignmentX(CENTER_ALIGNMENT);back.setBorderPainted(false);back.setContentAreaFilled(false);back.addActionListener(e->dispose());p.add(Box.createVerticalStrut(20));p.add(create);p.add(back);add(p);setVisible(true);
    }
    private void addField(JPanel p,String label,JTextField f){JLabel l=new JLabel(label);l.setFont(new Font("Arial",Font.BOLD,13));f.setMaximumSize(new Dimension(350,34));p.add(Box.createVerticalStrut(12));p.add(l);p.add(Box.createVerticalStrut(5));p.add(f);}
    private void register(){String n=name.getText().trim(),m=email.getText().trim(),pw=new String(pass.getPassword()),cp=new String(confirm.getPassword());
        if(n.isBlank()||m.isBlank()||pw.isBlank()||cp.isBlank()){JOptionPane.showMessageDialog(this,"Please fill all fields.");return;}
        if(!m.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){JOptionPane.showMessageDialog(this,"Enter a valid email.");return;}
        if(pw.length()<8){JOptionPane.showMessageDialog(this,"Password must be at least 8 characters.");return;}
        if(!pw.equals(cp)){JOptionPane.showMessageDialog(this,"Passwords do not match.");return;}
        try{dao.registerUser(new User(0,n,m,pw,"STUDENT"));JOptionPane.showMessageDialog(this,"Registration successful. Please login.");dispose();}
        catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Registration error",JOptionPane.ERROR_MESSAGE);}}
}
