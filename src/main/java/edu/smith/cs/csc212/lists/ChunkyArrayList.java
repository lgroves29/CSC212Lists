package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.EmptyListError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	/**
	 * How big is each chunk?
	 */
	private int chunkSize;
	/**
	 * Where do the chunks go?
	 */
	private GrowableList<FixedSizeList<T>> chunks;

	/**
	 * Create a ChunkedArrayList with a specific chunk-size.
	 * @param chunkSize - how many items to store per node in this list.
	 */
	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}

	@Override
	public T removeFront() {
		if (chunks.getFront().size() == 0) {
			chunks.removeFront();
		}
		if (chunks.size()== 0) {
			throw new EmptyListError();
		}
		else {
			return chunks.getFront().removeFront();
		}
	}

	@Override
	public T removeBack() {
		if (chunks.getBack().size() == 0) {
			chunks.removeBack();
		}
		return chunks.getBack().removeBack();
	}

	@Override
	public T removeIndex(int index) {
		int count = 0;
		T value = null;
		if (this.size()== 0) {
			throw new EmptyListError();
		}
		if (index < 0 || index >= this.size()) {
			throw new BadIndexError(index);
		}
		for (FixedSizeList<T> chunk : this.chunks) {
			int listIndex = 0;
			for (T entry : chunk) {
				if (count == index) {
					value = chunk.removeIndex(listIndex);
				}
				count ++;
				listIndex ++;
			}
		}
		return value;
	}

	@Override
	public void addFront(T item) {
		if (chunks.size() == 0) {
			chunks.addFront(makeChunk());
		}
			this.addIndex(0, item);
				
	}

	@Override
	public void addBack(T item) {
		if (chunks.isEmpty()) {
			this.addFront(item);
		}
		else if (chunks.getBack().size() == this.chunkSize) {
			chunks.addBack(makeChunk());
			chunks.getBack().addBack(item);
		}
		else {
			chunks.getBack().addBack(item);
		}
	}

	@Override
	public void addIndex(int index, T item) {
		// THIS IS THE HARDEST METHOD IN CHUNKY-ARRAY-LIST.
		// DO IT LAST.
		
		int chunkIndex = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index <= end) {
				if (chunk.isFull()) {
					// check can roll to next
					// or need a new chunk
					if (index == end) {
						chunks.getIndex(chunkIndex + 1).addFront(item);
					}
					if (chunkIndex + 1 == chunks.size()) {
						chunks.addBack(makeChunk());
					}
					if (chunks.getIndex(chunkIndex+1).size() == chunkSize) {
						chunks.addBack(makeChunk());
						T back = chunk.removeBack();
						chunks.getIndex(chunkIndex+1).addFront(back);
						chunk.addIndex(index-start, item);

					}
					else {
						T back = chunk.removeBack();
						chunks.getIndex(chunkIndex+1).addFront(back);
						chunk.addIndex(index-start, item);
					}
				
				} 
				else {
					// put right in this chunk, there's space.
					chunk.addIndex(index-start, item);
					
				}	
				// upon adding, return.
				return;
			}
			
			// update bounds of next chunk.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		return this.chunks.getBack().getBack();
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		if (index < 0 || index  >=  this.size()) {
			throw new BadIndexError(index);
		}
		int count = 0;
		for (FixedSizeList<T> chunk : this.chunks){
			int listCount = 0;
			for (T entry : chunk) {
				if (count == index) {
					chunk.setIndex(listCount, value);
					return;
				}
				count ++;
				listCount ++;
			}
		}
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
}