/*
 * MIT License
 *
 * Copyright (c) 2017 Avery
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.genderquery.seattleparking.arcgis.geometry;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GeometryCollection<E extends Geometry> extends Geometry implements List<E> {

  private final List<E> geometries;

  public GeometryCollection() {
    super();
    geometries = new ArrayList<>();
  }

  public GeometryCollection(@NonNull Collection<E> geometries) {
    super();
    this.geometries = new ArrayList<>(geometries);
  }

  @Override
  public int size() {
    return geometries.size();
  }

  @Override
  public boolean isEmpty() {
    return geometries.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return geometries.contains(o);
  }

  @NonNull
  @Override
  public Iterator<E> iterator() {
    return geometries.iterator();
  }

  @NonNull
  @Override
  public Object[] toArray() {
    return geometries.toArray();
  }

  @SuppressWarnings("SuspiciousToArrayCall")
  @NonNull
  @Override
  public <T> T[] toArray(@NonNull T[] a) {
    return geometries.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return geometries.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return geometries.remove(o);
  }

  @Override
  public boolean containsAll(@NonNull Collection<?> c) {
    return geometries.containsAll(c);
  }

  @Override
  public boolean addAll(@NonNull Collection<? extends E> c) {
    return geometries.addAll(c);
  }

  @Override
  public boolean addAll(int index, @NonNull Collection<? extends E> c) {
    return geometries.addAll(c);
  }

  @Override
  public boolean removeAll(@NonNull Collection<?> c) {
    return geometries.removeAll(c);
  }

  @Override
  public boolean retainAll(@NonNull Collection<?> c) {
    return geometries.retainAll(c);
  }

  @Override
  public void clear() {
    geometries.clear();
  }

  @Override
  public E get(int index) {
    return geometries.get(index);
  }

  @Override
  public E set(int index, E element) {
    return geometries.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    geometries.add(index, element);
  }

  @Override
  public E remove(int index) {
    return geometries.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return geometries.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return geometries.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return geometries.listIterator();
  }

  @NonNull
  @Override
  public ListIterator<E> listIterator(int index) {
    return geometries.listIterator(index);
  }

  @NonNull
  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return geometries.subList(fromIndex, toIndex);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GeometryCollection)) {
      return false;
    }
    GeometryCollection<?> that = (GeometryCollection<?>) o;
    return geometries.equals(that.geometries);
  }

  @Override
  public int hashCode() {
    return geometries.hashCode();
  }
}
