import javax.swing.*;
import java.awt.*;

public class MazmorraGui {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Mazmorra");
        jFrame.setSize(500,500);

        JButton buttonNorte= new JButton("Norte");
        JButton buttonSur = new JButton("Sur");
        JButton buttonEste= new JButton("Este");
        JButton buttonOeste = new JButton("Oeste");

        JTextField txtDescripcion = new JTextField("Descripcion");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Menu lateral (hay que cargar el xml aca)
        //JTree

        jFrame.getContentPane().add(BorderLayout.NORTH, buttonNorte);
        jFrame.getContentPane().add(BorderLayout.SOUTH, buttonSur);
        jFrame.getContentPane().add(BorderLayout.EAST, buttonEste);
        jFrame.getContentPane().add(BorderLayout.WEST, buttonOeste);

        jFrame.getContentPane().add(BorderLayout.CENTER, txtDescripcion);

        jFrame.setVisible(true);
    }
}
