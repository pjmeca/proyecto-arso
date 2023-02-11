package arso.restaurantes.dom;

import java.util.Scanner;
import org.junit.jupiter.api.Test;

class BuscadorCPTest {

	private BuscadorCP buscador = BuscadorCP.getInstance();
	
	@Test
	void test() {
		System.out.print("Por favor, escribe un código postal: ");
		Scanner sc = new Scanner(System.in);
        String codigo = sc.nextLine();
        buscador.findByCP(codigo);
        sc.close();
	}

}
