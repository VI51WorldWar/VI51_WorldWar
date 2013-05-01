package fr.utbm.vi51.gui;

import java.awt.Color;

import javax.swing.JFrame;

import fr.utbm.vi51.configs.Consts;

/**
 * @author valentin
 */
public class Window extends JFrame {
    // Dimensions of the Window

    public Window() {
        this.setTitle("Fenetre de base");
        this.setSize(Consts.WINWIDTH, Consts.WINHEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Instanciation d'un objet JPanel
        Panel pan = new Panel();
        // Définition de sa couleur de fond
        pan.setBackground(Color.ORANGE);
        // On prévient notre JFrame que notre JPanel sera son content pane
        this.setContentPane(pan);
        this.setVisible(true);
    }




}
