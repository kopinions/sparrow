package com.kopinions.fs;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;

import com.kopinions.core.Disk;
import com.kopinions.fs.FS.File.Operation;
import com.kopinions.fs.FS.File.Status;
import com.kopinions.kernel.Kernel;
import java.nio.ByteBuffer;

public class SwapFS implements FS {
  private final Operations operations;
  private final Disk disk;

  public SwapFS(Disk disk) {
    this.disk = disk;
    operations = new Operations() {

      @Override
      public Operation<byte[]> read(int size) {
        return new ReadOperation(size);
      }

      @Override
      public Operation<Void> write(byte[] data) {
        return new WriteOperation(data);
      }

      @Override
      public Operation<Void> close() {
        return new CloseOperation();
      }
    };
  }

  @Override
  public File root() {
    return new File(Kernel.HDD_SWAP_ROOT, operations);
  }

  @Override
  public File open(int location) {
    return null;
  }

  private class WriteOperation implements Operation<Void> {

    private byte[] data;

    public WriteOperation(byte[] data) {
      this.data = data;
    }

    @Override
    public Void applied(File file) {
      ByteBuffer wrap = wrap(data);
      short[] array = wrap.asShortBuffer().array();
      for (int i = 0; i < array.length; i++) {
        disk.write(file.location + i * 2, array[i]);
      }
      return null;
    }
  }

  private class ReadOperation implements Operation<byte[]> {

    private int size;

    public ReadOperation(int size) {
      this.size = size;
    }

    @Override
    public byte[] applied(File file) {
      ByteBuffer allocate = allocate(size);
      for (int i = 0; i < size; i += 2) {
        short read = disk.read(file.location + i);
        ByteBuffer byteBuffer = allocate(2).putShort(read);
        allocate.put(byteBuffer.slice(0, Math.min(size, 2)));
      }
      file.offset += size;
      return allocate.array();
    }
  }

  private static class CloseOperation implements Operation<Void> {

    @Override
    public Void applied(File file) {
      file.status = Status.CLOSED;
      return null;
    }
  }
}
