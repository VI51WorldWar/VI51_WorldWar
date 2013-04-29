package fr.utbm.vi51.gui;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * @author valentin
 */
public class Window extends JFrame {
	// Dimensions of the Window
	private final int WIN_HEIGHT = 600;
	private final int WIN_WIDTH = 800;
	
    public Window() {
        this.setTitle("Fenetre de base");
        this.setSize(this.WIN_WIDTH, this.WIN_HEIGHT);
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
