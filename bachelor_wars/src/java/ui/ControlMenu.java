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
import mapping.GameSettings;


public class ControlMenu extends JPanel implements ActionListener{
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
	
	public ControlMenu(GameView gameView) {
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
		
		if (view.settings.getMode() == GameSettings.DOMINATION)  {
			value = new JLabel("seized: --");
			roundsToWin = new JLabel("Rounds to win: " + 1 + "/" + GameSettings.DOMINATION_WIN_ROUNDS);
		}
		else if (view.settings.getMode() == GameSettings.ANIHLIATION) {
			value = new JLabel("killed: --");
			roundsToWin = new JLabel("Rounds to win: " + 1 + "/" + maxRounds);
		}
		else {
			value = new JLabel("survived: --");
			roundsToWin = new JLabel("Rounds to win: " + 1 + "/" + maxRounds);
		}
		
		
		round = new JLabel("Round: " + GameMap.ROUND );
		time = new JLabel("time: " + ft.format(dNow));
		buttonEndRound = new JButton("end round");
		buttonEndRound.addMouseListener(new EndRoundListener());
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
		canRepaint = true;
		repaint();
		timer = new Timer(1000, this);
		timer.start();

		value.setForeground(Color.white);
		round.setForeground(Color.white);
		time.setForeground(Color.white);
		winningBase.setForeground(Color.white);
		roundsToWin.setForeground(Color.white);
		
		time.setFont(font);
		winningBase.setFont(font);
		value.setFont(font);
		roundsToWin.setFont(font);
		round.setFont(font);
		
		repaint();
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
			    	if (view.env.analyzer.winningBase == null)
			    		value.setText("seized: --");
			    	else 
			    		value.setText("seized: " + view.env.analyzer.winningBase.getKnowledgeList().size());
					roundsToWin.setText("Rounds to win: " + view.env.analyzer.getConditionCounter() + "/" + GameSettings.DOMINATION_WIN_ROUNDS);
				}
				else if (view.settings.getMode() == GameSettings.ANIHLIATION) {
					if (view.env.analyzer.winningBase == null)
						value.setText("killed: --");
					else
						value.setText("killed: " + view.env.analyzer.winningBase.getKilledEnemies());
						
					roundsToWin.setText("Rounds to win: " + view.env.analyzer.getConditionCounter() + "/" + maxRounds);
				}
				else {
					if (view.env.analyzer.winningBase == null)
						value.setText("survived: --");
					else
						value.setText("survived: " + view.env.analyzer.winningBase);
					roundsToWin.setText("Rounds to win: " + view.env.analyzer.winningBase.getRoundsSurvived() + "/" + maxRounds);
				}
		    }
		    
			round.setText("Round: " + GameMap.ROUND);
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
