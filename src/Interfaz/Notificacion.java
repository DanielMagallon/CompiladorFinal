package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Notificacion extends JDialog implements ActionListener
{
    private JLabel lblMsg;

    public Notificacion()
    {
        setSize(300,100);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width-330,
                    10);
        lblMsg = new JLabel();
        lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        lblMsg.setFont(new Font("Monospace",Font.BOLD,14));
        getContentPane().add(lblMsg);
        setAlwaysOnTop(true);
    }

    public Notificacion notifyMsg(String msg)
    {
        lblMsg.setText(msg);
        return this;
    }

    public void run(){
        setVisible(true);
        timer.start();

    }

    private Timer timer = new Timer(6000,this);

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Compilador.getInstance().requestFocus();
        this.dispose();
        timer.stop();
    }
}
