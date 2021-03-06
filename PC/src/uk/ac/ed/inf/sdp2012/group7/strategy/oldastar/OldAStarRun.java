package uk.ac.ed.inf.sdp2012.group7.strategy.oldastar;

import java.awt.Point;
import java.util.ArrayList;

import uk.ac.ed.inf.sdp2012.group7.strategy.Strategy;
import uk.ac.ed.inf.sdp2012.group7.strategy.astar.heuristics.ClosestHeuristic;

public class OldAStarRun {
		
		private OldPath shortestPath;
		private OldAreaMap map;
		
		public OldAreaMap getAreaMap() {
			return map;
		}

		public OldAStarRun(int pitch_height_in_nodes, int pitch_width_in_nodes, Point ball, Point some_robot, ArrayList<Point> obstacles) {
			map = new OldAreaMap(pitch_width_in_nodes, pitch_height_in_nodes);
			
			// set obstacles
			if (obstacles.size() > 0){
				for (Point obstacle : obstacles) {
					if(!(obstacle.x < 0 || obstacle.y < 0)){
						try{
							map.setObstical(obstacle.x, obstacle.y, true);
						} catch (Exception ex){
							//Do Nothing
						}
					}
				}
			}
			
			// set heuristic and run the path finder
			OldAStarHeuristic heuristic = new ClosestHeuristic();
			OldAStar pathFinder = new OldAStar(map, heuristic);
			try{
//				Strategy.logger.error("start x: " + some_robot.x);
//				Strategy.logger.error("start y: " + some_robot.y);
//				Strategy.logger.error("ball x: " + ball.x);
//				Strategy.logger.error("ball y: " + ball.y);
				shortestPath = pathFinder.calcShortestPath(some_robot.x, some_robot.y, ball.x, ball.y);
			} catch (Exception ex) {
				Strategy.logger.error("Shortest path calculation failed: " + ex.getMessage());
				shortestPath = new OldPath();
			}
			// copied from A* for printing
//			Node node;
//			for(int x=0; x<map.getMapWidth(); x++) {
//				for(int y=0; y<map.getMapHeight(); y++) {
//					node = map.getNode(x, y);
//					//System.out.println(node.getX());
//					//System.out.println(node.getY());
//					if (node.isObstacle) {
//						System.out.print("O");
//					} else if (node.isStart) {
//						System.out.print("R");
//					} else if (node.isGoal()) {
//						System.out.print("B");
//					} else if (shortestPath.contains(node.getX(), node.getY())) {
//						System.out.print("X");
//					} else {
//						System.out.print("*");
//					}
//				}
//				System.out.println();
//			}
		}
		
		public OldPath getPath() {
			try{
				return shortestPath;
			} catch (Exception ex) {
				Strategy.logger.error("getPath return failed: " + ex.getMessage());
				return null;
			}
		}
		
		public ArrayList<Point> getPathInPoints() {
			try {
				return this.shortestPath.pathToPoints();
			} catch (Exception ex) {
				Strategy.logger.error("getPathInPoints return failed: " + ex.getMessage());
				return new ArrayList<Point>();
			}
		}
}
