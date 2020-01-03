package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * A Singly-Linked List is a list that has only knowledge of its very first
 * element. Elements after that are chained, ending with a null node.
 * 
 * @author jfoley
 *
 * @param <T> - the type of the item stored in this list.
 */
public class SinglyLinkedList<T> extends ListADT<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start;

	@Override
	public T removeFront() {
		checkNotEmpty();
		/**
		 * save the start value
		 */
		T value = this.start.value;
		/**
		 * the second value is now the first value
		 */
		this.start = this.start.next;
		return value;
	}

	@Override
	public T removeBack() {
		T value = this.getBack();
		if (this.size() == 1) {
			removeFront();
		}
		else {
			Node<T> newBack = null;
			for (Node<T> current = this.start; current.next != null; current = current.next) {
				newBack = current;
			}
			newBack.next = null;
		}
		return value;
		
	}

	@Override
	public T removeIndex(int index) {
		if (index == 0) {
			T value = removeFront();
			return value;
		}
		else {
			T value = this.getIndex(index);
			Node<T> previous = this.start;
			for (int count = 0; count < index -1; count ++) {
				previous =  previous.next;
			}
			previous.next = previous.next.next;
			return value;
		}
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
		
	}

	@Override
	public void addBack(T item) {
		if (this.start == null) {
			this.addFront(item);
		}
		else {
			Node<T> last = null;
			for (Node<T> current = this.start; current != null; current = current.next) {
				last = current;
			}
			last.next = new Node<T>(item, null);
		}
	}

	@Override
	public void addIndex(int index, T item) {
		checkInclusiveIndex(index);
		if (index == 0) {
			addFront(item);
		}
		else {
			Node<T> previous = this.start;
			for (int count = 0; count < index -1; count ++) {
				previous =  previous.next;
			}
			Node<T> next = previous.next;
			previous.next = new Node<T> (item, next);
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
		for (Node<T> current = this.start; current != null; current = current.next) {
			back = current;
		}
		return back.value;
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		checkInclusiveIndex(index);
		if (index >= this.size()) {
			throw new BadIndexError(index);
		}
		Node<T> element = this.start;
		for (int count = 0; count < index; count++) {
			element = element.next;
		}
		element.value = value;
		
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value - the value to put in it.
		 * @param next - the successor to this node.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

}
