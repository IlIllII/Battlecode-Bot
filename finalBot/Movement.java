package player35;

import battlecode.common.*;

public class Movement {

    final static int MINIMUM_BFS_COST = 2600;
    final static int FULL_BFS_COST = 4700;
    final static int THREE_QUARTER_BFS_COST = 3700;
    final static int HALF_BFS_BFS_COST = MINIMUM_BFS_COST;
    final static int REALLY_HIGH_RUBBLE_VALUE = 200;

    /**
     * Multiteired movement dispatch based on the the amount of bytecode remaining.
     * Prefers
     * to use the most expensive, most effective pathing algorithm possible. This
     * means you
     * should call move near the end of your turn because it eat up remaining
     * bytecode if it
     * can.
     * 
     * Additionally, if you pass in the third previous location you visited, move
     * will try not to
     * revisit this location. This can help with avoiding pathological pathing
     * situations but
     * interferes with micro during combat, so it can help to pass a bogus parameter
     * to
     * lastLastLocation if a unit is in combat.
     * 
     * @param rc
     * @param target
     * @param lastLastLocation
     * @return boolean denoting whether we moved or not
     * @throws GameActionException
     */
    static boolean move(RobotController rc, MapLocation target, MapLocation lastLastLocation)
            throws GameActionException {

        // * CONSIDER: May overflow bytecode when falling back
        // to simpleMove because we don't account for that cost.
        int bytecodeLeft = Clock.getBytecodesLeft();

        if (rc.getLocation().equals(target)) {
            rc.setIndicatorString("already on target");
            return false;
        }

        if (bytecodeLeft > MINIMUM_BFS_COST) {

            Direction dir = getBestDirectionUsingBFS(rc, target, bytecodeLeft);

            if (dir != null && !dir.equals(Direction.CENTER)) {
                if (!rc.getLocation().equals(lastLastLocation)) {
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                        rc.setIndicatorString("using BFS");
                        return true;
                    } else {
                        rc.setIndicatorString("BFS lead to bad loc");
                        simpleMove(rc, target);
                    }
                } else {
                    rc.setIndicatorString("simpleMove d/t lastLocation");
                    simpleMove(rc, target);
                }
            } else {
                rc.setIndicatorString("simpleMove d/t bfs==null/center");
                simpleMove(rc, target);
            }

        } else {
            rc.setIndicatorString("simpleMove d/t bytecode");
            simpleMove(rc, target);
            return true;
        }
        // rc.setIndicatorString("didn't move for some reason");
        return false;
    }

    /**
     * Simple, cheap movement logic to move toward a location without ever
     * moving to a rubble tile that is worse than the one the unit is currently on.
     * 
     * Useful when in a combat situation and you really do not want to move to a
     * high
     * rubble tile no matter what.
     * 
     * @param rc
     * @param target
     * @return boolean denoting whether unit moved or not
     * @throws GameActionException
     */
    public static boolean fallingBackMove(RobotController rc, Direction directionToTarget) throws GameActionException {

        /*
         * Basically we loop over 3 possible map locations to select the
         * best one, but we unroll it to make it harder to read.
         */
        MapLocation myLoc = rc.getLocation();
        Direction dir = directionToTarget;
        int baseRubble = rc.senseRubble(myLoc);
        if (rc.isMovementReady()) {
            Direction leftDir = dir.rotateLeft();
            Direction rightDir = dir.rotateRight();

            int rubbleLeft = rc.canSenseLocation(rc.adjacentLocation(leftDir)) && rc.canMove(leftDir)
                    ? rc.senseRubble(rc.adjacentLocation(leftDir))
                    : REALLY_HIGH_RUBBLE_VALUE;
            int rubbleMiddle = rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.canMove(dir)
                    ? rc.senseRubble(rc.adjacentLocation(dir))
                    : REALLY_HIGH_RUBBLE_VALUE;
            int rubbleRight = rc.canSenseLocation(rc.adjacentLocation(rightDir)) && rc.canMove(rightDir)
                    ? rc.senseRubble(rc.adjacentLocation(rightDir))
                    : REALLY_HIGH_RUBBLE_VALUE;

            boolean turnLeft = false;
            boolean turnRight = false;
            int minValue = rubbleMiddle;

            if (rubbleLeft < minValue) {
                minValue = rubbleLeft;
                turnLeft = true;
            }
            if (rubbleRight < minValue) {
                minValue = rubbleRight;
                turnLeft = false;
                turnRight = true;
            }

            dir = turnLeft ? dir.rotateLeft() : turnRight ? dir.rotateRight() : dir;

            if (rc.canSenseLocation(rc.adjacentLocation(dir)) && rc.senseRubble(rc.adjacentLocation(dir)) <= baseRubble && rc.canMove(dir)) {
                rc.move(dir);
                return true;
            } else {
                // ? CONSIDER: Is this a good idea?
                stepOffRubble(rc, myLoc);
            }
        }
        return false;
    }

    /**
     * Use BFS style movement without ever stepping on rubble. In effect we
     * use BFS pathing if it doesn't put us on a worse tile, otherwise we resort
     * to fallingBackMove, a cheaper move that won't step on rubble.
     * 
     * @param rc
     * @param target
     * @return boolean denoting whether unit moved
     * @throws GameActionException
     */
    static boolean moveButDontStepOnRubble(RobotController rc, MapLocation target) throws GameActionException {

        // TODO: Can we make a BFS that prunes paths whose initial directions
        // put us on worse rubble?
        int bytecodeLeft = Clock.getBytecodesLeft();
        if (bytecodeLeft > MINIMUM_BFS_COST) {

            Direction dir = getBestDirectionUsingBFS(rc, target, bytecodeLeft);
            if (dir != null && !dir.equals(Direction.CENTER)) {
                if (rc.canMove(dir)) {
                    if (rc.senseRubble(rc.getLocation()) >= rc.senseRubble(rc.adjacentLocation(dir))) {
                        rc.move(dir);
                        return true;
                    }
                }
            }
        } else {
            fallingBackMove(rc, rc.getLocation().directionTo(target));
        }

        return false;
    }

    /**
     * Simple BugNav style movement.
     * 
     * @param rc
     * @param target
     * @return boolean denoting whether the unit moved or not.
     * @throws GameActionException
     */
    static boolean simpleMove(RobotController rc, MapLocation target) throws GameActionException {

        if (!rc.isMovementReady()) {
            return false;
        }

        MapLocation myLoc = rc.getLocation();
        Direction dir = myLoc.directionTo(target);
        int numDirections = 8;
        for (int i = numDirections; --i >= 0;) {

            if (rc.canMove(dir)) {
                rc.move(dir);
                return true;
            }
            dir = Statics.rotateLeft ? dir.rotateLeft() : dir.rotateRight();
        }
        return false;
    }

    /**
     * Find a proximate tile that has less rubble than the current tile the unit is
     * on and move there.
     * 
     * The order in which surrounding tiles are assessed and possibly moved to is
     * undefined, so there may or may not be bias in the way units step off rubble.
     * 
     * @param rc
     * @param me
     * @return boolean denoting whether the unit moved.
     * @throws GameActionException
     */
    static boolean stepOffRubble(RobotController rc, MapLocation me) throws GameActionException {
        if (!rc.isMovementReady()) {
            return false;
        }

        int baseRubble = rc.senseRubble(me);
        if (baseRubble == 0) {
            return false;
        }

        int rubble;
        MapLocation[] locations = rc.getAllLocationsWithinRadiusSquared(me, 2);
        Direction moveDirection = Direction.CENTER;

        for (int i = locations.length - 1; i >= 0; i--) {
            if (rc.canSenseLocation(locations[i]) && !rc.canSenseRobotAtLocation(locations[i])) {
                rubble = rc.senseRubble(locations[i]);
                if (rubble < baseRubble) {
                    moveDirection = me.directionTo(locations[i]);
                    baseRubble = rubble;
                }
            }
        }

        if (rc.canMove(moveDirection)) {
            rc.move(moveDirection);
            return true;
        }
        return false;
    }

    private static Direction getBestDirectionUsingBFS(RobotController rc, MapLocation target, int bytecodeLeft) {
        Direction dir = null;
        if (bytecodeLeft > FULL_BFS_COST) {
            dir = FullBFS.getBestDir(rc, target);
        } else if (bytecodeLeft > THREE_QUARTER_BFS_COST) {
            dir = ThreeQuarterBFS.getBestDir(rc, target);
        } else if (bytecodeLeft > HALF_BFS_BFS_COST) {
            dir = HalfBFS.getBestDir(rc, target);
        }
        return dir;
    }
}
