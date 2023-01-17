package com.github.jan5757.restaurantvoting.util.validation;

import lombok.experimental.UtilityClass;
import com.github.jan5757.restaurantvoting.error.IllegalRequestDataException;
import com.github.jan5757.restaurantvoting.model.HasId;

import java.util.Optional;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    public static <T> T checkExisted(Optional<T> obj, int id) {
        if (obj.isEmpty()) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
        return obj.get();
    }
}
