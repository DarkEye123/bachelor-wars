package ui.menu;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mapping.GameSettings;
import objects.Base;
import objects.Knowledge;

import org.netbeans.lib.awtextra.AbsoluteConstraints;

import ui.GameView;
import env.GameEnv;


public class GameSettingsMenu extends Menu {
	private javax.swing.JPanel agentSelectPanel;
	private javax.swing.JRadioButton annihilationModeButton;
	private javax.swing.JSlider obstaclesSlider;
	private javax.swing.JButton buttonStart;
	private javax.swing.JPanel colorPanelP1;
	private javax.swing.JPanel colorPanelP2;
	private javax.swing.JPanel colorPanelP3;
	private javax.swing.JPanel colorPanelP4;
	private javax.swing.JSlider columnsSlider;
	private javax.swing.JComboBox comboP1;
	private javax.swing.JComboBox comboP2;
	private javax.swing.JComboBox comboP3;
	private javax.swing.JComboBox comboP4;
	private javax.swing.JComboBox comboT1;
	private javax.swing.JComboBox comboT2;
	private javax.swing.JComboBox comboT3;
	private javax.swing.JComboBox comboT4;
	private javax.swing.JTextPane descriptionPane;
	private javax.swing.JPanel gameSettingsContainer;
	private javax.swing.JPanel generalSettings;
	private javax.swing.JSpinner incomePerKnowledge;
	private javax.swing.JSpinner incomePerRound;
	private javax.swing.JButton buttonExit;
	private javax.swing.JCheckBox obstaclesCheckBox;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBox2;
	private javax.swing.JComboBox jComboBox3;
	private javax.swing.JComboBox jComboBox4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel valueLabel;
	private javax.swing.JList resolution;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSpinner jSpinner1;
	private javax.swing.JSpinner jSpinner2;
	private javax.swing.JSpinner jSpinner3;
	private javax.swing.JSpinner jSpinner4;
	private javax.swing.JSpinner value;
	private javax.swing.JSlider knowledgeSlider;
	private javax.swing.JRadioButton madnessModeButton;
	private javax.swing.JPanel mapGenerationPanel;
	private javax.swing.ButtonGroup modeButtonGroup;
	private javax.swing.JRadioButton modeDominationButton;
	private javax.swing.JPanel modePanel;
	private javax.swing.JTextField playerName;
	private javax.swing.JSpinner rounds;
	private javax.swing.JSlider rowsSlider;
	private JFrame frame;
	private ArrayList<JComboBox> comboArray;
	
	GameEnv env;

	public GameSettingsMenu(GameEnv env) {
		this.env = env;
		initComponents();
	}
	
	private boolean startEnabled() {
		int cnt = 0;
		for (JComboBox box:comboArray) {
			if (box.isEnabled())
				cnt++;
		}
		if (cnt >= 2) 
			return true;
		else 
			return false;
	}

	private void initComponents() {
		int x = 860, y = 600;
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
			x = 900;
			y = 630;
		}
		frame = new JFrame();
		modeButtonGroup = new javax.swing.ButtonGroup();
		gameSettingsContainer = new javax.swing.JPanel();
		buttonStart = new javax.swing.JButton();
		agentSelectPanel = new javax.swing.JPanel();
		comboP1 = new javax.swing.JComboBox();
		comboP2 = new javax.swing.JComboBox();
		comboP3 = new javax.swing.JComboBox();
		comboP4 = new javax.swing.JComboBox();
		comboT1 = new javax.swing.JComboBox();
		comboT2 = new javax.swing.JComboBox();
		comboT3 = new javax.swing.JComboBox();
		comboT4 = new javax.swing.JComboBox();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		colorPanelP1 = new javax.swing.JPanel();
		colorPanelP2 = new javax.swing.JPanel();
		colorPanelP3 = new javax.swing.JPanel();
		colorPanelP4 = new javax.swing.JPanel();
		jComboBox1 = new javax.swing.JComboBox();
		jComboBox2 = new javax.swing.JComboBox();
		jComboBox3 = new javax.swing.JComboBox();
		jComboBox4 = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jSpinner1 = new javax.swing.JSpinner();
		jSpinner2 = new javax.swing.JSpinner();
		jSpinner3 = new javax.swing.JSpinner();
		jSpinner4 = new javax.swing.JSpinner();
		value = new javax.swing.JSpinner();
		buttonExit = new javax.swing.JButton();
		mapGenerationPanel = new javax.swing.JPanel();
		knowledgeSlider = new javax.swing.JSlider();
		jLabel7 = new javax.swing.JLabel();
		obstaclesCheckBox = new javax.swing.JCheckBox();
		obstaclesSlider = new javax.swing.JSlider();
		jLabel8 = new javax.swing.JLabel();
		columnsSlider = new javax.swing.JSlider();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		rowsSlider = new javax.swing.JSlider();
		jScrollPane1 = new javax.swing.JScrollPane();
		resolution = new javax.swing.JList();
		modePanel = new javax.swing.JPanel();
		modeDominationButton = new javax.swing.JRadioButton();
		annihilationModeButton = new javax.swing.JRadioButton();
		madnessModeButton = new javax.swing.JRadioButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		descriptionPane = new javax.swing.JTextPane();
		generalSettings = new javax.swing.JPanel();
		jLabel11 = new javax.swing.JLabel();
		rounds = new javax.swing.JSpinner();
		incomePerRound = new javax.swing.JSpinner();
		incomePerKnowledge = new javax.swing.JSpinner();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		valueLabel = new javax.swing.JLabel();
		playerName = new javax.swing.JTextField();
		comboArray = new ArrayList<>();

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setMaximumSize(new java.awt.Dimension(x, y));
		frame.setPreferredSize(new java.awt.Dimension(x, y));
		frame.setResizable(false);

		gameSettingsContainer.setMaximumSize(new java.awt.Dimension(x, y));
		gameSettingsContainer.setMinimumSize(new java.awt.Dimension(x, y));
		gameSettingsContainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		
		comboArray.add(comboT1);
		comboArray.add(comboT2);
		comboArray.add(comboT3);
		comboArray.add(comboT4);

		buttonStart.setText("Start Game");
		buttonStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonStartActionPerformed(evt);
			}
		});
		gameSettingsContainer.add(buttonStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 560, -1, -1));
		
//-------------------------------------------------------------------PLAYER-SELECTION----------------------------------------------------------	
		agentSelectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Players Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("VL Gothic", 0, 12))); // NOI18N
		agentSelectPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		comboP1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Player", "Simple AI", "Medium AI", "Advanced AI", "Closed" }));
		comboP1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem().equals("Closed")) {
						decideStatus(comboT1, jComboBox1, jSpinner1, false);
					} else {
						decideStatus(comboT1, jComboBox1, jSpinner1, true);
					}
				}
			}
		});
		agentSelectPanel.add(comboP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 120, 25));

		comboP2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Closed", "Simple AI", "Medium AI", "Advanced AI" }));
		comboP2.setSelectedIndex(1);
		comboP2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem().equals("Closed")) {
						decideStatus(comboT2, jComboBox2, jSpinner2, false);
					} else {
						decideStatus(comboT2, jComboBox2, jSpinner2, true);
					}
				}
			}
		});
		agentSelectPanel.add(comboP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 120, 25));

		comboP3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Closed", "Simple AI", "Medium AI", "Advanced AI" }));
		comboP3.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem().equals("Closed")) {
						decideStatus(comboT3, jComboBox3, jSpinner3, false);
					} else {
						decideStatus(comboT3, jComboBox3, jSpinner3, true);
					}
				}
			}
		});
		agentSelectPanel.add(comboP3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 120, 25));

		comboP4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Closed", "Simple AI", "Medium AI", "Advanced AI" }));
		comboP4.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem().equals("Closed")) {
						decideStatus(comboT4, jComboBox4, jSpinner4, false);
					} else {
						decideStatus(comboT4, jComboBox4, jSpinner4, true);
					}
				}
			}
		});
		agentSelectPanel.add(comboP4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 120, 25));
//===========================================================================================================================================	
		
//-------------------------------------------------------------------TEAM-SELECTION----------------------------------------------------------	
		comboT1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "1", "2" }));
		comboT1.setToolTipText("");
		agentSelectPanel.add(comboT1, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 50, 50, 25));

		comboT2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "1", "2" }));
		comboT2.setToolTipText("");
		agentSelectPanel.add(comboT2, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 90, 50, 25));

		comboT3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "1", "2" }));
		comboT3.setToolTipText("");
		comboT3.setEnabled(false);
		jComboBox3.setEnabled(false);
		jSpinner3.setEnabled(false);
		agentSelectPanel.add(comboT3, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 130, 50, 25));

		comboT4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--", "1", "2" }));
		comboT4.setToolTipText("");
		comboT4.setEnabled(false);
		jComboBox4.setEnabled(false);
		jSpinner4.setEnabled(false);
		agentSelectPanel.add(comboT4, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 170, 50, 25));
		agentSelectPanel.add(new JLabel("Team"), new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, -1, -1));
//===========================================================================================================================================
		jLabel2.setText("4.");
		agentSelectPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, 30));

		jLabel3.setText("1.");
		agentSelectPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 30));

		jLabel4.setText("2.");
		agentSelectPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 30));

		jLabel5.setText("3.");
		agentSelectPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, 30));

		colorPanelP1.setBackground(java.awt.Color.orange);
		colorPanelP1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout colorPanelP1Layout = new javax.swing.GroupLayout(colorPanelP1);
		colorPanelP1.setLayout(colorPanelP1Layout);
		colorPanelP1Layout.setHorizontalGroup(
				colorPanelP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);
		colorPanelP1Layout.setVerticalGroup(
				colorPanelP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);

		agentSelectPanel.add(colorPanelP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, 25, 25));

		colorPanelP2.setBackground(java.awt.Color.red);
		colorPanelP2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout colorPanelP2Layout = new javax.swing.GroupLayout(colorPanelP2);
		colorPanelP2.setLayout(colorPanelP2Layout);
		colorPanelP2Layout.setHorizontalGroup(
				colorPanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);
		colorPanelP2Layout.setVerticalGroup(
				colorPanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);

		agentSelectPanel.add(colorPanelP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 90, -1, 25));

		colorPanelP3.setBackground(java.awt.Color.blue);
		colorPanelP3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout colorPanelP3Layout = new javax.swing.GroupLayout(colorPanelP3);
		colorPanelP3.setLayout(colorPanelP3Layout);
		colorPanelP3Layout.setHorizontalGroup(
				colorPanelP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);
		colorPanelP3Layout.setVerticalGroup(
				colorPanelP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);

		agentSelectPanel.add(colorPanelP3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, 25, 25));

		colorPanelP4.setBackground(new java.awt.Color(192, 97, 192));
		colorPanelP4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout colorPanelP4Layout = new javax.swing.GroupLayout(colorPanelP4);
		colorPanelP4.setLayout(colorPanelP4Layout);
		colorPanelP4Layout.setHorizontalGroup(
				colorPanelP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);
		colorPanelP4Layout.setVerticalGroup(
				colorPanelP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 19, Short.MAX_VALUE)
				);

		agentSelectPanel.add(colorPanelP4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 25, 25));
//===========================================================COLORS=============================================
		String[] colors = { "orange", "red", "blue", "purple", "green"};
		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(colors));
		agentSelectPanel.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, 25));
		jComboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorPanelP1.setBackground(getSelColor((jComboBox1.getSelectedItem().toString())));
				colorPanelP1.repaint();
			}
		});

		jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(colors));
		jComboBox2.setSelectedIndex(1);
		agentSelectPanel.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, -1, 25));
		jComboBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorPanelP2.setBackground(getSelColor((jComboBox2.getSelectedItem().toString())));
				colorPanelP2.repaint();
			}
		});

		jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(colors));
		jComboBox3.setSelectedIndex(2);
		agentSelectPanel.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 130, -1, 25));
		jComboBox3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorPanelP3.setBackground(getSelColor((jComboBox3.getSelectedItem().toString())));
				colorPanelP3.repaint();
			}
		});

		jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(colors));
		jComboBox4.setSelectedIndex(3);
		agentSelectPanel.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 170, -1, 25));
		jComboBox4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorPanelP4.setBackground(getSelColor((jComboBox4.getSelectedItem().toString())));
				colorPanelP4.repaint();
			}
		});

		jLabel1.setText("Players");
		agentSelectPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, 20));

		jLabel6.setText("Max Slots");
		agentSelectPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

		jSpinner1.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jSpinner1.setModel(new javax.swing.SpinnerNumberModel(6, 3, 15, 1));
		agentSelectPanel.add(jSpinner1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 50, 25));

		jSpinner2.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jSpinner2.setModel(new javax.swing.SpinnerNumberModel(6, 3, 15, 1));
		agentSelectPanel.add(jSpinner2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 50, 25));

		jSpinner3.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jSpinner3.setModel(new javax.swing.SpinnerNumberModel(6, 3, 15, 1));
		agentSelectPanel.add(jSpinner3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 50, 25));

		jSpinner4.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jSpinner4.setModel(new javax.swing.SpinnerNumberModel(6, 3, 15, 1));
		agentSelectPanel.add(jSpinner4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 50, 25));

		gameSettingsContainer.add(agentSelectPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 500, 220));

		buttonExit.setText("Exit");
		gameSettingsContainer.add(buttonExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, 90, -1));
		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});

		mapGenerationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Map Generation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("VL Gothic", 0, 12))); // NOI18N
		mapGenerationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		knowledgeSlider.setMaximum(25);
		knowledgeSlider.setMinimum(3);
		knowledgeSlider.setToolTipText("Beware higher values. It may cause unpredicted behavior");
		knowledgeSlider.setValue(6);
		mapGenerationPanel.add(knowledgeSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

		jLabel7.setText("Knowledge amount");
		jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		mapGenerationPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, 30));

		obstaclesCheckBox.setText("Allow obstacles");
		obstaclesCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				obstaclesSlider.setEnabled(obstaclesCheckBox.isSelected());
			}
		});
		mapGenerationPanel.add(obstaclesCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, 30));

		obstaclesSlider.setMaximum(25);
		obstaclesSlider.setMinimum(1);
		obstaclesSlider.setToolTipText("How much obstacles will be generated - default 6 %");
		obstaclesSlider.setValue(6);
		obstaclesSlider.setEnabled(false);
		mapGenerationPanel.add(obstaclesSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

//		jLabel8.setText("%");
//		mapGenerationPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, -1, -1));

		columnsSlider.setMaximum(60);
		columnsSlider.setMinimum(18);
		columnsSlider.setValue(24);
		mapGenerationPanel.add(columnsSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, -1, -1));

		jLabel9.setText("Map Columns");
		mapGenerationPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, 30));

		jLabel10.setText("Map Rows");
		mapGenerationPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, -1, 30));

		rowsSlider.setMaximum(60);
		rowsSlider.setMinimum(18);
		rowsSlider.setValue(24);
		mapGenerationPanel.add(rowsSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, -1));

		gameSettingsContainer.add(mapGenerationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 530, 260));

		jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Screen Resolution", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("VL Gothic", 0, 12))); // NOI18N

		resolution.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "1024 x 768", "1366 x 768", "1600 x 900" };
			public int getSize() { return strings.length; }
			public Object getElementAt(int i) { return strings[i]; }
		});
		resolution.setSelectedValue("1024 x 768", true);
		resolution.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jScrollPane1.setViewportView(resolution);

		gameSettingsContainer.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 130, 130));

/*===============================================================================================================================================*
 * Modes
 *===============================================================================================================================================*/
		modePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("VL Gothic", 0, 12))); // NOI18N
		modePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		modeButtonGroup.add(modeDominationButton);
		modeDominationButton.setSelected(true);
		modeDominationButton.setText("Domination");
		modeDominationButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modeDominationButtonActionPerformed(evt);
			}
		});
		modePanel.add(modeDominationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

		modeButtonGroup.add(annihilationModeButton);
		annihilationModeButton.setText("Annihilation");
		modePanel.add(annihilationModeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
		annihilationModeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modeAnihilationButtonActionPerformed(evt);
			}
		});

		modeButtonGroup.add(madnessModeButton);
		madnessModeButton.setText("Madness");
		modePanel.add(madnessModeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));
		madnessModeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modeMadnessButtonActionPerformed(evt);
			}
		});

		descriptionPane.setText("DOMINATION\nMode where you have to own at least 80% of \"knowledge resources\" for 3 rounds.");
		gameSettingsContainer.add(modePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 150, 150));
//*****************************************************************************************************************************************************************
		
		descriptionPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mode Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("VL Gothic", 0, 12))); // NOI18N
		descriptionPane.setFont(new java.awt.Font("WenQuanYi Zen Hei Sharp", 0, 12)); // NOI18N
		jScrollPane2.setViewportView(descriptionPane);

		gameSettingsContainer.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, 310, 100));

		generalSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("VL Gothic", 0, 12))); // NOI18N
		generalSettings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel11.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jLabel11.setText("Number of Rounds");
		generalSettings.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 30));

		rounds.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
		rounds.setToolTipText("0 means infinite number of rounds");
		generalSettings.add(rounds, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 60, -1));

		incomePerRound.setModel(new javax.swing.SpinnerNumberModel(10, 5, 40, 1));
		generalSettings.add(incomePerRound, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 60, -1));

		incomePerKnowledge.setModel(new javax.swing.SpinnerNumberModel(5, 5, 40, 1));
		generalSettings.add(incomePerKnowledge, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 60, -1));

		jLabel12.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jLabel12.setText("Income per Round");
		generalSettings.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

		jLabel13.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		jLabel13.setText("Income per Knowledge");
		generalSettings.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, 30));

		value.setModel(new SpinnerNumberModel(GameSettings.DOMINATION_WIN_ROUNDS, 1, 5, 1));
		valueLabel.setText("Rounds to seize");
		valueLabel.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
		generalSettings.add(valueLabel,new AbsoluteConstraints(20, 140, -1, 30));
		generalSettings.add(value,new AbsoluteConstraints(200, 140, 60, -1));
		
		value.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				valueStateChanged(e);
			}
		});
		
		jLabel14.setText("Player Nickname:");
		generalSettings.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, 20));

		playerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		playerName.setText("Player");
		playerName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				playerNameActionPerformed(evt);
			}
		});
		generalSettings.add(playerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 170, 40));

		gameSettingsContainer.add(generalSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 290, 280, 250));

		frame.getContentPane().add(gameSettingsContainer, java.awt.BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	protected void valueStateChanged(ChangeEvent e) {
		if (modeDominationButton.isSelected()) {
			if (Integer.parseInt(value.getValue().toString()) > 1)
				descriptionPane.setText("DOMINATION\nMode where you have to own at least 80% of \"knowledge resources\" for " + value.getValue() + " rounds.");
			else
				descriptionPane.setText("DOMINATION\nMode where you have to own at least 80% of \"knowledge resources\" for " + value.getValue() + " round.");
		}
	}

	protected void decideStatus(JComboBox jComboBox1, JComboBox jComboBox2, JSpinner jSpinner, boolean enabled) {
		jComboBox1.setEnabled(enabled);
		jComboBox2.setEnabled(enabled);
		jSpinner.setEnabled(enabled);
		buttonStart.setEnabled(startEnabled());
	}

	protected Color getSelColor(String selectedItem) {
		String[] colors = { "orange", "red", "blue", "purple", "green"};
		Color[] cArr = {Color.orange, Color.red, Color.blue, new java.awt.Color(192, 97, 192), Color.green};
		return cArr[getIndex(colors, selectedItem)];
	}

	protected void modeMadnessButtonActionPerformed(ActionEvent evt) {
		descriptionPane.setText("MADNESS\nMode where the winner is the one with the highest number of killed enemies. If you destroy base, every unit of this base is killed by you.");
		value.setModel(new SpinnerNumberModel(GameSettings.MADNESS_KILL, 5, null, 1));
		valueLabel.setText("Kills to win");
		value.setVisible(true);
		valueLabel.setVisible(true);
	}

	protected void modeAnihilationButtonActionPerformed(ActionEvent evt) {
		descriptionPane.setText("ANIHILIATION\nMode where you have to destroy all your opponents (get to their base and survive one round there)");
//		value.setModel(new SpinnerNumberModel(GameSettings.MADNESS_KILL, 5, null, 1));
//		valueLabel.setText("Kills to win");
		value.setVisible(false);
		valueLabel.setVisible(false);
	}

	protected void modeDominationButtonActionPerformed(ActionEvent evt) {
		descriptionPane.setText("DOMINATION\nMode where you have to own at least 80% of \"knowledge resources\" for " + GameSettings.DOMINATION_WIN_ROUNDS + " rounds.");
		value.setModel(new SpinnerNumberModel(GameSettings.DOMINATION_WIN_ROUNDS, 1, 5, 1));
		valueLabel.setText("Rounds to seize");
		value.setVisible(true);
		valueLabel.setVisible(true);
	}
	
	private int getIndex(Object[] source, Object item) {
		int x = 0;
		for (Object c:source) {
			if (c.equals(item))
				return x;
			x++;
		}
		return x;
	}
	
	protected int getAI(String val) {
		int [] ai = {GameSettings.SIMPLE_AI, GameSettings.MEDIUM_AI, GameSettings.ADVANCED_AI};
		return ai[getIndex(new String [] {"Simple AI", "Medium AI", "Advanced AI"}, val)];
	}

	protected void buttonStartActionPerformed(ActionEvent evt) {
		frame.dispose();
		GameSettings settings = new GameSettings();
//=======================================GENERAL================================================================
		settings.setIncomePerRound((Integer)incomePerRound.getValue());
		Knowledge.setKnowledgePerRound((Integer)incomePerKnowledge.getValue());
		settings.setMaxRounds((Integer)rounds.getValue());
		settings.setPlayerName(playerName.getText());
		
//=======================================Map-Generation=========================================================
		settings.setMapRows(rowsSlider.getValue());
		settings.setMapColumns(columnsSlider.getValue());
		settings.setNumKnowledgeResources(knowledgeSlider.getValue());
		if (obstaclesCheckBox.isSelected())
			settings.setNumObstacles(obstaclesSlider.getValue());

//======================================Resolution==============================================================
		String [] split= ((String)resolution.getSelectedValue()).split(" x ");
		settings.setWidth(Integer.parseInt(split[0]));
		settings.setHeight(Integer.parseInt(split[1]));

//======================================Modes===================================================================
		if (modeDominationButton.isSelected()) {
			settings.setMode(GameSettings.DOMINATION);
			settings.setWinQuota(Integer.parseInt(value.getValue().toString()));
		}
		if (annihilationModeButton.isSelected())
			settings.setMode(GameSettings.ANNIHLIATION);
		if (madnessModeButton.isSelected()) {
			settings.setMode(GameSettings.MADNESS);
			settings.setWinQuota(Integer.parseInt(value.getValue().toString()));
		}
		
//======================================Player-Creation=========================================================
		int width = Base.DEFAULT_BASE_SIZE.width;
		int height = Base.DEFAULT_BASE_SIZE.height;
		int index = 0;
		if (!comboP1.getSelectedItem().equals("Closed")) {
			String player = (String)comboP1.getSelectedItem();
			if (player.equals("Player"))
				settings.addPlayer(GameSettings.PLAYER, colorPanelP1.getBackground(), new Location(0, 0));
			else {
				settings.addPlayer(getAI(player), colorPanelP1.getBackground(),  new Location(0, 0));
				index = 1;
			}
		}
		if (!comboP2.getSelectedItem().equals("Closed"))
			settings.addPlayer(getAI((String)comboP2.getSelectedItem()), colorPanelP2.getBackground(), new Location(settings.getMapColumns() - width, 0));
		if (!comboP3.getSelectedItem().equals("Closed"))
			settings.addPlayer(getAI((String)comboP3.getSelectedItem()), colorPanelP3.getBackground(), new Location(0, settings.getMapRows() - height));
		if (!comboP4.getSelectedItem().equals("Closed"))
			settings.addPlayer(getAI((String)comboP4.getSelectedItem()), colorPanelP4.getBackground(), new Location(settings.getMapColumns() - width, settings.getMapRows() - height));
		settings.init();
		
		HashMap<String, ArrayList<Integer>> teams = new HashMap<>();
		for (int i = 0; i < comboT1.getItemCount(); ++i) {
			teams.put((String) comboT1.getItemAt(i), new ArrayList<Integer>());
		}
		
		for (JComboBox box:comboArray) {
			if (box.isEnabled()) {
				teams.get((String)box.getSelectedItem()).add(index++);
			}
		}
//		System.out.println(teams);
		
		settings.addTeams(teams);
		
		env.setView(new GameView(settings));
	}
}
