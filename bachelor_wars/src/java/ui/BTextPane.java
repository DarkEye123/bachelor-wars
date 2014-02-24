package ui;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class BTextPane extends JTextPane {
	private static final long serialVersionUID = 3325289944337440580L;

	public void append(String s) {
	   try {
	      Document doc = this.getDocument();
	      doc.insertString(doc.getLength(), s+"\n", null);
	   } catch(BadLocationException exc) {
	      exc.printStackTrace();
	   }
	}
}
