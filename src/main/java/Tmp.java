import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Tmp {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("experiments/raw_data/inp.txt"));
		PrintWriter pw = new PrintWriter("experiments/raw_data/data3.txt");

		String x1 = br.readLine();
		String x2 = br.readLine();
		String x3 = br.readLine();
		String x4 = br.readLine();

		String x;

		while ((x = br.readLine()) != null) {
			pw.println(String.format("f(%s, %s,%s, %s) = %s", x1, x2, x3, x4, x));
			x1 = x2;
			x2 = x3;
			x3 = x4;
			x4 = x;
		}

		pw.flush();
		pw.close();
		br.close();
	}

}
