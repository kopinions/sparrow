package com.kopinions.kernel;

public class Kernel {

  public static final int PAGE_SIZE = 512;
  public static final int MEM_SIZE = 32 * 1024;
  public static final int MEM_USERSPACE_SIZE = 16 * 1024;
  public static final int MEM_KERNEL_SIZE = 16 * 1024;

  public static final int HDD_SIZE = 1024 * 1024;
  public static final int HDD_SWAP_SIZE = 64 * 1024;
  public static final int HDD_SYS_SIZE = 16 * 1024;
  public static final int HDD_FS_ROOT = HDD_SWAP_SIZE + HDD_SYS_SIZE;
}
