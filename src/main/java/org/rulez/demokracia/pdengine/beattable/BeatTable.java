package org.rulez.demokracia.pdengine.beattable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BeatTable extends MapMatrix<String, Pair>
    implements ContainingBeats {

  private static final long serialVersionUID = 1L;

  public BeatTable(final BeatTable beatTable) {
    super(beatTable.getKeyCollection());
    for (final String row : getKeyCollection())
      for (final String col : getKeyCollection()) {
        final Pair sourcePair = beatTable.getElement(col, row);
        if (Objects.nonNull(sourcePair))
          setElement(col, row, createPair(sourcePair));
      }
  }

  @Override
  public Pair getElement(final String columnKey, final String rowKey) {
    return Optional.ofNullable(super.getElement(columnKey, rowKey))
        .orElse(new Pair(0, 0));
  }

  private Pair createPair(final Pair sourcePair) {
    return new Pair(sourcePair.getWinning(), sourcePair.getLosing());
  }

  public enum Direction {
    DIRECTION_FORWARD, DIRECTION_BACKWARD
  }

  public BeatTable() {
    this(new ArrayList<String>());
  }

  public BeatTable(final Collection<String> keyCollection) {
    super(keyCollection);
  }

  @Override
  public String toString() {
    String representation = "BeatTable(\nkeys   ";
    final List<String> keys = new ArrayList<>(getKeyCollection());
    Collections.sort(keys);
    for (final String key : keys)
      representation += "|" + key;
    representation += "\n";
    for (final String row : keys) {
      representation += row;
      for (final String column : keys)
        representation += "|  " + getElement(column, row);
      representation += "\n";
    }
    return representation + ")";
  }

  @Override
  public boolean equals(final Object otherThing) {
    if (!(otherThing instanceof BeatTable))
      return false;
    final BeatTable other = (BeatTable) otherThing;
    final Collection<String> keys = getKeyCollection();
    for (final String row : keys)
      for (final String column : keys)
        if (!other.getElement(column, row).equals(getElement(column, row)))
          return false;
    return true;

  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
