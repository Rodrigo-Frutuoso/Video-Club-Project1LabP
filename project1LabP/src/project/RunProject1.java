package project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

public class RunProject1 {

	public static void main(String[] args) throws FileNotFoundException {
		new RunProject1().execute();
	}

	private void execute() throws FileNotFoundException {

		int exec = 1;
		// pedido 1
		try (PrintWriter writer = new PrintWriter(String.format("./log/log%d.txt", exec))) {
			VideoClub blockbuster = new VideoClub(String.format("./csv/stockIn/stockIn%d.csv", exec), 2);
			String purchaseLog = blockbuster.activityLog("./csv/rentals/rentals1.csv");
			writeLog(writer, purchaseLog, exec);
			updateLogAndStock(exec, writer, blockbuster);
		};

		exec++;
		// pedido 2
		try (PrintWriter writer = new PrintWriter(String.format("./log/log%d.txt", exec))) {
			VideoClub blockbuster = new VideoClub(String.format("./csv/stockIn/stockIn%d.csv", exec), 6);
			String purchaseLog = blockbuster.activityLog("./csv/rentals/rentals" + exec + ".csv");
			writeLog(writer, purchaseLog, exec);
			updateLogAndStock(exec, writer, blockbuster);
		};

		exec++;
		// pedido 3
		try (PrintWriter writer = new PrintWriter(String.format("./log/log%d.txt", exec))) {
			VideoClub blockbuster = new VideoClub(String.format("./csv/stockIn/stockIn%d.csv", exec), 20);
			String purchaseLog = blockbuster.activityLog("./csv/rentals/rentals" + exec + ".csv");
			writeLog(writer, purchaseLog, exec);
			updateLogAndStock(exec, writer, blockbuster);
		};

	}

	private void updateLogAndStock(int exec, PrintWriter writer, VideoClub blockbuster) throws FileNotFoundException {
		writer.println(String.format(Locale.US, "Revenue: $%.2f", blockbuster.getTotalRevenue()));
		writer.println(String.format(Locale.US, "Profit: $%.2f", blockbuster.getTotalProfit()));
		blockbuster.updateStock(String.format("./csv/stockOut/stockOut%d.csv", exec));
	}

	private void writeLog(PrintWriter writer, String rentalLog, int i) {
		writer.println(">> Log from rentals" + i);
		writer.println(rentalLog);
		writer.println();
	}
}
