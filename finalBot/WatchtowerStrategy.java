package player35;

import battlecode.common.*;


strictfp class WatchtowerStrategy {

    static final int VISION_RADIUS_SQUARED = 34;
    static final int ACTION_RADIUS_SQUARED = 20;
    static final int MINIMUM_DISTANCE_TO_ARCHON_FOR_DEFENSE = 36;
    static final int MINIMUM_DISTANCE_TO_ENEMY_FOR_DEFENSE = 49;
    static final int MAX_DISTANCE_TO_TARGET_BEFORE_MOVING = 81;
    static final int HALF_TIME = 1000;
    static int turretModeCounter = 0;
    static MapLocation repairLocation;
    static MapLocation backupLocation = Statics.getRandomMapLocation();
    static MapLocation lastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLastLocation = new MapLocation(0, 0);

    static void run(RobotController rc) throws GameActionException {
        
        // * Setting main variables
        int round = rc.getRoundNum();
        MapLocation me = rc.getLocation();
        RobotInfo[] enemies = rc.senseNearbyRobots(VISION_RADIUS_SQUARED, Statics.opponent);
        MapLocation offensiveTarget = Targeting.getOffensiveTarget(rc);
        MapLocation defensiveTarget = Targeting.getDefensiveTarget(rc);
        MapLocation target = Targeting.getTargetFromGlobalAndLocalEnemyLocations(rc, enemies, backupLocation);

        // * These values vary based on current conditions
        if (!lastLocation.equals(me)) {
            lastLastLastLocation = lastLastLocation;
            lastLastLocation = lastLocation;
            lastLocation = me;
        }
        
        // * Targeting cascade
        if (target == null) {
            backupLocation = Statics.getRandomMapLocation();
            target = backupLocation;
        }
        if (target.equals(backupLocation)) {
            if (offensiveTarget != null) {
                target = offensiveTarget;
            }
            if (defensiveTarget != null) {
                target = defensiveTarget;
            }
        }
        if (defensiveTarget != null && me.distanceSquaredTo(target) > MINIMUM_DISTANCE_TO_ENEMY_FOR_DEFENSE && me.distanceSquaredTo(defensiveTarget) > MINIMUM_DISTANCE_TO_ARCHON_FOR_DEFENSE) {
            target = defensiveTarget;
        }


        // Attack if able before moving.
        if (rc.isActionReady() && rc.getMode().equals(RobotMode.TURRET)) {
            if (rc.canAttack(target)) {
                rc.attack(target);
            }
        }

        // We transform when it has been a while since we've seen an enemy
        if (enemies.length == 0 && rc.getMode().equals(RobotMode.TURRET)) {
            turretModeCounter++;
            if (round < HALF_TIME) {
                if (turretModeCounter > 15 && me.distanceSquaredTo(target) > MAX_DISTANCE_TO_TARGET_BEFORE_MOVING) {
                    if (rc.isTransformReady() && rc.canTransform()) {
                        rc.transform();
                    }
                }
            } else {
                if (turretModeCounter > 15 && me.distanceSquaredTo(target) > ACTION_RADIUS_SQUARED) {
                    if (rc.isTransformReady() && rc.canTransform()) {
                        rc.transform();
                    }
                }
            }
        } else if (enemies.length > 0 && rc.getMode().equals(RobotMode.TURRET)) {
            turretModeCounter = 0;
        }

        if (rc.getMode().equals(RobotMode.PORTABLE)) {
            if (round < HALF_TIME) {
                if (enemies.length > 0) { // Transform if enemies are in range

                    // ? Should we step off rubble first? Test.
                    // Movement.stepOffRubble(rc, me);
    
                    if (rc.isTransformReady() && rc.canTransform()) {
                        rc.transform();
                    }
                } else { // We movin'
                    if (rc.isMovementReady()) {
                        Movement.move(rc, target, lastLastLocation);
                    }
                }
            } else {
                if (me.distanceSquaredTo(target) <= ACTION_RADIUS_SQUARED) {
                    if (rc.isTransformReady() && rc.canTransform()) {
                        rc.transform();
                    }
                } else { // We movin'
                    if (rc.isMovementReady()) {
                        Movement.move(rc, target, lastLastLocation);
                    }
                }
            }
        }
    }
}
