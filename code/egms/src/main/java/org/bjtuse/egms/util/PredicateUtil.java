package org.bjtuse.egms.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class PredicateUtil {
	public static Predicate add(Predicate pred1, Predicate pred2, CriteriaBuilder cb) {
		if (pred1 == null)
			return pred2;
		else
			return cb.and(pred1, pred2);
	}
	
	public static Predicate or(Predicate pred1, Predicate pred2, CriteriaBuilder cb) {
		if (pred1 == null)
			return pred2;
		else
			return cb.or(pred1, pred2);
	}
	
	public static Predicate like(CriteriaBuilder cb, Path<String> pathExp, String searchTerm){
		StringBuilder pattern = new StringBuilder();
		pattern.append("%");
		pattern.append(searchTerm.toLowerCase());
		pattern.append("%");
		
		return cb.like(pathExp, pattern.toString());
	}
}
