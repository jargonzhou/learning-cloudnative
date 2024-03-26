package com.spike.vertx.activity;

// TODO: how to do the paging?
public final class PgSQLs {
    /**
     * $1: device_id, $2: device_sync, $3: steps_count
     */
    public static String insertStepEvent() {
        return "INSERT INTO stepevent " +
                "VALUES($1, $2, current_timestamp, $3)";
    }

    /**
     * $1: device_id
     */
    public static String stepsCountForToday() {
        return "SELECT current_timestamp, coalesce(sum(steps_count), 0) " +
                "FROM stepevent " +
                "WHERE (device_id = $1) " +
                "AND (date_trunc('day', sync_timestamp) = date_trunc('day', current_timestamp))";
    }

    /**
     * $1: device_id
     */
    public static String totalStepsCount() {
        return "SELECT sum(steps_count) " +
                "FROM stepevent " +
                "WHERE (device_id = $1)";
    }

    /**
     * $1: device_id, $2: month timestamp
     */
    public static String monthlyStepsCount() {
        return "SELECT sum(steps_count) " +
                "FROM stepevent " +
                "WHERE (device_id = $1) " +
                "AND (date_trunc('month', sync_timestamp) = $2::timestamp)";
    }

    /**
     * $1: device_id, $2: day timestamp
     */
    public static String dailyStepsCount() {
        return "SELECT sum(steps_count) " +
                "FROM stepevent " +
                "WHERE (device_id = $1) " +
                "AND (date_trunc('day', sync_timestamp) = $2::timestamp)";
    }

    public static String rankingLast24Hours() {
        return "SELECT device_id, SUM(steps_count) as steps " +
                "FROM stepevent " +
                "WHERE (now() - sync_timestamp <= (interval '24 hours')) " +
                "GROUP BY device_id " +
                "ORDER BY steps DESC";
    }
}
