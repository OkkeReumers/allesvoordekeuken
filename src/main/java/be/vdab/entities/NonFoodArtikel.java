package be.vdab.entities;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

// enkele imports ...
@Entity
@DiscriminatorValue("NF")
public class NonFoodArtikel extends Artikel {
	private static final long serialVersionUID = 1L;
	private int garantie;

	public NonFoodArtikel(String naam, BigDecimal aankoopprijs,
			BigDecimal verkoopprijs, int garantie, Artikelgroep artikelgroep) {
		super(naam, aankoopprijs, verkoopprijs, artikelgroep);
		setGarantie(garantie);
	}

	public static boolean isGarantieValid(int garantie) {
		return garantie >= 0;
	}

	public int getGarantie() {
		return garantie;
	}

	public void setGarantie(int garantie) {
		if (!isGarantieValid(garantie)) {
			throw new IllegalArgumentException();
		}
		this.garantie = garantie;
	}

	protected NonFoodArtikel() {
	}
}