package com.exa.base.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.exa.base.mapper.UsuarioMapper;
import com.exa.base.model.Usuario;

@Component
public class UsuarioDao {

	@Autowired
	private BCryptPasswordEncoder bCrypt;

	@Autowired
	private JdbcTemplate postgresTemplate;

	public boolean grabaUsuario(Usuario objeto, String[] roles) {
		boolean ok = false;

		if (!buscaEmail(objeto.getEmail())) {

			objeto.setPassword(bCrypt.encode(objeto.getPassword()));

			String query = "SELECT NOMBRE FROM ROLE WHERE ID = ?";

			String grabaRoles = "-";

			if (roles != null) {
				for (String roleId : roles) {
					String role = postgresTemplate.queryForObject(query, String.class, Integer.parseInt(roleId));
					grabaRoles += String.valueOf(role) + "-";
				}
			}

			Object[] parametros = new Object[] {
					objeto.getNombre(), objeto.getEmail(), objeto.getPassword(), grabaRoles
			};

			query = "INSERT INTO USUARIO(NOMBRE,EMAIL,PASS,ROLES,ACTIVO) VALUES(?, ?, ?, ?, TRUE)";

			if (postgresTemplate.update(query, parametros) >= 1) {
				query = "SELECT ID FROM USUARIO WHERE NOMBRE = ? AND EMAIL = ?";

				parametros = new Object[] {
						objeto.getNombre(), objeto.getEmail()
				};

				int usuarioId = postgresTemplate.queryForObject(query, Integer.class, parametros);

				if (roles != null) {
					for (String roleId : roles) {
						query = "INSERT INTO USUARIO_ROLE(USUARIO_ID, ROLE_ID) VALUES( ?, ?)";
						postgresTemplate.update(query, new Object[] { usuarioId, Integer.parseInt(roleId) });
					}
				}
				ok = true;
			}
		}
		return ok;
	}

	public boolean editarUsuario(Usuario objeto, String[] roles) {
		boolean ok = false;

		String query = "SELECT NOMBRE FROM ROLE WHERE ID = ?";

		String grabaRoles = "-";

		if (roles != null) {
			for (String roleId : roles) {
				String role = postgresTemplate.queryForObject(query, String.class, Integer.parseInt(roleId));
				grabaRoles += String.valueOf(role) + "-";
			}
		}

		Object[] parametros = new Object[] {
				objeto.getNombre(), objeto.getEmail(), grabaRoles, objeto.getId()
		};

		query = "UPDATE USUARIO SET NOMBRE = ?, EMAIL = ?, ROLES = ? WHERE ID = ?";

		if (postgresTemplate.update(query, parametros) >= 1) {
			query = "DELETE FROM USUARIO_ROLE WHERE USUARIO_ID = ?";
			if (postgresTemplate.update(query, new Object[] { objeto.getId() }) > 0) {
				if (roles != null) {
					for (String roleId : roles) {
						query = "INSERT INTO USUARIO_ROLE(USUARIO_ID, ROLE_ID) VALUES( ?, ?)";
						postgresTemplate.update(query, new Object[] { objeto.getId(), Integer.parseInt(roleId) });
					}
				}
			}
			ok = true;
		}
		return ok;
	}

	public boolean editarPassword(Usuario objeto) {
		boolean ok = false;

		System.out.println("UNO : "+objeto.getId()+" - "+objeto.getPassword());
		
		objeto.setPassword(bCrypt.encode(objeto.getPassword()));
		
		Object[] parametros = new Object[] {
			objeto.getPassword(), objeto.getId()
		};
		
		System.out.println("DOS : "+objeto.getId()+" - "+objeto.getPassword());

		String query = "UPDATE USUARIO SET PASS = ? WHERE ID = ?";

		if (postgresTemplate.update(query, parametros) >= 1) {
			ok = true;
		}
		return ok;
	}

	public boolean cambiarStatus(int id, boolean status) {
		boolean ok = false;
		String query = "";

		if (status) {
			query = "UPDATE USUARIO SET ACTIVO = FALSE WHERE ID = ?";
		} else {
			query = "UPDATE USUARIO SET ACTIVO = TRUE WHERE ID = ?";
		}

		try {
			if (postgresTemplate.update(query, new Object[] { id }) >= 1) {
				ok = true;
			}
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | cambiarStatus | " + e);
		}
		return ok;
	}

	public List<Usuario> listaUsuarios() {
		List<Usuario> lista = new ArrayList<Usuario>();
		String query = "SELECT * FROM USUARIO ORDER BY NOMBRE";

		try {
			lista = postgresTemplate.query(query, new UsuarioMapper());
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | listaUsuarios | " + e);
		}

		return lista;
	}

	public Usuario buscaUsuarioPorId(int id) {
		Usuario objeto = new Usuario();
		String query = "SELECT * FROM USUARIO WHERE ID = ?";

		try {
			objeto = postgresTemplate.queryForObject(query, new UsuarioMapper(), id);
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | buscaUsuarioPorId | " + e);
		}

		return objeto;
	}

	public boolean buscaEmail(String email) {
		boolean ok = false;
		String query = "SELECT COUNT(*) FROM USUARIO WHERE EMAIL = ?";

		try {
			if (postgresTemplate.queryForObject(query, Integer.class, email) > 0) {
				ok = true;
			}
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | buscaUsuarioPorId | " + e);
		}

		return ok;
	}
}
