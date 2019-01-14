package main;

import controller.Controller;
import model.Model;
import view.View;

public class Main {

	public static void main(String[] args) {
		Model model = new Model();
		View fenetre = new View(model);
		Controller controller = new Controller(fenetre, model);
		
		fenetre.addBtn.addActionListener(controller);
		fenetre.minusBtn.addActionListener(controller);
		fenetre.getBtnSettings().addActionListener(controller);
		fenetre.getBtnGraph().addActionListener(controller);

	}
}
