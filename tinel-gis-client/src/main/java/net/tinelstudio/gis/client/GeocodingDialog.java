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
import javax.swing.JTextField;

import net.tinelstudio.gis.geocoding.locator.AddressLocator;
import net.tinelstudio.gis.geocoding.locator.BuildingLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.locator.StreetLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByNameLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByTownLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByTownLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByTownLocator;
import net.tinelstudio.gis.geocoding.locator.multi.DefaultFallbackLocator;
import net.tinelstudio.gis.geocoding.locator.multi.FullCompositeLocator;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author TineL
 */
public class GeocodingDialog implements ActionListener {

  private Locator locator;
  private boolean clearAddresses=true;
  private boolean clearStreets=true;
  private boolean clearBuildings=true;

  private JDialog dialog;

  private boolean canceled;

  private JLabel searchL, locatorL, maxL, removePrevL;
  private JTextField searchTF;
  private JSpinner maxS;
  private JComboBox locatorCB;
  private JCheckBox clearAsCB, clearStreetsCB, clearBuildsCB;
  private JButton okB, cancelB;

  public void showGeocodingDialog(JFrame owner) {
    this.dialog=new JDialog(owner);

    dialog.setModal(true);
    dialog.setTitle("Locate Geoplaces (Geocoding)");

    dialog.add(buildComponents());
    dialog.pack();
    dialog.setLocationRelativeTo(owner);
    dialog.getRootPane().setDefaultButton(okB);
    dialog.setVisible(true);
  }

  private JComponent buildComponents() {
    this.searchL=new JLabel("Search for:");
    searchL.setDisplayedMnemonic('f');
    this.searchTF=new JTextField(15);
    if (locator!=null) {
      searchTF.setText(locator.getSearchString());
    }
    searchL.setLabelFor(searchTF);

    this.locatorL=new JLabel("Use Locator:");
    locatorL.setDisplayedMnemonic('L');
    this.locatorCB=new JComboBox();
    locatorCB.addItem(DefaultFallbackLocator.class.getSimpleName());
    locatorCB.addItem(FullCompositeLocator.class.getSimpleName());
    locatorCB.addItem(AddressLocator.class.getSimpleName());
    locatorCB.addItem(DistinctAddressByNameLocator.class.getSimpleName());
    locatorCB.addItem(DistinctAddressByTownLocator.class.getSimpleName());
    locatorCB.addItem(DistinctAddressByRegionLocator.class.getSimpleName());
    locatorCB.addItem(DistinctAddressByCountryLocator.class.getSimpleName());
    locatorCB.addItem(DistinctAddressByContinentLocator.class.getSimpleName());
    locatorCB.addItem(StreetLocator.class.getSimpleName());
    locatorCB.addItem(DistinctStreetByTownLocator.class.getSimpleName());
    locatorCB.addItem(DistinctStreetByRegionLocator.class.getSimpleName());
    locatorCB.addItem(DistinctStreetByCountryLocator.class.getSimpleName());
    locatorCB.addItem(DistinctStreetByContinentLocator.class.getSimpleName());
    locatorCB.addItem(BuildingLocator.class.getSimpleName());
    locatorCB.addItem(DistinctBuildingByTownLocator.class.getSimpleName());
    locatorCB.addItem(DistinctBuildingByRegionLocator.class.getSimpleName());
    locatorCB.addItem(DistinctBuildingByCountryLocator.class.getSimpleName());
    locatorCB.addItem(DistinctBuildingByContinentLocator.class.getSimpleName());
    if (locator!=null) {
      locatorCB.setSelectedItem(locator.getClass().getSimpleName());
    }
    locatorL.setLabelFor(locatorCB);

    this.maxL=new JLabel("Max Geoplaces:");
    maxL.setDisplayedMnemonic('M');
    this.maxS=new JSpinner();
    int maxResults=1000;
    if (locator!=null) {
      maxResults=locator.getMaxResults();
    }
    maxS.setValue(maxResults);
    maxL.setLabelFor(maxS);

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

    FormLayout layout=new FormLayout(
      "pref, 12px, 80px:grow, 6px, 80px:grow, 6px, 80px:grow");
    DefaultFormBuilder builder=new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.append(searchL);
    builder.append(searchTF, 5);
    builder.append(maxL);
    builder.append(maxS, 5);
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
      String search=searchTF.getText();
      String locatorName=(String)locatorCB.getSelectedItem();
      int max=(Integer)maxS.getValue();

      Locator l;
      if (DefaultFallbackLocator.class.getSimpleName().equals(locatorName)) {
        l=new DefaultFallbackLocator();

      } else if (FullCompositeLocator.class.getSimpleName().equals(locatorName)) {
        l=new FullCompositeLocator();

      } else if (AddressLocator.class.getSimpleName().equals(locatorName)) {
        l=new AddressLocator();

      } else if (DistinctAddressByNameLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctAddressByNameLocator();

      } else if (DistinctAddressByTownLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctAddressByTownLocator();

      } else if (DistinctAddressByRegionLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctAddressByRegionLocator();

      } else if (DistinctAddressByCountryLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctAddressByCountryLocator();

      } else if (DistinctAddressByContinentLocator.class.getSimpleName()
        .equals(locatorName)) {
        l=new DistinctAddressByContinentLocator();

      } else if (StreetLocator.class.getSimpleName().equals(locatorName)) {
        l=new StreetLocator();

      } else if (DistinctStreetByTownLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctStreetByTownLocator();

      } else if (DistinctStreetByRegionLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctStreetByRegionLocator();

      } else if (DistinctStreetByCountryLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctStreetByCountryLocator();

      } else if (DistinctStreetByContinentLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctStreetByContinentLocator();

      } else if (BuildingLocator.class.getSimpleName().equals(locatorName)) {
        l=new BuildingLocator();

      } else if (DistinctBuildingByTownLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctBuildingByTownLocator();

      } else if (DistinctBuildingByRegionLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctBuildingByRegionLocator();

      } else if (DistinctBuildingByCountryLocator.class.getSimpleName().equals(
        locatorName)) {
        l=new DistinctBuildingByCountryLocator();

      } else if (DistinctBuildingByContinentLocator.class.getSimpleName()
        .equals(locatorName)) {
        l=new DistinctBuildingByContinentLocator();

      } else {
        return;
      }
      l.setMaxResults(max);
      l.setSearchString(search);
      locator=l;
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

  public Locator getLocator() {
    return this.locator;
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

  public void setClearBuildings(boolean clearBuilds) {
    this.clearBuildings=clearBuilds;
  }

  public boolean isCanceled() {
    return this.canceled;
  }
}
