package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

import com.fazecast.jSerialComm.SerialPort;

import model.Model;
import controller.Controller;

@SuppressWarnings("serial")
public class View extends JFrame {

	public XYSeries series1 = new XYSeries("Température Frigo");
	public XYSeries series2 = new XYSeries("Température Externe");
	public XYSeries series3 = new XYSeries("Point de rosée");
	public XYSeries series4 = new XYSeries("Consigne");

	public JButton minusBtn;
	public JButton addBtn;
	public JTextField cons;

	private JButton btnGraph;
	private JButton btnSettings;
	private JPanel panelGraph;
	private JPanel panelSettings;
	private JComboBox<String> portList;
	public JTextField TempList;
	public model.Model ModInter;
	
	public Model model;

	public XYLineAndShapeRenderer renderer;

	private static SerialPort chosenPort;

	public View(model.Model modInter) {
		super();
		this.setModInter(modInter);
		build();
		modInter.SetPoint = 18;
	}

	private void build() {
		setTitle("PMF"); // On donne un titre à l'application
		setSize(600, 400); // On donne une taille à notre fenêtre
		setLocationRelativeTo(null); // On centre la fenêtre sur l'écran
		setResizable(true); // On permet le redimensionnement
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // On dit à l'application de se fermer lors du clic sur la croix
		setContentPane(buildContentPane());
		setVisible(true);// On la rend visible

	}

	JPanel buildContentPane() {
		JPanel pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setLayout(null);
		pan.add(menupanel());
		pan.add(ContentPaneGraph());
		pan.add(ContentPaneSettings());

		return pan;
	}

	JPanel menupanel() {
		JPanel pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setLayout(null);
		pan.setBounds(0, 0, 600, 35);
		CreateJButtonGraph();
		pan.add(getBtnGraph());
		//CreateAlert();
		CreateJButtonSettings();
		pan.add(getBtnSettings());
		return pan;
	}

	JPanel ContentPaneGraph() {
		JPanel pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setLayout(null);
		pan.setBounds(0, 35, 600, 365);
		pan.add(initUI());
		pan.add(CreateFieldTa());
		pan.add(CreateFieldTi());
		pan.add(CreateFieldTv());
		pan.add(CreateFieldTr());

		this.setPanelGraph(pan);

		return pan;
	}

	JPanel ContentPaneSettings() {
		JPanel pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setLayout(null);
		pan.setBounds(0, 35, 600, 365);
		pan.add(CreateTempField());
		pan.add(CreateAlert());
		pan.add(createMinusBtn());
		pan.add(createAddBtn());

		pan.add(CreatePortField());
		pan.add(CreatePortList());
		pan.setVisible(false);
		this.setPanelSettings(pan);

		return pan;
	}
	
	

	public JTextField CreateFieldTa() {
		JTextField boxTa = new JTextField("T.Fridge°C");
		boxTa.setBounds(500, 50, 60, 20);
		boxTa.setBackground(Color.WHITE);
		boxTa.setFont(new Font("Century Gothic", Font.BOLD, 15));
		boxTa.setForeground(new Color(255, 12, 10));
		boxTa.setEditable(false);
		boxTa.setFocusable(false);
		boxTa.setBorder(null);
		return boxTa;
	}

	public JTextField CreateFieldTi() {
		JTextField boxTi = new JTextField("T.int°C");
		boxTi.setBounds(500, 70, 60, 20);
		boxTi.setBackground(Color.WHITE);
		boxTi.setFont(new Font("Century Gothic", Font.BOLD, 15));
		boxTi.setForeground(new Color(11, 183, 0));
		boxTi.setEditable(false);
		boxTi.setFocusable(false);
		boxTi.setBorder(null);
		return boxTi;
	}

	public JTextField CreateFieldTv() {
		JTextField boxTv = new JTextField("Hum %");
		boxTv.setBounds(500, 90, 60, 20);
		boxTv.setBackground(Color.WHITE);
		boxTv.setFont(new Font("Century Gothic", Font.BOLD, 15));
		boxTv.setForeground(new Color(1, 0, 183));
		boxTv.setEditable(false);
		boxTv.setFocusable(false);
		boxTv.setBorder(null);
		return boxTv;
	}

	public JTextField CreateFieldTr() {
		JTextField boxTr = new JTextField("T.PdR°C");
		boxTr.setBounds(500, 110, 60, 20);
		boxTr.setBackground(Color.WHITE);
		boxTr.setFont(new Font("Century Gothic", Font.BOLD, 15));
		boxTr.setForeground(new Color(255, 201, 14));
		boxTr.setEditable(false);
		boxTr.setFocusable(false);
		boxTr.setBorder(null);
		return boxTr;
	}

	public JTextField CreateTempField() {
		JTextField temp = new JTextField("Consigne °C");
		temp.setBounds(195, 80, 220, 32);
		temp.setBackground(Color.WHITE);
		temp.setFont(new Font("Century Gothic", Font.BOLD, 22));
		temp.setForeground(new Color(127, 128, 130));
		temp.setEditable(false);
		temp.setFocusable(false);
		temp.setBorder(null);
		return temp;
	}

	/*
	 * public JTextField CreateTemp() { JTextField temp2 = new JTextField();
	 * temp2.setBounds(200,120,200,35); temp2.setBackground(Color.WHITE);
	 * temp2.setFont(new Font("Century Gothic",Font.BOLD,22));
	 * temp2.setForeground(new Color(127,128,130)); temp2.setEditable(true);
	 * temp2.setFocusable(true);
	 * 
	 * return temp2; }
	 */

	private JTextField CreateAlert() {
		cons = new JTextField("18.0");
		cons.setBounds(200, 120, 100, 35);
		cons.setFont(new Font("Century Gothic", Font.BOLD, 18));
		cons.setBackground(Color.WHITE);
		cons.setForeground(new Color(127, 128, 130));
		cons.setEditable(false);
		cons.setVisible(true);
		return cons;
	}
	
	

	private JButton createMinusBtn() {
		minusBtn = new JButton("-");
		minusBtn.setBounds(100, 120, 100, 35);
		minusBtn.setFont(new Font("Century Gothic", Font.BOLD, 18));
		minusBtn.setBackground(Color.WHITE);
		minusBtn.setForeground(new Color(127, 128, 130));
		// TempList.setEditable(true);
		// this.setTempList(TempList);
		return minusBtn;
	}

	private JButton createAddBtn() {
		addBtn = new JButton("+");
		addBtn.setBounds(300, 120, 100, 35);
		addBtn.setFont(new Font("Century Gothic", Font.BOLD, 18));
		addBtn.setBackground(Color.WHITE);
		addBtn.setForeground(new Color(127, 128, 130));
		return addBtn;
	}

	public void CreateJButtonGraph() {
		setBtnGraph(new JButton("Graphique"));
		getBtnGraph().setBounds(0, 0, 300, 35);
		getBtnGraph().setFocusPainted(false);
		getBtnGraph().setBackground(new Color(255, 12, 10));
		getBtnGraph().setForeground(Color.WHITE);
		getBtnGraph().setFont(new Font("Century Gothic", Font.BOLD, 25));
	}

	public void CreateJButtonSettings() {
		setBtnSettings(new JButton("Règlages"));
		getBtnSettings().setBounds(300, 0, 300, 35);
		getBtnSettings().setFocusPainted(false);
		getBtnSettings().setBackground(new Color(255, 12, 10));
		getBtnSettings().setForeground(Color.WHITE);
		getBtnSettings().setFont(new Font("Century Gothic", Font.BOLD, 25));
	}

	public JTextField CreatePortField() {
		JTextField port = new JTextField("port");
		port.setBounds(20, 12, 40, 22);
		port.setBackground(Color.WHITE);
		port.setFont(new Font("Century Gothic", Font.BOLD, 18));
		port.setForeground(new Color(127, 128, 130));
		port.setEditable(false);
		port.setFocusable(false);
		port.setBorder(null);

		return port;
	}

	public JComboBox<String> CreatePortList() {
		JComboBox<String> portList = new JComboBox<String>();
		SerialPort[] portNames = SerialPort.getCommPorts();
		for (int i = 0; i < portNames.length; i++)
			portList.addItem(portNames[i].getSystemPortName());
		portList.setBounds(65, 15, 65, 20);
		portList.setForeground(new Color(127, 128, 130));
		portList.setFont(new Font("Century Gothic", Font.BOLD, 13));
		portList.setBackground(Color.WHITE);
		this.setPortList(portList);

		return portList;
	}

	public ChartPanel initUI() {
		JFreeChart chart = createChart(createDataset());
		ChartPanel chartpane = new ChartPanel(chart);
		chartpane.setSize(470, 330);
		chartpane.setPopupMenu(null);
		return chartpane;
	}

	private JFreeChart createChart(final XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart(null, "Temps", "Température (°C)", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		XYPlot plot = chart.getXYPlot();

		setRenderer(new XYLineAndShapeRenderer());

		getRenderer().setSeriesPaint(2, new Color(1, 0, 183));
		getRenderer().setSeriesStroke(2, new BasicStroke(2.0f));

		getRenderer().setSeriesPaint(1, new Color(11, 183, 0));
		getRenderer().setSeriesStroke(1, new BasicStroke(2.0f));

		getRenderer().setSeriesPaint(4, new Color(1, 0, 183));
		getRenderer().setSeriesStroke(4, new BasicStroke(2.0f));

		getRenderer().setSeriesPaint(3, new Color(255, 201, 14));
		getRenderer().setSeriesStroke(3, new BasicStroke(2.0f));

		plot.setRenderer(getRenderer());
		plot.setBackgroundPaint(Color.white);

		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		return chart;

	}

	public XYDataset createDataset() {

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);

		return dataset;
	}

	public JButton getBtnGraph() {
		return btnGraph;
	}

	public void setBtnGraph(JButton btnGraph) {
		this.btnGraph = btnGraph;
	}

	public JPanel getPanelGraph() {
		return panelGraph;
	}

	public void setPanelGraph(JPanel panelGraph) {
		this.panelGraph = panelGraph;
	}

	public JPanel getPanelSettings() {
		return panelSettings;
	}

	public void setPanelSettings(JPanel panelSettings) {
		this.panelSettings = panelSettings;
	}

	public JButton getBtnSettings() {
		return btnSettings;
	}

	public void setBtnSettings(JButton btnSettings) {
		this.btnSettings = btnSettings;
	}

	public JComboBox<String> getPortList() {
		return portList;
	}

	public void setPortList(JComboBox<String> portList) {
		this.portList = portList;
	}

	public static SerialPort getChosenPort() {
		return chosenPort;
	}

	public static void setChosenPort(SerialPort chosenPort) {
		View.chosenPort = chosenPort;
	}

	public XYLineAndShapeRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(XYLineAndShapeRenderer renderer) {
		this.renderer = renderer;
	}

	public JTextField getTempList() {
		return TempList;
	}

	public void setTempList(JTextField tempList2) {
		TempList = tempList2;
	}

	public model.Model getModInter() {
		return ModInter;
	}

	public void setModInter(model.Model modInter) {
		ModInter = modInter;
	}

}
