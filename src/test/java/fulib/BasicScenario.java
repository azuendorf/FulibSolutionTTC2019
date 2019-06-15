
package fulib;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.tables.FulibTable;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.StringTable;
import org.junit.Test;
import ttc2019.metamodels.bdd.*;
import ttc2019.metamodels.bddg.BDDGPackage;
import ttc2019.model.validator.Launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BasicScenario
{
   @Test
   public void testBasic() throws IOException
   {
      Launcher.main(new String[]{"models/GeneratedI4O2Seed42.ttmodel", "GeneratedI4O2Seed42.ttmodel.xmi"});
   }

   int count = 0;

   @Test
   public void testTables() throws IOException
   {
      BDD bdd = FulibSolution.doTransform("models/GeneratedI4O2Seed42.ttmodel", "models/GeneratedI4O2Seed42.ttmodel", "0", "Fulib");

      {
         ObjectTable bddTable = new ObjectTable(bdd);
         ObjectTable portTable = bddTable.expandLink("Ports", "ports");
         ObjectTable treeTable = bddTable.expandLink("Tree", "tree");
         System.out.println(bddTable);
         treeTable.hasLink("port", portTable);
         System.out.println(bddTable);
      }

      ObjectTable rootTable = new ObjectTable(bdd);
      StringTable nameTable = rootTable
            .expandLink("P", "ports")
            .expandString("Ports", "name");
      rootTable.selectColumns("Ports");
      System.out.println(rootTable);
      ArrayList<String> nameList = nameTable.toList();
      System.out.println(nameList);

      ObjectTable bddTable = new ObjectTable("BDD", bdd);
      ObjectTable portTable = bddTable.expandLink("ports", "ports");
      ObjectTable assignsTable = portTable.expandLink("assignments", "assignments");
      ObjectTable leafTable = assignsTable.expandLink("Leaf", "owner");
      leafTable.selectColumns("Leaf");
      leafTable.addColumn("LeafNo", map -> "L" + (count++));
      assignsTable = leafTable.expandLink("Assigns", "assignments");
      portTable = assignsTable.expandLink("Port", "port");
      portTable.expandString("Port Name", "name");
      assignsTable.expandInt("Value", "value");
      assignsTable.selectColumns("LeafNo", "Port Name", "Value");

      System.out.println(bddTable);

   }

}
