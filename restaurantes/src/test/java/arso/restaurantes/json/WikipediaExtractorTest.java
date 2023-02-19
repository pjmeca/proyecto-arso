package arso.restaurantes.json;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

class WikipediaExtractorTest {

	private WikipediaExtractor wpExtractor = WikipediaExtractor.getInstance();

	@Test
	void test() {
		System.out.print("Por favor, escribe un código postal: ");
		Scanner sc = new Scanner(System.in);
		String codigo = sc.nextLine();
		wpExtractor.getInfo(codigo);
		sc.close();
	}
}
