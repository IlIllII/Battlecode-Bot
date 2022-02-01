package player35;

import battlecode.common.*;

public class Targeting {

    static final int EXPLORATION_FIDELITY = 4;

    /**
     * Returns a location of an enemy archon, or null if none found.
     * 
     * @param rc
     * @return
     * @throws GameActionException
     */
    static MapLocation getOffensiveTarget(RobotController rc) throws GameActionException {
        EnemyLocation enemyLocation = Comms.getOffensiveLocation(rc);

        if (!enemyLocation.exists) {
            return null;
        } else {
            if (rc.canSenseLocation(enemyLocation.location)
                    && rc.canSenseRobotAtLocation(enemyLocation.location)
                    && rc.senseRobotAtLocation(enemyLocation.location).type != RobotType.ARCHON) {

                Comms.clearOffensiveLocation(rc);
                return null;
            } else {
                return enemyLocation.location;
            }
        }
    }

    /**
     * Returns nearest allied archon, or null if they aren't in need of defense.
     * 
     * @param rc
     * @return
     * @throws GameActionException
     */
    static MapLocation getDefensiveTarget(RobotController rc) throws GameActionException {

        MapLocation me = rc.getLocation();
        ArchonLocation[] archonLocations = Comms.getArchonLocations(rc);
        double minDistance = 1000000;
        MapLocation minLocation = null;

        for (ArchonLocation archonLocation : archonLocations) {
            if (archonLocation.exists && archonLocation.shouldDefend) {
                if (minLocation == null) {
                    minLocation = archonLocation.location;
                    minDistance = me.distanceSquaredTo(minLocation);
                } else if (me.distanceSquaredTo(archonLocation.location) < minDistance) {
                    minLocation = archonLocation.location;
                    minDistance = me.distanceSquaredTo(minLocation);
                }
            }
        }

        return minLocation;
    }

    /**
     * Returns nearest target. These are enemies in vision, in the shared array, or
     * a provided backup location. If None of these exist, returns null. If you
     * recieve null, you should update the backupTarget to a new random location.
     * 
     * @param rc
     * @param nearbyEnemies
     * @return
     * @throws GameActionException
     */
    static MapLocation getTargetFromGlobalAndLocalEnemyLocations(RobotController rc, RobotInfo[] nearbyEnemies,
            MapLocation backupTarget) throws GameActionException {

        MapLocation me = rc.getLocation();
        EnemyLocation[] globalEnemyLocations = Comms.getEnemyLocations(rc);
        MapLocation target = null;
        boolean foundDangerousTarget = false;
        boolean foundLocalTarget = false;
        int lowestHPDangerousEnemy = 100000;
        int lowestHPBenignEnemy = 100000;
        int closestDistanceToGlobalEnemy = 100000;

        if (nearbyEnemies.length == 0) {

            boolean clearedGlobalEnemyFromArray;
            int distanceToEnemy;
            for (int i = globalEnemyLocations.length - 1; i >= 0; i--) {
                if (globalEnemyLocations[i] == null || !globalEnemyLocations[i].exists) {
                    break;
                }

                clearedGlobalEnemyFromArray = false;
                if (rc.canSenseLocation(globalEnemyLocations[i].location)) {
                    if (!rc.canSenseRobotAtLocation(globalEnemyLocations[i].location)
                            || rc.senseRobotAtLocation(globalEnemyLocations[i].location).team.equals(rc.getTeam())) {
                        Comms.clearEnemyLocation(rc, globalEnemyLocations[i].index);
                        clearedGlobalEnemyFromArray = true;
                    }
                }

                distanceToEnemy = me.distanceSquaredTo(globalEnemyLocations[i].location);
                if (!clearedGlobalEnemyFromArray && distanceToEnemy < closestDistanceToGlobalEnemy) {
                    target = globalEnemyLocations[i].location;
                    closestDistanceToGlobalEnemy = distanceToEnemy;
                }

            }
        }

        for (int i = nearbyEnemies.length - 1; i >= 0; i--) {
            if (nearbyEnemies[i].type.equals(RobotType.SOLDIER)
                    || nearbyEnemies[i].type.equals(RobotType.SAGE)
                    || nearbyEnemies[i].type.equals(RobotType.WATCHTOWER)) {
                if (nearbyEnemies[i].health < lowestHPDangerousEnemy) {
                    target = nearbyEnemies[i].location;
                    lowestHPDangerousEnemy = nearbyEnemies[i].health;
                    foundDangerousTarget = true;
                    foundLocalTarget = true;
                }
            }

            if (!foundDangerousTarget) {
                if (nearbyEnemies[i].health < lowestHPBenignEnemy) {
                    target = nearbyEnemies[i].location;
                    lowestHPBenignEnemy = nearbyEnemies[i].health;
                    foundLocalTarget = true;
                }
            }
        }

        if (target != null && foundLocalTarget && rc.senseRobotAtLocation(target).type == RobotType.ARCHON) {
            Comms.setOffensiveLocation(rc, target);
        } else if (target != null && foundLocalTarget) {
            Comms.setEnemyLocation(rc, target, globalEnemyLocations);
        }

        if (target == null) {
            target = backupTarget;
            if (me.distanceSquaredTo(backupTarget) <= EXPLORATION_FIDELITY) {
                target = null;
            }
        }

        return target;
    }

    /**
     * Reflects the target map location about the unit's location to give them
     * a target in the perfectly opposite direction.
     * @param me
     * @param target
     * @return
     */
    static MapLocation getFallbackTarget(MapLocation me, MapLocation target) {
        // ? Should we clamp these coords to map width and height?
        int x = (target.x - me.x);
        int y = (target.y - me.y);
        return new MapLocation(me.x - x, me.y - y);
    }

    /**
     * Returns nearest target. These are enemies in vision, in the shared array, or
     * a provided backup location. If None of these exist, returns null. If you
     * recieve null, you should update the backupTarget to a new random location.
     * 
     * Prioritizes highest HP enemies when making comparisons.
     * 
     * @param rc
     * @param nearbyEnemies
     * @return
     * @throws GameActionException
     */
    static MapLocation getTargetFromGlobalAndLocalEnemyLocationsMAXHP(RobotController rc, RobotInfo[] nearbyEnemies,
            MapLocation backupTarget) throws GameActionException {

        MapLocation me = rc.getLocation();
        EnemyLocation[] globalEnemyLocations = Comms.getEnemyLocations(rc);
        MapLocation target = null;
        boolean foundDangerousTarget = false;
        boolean foundLocalTarget = false;
        int highestHPDangerousEnemy = 100000;
        int highestHPBenignEnemy = 100000;
        int closestDistanceToGlobalEnemy = 100000;

        if (nearbyEnemies.length == 0) {

            boolean clearedGlobalEnemyFromArray;
            int distanceToEnemy;
            for (int i = globalEnemyLocations.length - 1; i >= 0; i--) {
                if (globalEnemyLocations[i] == null || !globalEnemyLocations[i].exists) {
                    break;
                }

                clearedGlobalEnemyFromArray = false;
                if (rc.canSenseLocation(globalEnemyLocations[i].location)) {
                    if (!rc.canSenseRobotAtLocation(globalEnemyLocations[i].location)
                            || rc.senseRobotAtLocation(globalEnemyLocations[i].location).team.equals(rc.getTeam())) {
                        Comms.clearEnemyLocation(rc, globalEnemyLocations[i].index);
                        clearedGlobalEnemyFromArray = true;
                    }
                }

                distanceToEnemy = me.distanceSquaredTo(globalEnemyLocations[i].location);
                if (!clearedGlobalEnemyFromArray && distanceToEnemy < closestDistanceToGlobalEnemy) {
                    target = globalEnemyLocations[i].location;
                    closestDistanceToGlobalEnemy = distanceToEnemy;
                }

            }
        }

        for (int i = nearbyEnemies.length - 1; i >= 0; i--) {
            if (nearbyEnemies[i].type.equals(RobotType.SOLDIER)
                    || nearbyEnemies[i].type.equals(RobotType.SAGE)
                    || nearbyEnemies[i].type.equals(RobotType.WATCHTOWER)) {
                if (nearbyEnemies[i].health > highestHPDangerousEnemy) {
                    target = nearbyEnemies[i].location;
                    highestHPDangerousEnemy = nearbyEnemies[i].health;
                    foundDangerousTarget = true;
                    foundLocalTarget = true;
                }
            }

            if (!foundDangerousTarget) {
                if (nearbyEnemies[i].health > highestHPBenignEnemy) {
                    target = nearbyEnemies[i].location;
                    highestHPBenignEnemy = nearbyEnemies[i].health;
                    foundLocalTarget = true;
                }
            }
        }

        if (target != null && foundLocalTarget && rc.senseRobotAtLocation(target).type == RobotType.ARCHON) {
            Comms.setOffensiveLocation(rc, target);
        } else if (target != null && foundLocalTarget) {
            Comms.setEnemyLocation(rc, target, globalEnemyLocations);
        }

        if (target == null) {
            target = backupTarget;
            if (me.distanceSquaredTo(backupTarget) <= EXPLORATION_FIDELITY) {
                target = null;
            }
        }

        return target;
    }


    /**
     * Returns the lowest rubble tile in a certain radius.
     * 
     * @param rc
     * @param radiusSquared
     * @return
     * @throws GameActionException
     */
    static MapLocation findNearestLowestRubbleTile(RobotController rc, int radiusSquared) throws GameActionException {
        MapLocation me = rc.getLocation();
        MapLocation moveTarget = me;
        int minDistance = 100000;
        int minRubble = 100000;

        for (MapLocation loc : rc.getAllLocationsWithinRadiusSquared(me, radiusSquared)) {
            if (rc.canSenseLocation(loc) && rc.senseRubble(loc) <= minRubble) {
                if (rc.senseRubble(loc) < minRubble) {
                    minDistance = me.distanceSquaredTo(loc);
                    minRubble = rc.senseRubble(loc);
                    moveTarget = loc;
                } else if (me.distanceSquaredTo(loc) < minDistance) {
                    moveTarget = loc;
                    minDistance = me.distanceSquaredTo(loc);
                    minRubble = rc.senseRubble(loc);
                }
            }
        }

        return moveTarget;
    }

    /**
     * Returns the nearest empty tile, ie a tile not occupied by a robot or with lead/gold on it.
     * @param rc
     * @param me
     * @return
     * @throws GameActionException
     */
    static MapLocation findNearestEmptyTile(RobotController rc, MapLocation me) throws GameActionException {
        MapLocation[] nearbyTiles = rc.getAllLocationsWithinRadiusSquared(me, 100);
        int minDistance = 10000;
        MapLocation nearestFreeTile = null;

        for (MapLocation tile : nearbyTiles) {
            if (rc.canSenseLocation(tile) && !rc.canSenseRobotAtLocation(tile) && rc.senseLead(tile) == 0 && rc.senseGold(tile) == 0) {
                if (nearestFreeTile == null || me.distanceSquaredTo(tile) < minDistance) {
                    nearestFreeTile = tile;
                    minDistance = me.distanceSquaredTo(nearestFreeTile);
                }
            }
        }
        
        return nearestFreeTile;
    }
}
