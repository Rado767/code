package uk.ac.ed.inf.sdp2012.group7.control;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import uk.ac.ed.inf.sdp2012.group7.shared.ConstantsReuse;
import uk.ac.ed.inf.sdp2012.group7.shared.RobotDetails;

/**
 * This class holds the geometric location of our robot but is also responsible
 * for communicating with it.
 */
public class RobotControl extends RobotDetails {

	private CommunicationInterface comms;
	private NXTComm nxtComm;
	private NXTInfo info = new NXTInfo(NXTCommFactory.BLUETOOTH, ConstantsReuse.ROBOT_NAME,
			ConstantsReuse.ROBOT_MAC);
	private Queue<Integer> commandList = new LinkedList<Integer>();

	private boolean connectToSimulator;
	private boolean isConnected = false;
	private boolean keepConnected = true;
	public boolean askingToReset = false;
	private volatile int currentSpeed = 0;

	/**
	 * The constructor takes a boolean to indicate if the object should
	 * communicate with NXT or the simulator.
	 */
	public RobotControl(boolean connectToSimulator) {
		this.connectToSimulator = connectToSimulator;
	}

	/**
	 * This method updates the location and angle of the robot.
	 */
	public void updateRobotDetails(RobotDetails me) {
		coors = me.getCoors();
		angle = me.getAngle();
		updateRect();
	}

	/**
	 * This method initialises the connection and starts the thread which sends
	 * data to the robot.
	 */
	public boolean startCommunications() {

		// start up the connection
		try {
			if (connectToSimulator) {
				//connectToSimulator();
			} else {
				connectToRobot();
			}
		} catch (IOException ex) {
			System.err.println("Robot Connection Failed: ");
			System.err.println(ex.toString());
			return false;
		}

		// start up the thread which sends commands
		new Thread(new Runnable() {
			public void run() {

				// send data when necessary
				while (keepConnected) {
					if (commandList.isEmpty()) {
						sendToRobot(ConstantsReuse.OpCodes.DO_NOTHING);
					} else {
						sendToRobot(commandList.remove());
					}
					receiveFromRobot();

					// Tools.rest(10);
				}
				// disconnect when we're done
				if (connectToSimulator) {
					disconnectFromSimulator();
				} else {
					disconnectFromRobot();
				}

			}
		}).start();

		return true;

	}

	/**
	 * Stops the connection with the Robot
	 */
	public void stopCommunications() {
		keepConnected = false;
	}

	/**
	 * Connects to the NXT
	 */
	private void connectToRobot() throws IOException {
		comms = new BluetoothCommunication(nxtComm, info);
		comms.openConnection();
		setConnected(true);
		beep();
	}

	/**
	 * Disconnect from the simulator
	 */
	private void disconnectFromSimulator() {
		try {
			comms.closeConnection();
			setConnected(false);
		} catch (Exception e) {
			System.err.println("Error Disconnecting from simulator");
			System.err.println(e.toString());
		}
	}

	/**
	 * Disconnect from the NXT
	 */
	private void disconnectFromRobot() {
		try {
			comms.closeConnection();
			nxtComm.close();
			setConnected(false);
		} catch (IOException ex) {
			System.err.println("Error Disconnecting from NXT");
			System.err.println(ex.toString());
		}
	}

	/**
	 * Add a command to the queue to be sent to the robot
	 */
	private void addCommand(int command) {
		while (commandList.size() > 3) {
			commandList.remove();
			System.out.print("<");
		}
		commandList.add(command);
	}

	/**
	 * Clear the queue of commands to be sent to the robot
	 */
	public void clearAllCommands() {
		commandList.clear();
	}

	/**
	 * Sends a command to the robot
	 */
	private void sendToRobot(int command) {
		// System.out.println("SENT "+command+" TO ROBOT");
		comms.sendToRobot(command);
	}

	/**
	 * Receive an integer from the robot
	 */
	private int receiveFromRobot() {

		int response = comms.recieveFromRobot();

		if (response == 'r') {
			askingToReset = true;
			// clearAllCommands();
			// System.out.println("STACK CLEARED");
		}

		return response;

	}

	/**
	 * Returns the last speed we set the robot to
	 */
	public int getSpeed() {
		return currentSpeed;
	}

	public boolean moving = true;

	/**
	 * Commands the robot to move forward
	 */
	public void moveForward() {
		moving = true;
		addCommand(ConstantsReuse.OpCodes.FORWARDS);
	}

	/**
	 * Commands the robot to move backward
	 */
	public void moveBackward() {
		moving = true;
		addCommand(CostantsReuse.OpCodes.BACKWARDS);
	}

	/**
	 * Commands the robot to move back a little bit
	 */
	public void moveBackwardSlightly() {
		addCommand(ConstantsResue.OpCodes.BACKWARDS_SLIGHTLY);
	}

	/**
	 * Commands the robot to stop where it is
	 */
	public void stop() {
		moving = false;
		addCommand(ConstantsReuse.OpCodes.STOP);
	}

	/**
	 * Sets the speed of the motors to a given integer (900 is the max)
	 */
	public void changeSpeed(int to) {
		int command = ConstantsReuse.OpCodes.CHANGE_SPEED | (to << 8);
		currentSpeed = to;
		addCommand(command);
	}

	/**
	 * Commands the robot to kick
	 */
	public void kick() {
		System.out.println("kick");
		addCommand(ConstantsReuse.OpCodes.KICK);
	}

	/**
	 * Rotates the robot by a given number of radians
	 */
	public void rotateBy(double radians) {

		System.out.println("Rotate by " + radians + ":  "
				+ Math.toDegrees(radians));

		if (radians < 0)
			radians = (2 * Math.PI - radians);
		if (radians != 0) {
			int command = ConstantsReuse.OpCodes.ROTATE | ((int) Math.toDegrees(radians) << 8);
			addCommand(command);
		}

	}

	/**
	 * This method instructs the robot to move around a circle of given radius,
	 * the boolean is true when the robot should arc left.
	 */
	public void circleWithRadius(int radius, boolean arcLeft) {

		// interpreted on the robot as a negative
		if (arcLeft)
			radius += 1000;

		int command = ConstantsReuse.OpCodes.ARC | (radius << 8);
		addCommand(command);

	}

	/**
	 * Commands steers the robot based on a given ratio
	 */
	public void steerWithRatio(float ratio) {
		int command = ConstantsReuse.OpCodes.STEER_WITH_RATIO | ((int) ratio << 8);
		addCommand(command);
	}

	/**
	 * Commands the robot to make a noise
	 */
	public void beep() {
		addCommand(ConstantsReuse.OpCodes.BEEP);
	}

	/**
	 * Commands the robot to play a tune
	 */
	public void celebrate() {
		addCommand(ConstantsReuse.OpCodes.CELEBRATE);
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		return isConnected;
	}

}
