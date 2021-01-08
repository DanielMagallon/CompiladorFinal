package Interfaz;

import Exe.Snippet;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class About extends JDialog {

    public About()
    {
        setModal(true);
        setSize(800,800);

        StringBuilder data = new StringBuilder();

        try(FileReader fr = new FileReader(Snippet.propierties.pathFolder+"/AcercaDe.txt")) {
            try (BufferedReader br = new BufferedReader(fr)) {
                String line;

                while((line=br.readLine())!=null)
                    data.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        JTextArea about = new JTextArea();
        about.setEditable(false);
        about.setFont(new Font("Monospace",Font.PLAIN,17));
        about.setText(data.toString());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(about));
    }
}
