MERGE INTO genres AS g USING (
   SELECT 'Комедия' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM genres WHERE name = 'Комедия'
   )
)
ON (g.name = 'Комедия') WHEN NOT MATCHED THEN INSERT (name) VALUES('Комедия');

MERGE INTO genres AS g USING (
   SELECT 'Драма' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM genres WHERE name = 'Драма'
   )
)
ON (g.name = 'Драма') WHEN NOT MATCHED THEN INSERT (name) VALUES('Драма');

MERGE INTO genres AS g USING (
   SELECT 'Мультфильм' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM genres WHERE name = 'Мультфильм'
   )
)
ON (g.name = 'Мультфильм') WHEN NOT MATCHED THEN INSERT (name) VALUES('Мультфильм');

MERGE INTO genres AS g USING (
   SELECT 'Триллер' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM genres WHERE name = 'Триллер'
   )
)
ON (g.name = 'Триллер') WHEN NOT MATCHED THEN INSERT (name) VALUES('Триллер');

MERGE INTO genres AS g USING (
   SELECT 'Документальный' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM genres WHERE name = 'Документальный'
   )
)
ON (g.name = 'Документальный') WHEN NOT MATCHED THEN INSERT (name) VALUES('Документальный');

MERGE INTO genres AS g USING (
   SELECT 'Боевик' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM genres WHERE name = 'Боевик'
   )
)
ON (g.name = 'Боевик') WHEN NOT MATCHED THEN INSERT (name) VALUES('Боевик');




MERGE INTO mpas AS g USING (
   SELECT 'G' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM mpas WHERE name = 'G'
   )
)
ON (g.name = 'G') WHEN NOT MATCHED THEN INSERT (name) VALUES('G');

MERGE INTO mpas AS g USING (
   SELECT 'PG' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM mpas WHERE name = 'PG'
   )
)
ON (g.name = 'PG') WHEN NOT MATCHED THEN INSERT (name) VALUES('PG');

MERGE INTO mpas AS g USING (
   SELECT 'PG-13' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM mpas WHERE name = 'PG-13'
   )
)
ON (g.name = 'PG-13') WHEN NOT MATCHED THEN INSERT (name) VALUES('PG-13');

MERGE INTO mpas AS g USING (
   SELECT 'R' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM mpas WHERE name = 'R'
   )
)
ON (g.name = 'R') WHEN NOT MATCHED THEN INSERT (name) VALUES('R');

MERGE INTO mpas AS g USING (
   SELECT 'NC-17' AS name FROM dual WHERE NOT EXISTS(
      SELECT * FROM mpas WHERE name = 'NC-17'
   )
)
ON (g.name = 'NC-17') WHEN NOT MATCHED THEN INSERT (name) VALUES('NC-17');