package com.exa.base.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.exa.base.model.Role;
import org.springframework.lang.NonNull;

public class RoleMapper implements RowMapper<Role> {
    
    @Override
    public Role mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException{
        Role objeto = new Role();

        objeto.setId(rs.getInt("ID"));
        objeto.setNombre(rs.getString("NOMBRE"));

        return objeto;
    }
}
