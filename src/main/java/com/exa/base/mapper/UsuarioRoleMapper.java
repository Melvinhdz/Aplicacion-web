package com.exa.base.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.exa.base.model.UsuarioRole;
import org.springframework.lang.NonNull;

public class UsuarioRoleMapper implements RowMapper<UsuarioRole> {
    
    @Override
    public UsuarioRole mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException{
        UsuarioRole objeto = new UsuarioRole();

        objeto.setUsuarioId(rs.getInt("USUARIO_ID"));
        objeto.setRoleId(rs.getInt("ROLE_ID"));

        return objeto;
    }
}
