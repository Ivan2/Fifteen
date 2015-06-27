import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Слободин Иван
 * Программа ищет решение для случайно сгенерированного состояния пятнашек.
 * Решение ищется методом ветвей и границ с помощью алгоритма IDA*.
 * 
 * Fifteen - графический интерфейс.
 */

public class Fifteen extends JFrame{
	
	private static final long serialVersionUID = 5354642l;

	public static void main(String[] args) {
		new Fifteen();
	}
	
	public static final byte SIDE_SIZE = 4; //размер стороны
	public static final byte LENGTH = SIDE_SIZE*SIDE_SIZE; //размер поля
	public static final byte CELL_SIZE = 100; //размер клетки в пикселях (кратен 50)
	public static final int MOVE_TIME = 300; //время на перемещение одной клетки

	private JPanel mMainPanel;
	private JPanel mGamePanel;
	private JButton startButton;
	private Label[][] mLabels; //матрица визуальных объектов (костяшек)
	private JLabel mStatusLabel;

	private Board mBoard; //объект, хранящий поле и производящий над ним операции
	private Calc mCalc; //алгоритм, выполняющий поиск решения
	private FifteenThread mFifteenThread; //поток для решения задачи и отображения решения
	
	private MessageDialog mMessageDialog; //диалог, отображающий сообщения
	
	public Fifteen() {
		//создание поля
		mBoard = new Board(SIDE_SIZE);
		mBoard.createRandomArray();
		//создание объекта, выполняющего поиск решения
		mCalc = new Calc(mBoard);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Решатель пятнашек");

		mMainPanel = new JPanel();
		mMainPanel.setLayout(new BorderLayout());
		add(mMainPanel);
		
		mGamePanel = new JPanel();
		mGamePanel.setLayout(null);
		mGamePanel.setPreferredSize(new Dimension(CELL_SIZE*SIDE_SIZE,
				CELL_SIZE*SIDE_SIZE));
		mGamePanel.setBackground(Color.WHITE);
		mMainPanel.add(mGamePanel);
		
		mLabels = new Label[SIDE_SIZE][SIDE_SIZE];
		createField();
		
		mStatusLabel = new JLabel("Состояние: готов.");
		mMainPanel.add(mStatusLabel, BorderLayout.SOUTH);

		mMessageDialog = new MessageDialog(mBoard, this);
		mFifteenThread = new FifteenThread(mStatusLabel, mMessageDialog, mCalc,
				mLabels);
		mMessageDialog.setThread(mFifteenThread);

		startButton = new JButton("Решить задачу");
		mMainPanel.add(startButton, BorderLayout.NORTH);
		//событие нажатия на кнопку старта
		startButton.addActionListener(action -> {
			mFifteenThread.calcAndShow();
		});
		
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}
	
	/**
	 * Метод очищает поле от костяшек и заполняет новыми
	 */
	public void createField(){
		mGamePanel.removeAll(); //очищаем поле от предыдущих костяшек
		for (int i=0; i<SIDE_SIZE; i++)
			for (int j=0; j<SIDE_SIZE; j++)
				if (mBoard.get(j, i) != 0){
					mLabels[i][j] = new Label(); //создаем новые костяшек
					mGamePanel.add(mLabels[i][j]);
					mLabels[i][j].setSize(CELL_SIZE, CELL_SIZE);
					mLabels[i][j].setLocation(j*CELL_SIZE, i*CELL_SIZE);
					mLabels[i][j].setText(Byte.toString(mBoard.get(j, i))); //заполняем их числами из поля
				}else //если поле пустое, то объекта не создаем
					mLabels[i][j] = null;
	}
	
}
