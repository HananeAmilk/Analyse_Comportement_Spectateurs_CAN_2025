CREATE TABLE spectators (
                            spectator_id VARCHAR(50) PRIMARY KEY,
                            age INTEGER,
                            nationality VARCHAR(100),
                            total_matches INTEGER,
                            category VARCHAR(50)
);

CREATE TABLE entries (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         spectator_id VARCHAR(50),
                         match_id VARCHAR(50),
                         entry_time TIMESTAMP,
                         gate VARCHAR(50),
                         ticket_number VARCHAR(100),
                         ticket_type VARCHAR(50),
                         tribune VARCHAR(50),
                         bloc VARCHAR(10),
                         rang INTEGER,
                         siege INTEGER,
                         FOREIGN KEY (spectator_id) REFERENCES spectators(spectator_id)
);

CREATE TABLE statistics (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            stat_name VARCHAR(100),
                            stat_value VARCHAR(500),
                            calculation_date TIMESTAMP
);