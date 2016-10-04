package org.cocopapaya.eajod;

import java.util.ArrayList;
import java.util.List;

import org.sparx.Collection;
import org.sparx.Diagram;
import org.sparx.Package;

public aspect EAPackageAspect {

	public String Package.getName(){
		return this.GetName();
	}

	public List<Diagram> Package.getDiagrams(){
		
		List<Diagram> diagrams = new ArrayList<>();
		
		Collection<Diagram> theseDiagrams =  this.GetDiagrams();
		
		for( Diagram current : theseDiagrams){
			diagrams.add(current);
		}
		
		return diagrams;

	}
}
