package com.library.app.commontests.logaudit;

import com.library.app.common.utils.DateUtils;
import com.library.app.logaudit.model.LogAudit;
import com.library.app.logaudit.model.LogAudit.Action;
import com.library.app.user.model.User;
import org.junit.Ignore;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static com.library.app.commontests.utils.TestRepositoryUtils.findByPropertyNameAndValue;

/**
 * The type Log audit for tests repository. Dummy class to populate some audit data to test.
 */
@Ignore
public class LogAuditForTestsRepository {

    private LogAuditForTestsRepository() {
    }

    /**
     * All logs list.
     *
     * @return the list
     */
    public static List<LogAudit> allLogs() {
        final LogAudit logAudit1 = new LogAudit(admin(), Action.ADD, "Category");
        logAudit1.setCreatedAt(DateUtils.getAsDateTime("2015-01-08T19:32:22Z"));

        final LogAudit logAudit2 = new LogAudit(admin(), Action.UPDATE, "Category");
        logAudit2.setCreatedAt(DateUtils.getAsDateTime("2015-01-09T19:32:22Z"));

        final LogAudit logAudit3 = new LogAudit(johnDoe(), Action.ADD, "Order");
        logAudit3.setCreatedAt(DateUtils.getAsDateTime("2015-01-10T19:32:22Z"));

        return Arrays.asList(logAudit1, logAudit2, logAudit3);
    }

    /**
     * Log audit with id log audit.
     *
     * @param logAudit the log audit
     * @param id       the id
     * @return the log audit
     */
    public static LogAudit logAuditWithId(final LogAudit logAudit, final Long id) {
        logAudit.setId(id);
        return logAudit;
    }

    /**
     * Normalize dependencies log audit.
     *
     * @param logAudit the log audit
     * @param em       the em
     * @return the log audit
     */
    public static LogAudit normalizeDependencies(final LogAudit logAudit, final EntityManager em) {
        logAudit.setUser(findByPropertyNameAndValue(em, User.class, "email", logAudit.getUser().getEmail()));
        return logAudit;
    }

}