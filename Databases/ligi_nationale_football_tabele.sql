CREATE DATABASE LigiNationaleDeFootball;
GO

USE LigiNationaleDeFootball;

Create Table Ligi (
    id_liga INT PRIMARY KEY IDENTITY,
	nume VARCHAR(64),
	tara VARCHAR(64)
);

Create Table Antrenori(
    id_antrenor INT PRIMARY KEY IDENTITY,
	nume VARCHAR(64),
	prenume VARCHAR(64)
);

Create Table Arbitrii(
    id_arbitru INT PRIMARY KEY IDENTITY,
	nume VARCHAR(64),
	prenume VARCHAR(64)
);

Create Table Clasamente(
	id_clasament INT PRIMARY KEY IDENTITY,
	id_liga INT FOREIGN KEY REFERENCES Ligi(id_liga),
	nume VARCHAR(64)
)

Create Table Echipe(
	id_echipa INT PRIMARY KEY IDENTITY,
	id_clasament INT FOREIGN KEY REFERENCES Clasamente(id_clasament),
	id_antrenor INT FOREIGN KEY REFERENCES Antrenori(id_antrenor),
	nume VARCHAR(64)
);

Create Table Jucatori(
	id_jucatori INT PRIMARY KEY IDENTITY,
	id_echipa INT FOREIGN KEY REFERENCES Echipe(id_echipa),
	nume VARCHAR(64),
	prenume VARCHAR(64),
	post VARCHAR(64),
	salariu MONEY
);

Create Table Meciuri(
	id_meci INT PRIMARY KEY IDENTITY,
	id_liga INT FOREIGN KEY REFERENCES Ligi(id_liga),
	id_echipa_gazda INT FOREIGN KEY REFERENCES Echipe(id_echipa),
	id_echipa_oaspete INT FOREIGN KEY REFERENCES Echipe(id_echipa),
	data_ora DATETIME
);

DROP TABLE Jucatori

Create Table Jucatori(
	id_jucator INT PRIMARY KEY IDENTITY,
	id_echipa INT FOREIGN KEY REFERENCES Echipe(id_echipa),
	nume VARCHAR(64),
	prenume VARCHAR(64),
	post VARCHAR(64),
	salariu MONEY
);

Create Table Goluri(
	id_goluri INT PRIMARY KEY IDENTITY,
	id_jucator INT FOREIGN KEY REFERENCES Jucatori(id_jucator),
	id_meci INT FOREIGN KEY REFERENCES Meciuri(id_meci)
);

Create Table Transferuri(
	id_transfer INT PRIMARY KEY IDENTITY,
	id_jucator INT FOREIGN KEY REFERENCES Jucatori(id_jucator),
	id_echipa_de_la INT FOREIGN KEY REFERENCES Echipe(id_echipa),
	id_echipa_la_care INT FOREIGN KEY REFERENCES Echipe(id_echipa),
	valoare MONEY
);

Create Table ArbitriiMeciuri(
	id_arbitru INT FOREIGN KEY REFERENCES Arbitrii(id_arbitru),
	id_meci INT FOREIGN KEY REFERENCES Meciuri(id_meci),
	CONSTRAINT pk_ArbitriiMeciuri PRIMARY KEY (id_arbitru, id_meci)
);

Alter Table Clasamente
DROP COLUMN nume;

Alter Table Clasamente
Add Constraint unique_id_liga UNIQUE (id_clasament, id_liga);

Alter Table Echipe
Add Constraint unique_id_antrenor UNIQUE (id_echipa, id_antrenor);

Alter Table Echipe
Drop Constraint unique_id_antrenor

Alter Table Echipe
Add Constraint unique_id_antrenor UNIQUE (id_antrenor);

Alter Table Clasamente
Drop Constraint unique_id_liga

Alter Table Clasamente
Add Constraint unique_id_liga UNIQUE (id_liga);


EXEC sp_rename 'Arbitrii', 'Arbitri'

EXEC sp_rename 'PK__Arbitrii__335ACBDCD9DE5AC7', 'PK__Arbitri__335ACBDCD9DE5AC7'

EXEC sp_rename 'ArbitriiMeciuri', 'ArbitriMeciuri'

EXEC sp_rename 'pk_ArbitriiMeciuri', 'pk_ArbitriMeciuri'

EXEC sp_rename 'FK__ArbitriiM__id_ar__440B1D61', 'FK__ArbitriM__id_ar__440B1D61'

EXEC sp_rename 'FK__ArbitriiM__id_me__44FF419A', 'FK__ArbitriM__id_me__44FF419A'

Drop Table ArbitriMeciuri

DROP Table Arbitri

Create Table Arbitri(
    id_arbitru INT PRIMARY KEY IDENTITY,
	nume VARCHAR(64),
	prenume VARCHAR(64)
);

Create Table ArbitriMeciuri(
	id_arbitru INT FOREIGN KEY REFERENCES Arbitri(id_arbitru),
	id_meci INT FOREIGN KEY REFERENCES Meciuri(id_meci),
	CONSTRAINT pk_ArbitriMeciuri PRIMARY KEY (id_arbitru, id_meci)
);