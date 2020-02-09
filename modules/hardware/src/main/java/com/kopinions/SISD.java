package com.kopinions;

import static java.util.Optional.ofNullable;

import com.kopinions.core.Bus;
import com.kopinions.core.CPU;
import com.kopinions.core.Decoder;
import com.kopinions.core.Instruction;
import com.kopinions.core.Registry;
import com.kopinions.core.Registry.Name;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Single Instruction Single Data
public class SISD implements CPU {
  Decoder<Short> decoder;
  MMU mmu;
  private Bus bus;
  private Interrupter interrupter;
  private Registry registry;


  public SISD(Bus bus) {
    this.mmu = new MMU(this, bus);
    this.mmu.bus = bus;
    this.mmu.cpu = this;
    this.bus = bus;
    interrupter = new Interrupter() {
      private Map<Type, List<Runnable>> isrs = new HashMap<>();

      @Override
      public void interrupt(Type type) {
        ofNullable(isrs.get(type)).ifPresent(i -> i.forEach(Runnable::run));
      }

      @Override
      public void on(Type type, Runnable isr) {
        List<Runnable> v = isrs.getOrDefault(type, new ArrayList<>());
        v.add(isr);
        isrs.put(type, v);
      }
    };
    registry = new Registry() {
      Map<Name, Short> registry = new HashMap<>() {{
        put(Name.CR1, (short) 0);
        put(Name.CR2, (short) 0);
        put(Name.CR3, (short) 0);
        put(Name.EIP, (short) 0);
      }};

      @Override
      public synchronized short get(Name name) {
        return registry.get(name);
      }

      @Override
      public synchronized void set(Name name, short value) {
        registry.put(name, value);
      }
    };
  }

  @Override
  public void tick() {
    short fetch = fetch();
    Instruction decode = decoder.decode(fetch);
    execute(decode);
  }

  @Override
  public Interrupter interrupter() {
    return interrupter;
  }

  private short fetch() {
    short eip = registry.get(Name.EIP);
    Address translate = mmu.translate(new Address(eip));
    registry.set(Name.EIP, (short) (eip + 2));
    return bus.read(translate);
  }


  @Override
  public void execute(Instruction instruction) {
    instruction.applied(this);
  }

  @Override
  public void poweron() {
    registry.set(Name.EIP, (short) 0);
  }

  @Override
  public MMU mmu() {
    return mmu;
  }

  @Override
  public Registry registry() {
    return registry;
  }
}
