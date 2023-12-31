package example.Advanced.Music.app.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;

public class FieldExistsValidator implements ConstraintValidator<FieldExists, Object> {
	private String dataFieldName;
	private String listFieldName;

	public void initialize(final FieldExists constraintAnnotation) {
		this.dataFieldName = constraintAnnotation.dataFieldName();
		this.listFieldName = constraintAnnotation.listFieldName();
	}

	@SuppressWarnings("unchecked")
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		try {
			Object dataField = PropertyUtils.getProperty(value, dataFieldName);
			List<String> fieldList = (List<String>) PropertyUtils.getProperty(value, listFieldName);
			for (String field : fieldList) {
				if (!PropertyUtils.isReadable(dataField, field)) {
					return false;
				}
			}
			return true;
		} catch (final Exception ex) {
			return false;
		}

	}
}
