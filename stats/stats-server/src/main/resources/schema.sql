DROP TABLE IF EXISTS hits, applications;

CREATE TABLE "applications"(
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE INDEX "applications_name_index" ON
    "applications"("name");

CREATE TABLE "hits"(
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "app_id" BIGINT NOT NULL,
    "uri" VARCHAR(254) NOT NULL,
    "ip" VARCHAR(15) NOT NULL,
    "hit_time" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX "hits_app_id_hit_time_uri_ip_index" ON
    "hits"("app_id", "hit_time", "uri", "ip");

ALTER TABLE
    "hits" ADD CONSTRAINT "hits_app_id_foreign" FOREIGN KEY("app_id") REFERENCES "applications"("id");