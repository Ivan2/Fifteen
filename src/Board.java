import java.util.Random;

/**
 * Хранит поле и производит операции над ним.
 */
public class Board {

	private byte[] mArray; //массив для хранения поля
	private int mSideSize; //размер стороны
	private int mLength; //размер массива
	
	/**
	 * @param sideSize - размер стороны поля
	 */
	public Board(int sideSize) {
		mSideSize = sideSize;
		mLength = mSideSize*mSideSize;
		mArray = new byte[mLength];
	}
	
	/**
	 * создает копию объекта
	 * @return копия поля
	 */
	public void copyTo(Board board){
		for (int i=0; i<mLength; i++)
			board.mArray[i] = mArray[i];
	}
	
	/**
	 * @return размер стороны поля
	 */
	public int getSideSize(){
		return mSideSize;
	}
	
	/**
	 * @return размер массива (поля)
	 */
	public int getLength(){
		return mLength;
	}
	
	/**
	 * @param hasSolution определяет метод вычисления оценки. Если он false, то
	 * оценка будет считаться с учетом того, что последние два числа поменяны местами.
	 * @return эвристическую оценку текущего состояния поля (манхэттенское расстояние)
	 */
	public int getH(boolean hasSolution){
		int h = 0;
		for (int i=0; i<mLength; i++)
			if (mArray[i] > 0){
				int x = i % mSideSize;
				int y = i / mSideSize;
				int goalX = (mArray[i]-1) % mSideSize;
				int goalY = (mArray[i]-1) / mSideSize;
				if (mArray[i] == mLength-2 && !hasSolution){
					goalX = mArray[i] % mSideSize;
					goalY = mArray[i] / mSideSize;
				}
				if (mArray[i] == mLength-1 && !hasSolution){
					goalX = (mArray[i]-2) % mSideSize;
					goalY = (mArray[i]-2) / mSideSize;
				}
				h += Math.abs(x - goalX);
				h += Math.abs(y - goalY);
			}
		return h;
	}
	
	/**
	 * генерирует случайное поле
	 */
	public void createRandomArray(){
		for (int i=0; i<mLength-1; i++)
			mArray[i] = (byte)(i+1);
		mArray[mLength-1] = 0;
		
		Random rand = new Random();
		for (int i=0; i<mLength-2; i++){
			int ind = rand.nextInt(mLength-1-i)+i+1;
			swap(i, ind);
		}
	}
	
	/**
	 * меняет местами два элемента с индексами ind1 и ind2
	 * @param ind1
	 * @param ind2
	 */
	public void swap(int ind1, int ind2){
		byte temp = mArray[ind1];
		mArray[ind1] = mArray[ind2];
		mArray[ind2] = temp;
	}

	/**
	 * меняет местами два элемента с координатами (x1, y1) и (x2, y2)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void swap(int x1, int y1, int x2, int y2){
		swap(y1*mSideSize+x1, y2*mSideSize+x2);
	}
	
	/**
	 * возвращает элемент с индексом ind1
	 * @param ind
	 * @return элемент с индексом ind1
	 */
	public byte get(int ind){
		return mArray[ind];
	}

	/**
	 * возвращает элемент с координатами (x, y)
	 * @param x
	 * @param y
	 * @return элемент с координатами (x, y)
	 */
	public byte get(int x, int y){
		return mArray[y*mSideSize+x];
	}
	
	/**
	 * ищет элемент v в массиве
	 * @param v
	 * @return индекс элемента v в массиве
	 */
	public int find(byte v){
		for (int i=0; i<mLength; i++)
			if (mArray[i] == v)
				return i;
		return -1;
	}

	/**
	 * возвращает строковое представление объекта
	 */
	@Override
	public String toString() {
		String str = "";
		for (int i=0; i<mLength; i++){
			if (i % mSideSize == 0 && i != 0)
				str += "\n";
			str += Byte.toString(mArray[i])+" ";
		}
		str += "\n";
		return str;	
	}
	
}
