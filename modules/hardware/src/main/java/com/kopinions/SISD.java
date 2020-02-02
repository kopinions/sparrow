package com.kopinions;

import static java.util.stream.Collectors.toList;

import com.kopinions.MMU.PTE;
import com.kopinions.core.Bus;
import com.kopinions.core.CPU;
import com.kopinions.core.CPU.Interrupter.Type;
import com.kopinions.core.Decoder;
import com.kopinions.core.Instruction;
import com.kopinions.core.Timer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

// Single Instruction Single Data
public class SISD implements CPU {

  short eip;
  short esp;
  short ebx;
  short ecx;
  short edx;
  short esi;
  short edi;
  short ebp;
  short cr3;
  Decoder<Short> decoder;
  MMU mmu;
  private Bus bus;


  public SISD(MMU mmu, Bus bus) {
    this.mmu = mmu;
    this.bus = bus;
  }

  @Override
  public void tick() {
    short fetch = fetch();
    Instruction decode = decoder.decode(fetch);
    execute(decode);
  }

  @Override
  public Interrupter interrupter() {
    return new Interrupter() {
      private Map<Type, Runnable> isrs = new HashMap<>();

      @Override
      public void interrupt(Type type) {
        isrs.get(type).run();
      }

      @Override
      public void on(Type type, Runnable isr) {
        isrs.put(type, isr);
      }
    };
  }

  private short fetch() {
    List<PTE> ptes = IntStream.range(0, 128).mapToObj(i -> bus.read(new Address(cr3 + i * 2)))
        .map(PTE::new)
        .collect(toList());
    mmu.update(ptes);
    Address translate = mmu.translate(new Address(eip));
    this.eip += 2;
    return bus.read(translate);
  }


  @Override
  public void execute(Instruction instruction) {
    List<PTE> ptes = IntStream.range(0, 128).mapToObj(i -> bus.read(new Address(cr3 + i * 2)))
        .map(PTE::new)
        .collect(toList());
    mmu.update(ptes);
    instruction.applied(this);
  }

  @Override
  public void poweron() {
    eip = (short) 0;
  }

}
