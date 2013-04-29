package fr.utbm.vi51.gui;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * @author valentin
 */
public class Window extends JFrame {
    public Window() {
        this.setTitle("Ma première fenêtre Java");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        // Instanciation d'un objet JPanel
        Panel pan = new Panel();
        // Définition de sa couleur de fond
        pan.setBackground(Color.ORANGE);
        // On prévient notre JFrame que notre JPanel sera son content pane
        this.setContentPane(pan);
        this.setVisible(true);
    }




}
