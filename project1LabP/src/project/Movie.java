package project;

/**
 * @author Rodrigo Frutuoso 61865
 */
public class Movie {
	private final String title;
	private final int year;
	private final int quantity;
	private String rentals;
	private int[][] matrizRentals;
	private final double price;
	private final double tax;
	private final String code;

	/**
	 * O contrutor inicializa os atributos, das informações sobre um filme
	 * 
	 * @param title    título do filme
	 * @param year     ano de estreia do filme
	 * @param quantity quantidade de exemplares deste filme que estão no inventário
	 * @param rentals  informação sobre os alugueres ativos
	 * @param price    preço de aluguer do filme
	 * @param tax      comissão cobrada pelo estúdio
	 * @requires {@code title!=null && year>0 && quantity>0 && rentals=!null && price>0.0, tax>0.0}
	 */
	public Movie(String title, int year, int quantity, String rentals, double price, double tax) {
		this.title = title;
		this.year = year;
		this.quantity = quantity;
		this.rentals = rentals;
		this.matrizRentals = new int[quantity][2];
		this.price = price;
		this.tax = tax;
		this.code = getCode();
	}

	/**
	 * @return o título do filme
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return o ano de estreia do filme
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return a quantidade de exemplares deste filme que estão no inventário
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return uma matriz com os alugueres ativos
	 */
	public int[][] getRentals() {
		String str = rentals.replace("(", "").replace(")", "");
		if (str.length() != 0) {
			// dividir a string em pares de numeros
			String[] pares = str.split(" ");

			// colocar cada par numa linha da matriz
			for (int i = 0; i < pares.length; i++) {
				String[] numeros = pares[i].split(";");
				matrizRentals[i][0] = Integer.parseInt(numeros[0]);
				matrizRentals[i][1] = Integer.parseInt(numeros[1]);
			}
		}
		return matrizRentals;
	}

	/**
	 * @return o numero de alugueres ativos
	 */
	public int numberOfRentals() {
		int counter = 0;
		for (int i = 0; i < getRentals().length; i++) {
			if (getRentals()[i][0] != 0)
				counter++;
		}
		return counter;
	}

	/**
	 * @param client para adicionar um rental
	 */
	public void updateRent(int client) {
		int linha = 0;
		while (getRentals()[linha][0] != 0 && linha < getRentals().length) {
			linha++;
		}
		getRentals()[linha][0] = client;
		getRentals()[linha][1] = 7;
	}

	/**
	 * @param linha para remover um rental
	 */
	public void updateReturn(int linha) {
		matrizRentals[linha][0] = 0;
		matrizRentals[linha][1] = 0;
		rentalsToString();
	}

	/**
	 * @return representação textual com os rentals como string
	 */
	public String rentalsToString() {
		StringBuilder novo = new StringBuilder();
		for (int i = 0; i < this.matrizRentals.length; i++) {
			if (matrizRentals[i][0] != 0) {
				novo.append("(" + matrizRentals[i][0] + ";" + matrizRentals[i][1] + ")");
				if (i != matrizRentals.length - 1)
					novo.append(" ");
			}
		}
		return rentals = novo.toString();
	}

	/**
	 * @return o preço de aluguer do filme
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return a comissão cobrada pelo estúdio
	 */
	public double getTax() {
		return tax;
	}

	/**
	 * @return cota identificativa constituída por 6 letras, desse filme
	 */
	public String getCode() {
		// remover todos os caracteres que não são letras, e colocar tudo em maiúscula
		String getcode = title.replaceAll("[^a-zA-Z]", "").toUpperCase();
		int direction = 1;// controlar a direção (de cima para baixo ou de baixo para cima)
		int linha = 0;// linha da grelha
		char[][] grelha = new char[3][getcode.length()];// criar a grelha com 3 linhas e com length colunas

		// loop para adicionar as letras na grelha como no enunciado
		for (int i = 0; i < getcode.length(); i++) {
			grelha[linha][i] = getcode.charAt(i);
			if (linha == 0)
				direction = 1;
			if (linha == 2)
				direction = -1;
			linha += direction;
		}
		// adicionar os caracteres num stringbuilder, linha a linha
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < grelha.length; i++) {
			for (int j = 0; j < grelha[i].length; j++) {
				if (grelha[i][j] != '\0') {
					code.append(grelha[i][j]);
				}
			}
		}
		// retornar as primeiras 3 letras, e as últimas 3 letras, desse StringBuilder
		return code.substring(0, 3) + code.substring(code.length() - 3);
	}

}