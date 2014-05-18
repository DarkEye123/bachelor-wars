package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import objects.Base;
import objects.Knowledge;
import objects.units.Unit;
import mapping.GameSettings;


public class StatisticalPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = -2262922170788475187L;
	
	public static final float HEIGHT_MULTIPLIER = 0.05f;
	private static final Insets DEFAULT_INSETS = new Insets(1, 20, 1, 20);

	GameView view;
	JLabel time;
	JLabel winningBase;
	JLabel value;
	JLabel round;
	JLabel roundsToWin;
	GridBagConstraints constraints;
	Timer timer;
	JButton buttonEndRound;
	boolean canRepaint = false;
	Image pic;
	String maxRounds;
	Base playerBase;
	
	public StatisticalPanel(GameView gameView) {
		view = gameView;
		pic = Toolkit.getDefaultToolkit().getImage("pics/ControlMenu.jpeg");
		if (view.settings.getMaxRounds() == GameSettings.INFINITE)
			maxRounds = "inf";
		else
			maxRounds = Integer.toString(view.settings.getMaxRounds());
	}

	/**
	 * Initialize ControlMenu. This method must be called after map initialization is done.
	 */
	public void init() {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	    constraints = new GridBagConstraints();
		constraints.insets = DEFAULT_INSETS;
		constraints.anchor = GridBagConstraints.CENTER;
		this.setLayout(new GridBagLayout());
		Font font = new Font("VL Gothic", Font.BOLD, 15);
		
		winningBase = new JLabel("Winning base: --");
		round = new JLabel("Round: " + GameMap.ROUND );
		time = new JLabel("time: " + ft.format(dNow));
		buttonEndRound = new JButton("end round");
		buttonEndRound.addMouseListener(new EndRoundListener());
		
		if (view.settings.getMode() == GameSettings.DOMINATION)  {
			value = new JLabel("seized: --");
			roundsToWin = new JLabel("Rounds to win: " + 1 + "/" + view.getSettings().getWinQuota());
			roundsToWin.setFont(font);
			roundsToWin.setForeground(Color.white);
			this.add(winningBase, constraints);
			constraints.gridx = 1;
			this.add(value, constraints);
			constraints.gridx = 2;
			this.add(roundsToWin, constraints);
			constraints.gridx = 3;
			this.add(round, constraints);
			constraints.gridx = 4;
			this.add(time, constraints);
			constraints.gridx = 5;
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			this.add(buttonEndRound, constraints);
		}
		else if (view.settings.getMode() == GameSettings.MADNESS) {
			value = new JLabel("killed: 0" + "/" + view.settings.getWinQuota());
			this.add(winningBase, constraints);
			constraints.gridx = 1;
			this.add(value, constraints);
			constraints.gridx = 2;
			this.add(round, constraints);
			constraints.gridx = 3;
			this.add(time, constraints);
			constraints.gridx = 4;
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			this.add(buttonEndRound, constraints);
		}
		else {
			value = new JLabel("seized bases: 0" + "/" + view.settings.getWinQuota());
			this.add(winningBase, constraints);
			constraints.gridx = 1;
			this.add(value, constraints);
			constraints.gridx = 2;
			this.add(round, constraints);
			constraints.gridx = 3;
			this.add(time, constraints);
			constraints.gridx = 4;
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			this.add(buttonEndRound, constraints);
		}
		
		
		
		canRepaint = true;
		repaint();
		timer = new Timer(1000, this);
		timer.start();

		value.setForeground(Color.white);
		round.setForeground(Color.white);
		time.setForeground(Color.white);
		winningBase.setForeground(Color.white);
		
		time.setFont(font);
		winningBase.setFont(font);
		value.setFont(font);
		round.setFont(font);
		
		repaint();
		playerBase = view.getGameMap().getPlayerBase();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(pic, 0, 0, getWidth(), getHeight(), null);
		if (buttonEndRound != null)
			buttonEndRound.setEnabled(view.getGameMap().canManipulate() && view.getGameMap().isLivingPlayer());
		if (canRepaint) {
			Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
		    
		    if (view.env.analyzer != null) {
			    if (view.env.analyzer.winningBase == null)
					winningBase.setText("Winning base: --");
				else
					winningBase.setText("Winning base: " + view.env.analyzer.winningBase.getName() );
			    
//			    roundsToWin.setText("Rounds to win: " + view.env.analyzer.getConditionCounter() + "/" + maxRounds);
			    
			    if (view.settings.getMode() == GameSettings.DOMINATION)  {
			    	if (view.env.analyzer.winningBase == null) {
			    		value.setText("seized: --");
			    		roundsToWin.setText("Rounds to win: " + 1 + "/" + view.settings.getWinQuota());
			    	}
			    	else  {
			    		value.setText("seized: " + view.env.analyzer.winningBase.getKnowledgeList().size());
			    		roundsToWin.setText("Rounds to win: " + view.env.analyzer.getConditionCounter() + "/" + view.settings.getWinQuota());
			    	}
				}
				else if (view.settings.getMode() == GameSettings.MADNESS) {
					if (view.env.analyzer.winningBase != null)
						value.setText("killed: " + view.env.analyzer.winningBase.getKilledEnemies() + "/" + view.settings.getWinQuota());
					else
						value.setText("killed: 0" + "/" + view.settings.getWinQuota());
				}
				else {
					if (view.env.analyzer.winningBase != null)
						value.setText("seized bases: " + view.env.analyzer.winningBase.getSeizedBases());
					else
						value.setText("seized bases: " + 0);
				}
		    }
		    
			round.setText("Round: " + GameMap.ROUND + "/" + maxRounds);
			time.setText("time: " + ft.format(dNow));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	protected class EndRoundListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			view.getGameMap().setCanManipulate(false);
			if (playerBase != null) {
				for (Unit u:playerBase.getUnitList())
					u.setLocation(u.getOldLocation());
				view.getGameMap().clearMovement();
				view.getGameMap().repaint();
			}
			GameMap.allowActions(GameMap.getActiveBases(), view.env);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {	}
		
	}
}
