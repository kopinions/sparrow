package com.kopinions.apps.monitors;

import com.kopinions.apps.utils.Json;
import java.lang.reflect.Field;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Monitor implements Runnable {

  private Map<String, Object> disk_info;
  private Map<String, Object> proc;
  private Map<String, Object> job;
  private Map<String, Object> mem_info;

  public Monitor() {

  }

  public void apply(ChangeSet cs) {
    cs.applied(this);
  }

  @Override
  public void run() {
    JFrame f = new JFrame();//creating instance of JFrame

    JButton b = new JButton("click");//creating instance of JButton
    b.setBounds(130, 100, 100, 40);//x axis, y axis, width, height


    f.add(b);//adding button in JFrame

    JLabel jLabel = new JLabel();
    f.add(jLabel);

    f.setSize(400, 500);//400 width and 500 height
    f.setLayout(null);//using no layout managers
    f.setVisible(true);//making the frame visible
    while (true) {
      try {
        System.out.println(Json.toJson(disk_info));
        System.out.println(Json.toJson(mem_info));
        System.out.println(Json.toJson(proc));
        System.out.println(Json.toJson(job));
        jLabel.setText(Json.toJson(disk_info));
        jLabel.setBounds(130, 200, 100, 40);
        f.validate();
        f.repaint();
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public interface ChangeSet {
    void applied(Monitor m);
  }

  public static class JsonChangeSet implements ChangeSet {
    private String field;
    private Map<String, Object> status;

    public JsonChangeSet(String field, Map<String, Object> status) {
      this.field = field;
      this.status = status;
    }

    @Override
    public void applied(Monitor m) {
      try {
        Field field = m.getClass().getDeclaredField(this.field);
        field.set(m, status);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
