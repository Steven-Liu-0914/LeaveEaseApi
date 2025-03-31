package com.leaveease.api.util;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseHelper {

    private final DataSource dataSource;

    public DatabaseHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, Object>> callStoredProcedureWithResult(
            String procedureName,
            MapSqlParameterSource params,
            boolean throwIfEmpty,
            String errorMessage
    ) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                    .withProcedureName(procedureName);

            Map<String, Object> result = jdbcCall.execute(params);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("#result-set-1");

            if ((rows == null || rows.isEmpty()) && throwIfEmpty) {
                throw new RuntimeException(
                        (errorMessage != null && !errorMessage.isEmpty())
                                ? errorMessage
                                : "No data returned from " + procedureName
                );
            }

            return rows;

        } catch (RuntimeException e) {
            throw e; // Rethrow known business exceptions as-is
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred. Please contact the administrator.");
        }
    }


    public void callStoredProcedureWithoutResult(
            String procedureName,
            MapSqlParameterSource params,
            String errorMessage
    ) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                    .withProcedureName(procedureName);

            jdbcCall.execute(params);

        } catch (RuntimeException e) {
            throw e; // Rethrow known runtime errors (if any)
        } catch (Exception e) {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                throw new RuntimeException(errorMessage);
            }
            throw new RuntimeException("An unexpected error occurred. Please contact the administrator.");
        }
    }

}