package example.Advanced.Music.app.Util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;

import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.enums.SortOrderEnum;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public class SearchUtil {
	public static Pageable getPageableFromParam(Integer page, Integer size, String sort, SortOrderEnum order) {
		Sort sortRequest;
		if (sort != null) {
			sortRequest = Sort.by(order == SortOrderEnum.asc ? Direction.ASC : Direction.DESC, sort);
		} else {
			sortRequest = Sort.unsorted();
		}
		if (size == null || size > Constants.DEFAULT_PAGE_SIZE_MAX) {
			size = Constants.DEFAULT_PAGE_SIZE_MAX;
		}
		return PageRequest.of(page, size, sortRequest);
	}

	public static <T> Specification<T> like (String fieldName, String value){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if (value != null) {
					return criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldName)), value.toLowerCase());
				}
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static <R, F> Specification<R> in(String fieldName, List<F> filterList) {
		return new Specification<R>() {
			@Override
			public Predicate toPredicate(Root<R> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if (filterList != null && !filterList.isEmpty()){
					if(filterList.size() > 1){
						In<F> inClause = (In<F>) criteriaBuilder.in(root.get(fieldName));
						filterList.forEach(e -> inClause.value(e));
						return (Predicate) inClause;
					} else {
						return criteriaBuilder.equal(root.<Comparable>get(fieldName), filterList.get(0));
					}
				}
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static <T> Specification<T> eq(String fieldName, Object value) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if (value != null) {
					return criteriaBuilder.equal(root.<Comparable>get(fieldName), value);
				}
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static <T> Specification<T> gt(String fieldName, Comparable value){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if(value != null){
					 criteriaBuilder.greaterThan(root.<Comparable>get(fieldName), value);
				}
				return criteriaBuilder.conjunction();

			}
		};
	}

	public static <T> Specification<T> ge(String fieldName, Comparable value){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if(value != null){
					return criteriaBuilder.greaterThanOrEqualTo(root.<Comparable>get(fieldName), value);
				}
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static <T> Specification<T> lt(String fieldName, Comparable value){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if (value != null){
					return criteriaBuilder.lessThan(root.<Comparable>get(fieldName), value);
				}
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static <T> Specification<T> le(String fieldName, Comparable value){
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
				if (value!= null){
					criteriaBuilder.lessThanOrEqualTo(root.<Comparable>get(fieldName), value);
				}
				return criteriaBuilder.conjunction();
			}
		};
	}
}
