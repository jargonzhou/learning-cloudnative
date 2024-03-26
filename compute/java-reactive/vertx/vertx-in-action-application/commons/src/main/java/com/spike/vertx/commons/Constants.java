package com.spike.vertx.commons;

public final class Constants {

    public interface AMQP {
        interface Address {
            String step_events = "step-events";
        }
    }

    public interface EventBus {
        interface Destination {
            String client_updates_throughput = "client.updates.throughput";
            String client_updates_city_trend = "client.updates.city-trend";
            String client_updates_publicRanking = "client.updates.publicRanking";

            String INBOUND_ALLOWED_PATTERN = "client.update.*";
            String OUTBOUND_ALLOWED_PATTERN = "client.update.*";
        }
    }

    public interface Kafka {
        interface Topic {
            String incoming_steps = "incoming.steps";
            String daily_step_updates = "daily.step.updates";

            String event_stats_throughput = "event-stats.throughput";
            String event_stats_user_activity_updates = "event-stats.user-activity.updates";
            String event_stats_city_trends_update = "event-stats.city-trends.update";
        }
    }

    public interface UserProfileService {
        String HOST = "localhost";
        int HTTP_PORT = 3000;

        String MONGODB_HOST = "localhost";
        int MONGODB_PORT = 27017;
        String MONGODB_DB_NAME = "profiles";
        String MONGODB_COL_NAME = "user";
        String MONGODB_USERNAME = "root";
        String MONGODB_PASSWORD = "root";

        interface HTTPApi {
            String REGISTER = "/register";
            String AUTHENTICATE = "/authenticate";
            String USERNAME_PATH = "/:username";
            String OWNS_DEVICE_ID_PATH = "/owns/:deviceId";

            /**
             * username
             */
            String USERNAME = "/%s";
            /**
             * deviceId
             */
            String OWNS_DEVICE_ID = "/owns/%s";

        }
    }

    public interface IngestionService {
        String HOST = "localhost";
        int HTTP_PORT = 3002;

        String KAFKA_SERVERS = "localhost:9092";

        String AMQP_HOST = "localhost";
        int AMQP_PORT = 5672;
        String AMQP_USERNAME = "devops";
        String AMQP_PASSWORD = "devops+artemis";

        interface HTTPApi {
            String INGEST = "/ingest";
        }
    }

    public interface ActivityService {
        String HOST = "localhost";
        int HTTP_PORT = 3001;

        String POSTGRESQL_HOST = "localhost";
        int POSTGRESQL_PORT = 5432;
        String POSTGRESQL_DB = "devops";
        String POSTGRESQL_USER = "devops";
        String POSTGRESQL_PASSWORD = "devops+postgresql";

        String KAFKA_SERVERS = "localhost:9092";
        String KAFKA_CONSUMER_GROUP_ID = "activity-service";

        interface HTTPApi {
            /**
             * deviceId
             */
            String DEVICE_TOTAL = "/%s/total";
            String DEVICE_TOTAL_PATH = "/:deviceId/total";
            /**
             * deviceId, year, month
             */
            String DEVICE_MONTHLY = "/%s/%s/%s";
            String DEVICE_MONTHLY_PATH = "/:deviceId/:year/:month";

            /**
             * deviceId, year, month, day
             */
            String DEVICE_DAILY = "/%s/%s/%s/%s";
            String DEVICE_DAILY_PATH = "/:deviceId/:year/:month/:day";

            String RANKING = "/ranking-last-24-hours";
        }
    }

    public interface PublicApi {
        int HTTP_PORT = 4000;
        String API_PREFIX = "/api/v1";

        interface HTTPApi {
            String REGISTER = API_PREFIX + "/register";
            String TOKEN = API_PREFIX + "/token";

            String USER_PATH = API_PREFIX + "/:username";
            String USER_TOTAL_PATH = API_PREFIX + "/:username/total";
            String USER_TOTAL_YEAR_MONTH_PATH = API_PREFIX + "/:username/:year/:month";
            String USER_TOTAL_YEAR_MONTH_DAY_PATH = API_PREFIX + "/:username/:year/:month/:day";
        }
    }

    public interface EventStatsService {

        String KAFKA_SERVERS = "localhost:9092";

        interface KafkaConsumerGroup {
            String event_stats_throughput = "event-stats-throughput";
            String event_stats_user_activity_updates = "event-stats-user-activity-updates";
            String event_stats_city_trends = "event-stats-city-trends";
        }

    }

    public interface CongratsService {
        String MAIL_HOSTNAME = "localhost";
        int MAIL_PORT = 1025;
        int MAIL_HTTP_PORT = 8025;

        String KAFKA_CONSUMER_GROUP_ID = "congrats-service";

    }

    public interface UserWebapp {
        int HTTP_PORT = 8080;
    }

    public interface DashboardWebapp {
        int HTTP_PORT = 8081;

        String KAFKA_SERVERS = "localhost:9092";

        interface KafkaConsumerGroup {
            String dashboard_webapp_throughput = "dashboard-webapp-throughput";
            String dashboard_webapp_city_trend = "dashboard-webapp-city-trend";
            String dashboard_webapp_ranking = "dashboard-webapp-ranking";
        }
    }
}