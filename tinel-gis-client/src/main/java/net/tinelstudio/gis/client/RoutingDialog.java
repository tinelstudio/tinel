/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.GreatCircleDistanceHeuristic;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.Heuristic;
import net.tinelstudio.gis.routing.searchalgorithm.cost.Cost;
import net.tinelstudio.gis.routing.searchalgorithm.cost.LengthCost;
import net.tinelstudio.gis.routing.searchalgorithm.cost.LevelLengthCost;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class RoutingDialog implements ActionListener {

  private Coordinate startLocation;
  private Coordinate goalLocation;
  private Cost cost;
  private Heuristic heuristic;

  enum CostType {
    LENGTH, SPEED
  }

  private boolean clearAddresses=true;
  private boolean clearStreets=true;
  private boolean clearBuildings=true;

  private JDialog dialog;

  private boolean canceled;

  private JLabel cooraL, coorbL, costL, weightL, removePrevL;
  private JComboBox costCB;
  private JSpinner lngaTF, lataTF, lngbTF, latbTF, weightS;
  private JCheckBox clearAsCB, clearStreetsCB, clearBuildsCB;
  private JButton okB, cancelB;

  public void showRoutingDialog(JFrame owner) {
    showRoutingDialog(owner, null, null);
  }

  public void showRoutingDialog(JFrame owner, Coordinate startLocation,
    Coordinate goalLocation) {
    this.startLocation=(startLocation!=null)?startLocation:new Coordinate();
    this.goalLocation=(goalLocation!=null)?goalLocation:new Coordinate();

    this.dialog=new JDialog(owner);

    dialog.setModal(true);
    dialog.setTitle("Find Route (Routing)");

    dialog.add(buildComponents());
    dialog.pack();
    dialog.setLocationRelativeTo(owner);
    dialog.getRootPane().setDefaultButton(okB);
    dialog.setVisible(true);
  }

  private JComponent buildComponents() {
    this.cooraL=new JLabel("From Location [long lat]:");
    cooraL.setDisplayedMnemonic('F');
    this.lngaTF=new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 0.1));
    lngaTF.setEditor(new JSpinner.NumberEditor(lngaTF, "#0.0##############"));
    lngaTF.setValue(startLocation.x);
    this.lataTF=new JSpinner(new SpinnerNumberModel(0.0, -90.0, 90.0, 0.1));
    lataTF.setEditor(new JSpinner.NumberEditor(lataTF, "#0.0##############"));
    lataTF.setValue(startLocation.y);
    cooraL.setLabelFor(lngaTF);

    this.coorbL=new JLabel("To Location [long lat]:");
    this.lngbTF=new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 0.1));
    lngbTF.setEditor(new JSpinner.NumberEditor(lngbTF, "#0.0##############"));
    lngbTF.setValue(goalLocation.x);
    this.latbTF=new JSpinner(new SpinnerNumberModel(0.0, -90.0, 90.0, 0.1));
    latbTF.setEditor(new JSpinner.NumberEditor(latbTF, "#0.0##############"));
    latbTF.setValue(goalLocation.y);
    coorbL.setLabelFor(lngbTF);

    this.costL=new JLabel("Cost Type:");
    costL.setDisplayedMnemonic('C');
    this.costCB=new JComboBox();
    costCB.addItem(CostType.LENGTH);
    costCB.addItem(CostType.SPEED);
    if (cost instanceof LengthCost) {
      costCB.setSelectedItem(CostType.LENGTH);

    } else if (cost instanceof LevelLengthCost) {
      costCB.setSelectedItem(CostType.SPEED);
    }
    costL.setLabelFor(costCB);

    this.weightL=new JLabel("Heuristic Weight:");
    weightL.setDisplayedMnemonic('H');
    this.weightS=new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.1));
    weightS.setEditor(new JSpinner.NumberEditor(weightS, "#0.0##"));
    double hw=1.0;
    if (heuristic!=null) {
      hw=heuristic.getWeight();
    }
    weightS.setValue(hw);
    weightL.setLabelFor(weightS);

    this.removePrevL=new JLabel("Remove Previous:");

    this.clearAsCB=new JCheckBox("Addresses");
    clearAsCB.setMnemonic('A');
    clearAsCB.setSelected(clearAddresses);

    this.clearStreetsCB=new JCheckBox("Streets");
    clearStreetsCB.setMnemonic('S');
    clearStreetsCB.setSelected(clearStreets);

    this.clearBuildsCB=new JCheckBox("Buildings");
    clearBuildsCB.setMnemonic('B');
    clearBuildsCB.setSelected(clearBuildings);

    this.okB=new JButton("OK");
    okB.addActionListener(this);
    this.cancelB=new JButton("Cancel");
    cancelB.addActionListener(this);

    ButtonBarBuilder buttonBar=new ButtonBarBuilder();
    buttonBar.setDefaultButtonBarGapBorder();
    buttonBar.addGlue();
    buttonBar.addGridded(okB);
    buttonBar.addRelatedGap();
    buttonBar.addGridded(cancelB);

    FormLayout layout1=new FormLayout("140px:grow, 6px, 140px:grow");
    DefaultFormBuilder builder1=new DefaultFormBuilder(layout1);
    builder1.append(lngaTF);
    builder1.append(lataTF);

    FormLayout layout2=new FormLayout("140px:grow, 6px, 140px:grow");
    DefaultFormBuilder builder2=new DefaultFormBuilder(layout2);
    builder2.append(lngbTF);
    builder2.append(latbTF);

    FormLayout layout=new FormLayout(
      "pref, 12px, 80px:grow, 6px, 80px:grow, 6px, 80px:grow");
    DefaultFormBuilder builder=new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.append(cooraL);
    builder.append(builder1.getPanel(), 5);
    builder.append(coorbL);
    builder.append(builder2.getPanel(), 5);
    builder.append(costL);
    builder.append(costCB, 5);
    builder.append(weightL);
    builder.append(weightS, 5);
    builder.append(removePrevL);
    builder.append(clearAsCB);
    builder.append(clearStreetsCB);
    builder.append(clearBuildsCB);
    builder.append(buttonBar.getPanel(), 7);

    return builder.getPanel();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==okB) {
      double lnga=(Double)lngaTF.getValue();
      double lata=(Double)lataTF.getValue();
      double lngb=(Double)lngbTF.getValue();
      double latb=(Double)latbTF.getValue();
      double hw=(Double)weightS.getValue();
      CostType costT=(CostType)costCB.getSelectedItem();

      startLocation.x=lnga;
      startLocation.y=lata;
      goalLocation.x=lngb;
      goalLocation.y=latb;
      if (costT==CostType.LENGTH) {
        cost=new LengthCost();

      } else if (costT==CostType.SPEED) {
        Map<Level, Double> speedMap=new HashMap<Level, Double>();
        speedMap.put(Level.UNKNOWN, 90/70.0);
        speedMap.put(Level.MAJOR, 90/110.0);
        speedMap.put(Level.MEDIUM, 90/65.0);
        speedMap.put(Level.MINOR, 90/55.0);
        cost=new LevelLengthCost(speedMap);
      }
      heuristic=new GreatCircleDistanceHeuristic(hw);
      setClearAddresses(clearAsCB.isSelected());
      setClearStreets(clearStreetsCB.isSelected());
      setClearBuildings(clearBuildsCB.isSelected());

      canceled=false;
      dialog.dispose();

    } else if (e.getSource()==cancelB) {
      canceled=true;
      dialog.dispose();
    }
  }

  public Coordinate getStartLocation() {
    return this.startLocation;
  }

  public Coordinate getGoalLocation() {
    return this.goalLocation;
  }

  public Cost getCost() {
    return this.cost;
  }

  public Heuristic getHeuristic() {
    return this.heuristic;
  }

  public boolean isCanceled() {
    return this.canceled;
  }

  public boolean isClearAddresses() {
    return this.clearAddresses;
  }

  public void setClearAddresses(boolean clearAddresses) {
    this.clearAddresses=clearAddresses;
  }

  public boolean isClearStreets() {
    return this.clearStreets;
  }

  public void setClearStreets(boolean clearStreets) {
    this.clearStreets=clearStreets;
  }

  public boolean isClearBuildings() {
    return this.clearBuildings;
  }

  public void setClearBuildings(boolean clearBuildings) {
    this.clearBuildings=clearBuildings;
  }
}
