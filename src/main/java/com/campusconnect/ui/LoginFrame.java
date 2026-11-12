package com.campusconnect.ui;
import com.campusconnect.dao.UserDAO;import com.campusconnect.model.User;import com.campusconnect.service.Session;
import javax.swing.*;import java.awt.*;
public class LoginFrame extends JFrame {
    private final JTextField email=new JTextField();private final JPasswordField password=new JPasswordField();private final UserDAO dao=new UserDAO();
    public LoginFrame(){setTitle("CampusConnect | Login");setSize(460,470);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);setResizable(false);
        JPanel p=new JPanel();p.setBackground(UiStyle.BG);p.setBorder(BorderFactory.createEmptyBorder(38,48,32,48));p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        JLabel title=new JLabel("CampusConnect");title.setFont(new Font("Arial",Font.BOLD,29));title.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sub=new JLabel("Lost & Found Management System");sub.setForeground(UiStyle.MUTED);sub.setAlignmentX(CENTER_ALIGNMENT);
        addField(p,"Email Address",email);addField(p,"Password",password);
        JButton login=UiStyle.primary("Login");login.setMaximumSize(new Dimension(350,42));login.setAlignmentX(CENTER_ALIGNMENT);login.addActionListener(e->login());
        JButton register=new JButton("Create New Account");register.setAlignmentX(CENTER_ALIGNMENT);register.setBorderPainted(false);register.setContentAreaFilled(false);register.addActionListener(e->new RegisterFrame());
        p.add(Box.createVerticalStrut(12));p.add(title);p.add(Box.createVerticalStrut(8));p.add(sub);p.add(Box.createVerticalStrut(34));p.add(login);p.add(Box.createVerticalStrut(10));p.add(register);add(p);getRootPane().setDefaultButton(login);setVisible(true);
    }
    private void addField(JPanel p,String label,JTextField field){JLabel l=new JLabel(label);l.setFont(new Font("Arial",Font.BOLD,14));field.setMaximumSize(new Dimension(350,38));p.add(Box.createVerticalStrut(22));p.add(l);p.add(Box.createVerticalStrut(7));p.add(field);}
    private void login(){String mail=email.getText().trim();String pass=new String(password.getPassword());if(mail.isBlank()||pass.isBlank()){JOptionPane.showMessageDialog(this,"Enter email and password.");return;}
        try{User user=dao.authenticate(mail,pass);if(user==null){JOptionPane.showMessageDialog(this,"Invalid email or password.","Login failed",JOptionPane.ERROR_MESSAGE);return;}Session.setCurrentUser(user);dispose();new DashboardFrame();}
        catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Login error",JOptionPane.ERROR_MESSAGE);}}
}
