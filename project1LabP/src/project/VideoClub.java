package project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;

/**
 * @author Rodrigo Frutuoso 61865
 */
public class VideoClub {
	private Movie[] filmes;
	private double revenue;
	private double profit;

	/**
	 * O contrutor inicializa os atributos, da gestão de um videoclube
	 * 
	 * @param filename       ficheiro do tipo csv com a lista de stock do videoclube
	 * @param numberOfMovies numero de filmes que quer ler do stock
	 * @requires {@code fileName!=null && numberOfMovies>0}
	 * @throws FileNotFoundException
	 */
	public VideoClub(String fileName, int numberOfMovies) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		int counter = 0;
		sc.nextLine();// ignora a primeira linha do ficheiro(cabeçalho)
		while (sc.hasNextLine() && counter < numberOfMovies) {
			sc.nextLine();
			counter++;// contar quantas linhas existem no ficheiro
		}
		this.filmes = new Movie[counter];// cria um vetor, para os filmes
		sc.close();
		Scanner scanner = new Scanner(new File(fileName));
		scanner.nextLine();// ignora a primeira linha do arquivo( o cabeçalho)
		for (int i = 0; i < counter; i++) {

			String[] cadafilme = scanner.nextLine().split(",");// divide a linha atual num array
			// cria um movie com os dados de "cadafilme", linha atual do arquivo
			Movie movie = new Movie(cadafilme[0], Integer.parseInt(cadafilme[1]), Integer.parseInt(cadafilme[2]),
					cadafilme[3], Double.valueOf(cadafilme[4]), Double.valueOf(cadafilme[5].replace("%", "")));
			filmes[i] = movie;// adiciona o movie ao array dos filmes
		}
		scanner.close();
		this.revenue = 0;
		this.profit = 0;
	}

	/**
	 * 
	 * @return a quantidade de títulos de filmes distintos presentes no catálogo do
	 *         videoclube
	 */
	public int getNumberOfMovies() {

		return filmes.length;
	}

	/**
	 * @return a quantidade de títulos de filmes únicos que ainda estão disponíveis
	 *         para aluguer no videoclube
	 */
	public int numberAvailableMovies() {
		int disponiveis = 0;
		for (int i = 0; i < getNumberOfMovies(); i++) {// for para percorer as linhas (filmes)
			if (filmes[i].getQuantity() - filmes[i].numberOfRentals() > 0)
				disponiveis++;
		}
		return disponiveis;
	}

	/**
	 * @return o total arrecadado (receita) com todas as transações
	 *         (alugueres/devoluções em atraso) do dia
	 */
	public double getTotalRevenue() {

		return revenue;
	}

	/**
	 * @return o lucro total com todas as transações (alugueres/devoluções em
	 *         atraso) do dia.
	 */
	public double getTotalProfit() {

		return profit;
	}

	/**
	 * @param year qual queremos selecionar todos os filmes que estrearam nesse ano
	 * @return a representação textual com todos os filmes que estrearam em
	 *         determinado ano year, dividida em linhas, onde cada linha contém o
	 *         título e o preço de aluguer do filme
	 */
	public String filterByYear(int year) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filmes.length; i++) {
			if (filmes[i].getYear() == year) {
				sb.append("Title:" + filmes[i].getTitle() + ",Price:$" + filmes[i].getPrice() + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * @param price qual queremos selecionar todos os filmes que têm price menor
	 * @return a representação textual dividida em linhas, onde cada linha contém o
	 *         título de filmes que têm preço menor que price e o preço de aluguer
	 *         do filme
	 */
	public String filterByPrice(double price) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filmes.length; i++) {
			if (Double.valueOf(filmes[i].getPrice()) < price) {
				sb.append("Title:" + filmes[i].getTitle() + ",Price:$" + filmes[i].getPrice() + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * @return representação textual dividida em linhas, onde cada linha contém o
	 *         título de filmes disponíveis para aluguer e o preço de aluguer do
	 *         filme
	 */
	public String filterAvailableMovies() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filmes.length; i++) {
			if (isAvailable(filmes[i].getTitle()))
				sb.append("Title:" + filmes[i].getTitle() + ",Price:$" + filmes[i].getPrice() + System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * @param rentalsFileName ficheiro csv, com as transações no dia do videoclub
	 * @return representação textual dividida em linhas com as informações sobre os
	 *         alugueres e devoluções que foram realizados nesse dia
	 * @throws FileNotFoundException
	 */
	public String activityLog(String rentalsFileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(rentalsFileName));
		sc.nextLine();// ignora a primeira linha, o cabeçalho
		StringBuilder sb = new StringBuilder();

		while (sc.hasNextLine()) {
			String linha = sc.nextLine();// vai ler cada linhado ficheiro
			String[] activity = linha.split(",");// vai separar o conteúdo do ficheiro num array

			boolean found = false;// ver se essa ação rent/return foi realizada
			for (int i = 0; i < filmes.length; i++) { // percorrer matriz do stock
				// ver se existe esse nome/code no stock esse filme
				if (activity[2].equals(filmes[i].getTitle()) || activity[2].equals(filmes[i].getCode())) {
					// vai alugar e diponível
					if (activity[0].equals("rent") && isAvailable(activity[2])) {

						filmes[i].updateRent(Integer.parseInt(activity[1]));
						revenue += filmes[i].getPrice();
						profit += filmes[i].getPrice() - filmes[i].getPrice() * filmes[i].getTax() / 100;
						sb.append("Rental successful: client " + activity[1] + " rented " + filmes[i].getTitle()
								+ " for $" + String.format(Locale.US, "%.2f", filmes[i].getPrice())
								+ System.lineSeparator() + "Total: $"
								+ String.format(Locale.US, "%.2f", filmes[i].getPrice()) + " [$"
								+ String.format(Locale.US, "%.2f",
										(filmes[i].getPrice() - filmes[i].getPrice() * filmes[i].getTax() / 100))
								+ "]" + System.lineSeparator());

						found = true;
					}
					// vai alugar e nao está disponível
					else if (activity[0].equals("rent") && !isAvailable(activity[2])) {
						sb.append("Movie currently not available: client " + activity[1] + " asked for "
								+ filmes[i].getTitle() + System.lineSeparator());
						found = true;
					}
					// vai devolver
					else if (activity[0].equals("return")) {

						for (int a = 0; a < filmes[i].getRentals().length; a++) {
							if (Integer.parseInt(activity[1]) == filmes[i].getRentals()[a][0]) {
								// com delay
								if (filmes[i].getRentals()[a][1] < 0) {
									String date = " days";
									if (filmes[i].getRentals()[a][1] == -1) {
										date = " day";
									}
									sb.append("Movie returned with " + Math.abs((filmes[i].getRentals()[a][1])) + date
											+ " of delay: client " + activity[1] + " returned " + filmes[i].getTitle()
											+ System.lineSeparator() + "Total: $"
											+ String.format(
													Locale.US, "%.2f", (2.0 * Math.abs(filmes[i].getRentals()[a][1])))
											+ " [$"
											+ String.format(Locale.US, "%.2f",
													(2.0 * Math.abs(filmes[i].getRentals()[a][1]))
															- (2.0 * Math.abs(filmes[i].getRentals()[a][1]))
																	* filmes[i].getTax() / 100)
											+ "]" + System.lineSeparator());
									revenue += (2.0 * Math.abs(filmes[i].getRentals()[a][1]));
									profit += (2.0 * Math.abs(filmes[i].getRentals()[a][1]))
											- (2.0 * Math.abs(filmes[i].getRentals()[a][1])) * filmes[i].getTax() / 100;
								} // sem delay
								else {
									sb.append("Movie returned: client " + activity[1] + " returned "
											+ filmes[i].getTitle() + System.lineSeparator() + "Total: $0.00 [$0.00]"
											+ System.lineSeparator());
								}
								filmes[i].updateReturn(a);
								found = true;
							}

						}

					}
				}
			}
			if (!found) {// nao existe esse filme em stock
				sb.append("Movie not found: client " + activity[1] + " asked for " + activity[2]
						+ System.lineSeparator());
			}
		}
		sc.close();
		return sb.toString();
	}

	/**
	 * 
	 * @param chave nome do filme ou o code do filme
	 * @return true se essa chave existir no catálogo e se existe em stock essa
	 *         chave, caso contrário retorna false se nao exister essa chave no
	 *         catálogo ou se não existir em stok esse chave
	 */
	private boolean isAvailable(String chave) {
		for (int i = 0; i < getNumberOfMovies(); i++) {// for para percorer as linhas (filmes)
			if (filmes[i].getTitle().equals(chave) || filmes[i].getCode().equals(chave)) {
				if (filmes[i].getQuantity() - filmes[i].numberOfRentals() > 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * @param fileName ficheiro csv, onde vai ser guardado a atualização do stock
	 * @throws FileNotFoundException
	 */
	public void updateStock(String fileName) throws FileNotFoundException {
		PrintWriter myWriter = new PrintWriter(fileName);
		myWriter.write("Title,Year,Quantity,Rentals,Price,Tax" + System.lineSeparator());
		for (int i = 0; i < filmes.length; i++) {
			myWriter.write(filmes[i].getTitle() + "," + filmes[i].getYear() + ",");
			myWriter.write(filmes[i].getQuantity() + "," + filmes[i].rentalsToString());
			myWriter.write("," + filmes[i].getPrice() + "," + filmes[i].getTax() + "%" + System.lineSeparator());
		}
		myWriter.close();
	}

}