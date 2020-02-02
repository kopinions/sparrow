package com.kopinions.kernel;

import java.util.List;
import java.util.Queue;

public interface Selector<T> {
  List<T> applied(Queue<T> elements);
}
