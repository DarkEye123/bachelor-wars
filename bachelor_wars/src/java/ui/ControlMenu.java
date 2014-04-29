package ui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	JLabel income;
	JLabel round;
	GridBagConstraints constraints;
	Timer timer;
	JButton buttonEndRound;
	boolean canRepaint = false;
	
	public ControlMenu(GameView gameView) {
		view = gameView;
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
		income = new JLabel("Income per round: " + computeIncome( view.getGameMap().getBaseList().get(GameSettings.PLAYER_ID) ) );
		round = new JLabel("Round: " + GameMap.ROUND );
		time = new JLabel("time: " + ft.format(dNow));
		buttonEndRound = new JButton("end round");
		buttonEndRound.addMouseListener(new EndRoundListener());
		this.add(income, constraints);
		constraints.gridx = 1;
		this.add(round, constraints);
		constraints.gridx = 2;
		this.add(time, constraints);
		constraints.gridx = 3;
		this.add(buttonEndRound, constraints);
		canRepaint = true;
		repaint();
		timer = new Timer(1000, this);
		timer.start();
	}
	
	public void repaint() {
		super.repaint();
		if (buttonEndRound != null)
			buttonEndRound.setEnabled(view.getGameMap().canManipulate());
		if (canRepaint) {
			Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
			income.setText("Income per round: " + computeIncome( view.getGameMap().getBaseList().get(GameSettings.PLAYER_ID) ));
			round.setText("Round: " + GameMap.ROUND);
			time.setText("time: " + ft.format(dNow));
		}
	}
	
	/**
	 * Compute income per round for given Base instance
	 * @param base
	 * @return - income of "knowledge" per round
	 */
	private int computeIncome(Base base) {
		int sum = view.getSettings().getIncomePerRound();
		
		for (Knowledge knowledge:base.getKnowledgeList()) {
			sum += knowledge.getKnowledgePerRound();
		}
		return sum;
	}

	@Override
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
