package be.vdab.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import be.vdab.valueobjects.Korting;

@Entity
@Table(name = "artikels")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "soort")
@NamedEntityGraph(name = "Artikel.metArtikelgroep",
attributeNodes = @NamedAttributeNode("artikelgroep"))
public abstract class Artikel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	private String naam;
	private BigDecimal aankoopprijs;
	private BigDecimal verkoopprijs;

	@ElementCollection
	@OrderBy("vanafAantal")
	@CollectionTable(name = "kortingen", joinColumns = @JoinColumn(name = "artikelid"))
	private Set<Korting> kortingen;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "artikelgroepid")
	private Artikelgroep artikelgroep;
	
	

	public Artikelgroep getArtikelgroep() {
		return artikelgroep;
	}

	public void setArtikelgroep(Artikelgroep artikelgroep) {
		if (this.artikelgroep != null
				&& this.artikelgroep.getArtikels().contains(this)) {
			this.artikelgroep.removeArtikel(this);
		}
		this.artikelgroep = artikelgroep;
		if (artikelgroep != null && !artikelgroep.getArtikels().contains(this)) {
			artikelgroep.addArtikel(this);
		}
	}

	public Set<Korting> getKortingen() {
		return Collections.unmodifiableSet(kortingen);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAankoopprijs() {
		return aankoopprijs;
	}

	public void setAankoopprijs(BigDecimal aankoopprijs) {
		if (!isAankoopprijsValid(aankoopprijs)) {
			throw new IllegalArgumentException();
		}
		this.aankoopprijs = aankoopprijs;
	}

	public BigDecimal getVerkoopprijs() {
		return verkoopprijs;
	}

	public void setVerkoopprijs(BigDecimal verkoopprijs) {
		if (!isVerkoopprijsValid(verkoopprijs)) {
			throw new IllegalArgumentException();
		}
		this.verkoopprijs = verkoopprijs;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		if (!isNaamValid(naam)) {
			throw new IllegalArgumentException();
		}
		this.naam = naam;
	}

	public BigDecimal getWinstPercentage() {
		return verkoopprijs.subtract(aankoopprijs)
				.divide(aankoopprijs, 2, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
	}

	public Artikel(String naam, BigDecimal aankoopprijs,
			BigDecimal verkoopprijs, Artikelgroep artikelgroep) {
		if (!isAankoopprijsVerkoopprijsValid(aankoopprijs, verkoopprijs)) {
			throw new IllegalArgumentException();
		}
		setNaam(naam);
		setAankoopprijs(aankoopprijs);
		setVerkoopprijs(verkoopprijs);
		setArtikelgroep(artikelgroep);
	}

	protected Artikel() {
	}

	public static boolean isNaamValid(String naam) {
		return naam != null && !naam.isEmpty();
	}

	public static boolean isAankoopprijsValid(BigDecimal aankoopprijs) {
		return aankoopprijs != null
				&& (aankoopprijs.compareTo(BigDecimal.ZERO) >= 0);
	}

	public static boolean isVerkoopprijsValid(BigDecimal verkoopprijs) {
		return verkoopprijs != null
				&& (verkoopprijs.compareTo(BigDecimal.ZERO) >= 0);
	}

	public static boolean isAankoopprijsVerkoopprijsValid(
			BigDecimal aankoopprijs, BigDecimal verkoopprijs) {
		return aankoopprijs.compareTo(verkoopprijs) <= 0;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Artikel)) {
			return false;
		}
		Artikel anderArtikel = (Artikel) object;
		return naam.equalsIgnoreCase(anderArtikel.naam);
	}

	@Override
	public int hashCode() {
		return naam.toUpperCase().hashCode();
	}

}