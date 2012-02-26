package uk.ac.ed.inf.sdp2012.group7.strategy.planning;

import java.awt.Point;

import uk.ac.ed.inf.sdp2012.group7.vision.worldstate.WorldState;

public class AllMovingObjects {
	
	private Point ourPosition ;
	private Point theirPosition;
	private Point ballPosition;
	private double ourVelocity;
	private double theirVelocity;
	private double ballVelocity;
	private double ourAngle;
	private double theirAngle;
	private double ballAngle;
	
	//worldstate getInstance
	public WorldState worldState;
	
	public AllMovingObjects() {
		
		worldState = WorldState.getInstance();
		
		this.ourPosition = worldState.getOurRobot().getPosition().getCentre();
		
		this.theirPosition = worldState.getOpponentsRobot().getPosition().getCentre();
		
		this.ballPosition = worldState.getBall().getPosition().getCentre();
		
		this.ourVelocity = worldState.getOurRobot().getVelocity();
		
		this.theirVelocity = worldState.getOpponentsRobot().getVelocity();
		
		this.ballVelocity = worldState.getBall().getVelocity();
		
	    this.ourAngle = worldState.getOurRobot().getAngle();
	    
	    this.theirAngle = worldState.getOpponentsRobot().getAngle();
	    
	    this.ballAngle = worldState.getBall().getAngle();
		
	}
	
	public Point getOurPosition() {
		return ourPosition;
	}
	
	public Point getTheirPosition() {
		return theirPosition;
	}
	
	public Point getBallPosition() {
		return ballPosition;
	}
	
	public double getOurVelocity() {
		return ourVelocity;
	}
	
	public double getTheirVelocity() {
		return theirVelocity;
	}
	
	public double getBallVelocity() {
		return ballVelocity;
	}
	
	public double getOurAngle() {
		return ourAngle;
	}
	
	public double getTheirAngle() {
		return theirAngle;
	}
	
	public double getBallAngle() {
		return ballAngle;
	}
	
	

}
