/**
 * 
 */
package uk.ac.ed.inf.sdp2012.group7.strategy.planning;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 */

/**
 * @author twig
 *
 */
public class PlanningBuffer implements Observer {

	private Plan held_plan;
	private boolean run;
	
	public PlanningBuffer(){
		   
		
	}
	
	
	public Plan getHeld_plan() {
		return this.held_plan;
	}

	@Override
	public void update(Observable o, Object arg) {
		synchronized(this){
			
		}
		
	}

}