import java.util.List;
import java.util.ListIterator;

import javax.swing.JLabel;

/**
 * Поток, выполняющий поиск решения и отображающий его
 */
public class FifteenThread extends Thread{

	private ListIterator<Byte> mIterator; //итератор для итерирования по списку передвижений
	private JLabel mStatusLabel;
	private MessageDialog mMessageDialog;
	private Calc mCalc;
	private Label[][] mLabels;
	
	private boolean toCalc = false;
	private boolean toShow = false;
	
	private List<Byte> mResList;
	private boolean mRes;
	private int zeroX, zeroY, newZeroX, newZeroY; //координаты двух обмениваемых клеток
	
	public FifteenThread(JLabel statusLabel, MessageDialog messageDialog,
			Calc calc, Label[][] labels) {
		super();
		setDaemon(true);
		mStatusLabel = statusLabel;
		mMessageDialog = messageDialog;
		mCalc = calc;
		mLabels = labels;
		
		start();
	}
	
	@Override
	public void run() {
		while (true){
			try{
				FifteenThread.sleep(500);
			}catch(InterruptedException e){}
			//вычисление
			if (toCalc){
				mStatusLabel.setText("Состояние: поиск решения... Подождите.");
				mRes = mCalc.findSolution(); //поиск решения
				mResList = mCalc.getResult();
				toShow = true;
				toCalc = false;
			}
			//отображение
			if (toShow){
				mStatusLabel.setText("Состояние: отображение решения.");
				mIterator = mResList.listIterator(0);
				//ищем пустое место на поле
				for (int i=0; i<Fifteen.SIDE_SIZE; i++)
					for (int j=0; j<Fifteen.SIDE_SIZE; j++)
						if (mLabels[i][j] == null){
							zeroX = j;
							zeroY = i;
						}
				//запускаем анимацию перемещения
				while(true){
					try{
						FifteenThread.sleep(100);
					}catch(InterruptedException e){}
					if (!mIterator.hasNext()) //если пятнашки собраны, то выход из цикла
						break;
					newZeroX = zeroX;
					newZeroY = zeroY;
					switch (mIterator.next()) { //получаем координаты перемещаемой костяшки
						case Calc.RIGHT:
							newZeroX++;
							break;
						case Calc.UP:
							newZeroY--;
							break;
						case Calc.LEFT:
							newZeroX--;
							break;
						case Calc.DOWN:
							newZeroY++;
							break;
					}
					//считаем значения, на которые будет передвигаться костяшка в цикле
					int dx = ((zeroX - newZeroX)*Fifteen.CELL_SIZE)/50;
					int dy = ((zeroY - newZeroY)*Fifteen.CELL_SIZE)/50;
					//координаты костяшки в пикселях
					int startX = mLabels[newZeroY][newZeroX].getX();
					int startY = mLabels[newZeroY][newZeroX].getY();
					//передвижение костяшки
					for (int i=1; i<51; i++){
						try{
							Thread.sleep(Fifteen.MOVE_TIME/50);
						}catch(InterruptedException e){}
						mLabels[newZeroY][newZeroX].setLocation(startX+i*dx, startY+i*dy);
					}
					//меняем местами костяшку и пустое поле
					mLabels[zeroY][zeroX] = mLabels[newZeroY][newZeroX];
					mLabels[newZeroY][newZeroX] = null;
					//запоминаем новые координаты
					zeroX = newZeroX;
					zeroY = newZeroY;
				}
				mStatusLabel.setText("Состояние: готов.");
				toShow = false;
				if (mRes)
					mMessageDialog.show("Задача решена. Может еще разок?");
				else
					mMessageDialog.show("Эта задача не имеет решения. Может еще разок?");
			}
		}
	}
	
	public void show(){
		if (!toShow)
			toShow = true;
	}
	
	public void calcAndShow(){
		if (!toCalc)
			toCalc = true;
	}
	
}
