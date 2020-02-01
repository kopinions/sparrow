package com.kopinions.kernel;

import java.util.Queue;

public interface Selector<T> {

  T applied(Queue<T> jobs);
}
