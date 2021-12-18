(defun substituie(a x y)
	(cond
		((equal a x) y)
		((atom a) a)
		(t (mapcar #'(lambda(arg) (substituie arg x y)) a))
	)
)
