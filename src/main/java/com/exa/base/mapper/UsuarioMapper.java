package com.exa.base.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.exa.base.model.Usuario;
import org.springframework.lang.NonNull;


public class UsuarioMapper implements RowMapper<Usuario> {
    
    @Override
    public Usuario mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException{
        Usuario objeto = new Usuario();

        objeto.setId(rs.getInt("ID"));
        objeto.setNombre(rs.getString("NOMBRE"));
        objeto.setEmail(rs.getString("EMAIL"));
        objeto.setPassword(rs.getString("PASS"));
        objeto.setRoles(rs.getString("ROLES"));
        objeto.setActivo(rs.getBoolean("ACTIVO"));

        return objeto;
    }
}
