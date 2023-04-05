DROP TABLE IF EXISTS HEROES;

CREATE TABLE HEROES (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    universe VARCHAR(50) NOT NULL,
    first_apparition SMALLINT
);

INSERT INTO HEROES (id, name, universe, first_apparition)
VALUES
    ('e2a2ebc5-8e1f-488c-bf0e-97948e12e01d', 'Spider-Man', 'Marvel', 1962),
    ('9f3a1c70-4272-4c4f-8b2a-fc84ad720e4a', 'Superman', 'DC', 1938),
    ('28e0a48c-8d61-45db-99a3-3d45a8778d20', 'Batman', 'DC', 1939),
    ('8e79d0c7-365f-4a13-b30b-c0fc6770577c', 'Wonder Woman', 'DC', 1941),
    ('787b6d5e-9c2f-4c6e-bf6d-11c3f68dbf6f', 'Flash', 'DC', 1940),
    ('b1ef9f6e-4492-4d01-ae16-2e05182bb5d5', 'Green Lantern', 'DC', 1940),
    ('b6e7af6f-4756-4b32-b11d-8e47a73291c2', 'Aquaman', 'DC', 1941),
    ('0c4a4e4a-42d7-4ad9-a86e-f9ea9f3f3a0d', 'Captain America', 'Marvel', 1941),
    ('01982077-d24f-472f-a9cc-d052b3226177', 'Iron Man', 'Marvel', 1963),
    ('ad0644a4-6989-43e3-84e3-15d0fba936a3', 'Thor', 'Marvel', 1962),
    ('29c9d9ed-2742-47b2-b29c-6f5f5c65d5d5', 'Hulk', 'Marvel', 1962),
    ('83d0832a-5af6-4411-bd5e-df5d6c5f6f9f', 'Black Widow', 'Marvel', 1964),
    ('b3c3d26d-c3ea-4c53-b9f8-50fbdcd31b4c', 'Doctor Strange', 'Marvel', 1963),
    ('c0af7949-9e8d-4b1d-a904-792a1c1817bf', 'Wolverine', 'Marvel', 1974)