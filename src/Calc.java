import java.util.LinkedList;

/**
 * Алгоритм IDA* для решения пятнашек
 */
public class Calc {
	
	//направления возможных ходов
	public static final byte RIGHT = 0;
	public static final byte UP = 1;
	public static final byte LEFT = 2;
	public static final byte DOWN = 3;

	private Board mBoard;
	private int mMaxDepth;
	private LinkedList<Byte> mResultList;
	private boolean hasSolution;

	public Calc(Board board) {
		mBoard = board;
		mResultList = new LinkedList<Byte>();
	}
	
	/**
	 * проверяет, возможно ли решить задачу
	 * @return true, если задача решаема
	 */
	private boolean hasSolution(){
		//поиск строки, содержащей 0-клетку
		int y = 0;
		for (int i=0; i<mBoard.getLength(); i++)
			if (mBoard.get(i) == 0)
				y = i/mBoard.getSideSize()+1;
		//подсчет количества пар, находящихся в состоянии инверсии
		int n = 0;
		for (int i=0; i<mBoard.getLength(); i++) {
			byte temp = mBoard.get(i);
			for (int j=i+1; j<mBoard.getLength(); j++)
				if (mBoard.get(j) != 0 && temp > mBoard.get(j))
					n++;
		}
		//если размер стороны четный, то необходимо проверить на четность n+y
		if (mBoard.getSideSize() % 2 == 0){
			if ((n+y)%2 == 0)
				return true;
			else
				return false;
		}else{ //иначе необходимо проверить на четность только n
			if (n%2 == 0)
				return true;
			else
				return false;
		}
	}
	
	/**
	 * меняет местами клетку с 0-клеткой и вызывает для нее процедуру поиска
	 * @param depth - глубина рекурсии
	 * @param zeroX - x координата 0
	 * @param zeroY - y координата 0
	 * @param x - x координата клетки для обмена местами с нулем
	 * @param y - y координата клетки для обмена местами с нулем
	 * @param where - направление
	 * @return найдено ли решение
	 */
	private boolean goToNeighbor(int depth, int zeroX, int zeroY, int x, int y,
			byte where){
		//проверяем на выход за границу
		if (x < 0 || x >= mBoard.getSideSize() || y < 0 || y >= mBoard.getSideSize())
			return false;
		mBoard.swap(zeroX, zeroY, x, y); //меняем местами клетки
		boolean res = ida(depth+1, where, x, y); //вызываем метод для новой позиции
		mBoard.swap(zeroX, zeroY, x, y); //возвращаем клетки на свои места
		if (res){ //если решение найдено
			mResultList.addFirst(where); //добавляем направление движения в список
			return true; 
		}
		return false;
	}
	
	/**
	 * 
	 * @param depth - глубина рекурсии
	 * @param where - направление
	 * @param x - x координата 0
	 * @param y - y координата 0
	 * @return
	 */
	private boolean ida(int depth, byte where, int zeroX, int zeroY){
		int h = mBoard.getH(hasSolution);
		//если цель найдена, то выход
		if (h == 0)
			return true;
		//если глубина рекурсии + оценка будущего пути больше допустимой величины, то выход
		if (depth+h > mMaxDepth)
			return false;
	
		//ход вправо
		if (where != LEFT)
			if (goToNeighbor(depth, zeroX, zeroY, zeroX+1, zeroY, RIGHT))
				return true;
		//ход вверх
		if (where != DOWN)
			if (goToNeighbor(depth, zeroX, zeroY, zeroX, zeroY-1, UP))
				return true;
		//ход влево
		if (where != RIGHT)
			if (goToNeighbor(depth, zeroX, zeroY, zeroX-1, zeroY, LEFT))
				return true;
		//ход вниз
		if (where != UP)
			if (goToNeighbor(depth, zeroX, zeroY, zeroX, zeroY+1, DOWN))
				return true;
		return false;
	}
	
	/**
	 * ищет решение
	 * @return true, если решение существует
	 */
	public boolean findSolution(){
		mResultList.clear();
		hasSolution = hasSolution();
		mMaxDepth = mBoard.getH(hasSolution); //начинаем с h для начального состояния
		//в цикле увеличивается максимальная глубина рекурсии и поиск начинается заново,
		//пока не завершится успешно
		while (true){
			int zeroInd = mBoard.find((byte)0); //ищем пустую клетку
			if (ida(0, (byte)-1, zeroInd%mBoard.getSideSize(),
					zeroInd/mBoard.getSideSize()))
				return hasSolution;
			mMaxDepth += 2; //увеличиваем максимальную глубину
		}
	}
	
	/**
	 * @return список направлений
	 */
	public LinkedList<Byte> getResult(){
		return mResultList;
	}
	
}
