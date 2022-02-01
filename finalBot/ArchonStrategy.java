package player35;

import battlecode.common.*;

strictfp class ArchonStrategy {

    static final int MINER_LEAD_COST = 50;
    static final int SAGE_GOLD_COST = 20;
    static final int OPENING_BUILD_STRAT_DURATION = 10;
    static final int SOLDIER_LEAD_COST = 70;
    static final int NUM_MINERS_ALLOWED_IN_RANGE_OF_ARCHON = 5;
    static final int TILE_LEAD_HARVEST_CUTOFF = 10;
    static final int LEAD_FARM_SEEDING_START_ROUND = 100;
    static final int LEAD_FARM_SEEDING_END_ROUND = 120;
    static final int MAX_LEAD_LIMIT = 200;
    static final int SOLDIER_SPAWN_PROBABILITY = 50;
    static final int MINER_SPAWN_PROBABILITY = 20 + SOLDIER_SPAWN_PROBABILITY;
    static final int ACTION_RADIUS_SQUARED = 20;
    static final int ROUND_WHEN_WE_START_ARCHON_RELOCATION = 15;
    static final int ADJACENT_TILE_RANGE = 2;
    static final int ROUND_WHEN_WE_START_STEPPING_OFF_RUBBLE = 50;
    static final int SHARED_ARRAY_CLEARING_FREQUENCY = 10;
    static final int ROUND_WHEN_WE_START_BUILDING_SAGES = 500;
    static final int LEAD_REQUIRED_FOR_LABS = 250;
    static int repairCount = 0;
    static boolean relocatedToMainArchon = false;
    static MapLocation lastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLocation = new MapLocation(0, 0);
    static MapLocation lastLastLastLocation = new MapLocation(0, 0);

    static void run(RobotController rc) throws GameActionException {

        MapLocation me = rc.getLocation();
        int round = rc.getRoundNum();
        int rcId = rc.getID();
        int id = rcId % 2 == 0 ? (rcId - 1) / 2 : (rcId - 2) / 2;
        int leadAmount = rc.getTeamLeadAmount(Statics.player);
        boolean freeTiles = false;
        int archonCount = rc.getArchonCount();
        MapLocation target = new MapLocation(1000, 1000);
        ArchonLocation[] archLocs = Comms.getArchonLocations(rc);
        EnemyLocation[] globalEnemyLocations = Comms.getEnemyLocations(rc);
        RobotInfo[] enemiesInRange = rc.senseNearbyRobots(-1, Statics.opponent);
        RobotInfo[] alliedLocs = rc.senseNearbyRobots(ACTION_RADIUS_SQUARED, Statics.player);
        freeTiles = Targeting.findNearestEmptyTile(rc, me) == null ? false : true;

        if (!lastLocation.equals(me)) {
            lastLastLastLocation = lastLastLocation;
            lastLastLocation = lastLocation;
            lastLocation = me;
        }

        int firstArch = 3;
        for (int i = 0; i < archLocs.length; i++) {
            if (archLocs[i].exists && i < firstArch) {
                firstArch = i;
            }
        }

        // Do we need to move to avoid rubble?
        MapLocation moveTarget = target;
        int minDistance = 100000;
        int minRubble = 100000;
        MapLocation[] locsNearMe = rc.getAllLocationsWithinRadiusSquared(me, 10000);
        for (int i = locsNearMe.length; --i >= 0;) {
            if (rc.canSenseLocation(locsNearMe[i]) && rc.senseRubble(locsNearMe[i]) <= minRubble && (!rc.canSenseRobotAtLocation(locsNearMe[i]) || locsNearMe[i].equals(me))) {
                if (rc.senseRubble(locsNearMe[i]) < minRubble) {
                    minDistance = me.distanceSquaredTo(locsNearMe[i]);
                    minRubble = rc.senseRubble(locsNearMe[i]);
                    moveTarget = locsNearMe[i];
                } else if (me.distanceSquaredTo(locsNearMe[i]) < minDistance) {
                    moveTarget = locsNearMe[i];
                    minDistance = me.distanceSquaredTo(locsNearMe[i]);
                    minRubble = rc.senseRubble(locsNearMe[i]);
                }
            }
        }

        // We choose not to relocate our main archon
        if (round == 2 && id == firstArch) {
            relocatedToMainArchon = true;
        }

        // We relocate all other archons to main at start of match
        if (round > ROUND_WHEN_WE_START_ARCHON_RELOCATION && id != firstArch && !relocatedToMainArchon) {
            if (rc.isTransformReady() && rc.getMode().equals(RobotMode.TURRET) && rc.canTransform()) {
                rc.transform();
            }

            if (rc.getMode().equals(RobotMode.PORTABLE)) {
                if (me.distanceSquaredTo(archLocs[firstArch].location) <= ADJACENT_TILE_RANGE) {
                    if (rc.isTransformReady() && rc.canTransform()) {
                        relocatedToMainArchon = true;
                    }
                }

                if (rc.isMovementReady()) {
                    Movement.move(rc, archLocs[firstArch].location, lastLastLocation);
                }
            }
        }

        rc.setIndicatorLine(me, moveTarget, 0, 0, 0);

        // Should we step off rubble now?
        if (relocatedToMainArchon && round > ROUND_WHEN_WE_START_STEPPING_OFF_RUBBLE && !moveTarget.equals(me)
                && rc.senseRubble(me) > 0) {
            if (rc.isTransformReady() && rc.getMode().equals(RobotMode.TURRET) && rc.canTransform()) {
                rc.transform();
            }
        }

        // Handling portable mode.
        if (relocatedToMainArchon && rc.getMode().equals(RobotMode.PORTABLE)) {
            if (me.equals(moveTarget)) {
                if (rc.isTransformReady() && rc.canTransform()) {
                    rc.transform();
                }
            }

            if (rc.isMovementReady()) {
                Movement.move(rc, moveTarget, lastLastLastLocation);
            }
        }

        // Clearing comms
        if (round % SHARED_ARRAY_CLEARING_FREQUENCY == 0) {
            if (id == firstArch) {
                if (round > 200) { // We give some time for relocation
                    Comms.clearArchonLocations(rc);
                }
                Comms.clearEnemyLocations(rc);
            }
        }

        // Setting comms
        if (enemiesInRange.length > 0) {
            Comms.setArchonLocation(rc, me, true, id);
            Comms.setEnemyLocation(rc, enemiesInRange[0].location, globalEnemyLocations);
        } else {
            Comms.setArchonLocation(rc, me, false, id);
        }

        // * We decide whether to stockpile some lead or not to build
        // advanced structures before dispatching build.
        if (round < ROUND_WHEN_WE_START_BUILDING_SAGES) {

            executeBuildStrategy(rc, me, round, leadAmount, archonCount, archLocs, enemiesInRange, alliedLocs,
                    freeTiles);

        } else if (round > ROUND_WHEN_WE_START_BUILDING_SAGES && leadAmount > LEAD_REQUIRED_FOR_LABS
                || rc.getTeamGoldAmount(Statics.player) > 0) {

            executeBuildStrategy(rc, me, round, leadAmount, archonCount, archLocs, enemiesInRange,
                    alliedLocs, freeTiles);
        }

        // * Repair if able
        for (int i = alliedLocs.length - 1; i >= 0; i--) {
            if (!rc.isActionReady()) {
                break;
            }
            if (alliedLocs[i].health < alliedLocs[i].type.health) {
                if (rc.canRepair(alliedLocs[i].location)) {
                    rc.repair(alliedLocs[i].location);
                    repairCount++;
                }
            }
        }
    }

    /**
     * Builds a unit in target direction. If target direction is CENTER, picks dir
     * randomly.
     * 
     * @param rc
     * @param type
     * @param dir
     * @throws GameActionException
     */
    static void buildUnit(RobotController rc, RobotType type, Direction dir) throws GameActionException {
        if (dir.equals(Direction.CENTER)) {
            int dirIndex = Statics.rng.nextInt(Statics.directions.length);
            dir = Statics.directions[dirIndex];
        }

        Direction oldDir = dir;
        int numDirections = 8;
        for (int i = 0; i < numDirections; i++) {
            if (rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
                break;
            } else {
                dir = dir.rotateLeft();
                if (dir.equals(oldDir)) {
                    break;
                }
            }
        }
    }

    static MapLocation reflectedLocation(MapLocation original, int mapWidth, int mapHeight) {
        int newX = (mapWidth - 1) - original.x;
        int newY = (mapHeight - 1) - original.y;
        int translateX = newX - original.x;
        int translateY = newY - original.y;
        MapLocation reflectedLocation = original.translate(translateX, translateY);
        return reflectedLocation;
    }

    static MapLocation rotatedLocation(MapLocation original, int mapWidth, int mapHeight) {
        int newX = (mapWidth - 1) - original.x;
        int translateX = newX - original.x;
        MapLocation reflectedLocation = original.translate(translateX, 0);
        return reflectedLocation;
    }

    static boolean executeBuildStrategy(RobotController rc, MapLocation me, int round, int leadAmount, int archonCount,
            ArchonLocation[] archLocs, RobotInfo[] enemies, RobotInfo[] allies, boolean freeTiles)
            throws GameActionException {

        MapLocation[] locsWithLead = rc.senseNearbyLocationsWithLead(-1);
        boolean leadTileReadyToBeHarvested = false;
        for (int i = locsWithLead.length - 1; i >= 0; i--) {
            if (rc.senseLead(locsWithLead[i]) > TILE_LEAD_HARVEST_CUTOFF) {
                leadTileReadyToBeHarvested = true;
                break;
            }
        }

        // Round 1 Strat - If we see lead, build a miner. Also set enemy
        // archon positions based on allied archon positions.
        if (round == 1) {
            MapLocation firstTarget = reflectedLocation(me, rc.getMapWidth(), rc.getMapHeight());
            EnemyLocation enemyArch = Comms.getOffensiveLocation(rc);

            if (!enemyArch.exists) {
                Comms.setOffensiveLocation(rc, firstTarget);
            } else {
                if (enemyArch.location.equals(me)) {
                    firstTarget = rotatedLocation(me, rc.getMapWidth(), rc.getMapHeight());
                    Comms.setOffensiveLocation(rc, firstTarget);
                }
            }

            if (locsWithLead.length > 0 && leadAmount >= MINER_LEAD_COST) {
                buildUnit(rc, RobotType.MINER, me.directionTo(locsWithLead[0]));
                return true;
            }

        } else if (rc.getTeamGoldAmount(rc.getTeam()) >= SAGE_GOLD_COST) {
            buildUnit(rc, RobotType.SAGE, Direction.CENTER);
            return true;

        } else if (round < OPENING_BUILD_STRAT_DURATION) {
            buildUnit(rc, RobotType.MINER, Direction.CENTER);
            return true;

        } else {

            // Build soldiers if enemies are near
            if (enemies.length > 0 && leadAmount >= SOLDIER_LEAD_COST) {
                buildUnit(rc, RobotType.SOLDIER, Direction.CENTER);
                return true;
            }

            boolean canBuildMiner = true;
            int minerCount = 0;
            for (int i = allies.length - 1; i >= 0; i--) {
                if (allies[i].type.equals(RobotType.MINER)) {
                    minerCount++;
                }
            }

            if (minerCount > NUM_MINERS_ALLOWED_IN_RANGE_OF_ARCHON) {
                canBuildMiner = false;
            }

            // If we need to build a miner we should, otherwise let probability
            // and minerCount guide miner production.
            if (minerCount == 0 && leadTileReadyToBeHarvested && leadAmount > MINER_LEAD_COST) {
                buildUnit(rc, RobotType.MINER, Direction.CENTER);
                return true;
            }

            // Might be too risky to seed a lead farm when we only have 1 archon
            if (round > LEAD_FARM_SEEDING_START_ROUND && round < LEAD_FARM_SEEDING_END_ROUND && archonCount > 1) {
                buildUnit(rc, RobotType.BUILDER, Direction.CENTER);

            } else { // Regular build strategy
                int n = Statics.rng.nextInt(100);
                int m = Statics.rng.nextInt(100);

                if (leadAmount > SOLDIER_LEAD_COST && (n < 100 / archonCount || leadAmount > MAX_LEAD_LIMIT)) {

                    if (m < SOLDIER_SPAWN_PROBABILITY) {
                        buildUnit(rc, RobotType.SOLDIER, Direction.CENTER);
                        return true;
                    } else if (canBuildMiner && m < MINER_SPAWN_PROBABILITY) {
                        buildUnit(rc, RobotType.MINER, Direction.CENTER);
                        return true;
                    } else if (freeTiles) {
                        buildUnit(rc, RobotType.BUILDER, Direction.CENTER);
                    } else {
                        buildUnit(rc, RobotType.SOLDIER, Direction.CENTER);
                    }
                }
            }
        }

        return false;
    }
}