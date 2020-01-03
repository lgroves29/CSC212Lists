package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ArrayWrapper;
import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.EmptyListError;
import me.jjfoley.adt.errors.RanOutOfSpaceError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * FixedSizeList is a List with a maximum size.
 * @author jfoley
 *
 * @param <T>
 */
public class FixedSizeList<T> extends ListADT<T> {
	/**
	 * This is the array of fixed size.
	 */
	private ArrayWrapper<T> array;
	/**
	 * This keeps track of what we have used and what is left.
	 */
	private int fill;

	/**
	 * Construct a new FixedSizeList with a given maximum size.
	 * @param maximumSize - the size of the array to use.
	 */
	public FixedSizeList(int maximumSize) {
		this.array = new ArrayWrapper<>(maximumSize);
		this.fill = 0;
	}

	@Override
	public boolean isEmpty() {
		return this.fill == 0;
	}

	@Override
	public int size() {
		return this.fill;
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		this.array.setIndex(index, value);
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		return this.array.getIndex(index);
	}

	@Override
	public T getFront() {
		if (fill == 0) {
			throw new EmptyListError();
		}
		/**
		 * return the value stored at index = 0
		 */
		return this.getIndex(0);
	}

	@Override
	public T getBack() {
		/**
		 * we want to return the value stored at the end of the list
		 */
		return this.getIndex(fill-1);
	}

	@Override
	public void addIndex(int index, T value) {
		// slide to the right
		this.checkInclusiveIndex(index);
		/**
		 * and throw an error if the list is already full
		 */
		if (fill == this.array.size()) {
			throw new RanOutOfSpaceError();
		}
		/**
		*we want every value from our index on to slide one over
		*/
		for (int i = this.size(); i > index; i --) {
			this.array.setIndex(i, this.array.getIndex(i-1));
		}
		this.array.setIndex(index, value);
		fill ++;
		
		
	}

	@Override
	public void addFront(T value) {
		this.addIndex(0, value);
	}

	@Override
	public void addBack(T value) {
		if (fill < array.size()) {
			array.setIndex(fill++, value);
		} else {
			throw new RanOutOfSpaceError();
		}
	}

	@Override
	public T removeIndex(int index) {
		// slide to the left
		/**
		 * don't try to remove if the list is empty!
		 */
		if (fill == 0) {
			throw new EmptyListError();
		}
		/**
		 * don't try to remove something not in the list
		 */
		checkInclusiveIndex(index);
		/**
		 * first save what's at the index
		 */
		T value = this.getIndex(index);
		/**
		 * then pull every other value over one
		 */
		for (int i = index; i < this.size()-1; i++) {
			this.array.setIndex(i, this.array.getIndex(i+1));
		}
		this.array.setIndex((fill-1), null);
		/**
		 * our list is smaller now, so fill shrinks
		 */
		fill -=1;
		return value;
	}

	@Override
	public T removeBack() {
		/**
		 * we already have a function that can remove a value at any index
		 */
		return removeIndex(fill -1);
		
		//throw new TODOErr();
	}

	@Override
	public T removeFront() {
		return removeIndex(0);
	}

	/**
	 * Is this data structure full? Used in challenge: {@linkplain ChunkyArrayList}.
	 * 
	 * @return if true this FixedSizeList is full.
	 */
	public boolean isFull() {
		return this.fill == this.array.size();
	}

}
