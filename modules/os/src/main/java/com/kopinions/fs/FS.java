package com.kopinions.fs;

import com.kopinions.fs.FS.File.Operation;

public interface FS {

  interface Operations {

    Operation<byte[]> read(int size);

    Operation<Void> write(byte[] data);

    Operation<Void> close();
  }

  class File {
    int location;
    int offset;
    Status status;
    Operations operations;

    enum Status {
      NONE, INIT, OPENED, CLOSED
    }

    public File(int location, Operations operations) {
      this.location = location;
      offset = 0;
      status = Status.OPENED;
      this.operations = operations;
    }

    public interface Operation<T> {
      T applied(File file);
    }

    public byte[] read(int size) {
      return operations.read(size).applied(this);
    }

    public void write(byte[] data) {
      operations.write(data).applied(this);
    }

    public void close() {
      operations.close().applied(this);
    }

  }

  File open(int location);

}
