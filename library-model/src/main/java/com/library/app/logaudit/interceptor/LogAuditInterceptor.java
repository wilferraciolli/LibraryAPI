package com.library.app.logaudit.interceptor;

import com.library.app.logaudit.model.LogAudit;
import com.library.app.logaudit.repository.LogAuditRepository;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.security.Principal;

/**
 * The type Log audit interceptor.
 */
public class LogAuditInterceptor {

    @Inject
    private LogAuditRepository logAuditRepository;

    @Inject
    private UserServices userServices;

    @Inject
    private Principal principal;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Intercept object. This method is going to run before each time a method annotated with @Auditable runs.
     *
     * @param invocationContext the invocation context
     * @return the object
     * @throws Exception the exception
     */
    @AroundInvoke
    public Object intercept(final InvocationContext invocationContext) throws Exception {
        logger.debug("Interceptor being executed..");

        //invoke the real method. Ps in here there can be added logic to even cancel the real method to be invoked
        final Object toReturn = invocationContext.proceed();

        //log outcome details
        try {
            processAuditableAnnotation(invocationContext);
        } catch (final UserNotFoundException e) {
            logger.info("No user found for " + principal.getName());
        }

        return toReturn;
    }

    /**
     * Process auditable annotation. This method gets the classes annotated with @Auditable and gets its name and the user who has triggered the event.
     *
     * @param invocationContext the invocation context
     * @throws UserNotFoundException the user not found exception
     */
    private void processAuditableAnnotation(final InvocationContext invocationContext) throws UserNotFoundException {
        final Auditable auditable = invocationContext.getMethod().getAnnotation(Auditable.class);

        //check that the method is annotated with the @auditable annotation
        if (auditable != null) {
            final String elementName = invocationContext.getParameters()[0].getClass().getSimpleName();
            final LogAudit logAudit = new LogAudit(userServices.findByEmail(principal.getName()), auditable.action(),
                    elementName);
            logger.debug("Creating log audit {}", logAudit);
            logAuditRepository.add(logAudit);
        }
    }

}