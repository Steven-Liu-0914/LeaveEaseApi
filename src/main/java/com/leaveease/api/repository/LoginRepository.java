package com.leaveease.api.repository;

import com.leaveease.api.dto.LoginDto;
import com.leaveease.api.util.DatabaseHelper;
import com.leaveease.api.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class LoginRepository {

    @Autowired
    private DatabaseHelper db;

    public Map<String, Object> login(String staffNumber, String passwordHash) {
        List<Map<String, Object>> rows = db.callStoredProcedureWithResult(
                "sp_LoginUser",
                new MapSqlParameterSource()
                        .addValue("p_staffNumber", staffNumber)
                        .addValue("p_inputHash", passwordHash),
                true,
                ErrorMessages.LOGIN_INVALID_CREDENTIALS.getMessage()
        );

        return rows.get(0);
    }

    public String getSaltByStaffNumber(String staffNumber) {
        List<Map<String, Object>> rows = db.callStoredProcedureWithResult(
                "sp_GetSaltByStaffNumber",
                new MapSqlParameterSource().addValue("p_staffNumber", staffNumber),
                true,
                ErrorMessages.LOGIN_INVALID_CREDENTIALS.getMessage()
        );

        return (String) rows.get(0).get("PasswordSalt");
    }

}