
drop view globalKunder;
go

-- Lav på database A et view, der viser alle kunder (dvs. den globale kundetabel).
CREATE VIEW globalKunder AS
SELECT *
FROM databaseA.dbo.Kunde aKunde
UNION
SELECT *
FROM databaseB.dbo.Kunde bKunde;
go


select * from globalKunder;
go

-- Lav på dette view en instead of trigger til insert af en ny kunde. 
-- Du afgør selv efter hvilke kriterier en kunde skal placeres.
CREATE TRIGGER InsertGlobalKunde
ON globalKunder
INSTEAD OF INSERT
AS
BEGIN
    -- Indsæt i databaseA.dbo.Kunde hvis postnr > 5000
    INSERT INTO databaseA.dbo.Kunde (kundeid, navn, postnr)
    SELECT kundeid, navn, postnr
    FROM inserted
    WHERE postnr > 5000;

    -- Indsæt i databaseB.dbo.Kunde hvis postnr <= 5000
    INSERT INTO databaseB.dbo.Kunde (kundeid, navn, postnr)
    SELECT kundeid, navn, postnr
    FROM inserted
    WHERE postnr <= 5000;
END;
go


-- Lav en query på både database A og B, der finder alle kunder og summen af deres posteringer.
SELECT gKunde.kundeid, SUM(beloeb) as totalPostering
FROM globalKunder gKunde
JOIN databaseB.dbo.Postering p on gKunde.kundeid = p.kundeid
GROUP BY gKunde.kundeid
go


drop view kundeBestillinger;
go

-- Lav et view på både database A og B, der finder alle kunder og antallet af deres bestillinger.
CREATE VIEW kundeBestillinger AS
SELECT gKunde.kundeid, COUNT(b.kundeid) as totalBestillinger from (SELECT *
FROM databaseA.dbo.Kunde aKunde
UNION
SELECT *
FROM databaseB.dbo.Kunde bKunde) as gKunde
JOIN databaseA.dbo.Bestilling b on gKunde.kundeid = b.kundeid
GROUP BY gKunde.kundeid;
go

select * from kundeBestillinger
go