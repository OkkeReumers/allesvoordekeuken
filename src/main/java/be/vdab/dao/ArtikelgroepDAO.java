package be.vdab.dao;

import java.util.List;

import be.vdab.entities.Artikelgroep;

// enkele imports
public class ArtikelgroepDAO extends AbstractDAO {
	public List<Artikelgroep> findAll() {
		return getEntityManager().createNamedQuery("Artikelgroepen.findAll",
				Artikelgroep.class).getResultList();
	}

	public Artikelgroep read(long id) {
		return getEntityManager().find(Artikelgroep.class, id);
	}
}