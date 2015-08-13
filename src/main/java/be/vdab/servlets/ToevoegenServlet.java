package be.vdab.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vdab.entities.Artikel;
import be.vdab.services.ArtikelService;

@WebServlet("/artikels/toevoegen.htm")
public class ToevoegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/artikels/toevoegen.jsp";
	private static final String REDIRECT_URL = "%s/artikels/toevoegen.htm?id=%d";
	private final transient ArtikelService artikelService = new ArtikelService();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, String> fouten = new HashMap<>();

		String naam = request.getParameter("naam");
		if (!Artikel.isNaamValid(naam)) {
			fouten.put("voornaam", "verplicht");
		}

		BigDecimal aankoopprijs = null;
		try {
			aankoopprijs = new BigDecimal(request.getParameter("aankoopprijs"));
			if (!Artikel.isAankoopprijsValid(aankoopprijs)) {
				fouten.put("aankoopprijs", "tik een positief getal of 0");
			}
		} catch (NumberFormatException ex) {
			fouten.put("aankoopprijs", "tik een positief getal of 0");
		}

		BigDecimal verkoopprijs = null;
		try {
			verkoopprijs = new BigDecimal(request.getParameter("verkoopprijs"));
			if (!Artikel.isVerkoopprijsValid(verkoopprijs)) {
				fouten.put("verkoopprijs", "tik een positief getal of 0");
			}
		} catch (NumberFormatException ex) {
			fouten.put("verkoopprijs", "tik een positief getal of 0");
		} if (fouten.isEmpty()) {
			if (!Artikel.isAankoopprijsVerkoopprijsValid(aankoopprijs, verkoopprijs)) {
				fouten.put("verkoopprijs", "moet groter zijn of gelijk aan de aankoopprijs");
			} else {
				Artikel artikel = new Artikel(naam, aankoopprijs, verkoopprijs);
				artikelService.create(artikel);
				response.sendRedirect(response.encodeRedirectUrl(String.format(REDIRECT_URL, request.getContextPath(), artikel.getId())));
			}
		}
		if (! fouten.isEmpty()) {
			request.setAttribute("fouten", fouten);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
		
	}

}