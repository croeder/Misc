package binaryhopfield;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.event.*;

/**
 * <p>Title: BinHopGui </p>
 * <p>Description: a GUI to explore the Binary Hopfield pattern matching code</p>
 * <p>Copyright: Copyright Chris Roeder (c) 2004</p>
 * @author Chris Roeder
 * @version 1.0
 */

public class BinHopGui extends JApplet {
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  JButton jButton0 = new JButton();

  boolean trainingMode = true;
  Image image;
  Image target;
  BinHop network;
  boolean setupTest;
  int iterateCount;

  static final int fancy=0;
  static final int sevenSegment=1;
  static final int weird=2;
  static final int fat=3;

  int letterStyle=fancy;

  JRadioButton testRadioButton = new JRadioButton();
  JRadioButton trainRadioButton = new JRadioButton();
  ButtonGroup trainTestGroup = new ButtonGroup();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JButton addNoiseButton = new JButton();
  JButton recognizeButton = new JButton();
  JSlider noiseSlider = new JSlider();
  JTextField noiseTextField = new JTextField();
  JButton resetButton = new JButton();
  JTextField iterationTextField = new JTextField();
  JLabel jLabel3 = new JLabel();
  JRadioButton largeRadioButton = new JRadioButton();
  JRadioButton smallRadioButton = new JRadioButton();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JTextField numBitsTextField = new JTextField();
  JLabel jLabel7 = new JLabel();
  JRadioButton jRadioButton1 = new JRadioButton();
  JRadioButton jRadioButton2 = new JRadioButton();
  JRadioButton jRadioButton3 = new JRadioButton();
  JRadioButton jRadioButton4 = new JRadioButton();
  ButtonGroup buttonGroup2 = new ButtonGroup();

  public BinHopGui() throws HeadlessException {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    network = new BinHop();
  }
  public static void main(String[] args) throws HeadlessException {
    BinHopGui binHopGui1 = new BinHopGui();
  }
  private void jbInit() throws Exception {
    noiseSlider.setMaximum(50);
    noiseSlider.setBounds(new Rectangle(41, 312, 87, 19));
    noiseSlider.addChangeListener(new BinHopGui_noiseSlider_changeAdapter(this));
    noiseSlider.addPropertyChangeListener(new BinHopGui_noiseSlider_propertyChangeAdapter(this));
    noiseSlider.setValue(10);
    recognizeButton.setBounds(new Rectangle(15, 228, 118, 22));
    recognizeButton.setText("Recognize");
    recognizeButton.addMouseListener(new BinHopGui_recognizeButton_mouseAdapter(this));
    this.getContentPane().setLayout(null);
    jButton1.setBounds(new Rectangle(15, 126, 41, 22));
    jButton1.setText("1");
    jButton1.addMouseListener(new BinHopGui_jButton1_mouseAdapter(this));
    jButton2.setText("2");
    jButton2.addMouseListener(new BinHopGui_jButton2_mouseAdapter(this));
    jButton2.setBounds(new Rectangle(15, 150, 41, 22));
    jButton3.setText("3");
    jButton3.addMouseListener(new BinHopGui_jButton3_mouseAdapter(this));
    jButton3.setBounds(new Rectangle(15, 176, 41, 22));
    jButton0.setText("0");
    jButton0.addMouseListener(new BinHopGui_jButton0_mouseAdapter(this));
    jButton0.setBounds(new Rectangle(15, 102, 41, 22));
    testRadioButton.setText("test");
    testRadioButton.setBounds(new Rectangle(18, 76, 74, 22));
    testRadioButton.addActionListener(new BinHopGui_testRadioButton_actionAdapter(this));
    trainRadioButton.setBounds(new Rectangle(15, 50, 70, 22));
    trainRadioButton.addActionListener(new BinHopGui_trainRadioButton_actionAdapter(this));
    trainRadioButton.setSelected(true);
    trainRadioButton.setText("train");
    jLabel1.setFont(new java.awt.Font("Arial", 1, 14));
    jLabel1.setText("Pattern Recognition Using a Binary Hopfield Network");
    jLabel1.setBounds(new Rectangle(10, 0, 374, 24));
    jLabel2.setText("Chris Roeder, CSC 5542 Fall 2004");
    jLabel2.setBounds(new Rectangle(11, 17, 340, 21));
    addNoiseButton.setBounds(new Rectangle(15, 254, 118, 22));
    addNoiseButton.setText("Add Noise");
    addNoiseButton.addMouseListener(new BinHopGui_addNoiseButton_mouseAdapter(this));
    noiseTextField.setBounds(new Rectangle(42, 283, 85, 23));
    resetButton.addMouseListener(new BinHopGui_resetButton_mouseAdapter(this));
    resetButton.setText("Reset");
    resetButton.addMouseListener(new BinHopGui_resetButton_mouseAdapter(this));
    resetButton.setBounds(new Rectangle(15, 202, 118, 22));
    iterationTextField.setText("");
    iterationTextField.setBounds(new Rectangle(338, 50, 42, 22));
    jLabel3.setText("iteration:");
    jLabel3.setBounds(new Rectangle(261, 49, 76, 27));
    largeRadioButton.setText("large");
    largeRadioButton.setBounds(new Rectangle(124, 53, 56, 21));
    largeRadioButton.addActionListener(new BinHopGui_largeRadioButton_actionAdapter(this));
    smallRadioButton.setBounds(new Rectangle(124, 78, 56, 21));
    smallRadioButton.addActionListener(new BinHopGui_smallRadioButton_actionAdapter(this));
    smallRadioButton.setText("small");
    smallRadioButton.setSelected(true);
    jLabel4.setRequestFocusEnabled(true);
    jLabel4.setText("Activations");
    jLabel4.setBounds(new Rectangle(157, 105, 71, 15));
    jLabel5.setText("Connections");
    jLabel5.setBounds(new Rectangle(272, 106, 87, 15));
    jLabel6.setBounds(new Rectangle(258, 74, 76, 27));
    jLabel6.setText("bits different:");
    numBitsTextField.setBounds(new Rectangle(338, 77, 42, 22));
    numBitsTextField.setText("");
    jLabel7.setBounds(new Rectangle(157, 254, 71, 15));
    jLabel7.setText("Initial Image");
    jLabel7.setRequestFocusEnabled(true);
    jRadioButton1.setText("fancy");
    jRadioButton1.setBounds(new Rectangle(68, 102, 78, 18));
    jRadioButton1.addActionListener(new BinHopGui_jRadioButton1_actionAdapter(this));
    jRadioButton2.setBounds(new Rectangle(68, 126, 78, 18));
    jRadioButton2.addActionListener(new BinHopGui_jRadioButton2_actionAdapter(this));
    jRadioButton2.setText("7 segment");
    jRadioButton3.setBounds(new Rectangle(68, 150, 78, 18));
    jRadioButton3.addActionListener(new BinHopGui_jRadioButton3_actionAdapter(this));
    jRadioButton3.setText("strange");
    jRadioButton4.setBounds(new Rectangle(68, 176, 78, 18));
    jRadioButton4.addActionListener(new BinHopGui_jRadioButton4_actionAdapter(this));
    jRadioButton4.setText("fat");
    trainTestGroup.add(testRadioButton);
    trainTestGroup.add(trainRadioButton);
    buttonGroup1.add(smallRadioButton);
    buttonGroup1.add(largeRadioButton);
    //trainTestGroup.set
    this.getContentPane().add(trainRadioButton, null);
    this.getContentPane().add(addNoiseButton, null);
    this.getContentPane().add(jButton1, null);
    this.getContentPane().add(recognizeButton, null);
    this.getContentPane().add(resetButton, null);
    this.getContentPane().add(iterationTextField, null);
    this.getContentPane().add(jLabel1, null);
    this.getContentPane().add(jLabel2, null);
    this.getContentPane().add(smallRadioButton, null);
    this.getContentPane().add(largeRadioButton, null);
    this.getContentPane().add(jLabel3, null);
    this.getContentPane().add(jLabel4, null);
    this.getContentPane().add(jLabel5, null);
    this.getContentPane().add(testRadioButton, null);
    this.getContentPane().add(noiseTextField, null);
    this.getContentPane().add(noiseSlider, null);
    this.getContentPane().add(jLabel6, null);
    this.getContentPane().add(numBitsTextField, null);
    this.getContentPane().add(jLabel7, null);
    this.getContentPane().add(jButton0, null);
    this.getContentPane().add(jButton1, BorderLayout.NORTH);
    this.getContentPane().add(jButton2, null);
    this.getContentPane().add(jButton3, null);
    this.getContentPane().add(jRadioButton1, null);
    this.getContentPane().add(jRadioButton2, null);
    this.getContentPane().add(jRadioButton3, null);
    this.getContentPane().add(jRadioButton4, null);
    buttonGroup2.add(jRadioButton1);
    buttonGroup2.add(jRadioButton2);
    buttonGroup2.add(jRadioButton3);
    buttonGroup2.add(jRadioButton4);
  }
  void testRadioButton_actionPerformed(ActionEvent e) {
    trainingMode = false;
    addNoiseButton.setEnabled(true);
    recognizeButton.setEnabled(true);
    noiseSlider.setEnabled(true);
    resetButton.setEnabled(false);
    setupTest = true;
    iterateCount=0;
  }
  void trainRadioButton_actionPerformed(ActionEvent e) {
    trainingMode = true;
    addNoiseButton.setEnabled(false);
    recognizeButton.setEnabled(false);
    noiseSlider.setEnabled(false);
    resetButton.setEnabled(true);
  }
  void jButton1_mouseClicked(MouseEvent e) {
    image = new Image(1 + 10 * letterStyle);
    try {target = (Image) image.clone();}
        catch (Exception x) {; }
    repaint();
    if (trainingMode) {
      network.train(image);
    }
  }
  public void  paint(Graphics g){
    super.paint(g);
    if (image != null) {
      if (trainingMode) {
        numBitsTextField.setText("");
        image.draw(g, 150, 120);
        network.drawNetwork(g, 270, 120);
      }
      else { // preparing to recognize
        if (setupTest) {
          image.draw(g, 150, 120);
          numBitsTextField.setText("");
        }
        else { // recognizing
          network.drawNetwork(g, 270, 120);
          image.draw(g, 150, 270);
          network.draw(g, 150, 120);
          numBitsTextField.setText("" + image.NumBitsDifferent(network.values));
          try {
            Thread.sleep(500);
          }
          catch (Exception x) {
            ;
          }
          boolean noChange = network.update();
          iterateCount++;
          iterationTextField.setText("" + iterateCount);
          if (iterateCount < 5 || (iterateCount < 10 && noChange))
            repaint();
          else {
            iterateCount = 0;
            setupTest = true;
          }
        }
      }
    }
  }
  void addNoiseButton_mouseClicked(MouseEvent e) {
    if (image != null) {
      image.addNoise(noiseSlider.getValue());
      repaint();
    }
  }
  void noiseSlider_propertyChange(PropertyChangeEvent e) {
    noiseTextField.setText("" + noiseSlider.getValue());
  }
  void noiseSlider_stateChanged(ChangeEvent e) {
noiseTextField.setText("" + noiseSlider.getValue());
  }
  void jButton0_mouseClicked(MouseEvent e) {
    image = new Image(0 + letterStyle * 10);
    try {target = (Image) image.clone();}
        catch (Exception x) {; }
    repaint();
    if (trainingMode) {
      network.train(image);
    }
  }
  void jButton3_mouseClicked(MouseEvent e) {
    image = new Image(3 + letterStyle * 10);
    try { target = (Image) image.clone();}
        catch (Exception x) {; }
    repaint();
    if (trainingMode) {
        network.train(image);
    }
  }
  void jButton2_mouseClicked(MouseEvent e) {
    image = new Image(2 + letterStyle * 10);
    try { target = (Image) image.clone();}
        catch (Exception x) {; }
    repaint();
    if (trainingMode) {
        network.train(image);
      }
  }
  void recognizeButton_mouseClicked(MouseEvent e) {
    if (image != null)
      network.prime(image);
    setupTest = false;
    repaint();
  }
  void resetButton_mouseClicked(MouseEvent e) {
    if (network == null)
      network = new BinHop();
    else
      network.reset();

    repaint();
  }
  void largeRadioButton_actionPerformed(ActionEvent e) {
    if (largeRadioButton.isSelected())
      Image.setLarge();
    else
      Image.setSmall();
    network = new BinHop();
    image = null;
  }
  void smallRadioButton_actionPerformed(ActionEvent e) {
    if (smallRadioButton.isSelected())
         Image.setSmall();
       else
         Image.setLarge();
       network = new BinHop();
       image = null;
  }

  void jRadioButton1_actionPerformed(ActionEvent e) {
    if (jRadioButton1.isSelected())
            letterStyle = fancy;
  }

  void jRadioButton2_actionPerformed(ActionEvent e) {
    if (jRadioButton2.isSelected())
            letterStyle = sevenSegment;
  }

  void jRadioButton3_actionPerformed(ActionEvent e) {
    if (jRadioButton3.isSelected())
            letterStyle = weird;
  }

  void jRadioButton4_actionPerformed(ActionEvent e) {
    if (jRadioButton4.isSelected())
            letterStyle = fat;
  }




}

class BinHopGui_testRadioButton_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_testRadioButton_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.testRadioButton_actionPerformed(e);
  }
}

class BinHopGui_trainRadioButton_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_trainRadioButton_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.trainRadioButton_actionPerformed(e);
  }
}

class BinHopGui_jButton1_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_jButton1_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jButton1_mouseClicked(e);
  }
}

class BinHopGui_addNoiseButton_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_addNoiseButton_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.addNoiseButton_mouseClicked(e);
  }
}

class BinHopGui_noiseSlider_propertyChangeAdapter implements java.beans.PropertyChangeListener {
  BinHopGui adaptee;

  BinHopGui_noiseSlider_propertyChangeAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void propertyChange(PropertyChangeEvent e) {
    adaptee.noiseSlider_propertyChange(e);
  }
}

class BinHopGui_noiseSlider_changeAdapter implements javax.swing.event.ChangeListener {
  BinHopGui adaptee;

  BinHopGui_noiseSlider_changeAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.noiseSlider_stateChanged(e);
  }
}

class BinHopGui_jButton0_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_jButton0_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jButton0_mouseClicked(e);
  }
}

class BinHopGui_jButton3_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_jButton3_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jButton3_mouseClicked(e);
  }
}

class BinHopGui_jButton2_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_jButton2_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.jButton2_mouseClicked(e);
  }
}

class BinHopGui_recognizeButton_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_recognizeButton_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.recognizeButton_mouseClicked(e);
  }
}

class BinHopGui_resetButton_mouseAdapter extends java.awt.event.MouseAdapter {
  BinHopGui adaptee;

  BinHopGui_resetButton_mouseAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.resetButton_mouseClicked(e);
  }
}

class BinHopGui_largeRadioButton_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_largeRadioButton_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.largeRadioButton_actionPerformed(e);
  }
}

class BinHopGui_smallRadioButton_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_smallRadioButton_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.smallRadioButton_actionPerformed(e);
  }
}

class BinHopGui_jRadioButton1_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_jRadioButton1_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jRadioButton1_actionPerformed(e);
  }
}

class BinHopGui_jRadioButton2_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_jRadioButton2_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jRadioButton2_actionPerformed(e);
  }
}

class BinHopGui_jRadioButton3_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_jRadioButton3_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jRadioButton3_actionPerformed(e);
  }
}

class BinHopGui_jRadioButton4_actionAdapter implements java.awt.event.ActionListener {
  BinHopGui adaptee;

  BinHopGui_jRadioButton4_actionAdapter(BinHopGui adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jRadioButton4_actionPerformed(e);
  }
}



