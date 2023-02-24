/*
 * Copyright (C) 2008  RS2DBase Development team
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.redrune.game.entity.actor;

import java.util.Iterator;
import java.util.Set;

public class ActorListIterator<E extends Actor> implements Iterator<E> {

    private final Integer[] indicies;

    private final Object[] entities;

    @SuppressWarnings("rawtypes")
    private final ActorList actorList;

    private int curIndex = 0;

    public ActorListIterator(Object[] entities, Set<Integer> indicies, @SuppressWarnings("rawtypes") ActorList actorList) {
        this.entities = entities;
        this.indicies = indicies.toArray(new Integer[indicies.size()]);
        this.actorList = actorList;
    }

    public boolean hasNext() {
        return indicies.length != curIndex;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        Object temp = entities[indicies[curIndex]];
        curIndex++;
        return (E) temp;
    }

    public void remove() {
        if (curIndex >= 1) {
            actorList.remove(indicies[curIndex - 1]);
        }
    }
}
