package com.ftn.Teretana.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.Teretana.dao.TipTreningaDAO;
import com.ftn.Teretana.dao.TreningDAO;
import com.ftn.Teretana.model.TipTreninga;
import com.ftn.Teretana.model.Trening;

@Repository
public class TreningDAOImpl implements TreningDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemlate;
	
	@Autowired
	private TipTreningaDAO tipTreningaDAO;
	
	
	private class TreningRowCallBackHandler implements RowCallbackHandler{
		
		private Map<Long, Trening> treninzi = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			// TODO Auto-generated method stub
			int index = 1;
			Long treningid = rs.getLong(index++);
			String naziv = rs.getString(index++);
			String trener = rs.getString(index++);
			String opis = rs.getString(index++);
			String slika = rs.getString(index++);
			Double cena = rs.getDouble(index++);
			String vrstaTreninga = rs.getString(index++);
			String nivoTreninga = rs.getString(index++);
			LocalTime trajanje = rs.getObject(index++, LocalTime.class);
			Float prosecnaOcena = rs.getFloat(index++);
			
			Trening trening = treninzi.get(treningid);
			if(trening == null) {
				trening = new Trening(treningid, naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena);
				treninzi.put(trening.getId(), trening);
			}
			
			Long tipTreningaId = rs.getLong(index++);
			String ime = rs.getString(index++);
			String opisTT = rs.getString(index++);
			TipTreninga tt = new TipTreninga(tipTreningaId, ime, opisTT);
			if(trening.getTipTreninga().contains(tt)) {
				trening.getTipTreninga().remove(tt);
				trening.getTipTreninga().add(tt);
			}else {
				trening.getTipTreninga().add(tt);
			}

		}
		
		public List<Trening> getTreninzi(){
			return new ArrayList<>(treninzi.values());
		}
		
	}


	@Override
	public List<Trening> findAll() {
		String sql = "SELECT * FROM treninzi t " +
				"LEFT JOIN treningTipTreninga ttt ON ttt.treningId = t.id " +
				"LEFT JOIN tipoviTreninga tt ON ttt.tipTreningaId = tt.id " +
			"ORDER BY t.id";
		
		TreningRowCallBackHandler rowCallbackHandler = new TreningRowCallBackHandler();
		jdbcTemlate.query(sql, rowCallbackHandler);
		return rowCallbackHandler.getTreninzi();
	}
	
	private class TreningRowMapper implements RowMapper<Trening> {

		@Override
		public Trening mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			Long treningId = rs.getLong(index++);
			String naziv = rs.getString(index++);
			String trener = rs.getString(index++);
			String opis = rs.getString(index++);
			String slika = rs.getString(index++);
			Double cena = rs.getDouble(index++);
			String vrstaTreninga = rs.getString(index++);
			String nivoTreninga = rs.getString(index++);
			LocalTime trajanje = rs.getObject(index++, LocalTime.class);
			Float prosecnaOcena = rs.getFloat(index++);



			Trening trening = new Trening(treningId, naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena);
			return trening;
		}

	}


	@Override
	public List<Trening> find(String naziv, Long tipTreningaId, String trener, Double cenaOd, Double cenaDo,
			String vrstaTreninga, String nivoTreninga) {
		// TODO Auto-generated method stub
		ArrayList<Object> lista = new ArrayList<Object>();
		
		String  sql = "SELECT * FROM treninzi";
		StringBuffer whereSql = new StringBuffer(" WHERE ");
		boolean imaArgumenata = false;
		
		if(naziv != null) {
			naziv = "%" + naziv + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("naziv LIKE ?");
			imaArgumenata = true;
			lista.add(naziv);
		}
		
		if(trener != null) {
			trener = "%" + trener + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("trener LIKE ?");
			imaArgumenata = true;
			lista.add(trener);
		}
		
		if(cenaOd != null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("cena >= ?");
			imaArgumenata = true;
			lista.add(cenaOd);
		}
		
		if(cenaDo != null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("cena <= ?");
			imaArgumenata = true;
			lista.add(cenaDo);
		}
		
		if(vrstaTreninga != null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("vrstaTreninga LIKE ?");
			imaArgumenata = true;
			lista.add(vrstaTreninga);
		}
		
		if(nivoTreninga != null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("nivoTreninga LIKE ?");
			imaArgumenata = true;
			lista.add(nivoTreninga);
		}
		
		if(imaArgumenata) {
			sql=sql + whereSql.toString() + " ORDER BY id";
		}else {
			sql=sql + " ORDER BY id";
			
		}
		
		List<Trening> treninzi = jdbcTemlate.query(sql, lista.toArray(), new TreningRowMapper());
		for(Trening tr : treninzi) {
			tr.setTipTreninga(findTreningTipTreninga(tr.getId(), null));
		}
		
		if(tipTreningaId != null)
			for (Iterator<Trening> iterator = treninzi.iterator(); iterator.hasNext();) {
				Trening t = (Trening) iterator.next();
				boolean zaBrisanje = true;
				for (TipTreninga tt : t.getTipTreninga()) {
					if(tt.getId() == tipTreningaId) {
						zaBrisanje = false;
						break;
					}
				}
				if(zaBrisanje)
					iterator.remove();
			}
		
		
		
		return treninzi;
	}
	
	private class TreningTipTreningaRowMapper implements RowMapper<Long []> {

		@Override
		public Long [] mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			Long treningId = rs.getLong(index++);
			Long tipTreningaId = rs.getLong(index++);

			Long [] treningTipTreninga = {treningId, tipTreningaId};
			return treningTipTreninga;
		}
	}
	
	private List<TipTreninga> findTreningTipTreninga(Long treningId, Long tipTreningaId) {
		
		List<TipTreninga> tipoviTTrening = new ArrayList<TipTreninga>();
		
		ArrayList<Object> listaArgumenata = new ArrayList<Object>();
		
		String sql = "SELECT ttt.treningId, ttt.tipTreningaId FROM treningTipTreninga ttt ";
		
		StringBuffer whereSql = new StringBuffer(" WHERE ");
		boolean imaArgumenata = false;
		
		if(treningId != null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ttt.treningId = ?");
			imaArgumenata = true;
			listaArgumenata.add(treningId);
		}
		
		if(tipTreningaId != null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ttt.tipTreningaId = ?");
			imaArgumenata = true;
			listaArgumenata.add(tipTreningaId);
		}

		if(imaArgumenata)
			sql=sql + whereSql.toString()+" ORDER BY ttt.treningId";
		else
			sql=sql + " ORDER BY ttt.tipTreningaId";
		
		List<Long[]> treningTipovi = jdbcTemlate.query(sql, listaArgumenata.toArray(), new TreningTipTreningaRowMapper()); 
				
		for (Long[] ttt : treningTipovi) {
			tipoviTTrening.add(tipTreningaDAO.findOne(ttt[1]));
		}
		return tipoviTTrening;
	}

	@Override
	public Trening findOne(Long id) {
		// TODO Auto-generated method stub
		String sql = 
				"SELECT t.id, t.naziv, t.trener, t.opis, t.slika, t.cena, t.vrstaTreninga, t.nivoTreninga, "
				+ "t.trajanje, t.prosecnaOcena, tt.id, tt.ime, tt.opis FROM treninzi t " + 
				"LEFT JOIN treningTipTreninga ttt ON ttt.treningId = t.id " + 
				"LEFT JOIN tipoviTreninga tt ON ttt.tipTreningaId = tt.id " + 
				"WHERE t.id = ? " + 
				"ORDER BY t.id";

		TreningRowCallBackHandler rowCallbackHandler = new TreningRowCallBackHandler();
		jdbcTemlate.query(sql, rowCallbackHandler, id);
		
		return rowCallbackHandler.getTreninzi().get(0);
	}

	@Transactional
	@Override
	public int edit(Trening trening) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM treningTipTreninga WHERE treningId = ? ";
		jdbcTemlate.update(sql, trening.getId());
		
		boolean uspeh = true;
		sql = "INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (?, ?) ";
		for (TipTreninga itTipTreninga : trening.getTipTreninga()) {
			uspeh = uspeh && jdbcTemlate.update(sql, trening.getId(), itTipTreninga.getId()) == 1;
			
		}
		
		sql = "UPDATE treninzi SET naziv = ?, trener = ?, opis = ?, cena = ?, vrstaTreninga = ?, nivoTreninga = ?, trajanje = ? WHERE id = ?";
		uspeh = jdbcTemlate.update(sql, trening.getNaziv(), trening.getTrener(), trening.getOpis(), trening.getCena(), trening.getVrstaTreninga(), trening.getNivoTreninga(), trening.getTrajanje(), trening.getId()) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int save(Trening trening) {
		// TODO Auto-generated method stub
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
				String sql = "INSERT INTO treninzi (naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, trening.getNaziv());
				preparedStatement.setString(index++, trening.getTrener());
				preparedStatement.setString(index++, trening.getOpis());
				preparedStatement.setString(index++, "images/fitness.jpg");
				preparedStatement.setDouble(index++, trening.getCena());
				preparedStatement.setString(index++, trening.getVrstaTreninga());
				preparedStatement.setString(index++, trening.getNivoTreninga());
				preparedStatement.setObject(index++, trening.getTrajanje());
				preparedStatement.setFloat(index++, 0);

				return preparedStatement;
			}
		};
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemlate.update(preparedStatementCreator, keyHolder) == 1;

		if(uspeh) {
			String sql = "INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (?, ?)";

			for (TipTreninga itTipTreninga : trening.getTipTreninga()) {
				uspeh = uspeh && jdbcTemlate.update(sql, keyHolder.getKey(), itTipTreninga.getId()) == 1;
			}
			
		}
		
		
		
		return uspeh?1:0;
	}

	@Override
	public int update(Trening trening) {
		// TODO Auto-generated method stub
		String sql = "UPDATE treninzi SET prosecnaOcena = ?  WHERE id = ?";
		return jdbcTemlate.update(sql, trening.getOcena(), trening.getId());
	
	}
	
	
	
	
	

}
