import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Отображает сообщение о том, что задача решена или не может быть решена
 */
public class MessageDialog extends JDialog{
	
	private static final long serialVersionUID = 4645424l;
	
	private JPanel mMainPanel;
	private JLabel mLabel;
	private JPanel mButtonPanel;
	private JButton mRefreshButton;
	private JButton mNewGameButton;
	private JButton mExitButton;
	
	private Board mBoard;
	private Fifteen mFifteen;
	private FifteenThread mThread;

	public MessageDialog(Board board, Fifteen fifteen) {
		mBoard = board;
		mFifteen = fifteen;
		
		setModal(true);
		setSize(500, 120);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Решатель пятнашек");
		
		mMainPanel = new JPanel();
		mMainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mMainPanel.setLayout(new BoxLayout(mMainPanel, BoxLayout.Y_AXIS));
		add(mMainPanel);
		
		mLabel = new JLabel();
		mLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		mMainPanel.add(mLabel);
		mMainPanel.add(Box.createRigidArea(new Dimension(1, 20)));
		
		mButtonPanel = new JPanel();
		mButtonPanel.setLayout(new BoxLayout(mButtonPanel, BoxLayout.X_AXIS));
		mMainPanel.add(mButtonPanel);
		
		mRefreshButton = new JButton("Собрать еще раз");
		mButtonPanel.add(mRefreshButton);
		mButtonPanel.add(Box.createRigidArea(new Dimension(20, 1)));
		
		mNewGameButton = new JButton("Создать случайное поле");
		mButtonPanel.add(mNewGameButton);
		mButtonPanel.add(Box.createRigidArea(new Dimension(20, 1)));
		
		mExitButton = new JButton("Выход");
		mButtonPanel.add(mExitButton);

		//событие нажатия на кнопку "Собрать еще раз"
		mRefreshButton.addActionListener(arg -> {
			//создаем визуальные объекты
			mFifteen.createField();
			mThread.show();
			setVisible(false);
		});
		
		//событие нажатия на кнопку "Новая игра"
		mNewGameButton.addActionListener(arg -> {
			//заполняем поле случайными значениями
			mBoard.createRandomArray();
			//создаем визуальные объекты
			mFifteen.createField();
			setVisible(false);
		});
		
		//событие нажатия на кнопку "Выход"
		mExitButton.addActionListener(arg -> {
			System.exit(0);
		});
	}
	
	/**
	 * сохраняет ссылку на поток
	 * @param thread
	 */
	public void setThread(FifteenThread thread){
		mThread = thread;
	}
	
	/**
	 * показывает диалог с сообщением msg 
	 * @param msg - строка для отображения
	 */
	public void show(String msg){
		mLabel.setText(msg);
		setVisible(true);
	}
	
}
