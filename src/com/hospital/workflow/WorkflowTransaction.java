
package com.hospital.workflow;

import java.sql.Connection;
import java.sql.SQLException;

public final class WorkflowTransaction {

    private WorkflowTransaction() {
    }

    public static <T> T execute(
            Connection connection,
            SqlOperation<T> operation
    ) throws SQLException {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        if (operation == null) {
            throw new IllegalArgumentException(
                    "Operation cannot be null."
            );
        }

        boolean originalAutoCommit = connection.getAutoCommit();

        // If another workflow already owns the transaction,
        // do not commit or rollback it here.
        if (!originalAutoCommit) {
            return operation.execute();
        }

        connection.setAutoCommit(false);

        try {
            T result = operation.execute();

            connection.commit();

            return result;

        } catch (SQLException | RuntimeException exception) {

            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                exception.addSuppressed(rollbackException);
            }

            throw exception;

        } finally {
            connection.setAutoCommit(true);
        }
    }

    @FunctionalInterface
    public interface SqlOperation<T> {
        T execute() throws SQLException;
    }
}
