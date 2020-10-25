package com.udacity.pricing.domain.price.exception;

import me.alidg.errors.HandledException;
import me.alidg.errors.WebErrorHandler;
import javax.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PriceValidationErrorHandler implements WebErrorHandler {

    @Override
    public boolean canHandle(Throwable t) {
        boolean result = (t instanceof DataIntegrityViolationException);
        while (!result && (t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        if (!result){
            result = t instanceof ConstraintViolationException;
        }

        return result;
    }

    @Override
    public HandledException handle(Throwable t) {
        List keys = new ArrayList<String>();
        keys.add("currency.code.invalid");
        keys.add("currency.code.required");
        keys.add("price.required");
        keys.add("vehicleId.required");
        keys.add("vehicleId.not.unique");

        Set codes = new HashSet<String>();

        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof DataIntegrityViolationException)) {
            t = t.getCause();
        }

        // There can be multiple constraint violations, so collect them all.
        if (t instanceof ConstraintViolationException) {
            for (int i = 0; i < keys.size(); i++) {
                if (t.getMessage().contains((CharSequence) keys.get(i))){
                    codes.add(keys.get(i));
                }
            }
        } else if (t instanceof DataIntegrityViolationException) {
            codes.add("vehicleId.not.unique");
        } else {
            codes.add("unknown_error");
        }
        return new HandledException(codes, HttpStatus.BAD_REQUEST, null);
    }
}
