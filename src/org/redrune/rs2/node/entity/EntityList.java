package org.redrune.rs2.node.entity;

import org.redrune.rs2.node.entity.npc.NPC;
import org.redrune.rs2.node.entity.player.Player;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EntityList<T extends Entity> implements Iterable<T> {
	
	/**
	 * The array of entities
	 */
	private T[] entities;
	
	/**
	 * The lowest free index available
	 */
	private int lowestFreeIndex;
	
	/**
	 * The size of entities
	 */
	private int size;
	
	@SuppressWarnings("unchecked")
	public EntityList(int capacity, boolean player) {
		entities = (T[]) (player ? new Player[capacity] : new NPC[capacity]);
	}
	
	/**
	 * Adds an entity to the list
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean add(T entity) {
		synchronized (this) {
			entity.setIndex(lowestFreeIndex + 1);
			entities[lowestFreeIndex] = entity;
			size++;
			for (int i = lowestFreeIndex + 1; i < entities.length; i++) {
				if (entities[i] == null) {
					lowestFreeIndex = i;
					break;
				}
			}
			return true;
		}
	}
	
	/**
	 * Removes an entity to the list
	 *
	 * @param entity
	 * 		The entity
	 */
	public void remove(T entity) {
		synchronized (this) {
			int listIndex = entity.getIndex() - 1;
			entities[listIndex] = null;
			size--;
			if (listIndex < lowestFreeIndex) {
				lowestFreeIndex = listIndex;
			}
		}
	}
	
	/**
	 * Gets an entity from the list
	 *
	 * @param index
	 * 		The entity
	 */
	public T get(int index) {
		if (index >= entities.length || index == 0) {
			return null;
		}
		return entities[index - 1];
	}
	
	/**
	 * If the list contains an entity
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean contains(T entity) {
		return entity.getIndex() != 0 && entities[entity.getIndex() - 1] == entity;
	}
	
	/**
	 * Gets the size of the entities
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Converts the array to a {@code Stream} {@code Object}
	 */
	public Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new EntityIterator();
	}
	
	private final class EntityIterator implements Iterator<T> {
		
		/**
		 * The previous index of this iterator.
		 */
		private int previousIndex = -1;
		
		/**
		 * The current index of this iterator.
		 */
		private int index = 0;
		
		@Override
		public boolean hasNext() {
			for (int i = index; i < entities.length; i++) {
				if (entities[i] != null) {
					index = i;
					return true;
				}
			}
			return false;
		}
		
		@Override
		public T next() {
			T entity = null;
			for (int i = index; i < entities.length; i++) {
				if (entities[i] != null) {
					entity = entities[i];
					index = i;
					break;
				}
			}
			if (entity == null) {
				throw new NoSuchElementException();
			}
			previousIndex = index;
			index++;
			return entity;
		}
		
		@Override
		public void remove() {
			if (previousIndex == -1) {
				throw new IllegalStateException();
			}
			EntityList.this.remove(entities[previousIndex]);
			previousIndex = -1;
		}
		
	}
	
}