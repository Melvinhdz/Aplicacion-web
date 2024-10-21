package com.exa.base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.exa.base.mapper.RoleMapper;
import com.exa.base.model.Role;

@Component
public class RoleDao {

    @Autowired
    private JdbcTemplate postgresTemplate;

    public List<Role> listaRoles() {
        List<Role> lista = new ArrayList<Role>();
        String query = "SELECT * FROM ROLE ORDER BY NOMBRE";

        try {
            lista = postgresTemplate.query(query, new RoleMapper());
        } catch (Exception e) {
            System.out.println("ERROR : RoleDao | listaRoles | " + e);
        }

        return lista;
    }

    public HashMap<Integer, Integer> mapaRolesUsuario(int id) {
        List<Integer> lista = new ArrayList<Integer>();
        HashMap<Integer, Integer> mapa = new HashMap<Integer, Integer>();
        String query = "SELECT ROLE_ID FROM USUARIO_ROLE WHERE USUARIO_ID = ?";

        try {
            lista = postgresTemplate.queryForList(query, Integer.class, id);
        } catch (Exception e) {
            System.out.println("ERROR : RoleDao | mapaRolesUsuario | " + e);
        }

        for (Integer dato : lista) {
            mapa.put(dato, dato);
        }

        return mapa;
    }
}
