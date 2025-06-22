CREATE TABLE venue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    total_capacity BIGINT NOT NULL
);

CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    venue_id BIGINT NOT NULL,
    total_capacity BIGINT NOT NULL,
    left_capacity BIGINT NOT NULL,
    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE
);

INSERT INTO ticketing.venue (name, address, total_capacity)
    VALUE ("Old Trafford", "Manchester,UK", 80000),
    ("Etihad Stadium", "Manchester,UK", 70000);

INSERT INTO ticketing.event(name, venue_id, total_capacity, left_capacity)
    VALUE ("Coldplay", 1, 40000, 40000),
    ("Bruno Mars", 2, 30000, 30000);