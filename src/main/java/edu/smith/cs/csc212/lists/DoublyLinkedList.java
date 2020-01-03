package edu.smith.cs.csc212.lists;


import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * A Doubly-Linked List is a list based on nodes that know of their successor and predecessor.
 * @author jfoley
 *
 * @param <T>
 */
public class DoublyLinkedList<T> extends ListADT<T> {
	/**
	 * This is a reference to the first node in this list.
	 */
	private Node<T> start;
	/**
	 * This is a reference to the last node in this list.
	 */
	private Node<T> end;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}
	

	@Override
	public T removeFront() {
		checkNotEmpty();
		T value = this.start.value;
		Node<T> newFront = this.start.after;
		this.start = newFront;
		//this.start.before = null;
		return value;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T value = this.getBack();
		if (this.size() == 1) {
			removeFront();
		}
		else {
			Node<T> newBack = null;
			for (Node<T> current = this.start; current.after != null; current = current.after) {
				newBack = current;
			}
			newBack.after = null;
		}
		return value;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		if (index == 0) {
			T value = removeFront();
			return value;
		}
		else {
			T value = this.getIndex(index);
			Node<T> previous = this.start;
			for (int count = 0; count < index -1; count ++) {
				previous =  previous.after;
			}
			Node <T> after = previous.after.after;
			previous.after = after;
			//after.before = previous;
			return value;
		}
	}

	@Override
	public void addFront(T item) {
		if (this.size() == 0) {
			this.start = new Node<T> (item);
		}
		else {
			Node<T> newStart = new Node<T> (item);
			newStart.after = this.start;
			this.start.before = newStart;
			this.start = this.start.before;
		}
	}

	@Override
	public void addBack(T item) {
		if (end == null) {
			start = end = new Node<T>(item);
		} else {
			Node<T> secondLast = end;
			end = new Node<T>(item);
			end.before = secondLast;
			secondLast.after = end;
		}
	}

	@Override
	public void addIndex(int index, T item) {
		if (index > this.size() || index < 0) {
			throw new BadIndexError(index);
		}
		if (index == 0) {
			addFront(item);
		}
		else {
			Node<T> previous = this.start;
			for (int count = 0; count < index -1; count ++) {
				previous =  previous.after;
			}
			Node<T> newEntry = new Node<T> (item);
			Node<T> next = previous.after;
			previous.after = newEntry;
			newEntry.before = previous;
			newEntry.after = next;
			if (next != null) {
				next.before = newEntry;
			}
			
		}
		
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		Node<T> back = null;
		for (Node<T> current = this.start; current != null; current = current.after) {
			back = current;
		}
		return back.value;
	}
	
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.after) {
			if (count == index) {
				return n.value;
			}
			count ++;
		}
		throw new BadIndexError(index);
	}
	
	public void setIndex(int index, T value) {
		checkNotEmpty();
		if (index >= this.size() || index < 0) {
			throw new BadIndexError(index);
		}
		Node<T> element = this.start;
		for (int count = 0; count < index; count++) {
			element = element.after;
		}
		element.value = value;
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.after) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null && this.end == null;
	}
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}
