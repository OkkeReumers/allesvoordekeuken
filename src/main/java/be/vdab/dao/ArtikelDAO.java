package be.vdab.dao;

import javax.persistence.EntityManager;

import be.vdab.entities.Artikel;

public class ArtikelDAO {
	public Artikel read(long id, EntityManager entityManager) {
			return entityManager.find(Artikel.class, id);
	}
	
	public void create(Artikel artikel, EntityManager entityManager) {
		entityManager.persist(artikel);
		}

}
