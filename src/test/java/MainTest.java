import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mypersonalprojects.Main;

public class MainTest {
    
    @DisplayName("Soma dois numeros com sucesso")
    @Test
    public void DeveApresentarASomaDeDoisNumeros() {
        var result = Main.sum(1, 2);

        Assertions.assertEquals(result, 3);
    }
}
