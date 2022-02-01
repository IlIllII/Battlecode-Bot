package player35;

import battlecode.common.*;

strictfp class SoldierStrategy {

    static final int BEGIN_HEALING_CUTOFF = 25;
    static final int ACTION_RADIUS_SQUARED = 13;
    static final int FINISHED_HEALING_CUTOFF = 48;
    static final int MINIMUM_DISTANCE_TO_ARCHON_FOR_DEFENSE = 36;
    static final int MINIMUM_DISTANCE_TO_ENEMY_FOR_DEFENSE = 49;
    static final int MINIMUM_DISTANCE_TO_ENEMY_FOR_OFFENSE = 64;
    static final int MINIMUM_DISTANCE_TO_ARCHON_BEFORE_SELF_DESTRUCT = 25;
    static final int CUTOFF_FOR_NEARBY_ALLIES_WHEN_ADVANCING = 4;
    static final int SELF_DESTRUCT_TIMER_CUTOFF = 10;
    static final int HEALTH_CUTOFF_FOR_SELF_DESTRUCT = 20;
    static final int ADJACENT_RADIUS_SQUARED = 2;
    static int aliveTime = 0;
    static int lastTurnsHealth = 0;
    static int selfDestructTimer = 0;
    static boolean retreating = false;
    static boolean selfDestructing = false;
    static MapLocation backupLocation = Statics.getRandomMapLocation();
    static MapLocation backupRetreatTarget;
    static MapLocation lastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLastLocation = new MapLocation(0, 0);

    static void run(RobotController rc) throws GameActionException {

        boolean fallingBack = false;
        boolean enemySoldierClose = false;
        boolean attacked = false;
        int currentHealth = rc.getHealth();
        MapLocation me = rc.getLocation();
        RobotInfo[] enemies = rc.senseNearbyRobots(ACTION_RADIUS_SQUARED, Statics.opponent);
        MapLocation offensiveTarget = Targeting.getOffensiveTarget(rc);
        MapLocation defensiveTarget = Targeting.getDefensiveTarget(rc);
        MapLocation target = Targeting.getTargetFromGlobalAndLocalEnemyLocations(rc, enemies, backupLocation);

        aliveTime++;
        if (aliveTime == 1) {
            backupRetreatTarget = me;
        }

        if (!lastLocation.equals(me)) {
            lastLastLastLocation = lastLastLocation;
            lastLastLocation = lastLocation;
            lastLocation = me;
        }

        if (selfDestructing) {
            selfDestructTimer++;
        }

        if (enemies.length > 0 || lastTurnsHealth != currentHealth) {
            selfDestructing = false;
            selfDestructTimer = 0;
        }

        if (currentHealth <= BEGIN_HEALING_CUTOFF && rc.getRoundNum() < 1000) {
            retreating = true;
        } else if (rc.getRoundNum() > 1000) {
            retreating = false;
            RobotInfo[] closeByTeammates = rc.senseNearbyRobots(2, rc.getTeam());
            if (closeByTeammates.length > 4 && !retreating) {
                Movement.move(rc, target, lastLastLocation);
            }
        }

        if (currentHealth > FINISHED_HEALING_CUTOFF) { // leave wiggle room heal as we move away
            retreating = false;
            selfDestructing = false;
            selfDestructTimer = 0;
        }

        lastTurnsHealth = currentHealth;

        // * Are there dangerous soldiers nearby?
        // We care because we want to retreat after attacking them.
        for (int i = enemies.length - 1; i >= 0; i--) {
            if (enemies[i].type == RobotType.SOLDIER) {
                enemySoldierClose = true;
                break;
            }
        }

        // * Targeting cascade
        // Fall back to backup target as default. Might not be needed d/t new targeting
        // scheme.
        if (target == null) {
            backupLocation = Statics.getRandomMapLocation();
            target = backupLocation;
        }
        // If offensive or defensive targets exist, we will prefer those.
        if (target.equals(backupLocation)) {
            if (offensiveTarget != null) {
                target = offensiveTarget;
            }
            if (defensiveTarget != null) {
                target = defensiveTarget;
            }
        }
        // We want to prefer defense when not engaged but still pursue enemies attacking
        // our archon.
        if (defensiveTarget != null && me.distanceSquaredTo(target) > MINIMUM_DISTANCE_TO_ENEMY_FOR_DEFENSE
                && me.distanceSquaredTo(defensiveTarget) > MINIMUM_DISTANCE_TO_ARCHON_FOR_DEFENSE) {
            target = defensiveTarget;
        }

        // * This is an optional group up before rushing
        // if (rc.getRoundNum() < 60) {
        // target =
        // rc.adjacentLocation(RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)]);
        // }

        // Advance when surrounded by allies.
        RobotInfo[] closeByTeammates = rc.senseNearbyRobots(ADJACENT_RADIUS_SQUARED, Statics.player);
        if (closeByTeammates.length > CUTOFF_FOR_NEARBY_ALLIES_WHEN_ADVANCING && !retreating) {
            Movement.move(rc, target, lastLastLocation);
        }

        // Attack if able before moving.
        // ? Should we be smarter about this and move off rubble if able?
        if (rc.isActionReady()) {
            if (rc.canAttack(target)) {
                rc.attack(target);
                attacked = true;
            }
        }

        // If we are engaged with enemy soldiers, we want to fall back if we can't
        // attack
        if (!rc.isActionReady() && enemySoldierClose) {
            int x = (target.x - me.x);
            int y = (target.y - me.y);
            target = new MapLocation(me.x - x, me.y - y);
            fallingBack = true;
        }

        // * Movement dispatch.
        if (rc.isMovementReady()) {
            if (!retreating) { // We are advancing.
                if (!fallingBack) { // We are really advancing

                    if (attacked || rc.getLocation().distanceSquaredTo(target) <= ACTION_RADIUS_SQUARED) {
                        // We do not want to move onto rubble if we are in combat, but
                        // we also want to move if there aren't enemies in attack range.
                        Movement.moveButDontStepOnRubble(rc, target);
                    } else {
                        // If we haven't attacked yet it means we are advancing so we move like normal.
                        Movement.move(rc, target, lastLastLastLocation);
                    }

                } else {
                    // Special move where we do not fall back onto rubble.
                    Movement.fallingBackMove(rc, me.directionTo(target));
                }

            } else { // Retreating
                if (!selfDestructing) { // We are going to archon
                    MapLocation retreatTarget = Comms.getNearestArchonLocation(rc, me);

                    if (retreatTarget == null) {
                        retreatTarget = backupRetreatTarget;
                    }

                    if (retreatTarget != null) {
                        // First check that we don't have to defend our archon
                        if (defensiveTarget != null
                                && me.distanceSquaredTo(target) > MINIMUM_DISTANCE_TO_ENEMY_FOR_OFFENSE) {
                            if (!fallingBack) {
                                Movement.move(rc, target, lastLastLastLocation);
                            } else {
                                Movement.fallingBackMove(rc, me.directionTo(target));
                            }
                        } else {
                            Movement.move(rc, retreatTarget, lastLastLocation);
                            if (me.distanceSquaredTo(retreatTarget) <= MINIMUM_DISTANCE_TO_ARCHON_BEFORE_SELF_DESTRUCT
                                    && enemies.length == 0) {
                                selfDestructing = true;
                            }
                        }

                    } else {
                        System.out.println("Couldnt find retreat target");
                    }
                } else { // We are self-destructing
                    MapLocation nearestFreeTile = Targeting.findNearestEmptyTile(rc, me);
                    if (nearestFreeTile != null && !enemySoldierClose) {

                        Movement.move(rc, nearestFreeTile, lastLastLocation);
                        if (rc.getLocation().equals(nearestFreeTile)) {
                            if (selfDestructTimer > SELF_DESTRUCT_TIMER_CUTOFF) {
                                if (currentHealth < HEALTH_CUTOFF_FOR_SELF_DESTRUCT) {
                                    rc.disintegrate();
                                } else {
                                    selfDestructing = false;
                                    retreating = false;
                                }
                            }
                        }

                    } else { // We don't have a free tile to destruct on
                        Movement.move(rc, target, lastLastLocation);
                    }
                }
            }

        }

        // Check for a cheeky attack on the tail end
        if (rc.isActionReady()) {
            if (rc.canAttack(target)) {
                rc.attack(target);
            }
        }
    }
}