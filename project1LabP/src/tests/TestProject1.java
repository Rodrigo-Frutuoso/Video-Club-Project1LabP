package tests;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import org.junit.Test;
import project.VideoClub;

public class TestProject1 {

    private static final String path = "./csv/tests/";
    private static final String STOCK_IN_CSV = path+"stockIn.csv";
    private static final String RENTALS1_CSV = path+"rentals1.csv";
    private static final String RENTALS2_CSV = path+"rentals2.csv";
    private static final String RENTALS3_CSV = path+"rentals3.csv";
    private static final String STOCK_OUT_CSV = path+"stockOut.csv";

    
    @Test
    public void testConstructor() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 2);
        assertEquals(2, blockbuster.getNumberOfMovies());
    }
    
    
    @Test
    public void testConstructor2() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 6);
        assertEquals(6, blockbuster.getNumberOfMovies());
    }
    
    
    @Test
    public void testAvailableMovies () throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 2);
        assertEquals(2, blockbuster.numberAvailableMovies());
        blockbuster.activityLog(RENTALS1_CSV);
        assertEquals(1, blockbuster.numberAvailableMovies());
    }
    
    
    @Test
    public void testMoviesByYear() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 10);
        String moviesYear = blockbuster.filterByYear(2009);
        StringBuilder sb = new StringBuilder();
        sb.append("Title:Angels & Demons,Price:$7.99");
        sb.append(System.lineSeparator());
        sb.append("Title:Inglorious Basterds,Price:$3.99");
        sb.append(System.lineSeparator());
        sb.append("Title:Harry Potter and the Half-Blood Prince,Price:$4.99");
        sb.append(System.lineSeparator());
        sb.append("Title:Sherlock Holmes,Price:$5.99");
        sb.append(System.lineSeparator());

        assertEquals(sb.toString(), moviesYear);
    }

    
    @Test
    public void testMoviesByPrice() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 10);
        String moviesPrice = blockbuster.filterByPrice(3);
        StringBuilder sb = new StringBuilder();
        sb.append("Title:Jurassic Park,Price:$1.99");
        sb.append(System.lineSeparator());
        sb.append("Title:E.T. the Extra-Terrestrial,Price:$0.99");
        sb.append(System.lineSeparator());

        assertEquals(sb.toString(), moviesPrice);
    }

    
    @Test
    public void testReadRENTALS1() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 2);
        String rentalsLog = blockbuster.activityLog(RENTALS1_CSV);
        StringBuilder expected = new StringBuilder();
        expected.append("Rental successful: client 5 rented Barbie for $10.99");
        expected.append(System.lineSeparator());
        expected.append("Total: $10.99 [$10.28]");
        expected.append(System.lineSeparator());
        expected.append("Movie not found: client 1 asked for Jurassic Park");
        expected.append(System.lineSeparator());

        assertEquals(expected.toString(), rentalsLog);
    }

    
    @Test
    public void testReadRENTALS2() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 10);
        String rentalsLog = blockbuster.activityLog(RENTALS2_CSV);
        StringBuilder expected = new StringBuilder();
        expected.append("Rental successful: client 5 rented Sherlock Holmes for $5.99");
        expected.append(System.lineSeparator());
        expected.append("Total: $5.99 [$5.81]");
        expected.append(System.lineSeparator());
        expected.append("Rental successful: client 13 rented Barbie for $10.99");
        expected.append(System.lineSeparator());
        expected.append("Total: $10.99 [$10.28]");
        expected.append(System.lineSeparator());
        expected.append("Movie currently not available: client 27 asked for Barbie");
        expected.append(System.lineSeparator());        
        expected.append("Movie returned: client 12 returned Harry Potter and the Half-Blood Prince");
        expected.append(System.lineSeparator());
        expected.append("Total: $0.00 [$0.00]");
        expected.append(System.lineSeparator());

        assertEquals(expected.toString(), rentalsLog);
    }

    
    @Test
    public void testReadRENTALS3() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 3);
        String rentalsLog = blockbuster.activityLog(RENTALS3_CSV);
        StringBuilder expected = new StringBuilder();
        expected.append("Movie currently not available: client 11 asked for Jurassic Park");
        expected.append(System.lineSeparator());
        expected.append("Movie returned with 1 day of delay: client 25 returned Jurassic Park");
        expected.append(System.lineSeparator());
        expected.append("Total: $2.00 [$1.92]");
        expected.append(System.lineSeparator());
        expected.append("Rental successful: client 33 rented Jurassic Park for $1.99");
        expected.append(System.lineSeparator());
        expected.append("Total: $1.99 [$1.91]");
        expected.append(System.lineSeparator());

        assertEquals(expected.toString(), rentalsLog);
    }

    
    @Test
    public void testTotalRevenue1() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 2);
        blockbuster.activityLog(RENTALS1_CSV);
        assertEquals(10.99, blockbuster.getTotalRevenue(), 0.01);
    }
    
    
    @Test
    public void testTotalProfit1() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 2);
        blockbuster.activityLog(RENTALS1_CSV);
        assertEquals(10.28, blockbuster.getTotalProfit(), 0.01);
    }
    
    
    @Test
    public void testTotalRevenue2() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 10);
        blockbuster.activityLog(RENTALS3_CSV);
        assertEquals(3.99, blockbuster.getTotalRevenue(), 0.01);
    }
    
    
    @Test
    public void testTotalProfit2() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 10);
        blockbuster.activityLog(RENTALS3_CSV);
        assertEquals(3.83, blockbuster.getTotalProfit(), 0.01);
    }

    
    @Test
    public void testUpdateStock() throws FileNotFoundException {
        VideoClub blockbuster = new VideoClub(STOCK_IN_CSV, 2);
        assertEquals(blockbuster.numberAvailableMovies(), 2);
        blockbuster.activityLog(RENTALS1_CSV);
        blockbuster.updateStock(STOCK_OUT_CSV);
        VideoClub blockbuster2 = new VideoClub(STOCK_OUT_CSV, 2);
        assertEquals(blockbuster2.numberAvailableMovies(), 1);
        
    }

}
