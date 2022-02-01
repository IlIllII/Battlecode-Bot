package player35;

import battlecode.common.*;

strictfp class BuilderStrategy {

    static final int ROUND_TO_STOP_LEAD_FARMING = 500;
    static final int WATCHTOWER_LEAD_COST = 150;
    static final int ADJACENT_TILE_RANGE = 2;
    static int round = 0;
    static boolean selfDestruct = false;
    static MapLocation backupLocation = Statics.getRandomMapLocation();
    static MapLocation lastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLastLocation = new MapLocation(0, 0);

    static void run(RobotController rc) throws GameActionException {

        MapLocation me = rc.getLocation();
        round = rc.getRoundNum();
        selfDestruct = true;
        int leadAmount = rc.getTeamLeadAmount(Statics.player);
        int goldAmount = rc.getTeamGoldAmount(Statics.player);
        RobotInfo[] allies = rc.senseNearbyRobots(-1, Statics.player);
        MapLocation repairSpot = new MapLocation(0, 0);
        boolean repairing = false;

        // Find nearest corner tile
        int x = Statics.mapWidth - me.x > Statics.mapWidth / 2 ? 0 : Statics.mapWidth - 1;
        int y = Statics.mapHeight - me.y > Statics.mapHeight / 2 ? 0 : Statics.mapHeight - 1;
        MapLocation target = new MapLocation(x, y);

        if (!lastLocation.equals(me)) {
            lastLastLastLocation = lastLastLocation;
            lastLastLocation = lastLocation;
            lastLocation = me;
        }

        // Should we lead farm, or build a lab, or build a watchtower?
        if (round > ROUND_TO_STOP_LEAD_FARMING) {
            selfDestruct = false;

            if (rc.canSenseLocation(target) && rc.canSenseRobotAtLocation(target)
                    && rc.senseRobotAtLocation(target).type.equals(RobotType.LABORATORY)) {
                selfDestruct = true;

            } else if (rc.canSenseLocation(target) && !rc.canSenseRobotAtLocation(target)) {
                if (rc.adjacentLocation(me.directionTo(target)).equals(target)
                        && rc.canBuildRobot(RobotType.LABORATORY, me.directionTo(target))) {
                    rc.buildRobot(RobotType.LABORATORY, me.directionTo(target));
                }

            }

            if (goldAmount > 0 && leadAmount > WATCHTOWER_LEAD_COST) {
                for (int i = 1; i < Statics.directions.length; i += 2) {
                    if (rc.canBuildRobot(RobotType.WATCHTOWER, Statics.directions[i])) {
                        rc.buildRobot(RobotType.WATCHTOWER, Statics.directions[i]);
                        break;

                    }
                }
            }
        }

        if (goldAmount > 0) {
            selfDestruct = true;
        }

        // Anything to repair?
        for (int i = allies.length - 1; i >= 0; i--) {
            if (((allies[i].type == RobotType.WATCHTOWER || allies[i].type == RobotType.LABORATORY)
                    && allies[i].mode == RobotMode.PROTOTYPE)
                    || (allies[i].type == RobotType.ARCHON && allies[i].getHealth() < RobotType.ARCHON.health) || (allies[i].type == RobotType.WATCHTOWER && allies[i].getHealth() < RobotType.WATCHTOWER.health)) {
                repairSpot = allies[i].location;
                repairing = true;
                break;
            }
        }

        // We should repair instead of self-destructing.
        if (repairing) {
            if (me.distanceSquaredTo(repairSpot) > ADJACENT_TILE_RANGE) {
                Movement.move(rc, repairSpot, lastLastLocation);
            }
            if (rc.canRepair(repairSpot)) {
                rc.repair(repairSpot);
            }
        }

        // We can self-destruct if we aren't doing anything
        if (selfDestruct && !repairing) {

            MapLocation nearestFreeTile = Targeting.findNearestEmptyTile(rc, me);
            if (nearestFreeTile != null) {
                Movement.move(rc, nearestFreeTile, lastLastLocation);
                if (rc.getLocation().equals(nearestFreeTile)) {
                    rc.disintegrate();
                }
            } else {
                if (!rc.adjacentLocation(me.directionTo(target)).equals(target)) {
                    Movement.move(rc, target, lastLastLocation);
                }
            }

        } else if (!selfDestruct) {
            if (!rc.adjacentLocation(me.directionTo(target)).equals(target)) {
                Movement.move(rc, target, lastLastLocation);
            }
        }
    }
}
