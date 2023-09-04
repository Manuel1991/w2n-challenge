-- liquibase formatted sql

-- changeset manuell:1693848609466-1
CREATE TABLE heroes (
    id VARCHAR(36) NOT NULL,
    first_apparition INTEGER,
    name VARCHAR(255),
    universe VARCHAR(255),
    primary key (id)
) engine=InnoDB;

-- changeset manuell:1693848609466-2
INSERT INTO heroes (id, name, universe, first_apparition) VALUES
    (UUID(), 'Superman', 'DC Comics', 1938),
    (UUID(), 'Spider-Man', 'Marvel Comics', 1962),
    (UUID(), 'Wonder Woman', 'DC Comics', 1941),
    (UUID(), 'Iron Man', 'Marvel Comics', 1963),
    (UUID(), 'Black Widow', 'Marvel Comics', 1964),
    (UUID(), 'The Flash', 'DC Comics', 1940),
    (UUID(), 'Hulk', 'Marvel Comics', 1962),
    (UUID(), 'Green Lantern', 'DC Comics', 1940),
    (UUID(), 'Thor', 'Marvel Comics', 1962),
    (UUID(), 'Captain America', 'Marvel Comics', 1941),
    (UUID(), 'Batman', 'DC Comics', 1939),
    (UUID(), 'Aquaman', 'DC Comics', 1941),
    (UUID(), 'Black Panther', 'Marvel Comics', 1966),
    (UUID(), 'Wolverine', 'Marvel Comics', 1974),
    (UUID(), 'Wonder Man', 'Marvel Comics', 1964),
    (UUID(), 'Supergirl', 'DC Comics', 1959),
    (UUID(), 'Doctor Strange', 'Marvel Comics', 1963),
    (UUID(), 'Hawkman', 'DC Comics', 1940),
    (UUID(), 'Green Arrow', 'DC Comics', 1941),
    (UUID(), 'Daredevil', 'Marvel Comics', 1964),
    (UUID(), 'Flash Gordon', 'King Features Syndicate', 1934),
    (UUID(), 'The Thing', 'Marvel Comics', 1961),
    (UUID(), 'Martian Manhunter', 'DC Comics', 1955),
    (UUID(), 'Storm', 'Marvel Comics', 1975),
    (UUID(), 'Silver Surfer', 'Marvel Comics', 1966);
