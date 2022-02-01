package player35;

import battlecode.common.*;

public class Comms {

    /*
     * Current idea is to use the 64 integer shared array as follows:
     * 
     * Indices:
     * 0: Message
     * 1: Offensive target
     * 2-5: allied archon locations
     * 6-7: allied unit counts
     * 8-63: enemy locations
     */
    static final int LENGTH_OF_SHARED_ARRAY = 64;
    static final int MESSAGE_INDEX = 0;
    static final int OFFENSIVE_TARGET_INDEX = 1;
    static final int ARCHON_INDEX = 2;
    static final int UNIT_COUNT_INDEX = 6;
    static final int ENEMY_TARGETS_INDEX = 10;
    static final int ENEMY_ARRAY_CAPACITY = 53;
    static final int ARCHON_SHOULD_DEFEND_BIT_POSITION = 14;
    static final int COORDINATE_BITMASK = 63;
    static final int X_COORD_OFFSET = 6;
    static EnemyLocation[] enemyLocationsArray = new EnemyLocation[ENEMY_ARRAY_CAPACITY];

    /**
     * Bytecode cost: 123
     */
    static void setOffensiveLocation(RobotController rc, MapLocation location) throws GameActionException {
        int bitvector = encodeMapLocationIntoBitvector(location);
        rc.writeSharedArray(OFFENSIVE_TARGET_INDEX, bitvector);
    }

    /**
     * Bytecode cost: 38
     */
    static EnemyLocation getOffensiveLocation(RobotController rc) throws GameActionException {
        int bitvector = rc.readSharedArray(OFFENSIVE_TARGET_INDEX);
        MapLocation location = decodeMapLocationFromBitvector(bitvector);
        boolean exists = bitvector != 0;
        return new EnemyLocation(location, exists, OFFENSIVE_TARGET_INDEX);
    }

    /**
     * Bytecode cost: 106
     */
    static void clearOffensiveLocation(RobotController rc) throws GameActionException {
        rc.writeSharedArray(OFFENSIVE_TARGET_INDEX, 0);
    }

    /**
     * Bytecode cost: 135
     */
    static void setArchonLocation(RobotController rc, MapLocation archonLocation, boolean shouldDefend, int archonID)
            throws GameActionException {
        int bitvector = encodeMapLocationIntoBitvector(archonLocation);
        int defendBit = shouldDefend ? 1 : 0;
        bitvector |= (defendBit << ARCHON_SHOULD_DEFEND_BIT_POSITION);
        rc.writeSharedArray(ARCHON_INDEX + archonID, bitvector);
    }

    /**
     * Bytecode cost: 211
     */
    static ArchonLocation[] getArchonLocations(RobotController rc) throws GameActionException {
        // We unroll the constant four item iteration.

        int bitvector1 = rc.readSharedArray(ARCHON_INDEX);
        int bitvector2 = rc.readSharedArray(ARCHON_INDEX + 1);
        int bitvector3 = rc.readSharedArray(ARCHON_INDEX + 2);
        int bitvector4 = rc.readSharedArray(ARCHON_INDEX + 3);

        MapLocation loc1 = decodeMapLocationFromBitvector(bitvector1);
        MapLocation loc2 = decodeMapLocationFromBitvector(bitvector2);
        MapLocation loc3 = decodeMapLocationFromBitvector(bitvector3);
        MapLocation loc4 = decodeMapLocationFromBitvector(bitvector4);

        int pos = ARCHON_SHOULD_DEFEND_BIT_POSITION;
        boolean shouldDefend1 = ((bitvector1 & (1 << pos)) >>> pos) == 1;
        boolean shouldDefend2 = ((bitvector2 & (1 << pos)) >>> pos) == 1;
        boolean shouldDefend3 = ((bitvector3 & (1 << pos)) >>> pos) == 1;
        boolean shouldDefend4 = ((bitvector4 & (1 << pos)) >>> pos) == 1;

        boolean exists1 = bitvector1 != 0;
        boolean exists2 = bitvector2 != 0;
        boolean exists3 = bitvector3 != 0;
        boolean exists4 = bitvector4 != 0;

        ArchonLocation archonLocation1 = new ArchonLocation(loc1, shouldDefend1, exists1);
        ArchonLocation archonLocation2 = new ArchonLocation(loc2, shouldDefend2, exists2);
        ArchonLocation archonLocation3 = new ArchonLocation(loc3, shouldDefend3, exists3);
        ArchonLocation archonLocation4 = new ArchonLocation(loc4, shouldDefend4, exists4);

        ArchonLocation[] result = { archonLocation1, archonLocation2, archonLocation3, archonLocation4 };

        return result;
    }

    static MapLocation getNearestArchonLocation(RobotController rc, MapLocation me) throws GameActionException {
        ArchonLocation[] archonLocations = Comms.getArchonLocations(rc);
        MapLocation nearestArchon = null;
        int minDistance = 1000000000;
        int distanceSquaredToArchon;

        for (int i = archonLocations.length - 1; i >= 0; i--) {
            if (archonLocations[i].exists) {
                distanceSquaredToArchon = me.distanceSquaredTo(archonLocations[i].location);
                if ((nearestArchon == null || distanceSquaredToArchon < minDistance)) {
                    nearestArchon = archonLocations[i].location;
                    minDistance = distanceSquaredToArchon;
                }
            }
        }

        return nearestArchon;
    }

    /**
     * Bytecode cost: 415
     * 
     * @param rc
     * @throws GameActionException
     */
    static void clearArchonLocations(RobotController rc) throws GameActionException {
        rc.writeSharedArray(ARCHON_INDEX, 0);
        rc.writeSharedArray(ARCHON_INDEX + 1, 0);
        rc.writeSharedArray(ARCHON_INDEX + 2, 0);
        rc.writeSharedArray(ARCHON_INDEX + 3, 0);
    }

    /**
     * NOT IMPLEMENTED
     */
    static void getMessage() {

    }

    /**
     * NOT IMPLEMENTED
     */
    static void setMessage() {

    }

    /**
     * NOT IMPLEMENTED
     */
    static void clearMessage() {

    }

    /**
     * NOT IMPLEMENTED
     */
    static void getUnitCounts() {

    }

    /**
     * NOT IMPLEMENTED
     */
    static void getUnitCount() {

    }

    /**
     * NOT IMPLEMENTED
     */
    static void setUnitCount() {

    }

    /**
     * NOT IMPLEMENTED
     */
    static void clearUnitCounts() {

    }

    static EnemyLocation[] getEnemyLocations(RobotController rc) throws GameActionException {
        int bitvector;

        for (int i = ENEMY_ARRAY_CAPACITY - 1; i >= 0; i--) {

            bitvector = rc.readSharedArray(ENEMY_TARGETS_INDEX + i);
            if (bitvector == 0) {
                enemyLocationsArray[i] = new EnemyLocation(decodeMapLocationFromBitvector(bitvector), false, i);
            } else {
                enemyLocationsArray[i] = new EnemyLocation(decodeMapLocationFromBitvector(bitvector), true, i);
            }
        }

        return enemyLocationsArray;
    }

    static void setEnemyLocation(RobotController rc, MapLocation location, EnemyLocation[] enemyLocations)
            throws GameActionException {
        int bitvector = encodeMapLocationIntoBitvector(location);
        bitvector |= (1 << 14); // exist bit for enemy locations in array.

        for (int i = enemyLocations.length - 1; i >= 0; i--) {
            if (enemyLocations[i] == null || !enemyLocations[i].exists) {
                rc.writeSharedArray(ENEMY_TARGETS_INDEX + i, bitvector);
                break;
            }
        }
    }

    static void clearEnemyLocation(RobotController rc, int index) throws GameActionException {
        rc.writeSharedArray(index, 0);
    }

    static void clearEnemyLocations(RobotController rc) throws GameActionException {
        for (int i = ENEMY_TARGETS_INDEX; i < 64; i++) {
            rc.writeSharedArray(i, 0);
        }
    }

    static MapLocation decodeMapLocationFromBitvector(int bitvector) {
        return new MapLocation(
                (bitvector & (COORDINATE_BITMASK << X_COORD_OFFSET)) >>> X_COORD_OFFSET,
                bitvector & COORDINATE_BITMASK);
    }

    static int encodeMapLocationIntoBitvector(MapLocation loc) {
        int bitvector = 0;
        int x = loc.x;
        int y = loc.y;
        bitvector |= (x << X_COORD_OFFSET) | y;
        return bitvector;
    }

}

class ArchonLocation {
    public MapLocation location;
    public boolean shouldDefend;
    public boolean exists;

    public ArchonLocation(MapLocation location, boolean shouldDefend, boolean exists) {
        this.location = location;
        this.shouldDefend = shouldDefend;
        this.exists = exists;
    }
}

class EnemyLocation {
    public MapLocation location;
    public boolean exists;
    public int index;

    public EnemyLocation(MapLocation location, boolean exists, int index) {
        this.location = location;
        this.exists = exists;
        this.index = index;
    }
}