package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import model.Model;
import view.View;

public class Controller implements ActionListener {

	public Model model;
	public View VInter;
	public int frequency = 5000;
	private JOptionPane alert;

	public Controller(View vInter, Model model) {
		super();
		this.model = model;
		this.setVInter(vInter);
		getVInter().getModInter().initialize();
		thread.start();

	}

	Thread thread = new Thread() {

		public void run() {
			try {
				thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) {
				System.out.println("New point");
				getVInter().series1.add(Model.getTemps(), Model.getTempInt());
				getVInter().series2.add(Model.getTemps(), Model.getTempExt());
				getVInter().series3.add(Model.getTemps(), Model.getHumidity());
				getVInter().series4.add(Model.getTemps(), Model.getDewPoint());

				getVInter().getPanelGraph().repaint();
				
				if(Model.getTempInt() <= Model.getDewPoint()) {
					System.out.println("ALERT !");
					alert = new JOptionPane();
					alert.showMessageDialog(null, "Attention Condensation", "Attention", JOptionPane.WARNING_MESSAGE);
				}
				
				try {
					thread.sleep(frequency);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	};

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());

		Object source = e.getSource();

		if (source == VInter.getBtnGraph()) {
			System.out.println("Vous avez cliqué ici.");
			VInter.getPanelGraph().setVisible(true);
			VInter.getPanelSettings().setVisible(false);

		} else if (source == VInter.getBtnSettings()) {
			System.out.println("Vous avez cliqué là.");
			VInter.getPanelGraph().setVisible(false);
			VInter.getPanelSettings().setVisible(true);

		} else if (VInter.getPortList().getSelectedIndex() < 0) {
		}

		else if (source == VInter.getTempList()) {

			System.out.println("Vous avez entré un texte.");
		}

		if (e.getActionCommand() == "+") {
			model.SetPoint += 1;
			model.writeData(Float.toString(model.SetPoint));
			VInter.cons.setText(Float.toString(model.SetPoint));
			System.out.println("CONSIGNE : " + model.SetPoint);
		} else if (e.getActionCommand() == "-") {
			model.SetPoint -= 1;
			model.writeData(Float.toString(model.SetPoint));
			VInter.cons.setText(Float.toString(model.SetPoint));
			System.out.println("CONSIGNE : " + model.SetPoint);
		}

	}

	public View getVInter() {
		return VInter;
	}

	public void setVInter(View vInter) {
		VInter = vInter;
	}

}
