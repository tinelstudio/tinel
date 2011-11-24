/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.tinelstudio.gis.reversegeocoding.locator.AddressLocator;
import net.tinelstudio.gis.reversegeocoding.locator.BuildingLocator;
import net.tinelstudio.gis.reversegeocoding.locator.Locator;
import net.tinelstudio.gis.reversegeocoding.locator.StreetLocator;
import net.tinelstudio.gis.reversegeocoding.locator.multi.DefaultFallbackLocator;
import net.tinelstudio.gis.reversegeocoding.locator.multi.FullCompositeLocator;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class ReverseGeocodingDialog implements ActionListener {

  private Locator locator;
  private Coordinate location;
  private boolean clearAddresses=true;
  private boolean clearStreets=true;
  private boolean clearBuildings=true;

  private JDialog dialog;

  private boolean canceled;

  private JLabel coorL, radiusL, maxL, locatorL, removePrevL;
  private JSpinner lngTF, latTF, radiusS, maxS;
  private JComboBox locatorCB;
  private JCheckBox clearAsCB, clearStreetsCB, clearBuildsCB;
  private JButton okB, cancelB;

  public void showReverseGeocodingDialog(JFrame owner) {
    showReverseGeocodingDialog(owner, null);
  }

  public void showReverseGeocodingDialog(JFrame owner, Coordinate c) {
    this.location=(c!=null)?c:new Coordinate();

    this.dialog=new JDialog(owner);

    dialog.setModal(true);
    dialog.setTitle("Find Nearest Geoplaces (Reverse Geocoding)");

    dialog.add(buildComponents());
    dialog.pack();
    dialog.setLocationRelativeTo(owner);
    dialog.getRootPane().setDefaultButton(okB);
    dialog.setVisible(true);
  }

  private JComponent buildComponents() {
    this.coorL=new JLabel("To Location [long lat]:");
    coorL.setDisplayedMnemonic('T');
    this.lngTF=new JSpinner(new SpinnerNumberModel(0.0, -180.0, 180.0, 0.1));
    lngTF.setEditor(new JSpinner.NumberEditor(lngTF, "#0.0##############"));
    lngTF.setValue(location.x);
    this.latTF=new JSpinner(new SpinnerNumberModel(0.0, -90.0, 90.0, 0.1));
    latTF.setEditor(new JSpinner.NumberEditor(latTF, "#0.0##############"));
    latTF.setValue(location.y);
    coorL.setLabelFor(lngTF);

    this.radiusL=new JLabel("Search Radius [m]:");
    radiusL.setDisplayedMnemonic('R');
    this.radiusS=new JSpinner();
    int maxDistanceMeters=5000;
    if (locator!=null) {
      maxDistanceMeters=locator.getMaxDistanceMeters();
    }
    radiusS.setValue(maxDistanceMeters);
    radiusL.setLabelFor(radiusS);

    this.maxL=new JLabel("Max Geoplaces:");
    maxL.setDisplayedMnemonic('M');
    this.maxS=new JSpinner();
    int maxResults=1000;
    if (locator!=null) {
      maxResults=locator.getMaxResults();
    }
    maxS.setValue(maxResults);
    maxL.setLabelFor(maxS);

    this.locatorL=new JLabel("Use Locator:");
    locatorL.setDisplayedMnemonic('L');
    this.locatorCB=new JComboBox();
    locatorCB.addItem(DefaultFallbackLocator.class.getSimpleName());
    locatorCB.addItem(FullCompositeLocator.class.getSimpleName());
    locatorCB.addItem(AddressLocator.class.getSimpleName());
    locatorCB.addItem(StreetLocator.class.getSimpleName());
    locatorCB.addItem(BuildingLocator.class.getSimpleName());
    if (locator!=null) {
      locatorCB.setSelectedItem(locator.getClass().getSimpleName());
    }
    locatorL.setLabelFor(locatorCB);

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
    builder1.append(lngTF);
    builder1.append(latTF);

    FormLayout layout=new FormLayout(
      "pref, 12px, 80px:grow, 6px, 80px:grow, 6px, 80px:grow");
    DefaultFormBuilder builder=new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.append(coorL);
    builder.append(builder1.getPanel(), 5);
    builder.append(maxL);
    builder.append(maxS, 5);
    builder.append(radiusL);
    builder.append(radiusS, 5);
    builder.append(locatorL);
    builder.append(locatorCB, 5);
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
      double lng=(Double)lngTF.getValue();
      double lat=(Double)latTF.getValue();
      int r=(Integer)radiusS.getValue();
      int max=(Integer)maxS.getValue();
      String locatorName=(String)locatorCB.getSelectedItem();

      Locator l;
      if (DefaultFallbackLocator.class.getSimpleName().equals(locatorName)) {
        l=new DefaultFallbackLocator();

      } else if (FullCompositeLocator.class.getSimpleName().equals(locatorName)) {
        l=new FullCompositeLocator();

      } else if (AddressLocator.class.getSimpleName().equals(locatorName)) {
        l=new AddressLocator();

      } else if (StreetLocator.class.getSimpleName().equals(locatorName)) {
        l=new StreetLocator();

      } else if (BuildingLocator.class.getSimpleName().equals(locatorName)) {
        l=new BuildingLocator();

      } else {
        return;
      }
      l.setMaxResults(max);
      l.setMaxDistanceMeters(r);
      location.x=lng;
      location.y=lat;
      l.setSearchLocation(location);
      locator=l;
      setClearAddresses(clearAsCB.isSelected());
      setClearStreets(clearStreetsCB.isSelected());
      setClearBuildings(clearBuildsCB.isSelected());

      canceled=false;
      dialog.dispose();

      canceled=false;
      dialog.dispose();

    } else if (e.getSource()==cancelB) {
      canceled=true;
      dialog.dispose();
    }
  }

  public Locator getLocator() {
    return this.locator;
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
