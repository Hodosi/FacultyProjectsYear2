(defun selectie(l n)
	(cond
		((null l) nil)
		((= n 0) (CAR l))
		(t (selectie (CDR l) (- n 1)))
	)
)

(defun apare(l atm)
	(cond
		((null l) nil)
		((equal (CAR l) atm) t)
		((ATOM (CAR l)) (apare (CDR l) atm))
		((apare (CAR l) atm) t)
		(t (apare (CDR l) atm))

	)
)

(defun subliste(l)
	(cond
		((null l) nil)
		((ATOM (CAR l)) (subliste (CDR l)))
		(t (APPEND (LIST (CAR l)) (subliste (CAR l)) (subliste (CDR l))))
	)
)

(defun sublisteMain(l)
	(subliste (LIST l))
)


(defun listaToMultime(l)
	(cond
		((null l) nil)
		((apare (CDR l) (CAR l)) (listaToMultime (CDR l)))
		(t (CONS (CAR l) (listaToMultime (CDR l))))
	)
)
