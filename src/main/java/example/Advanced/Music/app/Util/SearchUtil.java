package example.Advanced.Music.app.Util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import example.Advanced.Music.app.constans.Constants;
import example.Advanced.Music.app.enums.SortOrderEnum;
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

}
