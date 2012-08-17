/* 
   owlconvert : A simple OWL format converter using the OWLAPI
   Campbell Webb (c) 2012 <cwebb@oeb.harvard.edu>

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License version  3 as 
   published by the Free Software Foundation.

   Based on Matthew Horridge's LoadingOntologies.java and 
   SavingOntologies.java in the OWLAPI examples
*/

/* 
   To compile, place in the same dir as owlapi-bin.jar
     $ javac -classpath owlapi-bin.jar owlconvert.java
   Run with (replacing with correct paths)
     $ java -classpath "owlapi-bin.jar:." owlconvert
   Make a shell script or alias to run from anywhere:
     alias owlconvert 'java -classpath "$OWLAPI:$OWLCONVERTPATH" owlconvert'
*/

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;

public class owlconvert {
  public static void main(String[] args) {
	try {
	  // HELP
	  if ((args[0].compareTo("-h") == 0) || 
		  (args[0].compareTo("--help") == 0) ||
		  (args.length != 2)) {
		System.err.println(
		   "\n  Usage: owlconvert  manchester|turtle|rdfxml  <owl infile>\n");
		System.exit(1);
	  }
		
	  // Get hold of an ontology manager
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  
	  // LOAD
	  
	  // Let's load an ontology from the web. We load the ontology from a
	  File file = new File(args[1]); // new File("in.owl");
	  // Now load the local copy
	  OWLOntology myOWL = manager.loadOntologyFromOntologyDocument(file);
	  System.err.println("Loaded ontology: " + myOWL);
	  
	  // CHECK
	  
	  // We can get information about the format of an ontology from its
	  // manager
	  OWLOntologyFormat format = manager.getOntologyFormat(myOWL);
	  System.err.println("    format: " + format);

	  // CONVERT
	
	  if (args[0].compareTo("manchester") == 0) {
		ManchesterOWLSyntaxOntologyFormat myOut = 
		  new ManchesterOWLSyntaxOntologyFormat();
		if (format.isPrefixOWLOntologyFormat()) {
		  myOut.copyPrefixesFrom( format.asPrefixOWLOntologyFormat() );
		}
		manager.saveOntology(myOWL, myOut, new SystemOutDocumentTarget());
	  }

	  else if (args[0].compareTo("turtle") == 0) {
		TurtleOntologyFormat myOut = 
		  new TurtleOntologyFormat();
		if (format.isPrefixOWLOntologyFormat()) {
		  myOut.copyPrefixesFrom( format.asPrefixOWLOntologyFormat() );
		}
		manager.saveOntology(myOWL, myOut, new SystemOutDocumentTarget());
	  }

	  else if (args[0].compareTo("rdfxml") == 0) {
		RDFXMLOntologyFormat myOut = 
		  new RDFXMLOntologyFormat();
		if (format.isPrefixOWLOntologyFormat()) {
		  myOut.copyPrefixesFrom( format.asPrefixOWLOntologyFormat() );
		}
		manager.saveOntology(myOWL, myOut, new SystemOutDocumentTarget());
	  }

	  else {
		System.err.println("Could not understand requested format: " + args[0]);
	  }
	} 
	catch (OWLOntologyCreationException e) {
		System.err.println("Could not load ontology: " + e.getMessage());
	} 
	catch (OWLOntologyStorageException e) {
	  System.err.println("Could not save ontology: " + e.getMessage());
	}
  }
}
