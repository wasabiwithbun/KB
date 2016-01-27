/* (swing1.1.1beta2) */
package org.javacoo.cowswing.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JToolTip;
import javax.swing.plaf.basic.BasicArrowButton;

import org.apache.commons.lang.StringUtils;


public class SingleRowTabbedPane extends JTabbedPane implements MouseListener{
  
  public static final String ROTATE      = "Rotate";
  public static final String PREVIOUS    = "Previous";
  public static final String NEXT        = "Next";
  public static final String FIRST       = "First";
  public static final String LEFT_SHIFT  = "Left";
  public static final String RIGHT_SHIFT = "Right";
  public static final String LAST        = "Last";
  
  public static final int ONE_BUTTON   = 1; //                  ROTATE                 ;
  public static final int TWO_BUTTONS  = 2; //          PREVIOUS  |     NEXT           ;
  public static final int FOUR_BUTTONS = 4; // FIRST | LEFT_SHIFT | RIGHT_SHIFT | LAST ;
  
  protected int buttonPlacement;
  protected int buttonCount;
  protected JButton[] tabPaneButtons;
  protected Dimension buttonSize;

  protected int visibleCount;
  protected int visibleStartIndex;
  
  private final int BUTTON_WIDTH  = 16;
  private final int BUTTON_HEIGHT = 17;

	private double scaleRatio = 0.3;  
  private Map<String, Component> tabPanelCache = new ConcurrentHashMap<String, Component>();
  private Map<String,String> tabNameCache = new ConcurrentHashMap<String, String>();
  private Map<String,Integer> tabNameIndexCache = new ConcurrentHashMap<String, Integer>();
  public SingleRowTabbedPane() {
    this(TWO_BUTTONS, RIGHT);
    //this(ONE_BUTTON, RIGHT);
    //this(FOUR_BUTTONS, LEFT);
  }
  
  public SingleRowTabbedPane(int buttonCount, int buttonPlacement) {
	    super();  
	    addMouseListener(this);  
    setButtonPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));    
    tabPaneButtons = createButtons(buttonCount);
    this.buttonPlacement = buttonPlacement;    
    visibleStartIndex=0;
    
    setUI(new SingleRowTabbedPaneUI());
  }
    
  public void setTabPlacement(int tabPlacement) {
    if (tabPlacement == LEFT || tabPlacement == RIGHT) {
      throw new IllegalArgumentException("not suported: LEFT and RIGHT");
    }
    super.setTabPlacement(tabPlacement);
  }
    
  public int getButtonPlacement() {
    return buttonPlacement;
  }
  
  public void setButtonPreferredSize(Dimension d) {
    if (d != null) {
      buttonSize = d;
    }
  }
  
  public Dimension getButtonPreferredSize() {
    return buttonSize;
  }
  
  public JButton[] getButtons() {
    return tabPaneButtons;
  }
  
  public int getButtonCount() {
    return buttonCount;
  }
    
  public void insertTab(String title, Icon icon,
            Component component, String tip, int index) {   
    if (component instanceof TabbedPaneButton) {
      if (component != null) { 
        component.setVisible(true);
        addImpl(component, null, -1);
      }
      return;
    }
	  tip = "tab" + component.hashCode();  
      tabPanelCache.put(tip, component); 
      tabNameCache.put(tip,title);
      tabNameIndexCache.put(title,index);
    super.insertTab(title, icon, component, tip, index);
  }
    
  public boolean isVisibleTab(int index) {    
    if ((visibleStartIndex <= index) && 
        (index < visibleStartIndex + visibleCount)) {
      return true;
    } else {
      return false;
    }
  }
  
  public int getVisibleCount() {
    return visibleCount;
  }
  
  public void setVisibleCount(int visibleCount) {
    if (visibleCount < 0) {
      return;
    }
    this.visibleCount = visibleCount;
  }
  
  public int getVisibleStartIndex() {
    return visibleStartIndex;
  }
  
  public void setVisibleStartIndex(int visibleStartIndex) {
    if (visibleStartIndex < 0 || 
        getTabCount() <= visibleStartIndex) {
      return;
    }
    this.visibleStartIndex = visibleStartIndex;
  }
  
  
  protected JButton[] createButtons(int buttonCount) {
    JButton[] tabPaneButtons = null;
    switch (buttonCount) {
      case ONE_BUTTON: 
        this.buttonCount = buttonCount;
        tabPaneButtons = new JButton[buttonCount];
        tabPaneButtons[0] = new PrevOrNextButton(EAST);
        tabPaneButtons[0].setActionCommand(ROTATE);
        break;
      case TWO_BUTTONS: 
        this.buttonCount = buttonCount;
        tabPaneButtons = new JButton[buttonCount];
        tabPaneButtons[0] = new PrevOrNextButton(WEST);
        tabPaneButtons[0].setActionCommand(PREVIOUS);
        tabPaneButtons[1] = new PrevOrNextButton(EAST);
        tabPaneButtons[1].setActionCommand(NEXT);
        break;
      case FOUR_BUTTONS: 
        this.buttonCount = buttonCount;
        tabPaneButtons = new JButton[buttonCount];
        tabPaneButtons[0] = new FirstOrLastButton(WEST);
        tabPaneButtons[0].setActionCommand(FIRST);
        tabPaneButtons[1] = new PrevOrNextButton(WEST);
        tabPaneButtons[1].setActionCommand(LEFT_SHIFT);
        tabPaneButtons[2] = new PrevOrNextButton(EAST);
        tabPaneButtons[2].setActionCommand(RIGHT_SHIFT);
        tabPaneButtons[3] = new FirstOrLastButton(EAST);
        tabPaneButtons[3].setActionCommand(LAST);
        break;
      default:
    }
    return tabPaneButtons;
  }
  
  
  class PrevOrNextButton extends BasicArrowButton implements TabbedPaneButton {    
    public PrevOrNextButton(int direction) {
      super(direction);
    }
  }
   
  class FirstOrLastButton extends StopArrowButton implements TabbedPaneButton {
    public FirstOrLastButton(int direction) {
      super(direction);
    }
  }
    
  public void addTab(String title, Component component) {  
	  if(null != tabNameCache && StringUtils.isNotBlank(title)){
		  if(!tabNameCache.containsValue(title)){
			  this.addTab(title, component, null);
			  this.setSelectedComponent(component);
		  }
	  }else{
		  this.addTab(title, component, null); 
	  }
  }  
  public void addTab(String title, Component component, Icon extraIcon) {  
      super.addTab(title, new CloseTabIcon(extraIcon), component);  
  }  
//  public void insertTab(String title, Icon icon, Component component, String tip, int index) {  
//      tip = "tab" + component.hashCode();  
//      maps.put(tip, component);  
//      super.insertTab(title, icon, component, tip, index);  
//  }  
  public void removeTabAt(int index) {  
      Component component = getComponentAt(index); 
      String tip = "tab" + component.hashCode();
      tabPanelCache.remove(tip);  
      tabNameCache.remove(tip);
      if(!tabNameIndexCache.isEmpty()){
    	  Integer tempIndex = null;
    	  for(String key : tabNameIndexCache.keySet()){
    		  tempIndex = tabNameIndexCache.get(key);
    		  if(index == tempIndex){
    		      tabNameIndexCache.remove(key);
    		      break;
    		  }
    	  }
      }
      super.removeTabAt(index); 
      setVisibleCount(tabPanelCache.size());
  }  
  public JToolTip createToolTip() {  
      ImageToolTip tooltip = new ImageToolTip();  
      tooltip.setComponent(this);  
      return tooltip;  
  }  
  class ImageToolTip extends JToolTip {  
      public Dimension getPreferredSize() {  
          String tip = getTipText();  
          Component component = tabPanelCache.get(tip);  
          if (component != null) {  
              return new Dimension((int) (getScaleRatio() * component.getWidth()), (int) (getScaleRatio() * component.getHeight()));  
          } else {  
              return super.getPreferredSize();  
          }  
      }  
      public void paintComponent(Graphics g) {  
          String tip = getTipText();  
          Component component = tabPanelCache.get(tip);  
          if (component instanceof JComponent) {  
              JComponent jcomponent = (JComponent) component;  
              Graphics2D g2d = (Graphics2D) g;  
              AffineTransform at = g2d.getTransform();  
              g2d.transform(AffineTransform.getScaleInstance(getScaleRatio(), getScaleRatio()));  
              ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();  
              updateDoubleBuffered(jcomponent, dbcomponents);  
              jcomponent.paint(g);  
              resetDoubleBuffered(dbcomponents);  
              g2d.setTransform(at);  
          }  
      }  
      private void updateDoubleBuffered(JComponent component, ArrayList<JComponent> dbcomponents) {  
          if (component.isDoubleBuffered()) {  
              dbcomponents.add(component);  
              component.setDoubleBuffered(false);  
          }  
          for (int i = 0; i < component.getComponentCount(); i++) {  
              Component c = component.getComponent(i);  
              if (c instanceof JComponent) {  
                  updateDoubleBuffered((JComponent) c, dbcomponents);  
              }  
          }  
      }  
      private void resetDoubleBuffered(ArrayList<JComponent> dbcomponents) {  
          for (JComponent component : dbcomponents) {  
              component.setDoubleBuffered(true);  
          }  
      }  
  }  
  public double getScaleRatio() {  
      return scaleRatio;  
  }  
  public void setScaleRatio(double scaleRatio) {  
      this.scaleRatio = scaleRatio;  
  }  
  
  /**
 * @return the tabNameCache
 */
public Map<String, String> getTabNameCache() {
	return tabNameCache;
}


/**
 * @return the tabNameIndexCache
 */
public Map<String, Integer> getTabNameIndexCache() {
	return tabNameIndexCache;
}



/**
 * @return the tabPanelCache
 */
public Map<String, Component> getTabPanelCache() {
	return tabPanelCache;
}

public void mouseClicked(MouseEvent e) {  
      int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());  
      if (tabNumber < 0) {  
          return;  
      }  
      Rectangle rect = ((CloseTabIcon) getIconAt(tabNumber)).getBounds();  
      if (rect.contains(e.getX(), e.getY())) {  
          //the tab is being closed  
          this.removeTabAt(tabNumber);  
      }  
  }  
  public void mouseEntered(MouseEvent e) {  
  }  
  public void mouseExited(MouseEvent e) {  
  }  
  public void mousePressed(MouseEvent e) {  
  }  
  public void mouseReleased(MouseEvent e) {  
  }  
  
}  
/** 
* The class which generates the 'X' icon for the tabs. The constructor 
* accepts an icon which is extra to the 'X' icon, so you can have tabs 
* like in JBuilder. This value is null if no extra icon is required. 
*/  
class CloseTabIcon implements Icon {  
  private int x_pos;  
  private int y_pos;  
  private int width;  
  private int height;  
  private Icon fileIcon;  
  public CloseTabIcon(Icon fileIcon) {  
      this.fileIcon = fileIcon;  
      width = 16;  
      height = 16;  
  }  
  public void paintIcon(Component c, Graphics g, int x, int y) {  
      this.x_pos = x;  
      this.y_pos = y;  
      Color col = g.getColor();  
      g.setColor(Color.black);  
      int y_p = y + 2;  
      g.drawLine(x + 1, y_p, x + 12, y_p);  
      g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);  
      g.drawLine(x, y_p + 1, x, y_p + 12);  
      g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);  
      g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);  
      g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);  
      g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);  
      g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);  
      g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);  
      g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);  
      g.setColor(col);  
      if (fileIcon != null) {  
          fileIcon.paintIcon(c, g, x + width, y_p);  
      }  
  }  
  public int getIconWidth() {  
      return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);  
  }  
  public int getIconHeight() {  
      return height;  
  }  
  public Rectangle getBounds() {  
      return new Rectangle(x_pos, y_pos, width, height);  
  }  
  /*public static void main(String args[]) { 
  try { 
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
  } 
  catch (Exception e) { 
  e.printStackTrace(); 
  } 
  JClosableTabbedPane pane = new JClosableTabbedPane(); 
  ImageIcon icon = new ImageIcon("images/middle.jpg"); 
  pane.addTab("tab1",new JButton("first Button"),icon); 
  pane.addTab("tab2",new JButton("sec Button"),icon); 
  pane.addTab("tab3",new JButton("third Button"),icon); 
  pane.addTab("tab4",new JButton("fourth Button"),icon); 
  JFrame frame = new JFrame("Demo"); 
  frame.getContentPane().add(pane,BorderLayout.CENTER); 
  frame.setSize(500,300); 
  frame.setLocation(300,200); 
  frame.show(); 
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
  }*/  
}

