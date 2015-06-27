import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * Визуальный объект - костяшка
 */
public class Label extends JLabel{
	
	private static final long serialVersionUID = 435435l;
	
	public Label() {
		super();
		setForeground(Color.WHITE);
		setVerticalAlignment(JLabel.CENTER);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(new Font(null, Font.PLAIN, 28));
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		super.paint(g);
	}
	
}
