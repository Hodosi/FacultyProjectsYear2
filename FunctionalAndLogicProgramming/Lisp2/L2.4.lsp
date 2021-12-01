(defun isnotnull(s)
	(cond
		((null s) 0)
		(t 1)
	)
)

(defun preordine(l)
	(cond
		((null l) nil)
		(t (append
		        (list (car l))
				(list (+ (isnotnull(cadr l)) (isnotnull(caddr l))))
				(preordine(cadr l))
				(preordine(caddr l))
			)
		)
	)
)