package player35;

import battlecode.common.*;

strictfp class SageStrategy {

    static final int ACTION_RADIUS_SQUARED = 20;
    static final int START_HEALING_CUTOFF = 50;
    static final int LOW_RUBBLE_TILE_SEARCH_RADIUS = 16;
    static final int MINIMUM_DISTANCE_TO_ARCHON_FOR_DEFENSE = 36;
    static final int MINIMUM_DISTANCE_TO_ENEMY_FOR_DEFENSE = 49;
    static int aliveTime = 0;
    static int lastTurnsHealth = 100;
    static boolean retreating = false;
    static MapLocation repairLocation;
    static MapLocation backupLocation = Statics.getRandomMapLocation();
    static MapLocation lastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLastLocation = new MapLocation(0, 0);
    static MapLocation backupRetreatTarget;

    static void run(RobotController rc) throws GameActionException {

        boolean fallingBack = false;
        boolean enemySoldierClose = false;
        boolean attacked = false;
        int currentHealth = rc.getHealth();
        MapLocation me = rc.getLocation();
        RobotInfo[] enemies = rc.senseNearbyRobots(ACTION_RADIUS_SQUARED, Statics.opponent);
        MapLocation offensiveTarget = Targeting.getOffensiveTarget(rc);
        MapLocation defensiveTarget = Targeting.getDefensiveTarget(rc);
        MapLocation target = Targeting.getTargetFromGlobalAndLocalEnemyLocationsMAXHP(rc, enemies, backupLocation);

        aliveTime++;
        if (aliveTime == 1) {
            backupRetreatTarget = me;
        }
        if (!lastLocation.equals(me)) {
            lastLastLastLocation = lastLastLocation;
            lastLastLocation = lastLocation;
            lastLocation = me;
        }
        if (currentHealth <= START_HEALING_CUTOFF) { // TODO Should this be higher even?
            retreating = true;
        }
        if (currentHealth > 98) { // leave wiggle room heal as we move away
            retreating = false;
        }
        lastTurnsHealth = currentHealth;

        // * Are there dangerous soldiers nearby?
        // We care because we want to retreat after attacking them.
        // TODO: This can be more efficient - we iterate over enemies elsewhere.
        for (RobotInfo enemy : enemies) {
            if (enemy.type == RobotType.SOLDIER && me.distanceSquaredTo(enemy.location) <= 16) {
                enemySoldierClose = true;
                break;
            }
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
        if (defensiveTarget != null && me.distanceSquaredTo(target) > MINIMUM_DISTANCE_TO_ENEMY_FOR_DEFENSE
                && me.distanceSquaredTo(defensiveTarget) > MINIMUM_DISTANCE_TO_ARCHON_FOR_DEFENSE) {
            target = defensiveTarget;
        }

        // Attack if able before moving.
        if (rc.isActionReady()) {

            // We need to move to a nice tile before attacking otherwise
            // CD will be too high.

            if (rc.canAttack(target) && rc.senseRubble(me) <= 40) {
                rc.attack(target);
                attacked = true;
            }
        }

        // If we are engaged with enemy soldiers, we want to fall back
        // if (!rc.isActionReady() && enemySoldierClose) {
        //     int x = (target.x - me.x);
        //     int y = (target.y - me.y);
        //     target = new MapLocation(me.x - x, me.y - y);
        //     fallingBack = true;
        // }

        if (enemySoldierClose) {
            retreating = true;
        }

        // * Movement dispatch.
        if (rc.isMovementReady()) {
            if (!retreating) { // We are advancing.
                if (!fallingBack) {
                    if (attacked || rc.getLocation().distanceSquaredTo(target) <= rc.getType().actionRadiusSquared) {
                        Movement.moveButDontStepOnRubble(rc, target);
                    } else {
                        Movement.move(rc, target, lastLastLastLocation);
                    }
                } else {
                    Movement.fallingBackMove(rc, me.directionTo(target));
                }
            } else { // Retreating
                MapLocation retreatTarget = Comms.getNearestArchonLocation(rc, me);

                if (retreatTarget == null) {
                    retreatTarget = backupRetreatTarget;
                }

                if (retreatTarget != null) {
                    Movement.move(rc, retreatTarget, lastLastLocation);
                }
            }
        }
    }
}
