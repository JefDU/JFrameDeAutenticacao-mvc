package controller.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import controller.dao.UsuarioDao;
import controller.dao.util.UsuarioConnectionSql;
import controller.exception.UsuarioDaoException;
import model.Usuario;

public class UsuarioDaoImpl implements UsuarioDao {

	public void salvarUsuario(Usuario user) throws UsuarioDaoException {
		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Insert into usuario" + "(cpf, nome, login, senha) " + " values(?, ?, ?, ?)";

			PreparedStatement pSql = connection.prepareStatement(comandoSql);

			pSql.setString(1, user.getCpf());
			pSql.setString(2, user.getNome());
			pSql.setString(3, user.getLogin());
			pSql.setString(4, user.getSenha());

			pSql.execute();

			connection.close();

		} catch (SQLException e) {

			throw new UsuarioDaoException("Erro ao salvar usuario: " + e.getMessage());
		}

	}

	public Boolean editarUsuario(String loginNovo, String cpf, String nome) throws UsuarioDaoException {
		Boolean alterou = false;
		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Update usuario set login = ? where cpf = ? and nome = ?";

			PreparedStatement pSql = connection.prepareStatement(comandoSql);

			pSql.setString(1, loginNovo);
			pSql.setString(2, cpf);
			pSql.setString(3, nome);

			alterou = pSql.executeUpdate() > 0;

			connection.close();

		} catch (SQLException e) {
			throw new UsuarioDaoException("Erro ao editar login do usuario: " + e.getMessage());
		}

		return alterou;

	}

	public Boolean editarSenha(String senhaNova, String cpf, String nome, String login) throws UsuarioDaoException {
		Boolean alterou = false;
		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Update usuario set senha = ? where cpf = ? and nome = ? and login = ?";

			PreparedStatement pSql = connection.prepareStatement(comandoSql);

			pSql.setString(1, senhaNova);
			pSql.setString(2, cpf);
			pSql.setString(3, nome);
			pSql.setString(4, login);

			alterou = pSql.executeUpdate() > 0;

			connection.close();

		} catch (SQLException e) {
			throw new UsuarioDaoException("Erro ao editar a senha do usuario: " + e.getMessage());
		}

		return alterou;

	}

	public Boolean validarAcesso(String login, String senha) throws UsuarioDaoException {
		Boolean validar = false;

		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Select * from usuario where login = ? and senha = ?";

			PreparedStatement psSql = connection.prepareStatement(comandoSql);
			psSql.setString(1, login);
			psSql.setString(2, senha);

			ResultSet lista = psSql.executeQuery();

			validar = lista.next();

			connection.close();

		} catch (SQLException e) {
			throw new UsuarioDaoException("Erro ao buscar usuario: " + e.getMessage());
		}

		return validar;
	}

	public DefaultTableModel listarUsuarios() throws UsuarioDaoException {
		DefaultTableModel dados = new DefaultTableModel();
		dados.addColumn("CPF");
		dados.addColumn("Nome");
		dados.addColumn("Login");

		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Select usuario.cpf, usuario.nome, usuario.login from usuario";

			PreparedStatement psSql = connection.prepareStatement(comandoSql);
			ResultSet lista = psSql.executeQuery();

			while (lista.next()) {

				dados.addRow(
						new Object[] { lista.getString("cpf"), lista.getString("nome"), lista.getString("login") });
			}

			connection.close();

		} catch (SQLException e) {

			throw new UsuarioDaoException("Erro ao listar usuarios: " + e.getMessage());
		}

		return dados;

	}

	public List<Usuario> usuarioList() throws UsuarioDaoException {
		List<Usuario> listUsuario = new ArrayList<>();
		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Select usuario.cpf, usuario.nome, usuario.login, usuario.senha from usuario";

			PreparedStatement psSql = connection.prepareStatement(comandoSql);
			ResultSet lista = psSql.executeQuery();

			Usuario user = null;

			while(lista.next()) {
				user = new Usuario();
				user.setCpf(lista.getString("cpf"));
				user.setNome(lista.getString("nome"));
				user.setLogin(lista.getString("login"));
				user.setSenha(lista.getString("senha"));

				listUsuario.add(user);
			}

			connection.close();

		} catch (SQLException e) {

			throw new UsuarioDaoException("Erro ao Listar(Array) usuarios: " + e.getMessage());
		}

		return listUsuario;
	}

	public Boolean excluir(String cpf) throws UsuarioDaoException {
		Boolean deletou = false;
		try {
			Connection connection = UsuarioConnectionSql.connection();
			String comandoSql = "Delete from usuario where cpf = ?";

			PreparedStatement pSql = connection.prepareStatement(comandoSql);
			pSql.setString(1, cpf);

			deletou = pSql.executeUpdate() > 0;

			connection.close();

		} catch (SQLException e) {
			throw new UsuarioDaoException("Erro ao deletar usuario: " + e.getMessage());
		}

		return deletou;
	}

}
