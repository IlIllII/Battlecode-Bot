package player35;

import battlecode.common.*;

strictfp class MinerStrategy {

    static final int EXPLORATION_FIDELITY = 9;
    static final int START_HEALING_CUTOFF = 15;
    static final int MAX_HP = 40;
    static final int RETREAT_FIDELITY = 9;
    static final int STRIP_MINE_ARCHON_PROXIMITY_CUTOFF = 144;
    static final int STANDARD_MIN_LEAD_MINING_CUTOFF = 10;
    static final int STRIP_MINING_MIN_LEAD_MINING_CUTOFF = 0;
    static final int STANDARD_MIN_LEAD_TO_LEAVE_ON_A_TILE = 1;
    static final int STRIP_MINING_MIN_LEAD_TO_LEAVE_ON_A_TILE = 0;
    static final int ADJACENT_TILE_RANGE = 2;
    static final int VISION_RADIUS_SQUARED = 20;
    static int age = 0;
    static boolean healing = false;
    static boolean retreating = false;
    static boolean selfDestructing = false;
    static MapLocation zeroZero = new MapLocation(0, 0);
    static MapLocation globalTarget;
    static MapLocation backupRetreatTarget;
    static MapLocation lastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLastLocation = new MapLocation(0, 0);
    static MapLocation corner = null;

    static void run(RobotController rc) throws GameActionException {

        

        int currentHealth = rc.getHealth();
        MapLocation me = rc.getLocation();
        MapLocation fleeTarget = null;
        RobotInfo[] allies = rc.senseNearbyRobots(-1, Statics.player);
        RobotInfo[] enemies = rc.senseNearbyRobots(-1, Statics.opponent);
        boolean dontSteal = true;
        boolean fleeing = false;

        if (!lastLocation.equals(me)) {
            lastLastLastLocation = lastLastLocation;
            lastLastLocation = lastLocation;
            lastLocation = me;
        }

        // * Setting targets - globalTarget (for exploring) -> localTarget (has metal)
        age++;
        if (age == 1) {
            backupRetreatTarget = me; // HACK: need b/c something wrong with comms.

            int x = Statics.mapWidth - me.x > Statics.mapWidth / 2 ? 0 : Statics.mapWidth - 1;
            int y = Statics.mapHeight - me.y > Statics.mapHeight / 2 ? 0 : Statics.mapHeight - 1;
            corner = new MapLocation(x, y);
        }

        if (globalTarget == null) {
            globalTarget = Statics.getRandomMapLocation();
        }

        if (me.distanceSquaredTo(globalTarget) <= EXPLORATION_FIDELITY) {
            globalTarget = Statics.getRandomMapLocation();
        }

        MapLocation target = globalTarget;

        // Only steal when in enemy territory
        if (allies.length > 0) {
            dontSteal = true;
        }

        // * Should we flee/retreat?
        if (enemies.length > 0) {
            for (int i = enemies.length - 1; i >= 0; i--) {
                Comms.setEnemyLocation(rc, enemies[i].location, Comms.getEnemyLocations(rc));

                if (enemies[i].type.equals(RobotType.SOLDIER) || enemies[i].type.equals(RobotType.WATCHTOWER)
                        || enemies[i].type.equals(RobotType.SAGE)) {

                    fleeing = true;
                    fleeTarget = Targeting.getFallbackTarget(me, enemies[i].location);

                    // ? Should this clamping be in targeting?
                    if (fleeTarget.x >= 0 && fleeTarget.y >= 0 && fleeTarget.x <= Statics.mapWidth
                            && fleeTarget.y <= Statics.mapHeight) {
                        globalTarget = fleeTarget;
                    }
                    break;
                }
            }
        }

        // Passes target through if no mineable metals nearby.
        target = findNearbyMetals(rc, me, target, fleeing, dontSteal, allies, backupRetreatTarget);

        if (allies.length > 0 && rc.senseLead(me) <= 1) {
            for (int i = allies.length - 1; i >= 0; i--) {
                if (allies[i].type == RobotType.MINER) {
                    if (target.distanceSquaredTo(allies[i].location) <= 4) {
                        target = globalTarget;
                        break;
                    }
                }
            }
        }

        if (currentHealth < START_HEALING_CUTOFF) {
            retreating = true;
            target = backupRetreatTarget;
        }

        if (currentHealth == MAX_HP) {
            retreating = false;
        }

        if (retreating && !selfDestructing) {

            MapLocation retreatTarget = Comms.getNearestArchonLocation(rc, me);
            if (retreatTarget == null) {
                retreatTarget = backupRetreatTarget;
            }

            if (retreatTarget != null) {
                Movement.move(rc, retreatTarget, lastLastLocation);
                if (me.distanceSquaredTo(retreatTarget) <= RETREAT_FIDELITY) {
                    selfDestructing = true;
                }
            }
        } else if (selfDestructing) {
            MapLocation nearestFreeTile = Targeting.findNearestEmptyTile(rc, me);

            if (nearestFreeTile != null) {
                Movement.move(rc, nearestFreeTile, lastLastLocation);
                if (rc.getLocation().equals(nearestFreeTile)) {
                    rc.disintegrate();
                }

            } else {
                Movement.move(rc, target, lastLastLocation);
            }
        }

        if (!retreating) {
            if (fleeing && fleeTarget != null) {
                Movement.move(rc, fleeTarget, lastLastLocation);
            } else if (!me.equals(target) && !target.equals(corner)) {
                Movement.move(rc, target, lastLastLastLocation);
            } else {
                Movement.stepOffRubble(rc, me);
            }
        }
        rc.setIndicatorLine(me, target, 0, 0, 0);

        if (fleeTarget != null) {
            rc.setIndicatorLine(me, fleeTarget, 1000, 0, 0);
        }

        if (corner != null & me.equals(corner)) {
            rc.disintegrate();
        }

    }

    static MapLocation findNearbyMetals(RobotController rc, MapLocation me, MapLocation target, boolean fleeing,
            boolean dontSteal, RobotInfo[] allies, MapLocation backupRetreatLocation) throws GameActionException {

        int distanceToTarget = me.distanceSquaredTo(target);
        int leadCount = 0;
        int minDistance = 10000;
        int miningCutoff = fleeing ? 0 : 1;
        int leadCutoff = STANDARD_MIN_LEAD_MINING_CUTOFF;

        for (ArchonLocation archLoc : Comms.getArchonLocations(rc)) {
            if (!archLoc.location.equals(zeroZero) && me.distanceSquaredTo(archLoc.location) < minDistance) {
                minDistance = me.distanceSquaredTo(archLoc.location);
            }
        }

        if (me.distanceSquaredTo(backupRetreatLocation) < minDistance) {
            minDistance = me.distanceSquaredTo(backupRetreatLocation);
        }

        if (minDistance != 10000 && minDistance > STRIP_MINE_ARCHON_PROXIMITY_CUTOFF) {
            dontSteal = false;
        } else if (minDistance < STRIP_MINE_ARCHON_PROXIMITY_CUTOFF) {
            dontSteal = true;
        }

        if (dontSteal) {
            miningCutoff = STANDARD_MIN_LEAD_TO_LEAVE_ON_A_TILE;
        } else {
            miningCutoff = STRIP_MINING_MIN_LEAD_TO_LEAVE_ON_A_TILE;
            leadCutoff = STRIP_MINING_MIN_LEAD_MINING_CUTOFF;
        }

        MapLocation[] locsWithLead = rc.senseNearbyLocationsWithLead(VISION_RADIUS_SQUARED);
        for (int i = locsWithLead.length; --i >= 0;) {
            leadCount = rc.senseLead(locsWithLead[i]);
            if (leadCount > leadCutoff) {
                int distanceToLoc = me.distanceSquaredTo(locsWithLead[i]);
                if (distanceToLoc < distanceToTarget) {
                    target = locsWithLead[i];
                    distanceToTarget = distanceToLoc;
                }
            }

            if (leadCount > miningCutoff && me.distanceSquaredTo(locsWithLead[i]) <= ADJACENT_TILE_RANGE) {
                while (rc.isActionReady() && rc.senseLead(locsWithLead[i]) > miningCutoff) {
                    rc.mineLead(locsWithLead[i]);
                }
            }
        }

        // TODO: Should prioritize nearest gold tile
        MapLocation[] locsWithGold = rc.senseNearbyLocationsWithGold(VISION_RADIUS_SQUARED);
        for (int i = locsWithGold.length - 1; i >= 0;) {
            target = locsWithGold[i];
            if (me.distanceSquaredTo(target) <= ADJACENT_TILE_RANGE) {
                while (rc.senseGold(target) > 0 && rc.isActionReady()) {
                    rc.mineGold(target);
                }
            }
            break;
        }

        return target;
    }
}
