public class DerivadaInteligente {

    public static void main(String[] args) {
        String funcao = "2x^3 + 5x/2 - 7 + 3x*2x"; // Exemplo de entrada
        System.out.println("Função original: " + funcao);
        System.out.println("Derivada: " + derivar(funcao));
    }

    public static String derivar(String funcao) {
        // Remove espaços e separa os termos por "+" ou "-"
        String[] termos = funcao.replace(" ", "").split("(?=[+-])");

        StringBuilder resultado = new StringBuilder();

        for (String termo : termos) {
            String derivada = identificarEAplicarRegra(termo);
            if (!derivada.isEmpty()) {
                resultado.append(derivada);
            }
        }

        return resultado.toString();
    }

    private static String identificarEAplicarRegra(String termo) {
        // Caso seja um número constante
        if (!termo.contains("x")) {
            return ""; // Derivada de uma constante é 0 (não incluímos no resultado)
        }

        // Caso seja uma potência, exemplo: 2x^3
        if (termo.contains("x^")) {
            return derivarPotencia(termo);
        }

        // Caso seja um termo linear, exemplo: 5x
        if (termo.contains("x") && !termo.contains("/") && !termo.contains("*")) {
            return derivarLinear(termo);
        }

        // Caso seja um produto, exemplo: 3x*2x
        if (termo.contains("*")) {
            return derivarProduto(termo);
        }

        // Caso seja uma divisão, exemplo: 2x/3
        if (termo.contains("/")) {
            return derivarQuociente(termo);
        }

        return ""; // Caso não reconheça o termo
    }

    private static String derivarPotencia(String termo) {
        // Exemplo: 2x^3 -> 6x^2
        String[] partes = termo.split("x\\^");
        double coeficiente = partes[0].isEmpty() ? 1 : Double.parseDouble(partes[0]);
        double expoente = Double.parseDouble(partes[1]);

        double novoCoeficiente = coeficiente * expoente;
        double novoExpoente = expoente - 1;

        if (novoExpoente == 0) {
            return (novoCoeficiente > 0 ? "+" : "") + novoCoeficiente;
        } else if (novoExpoente == 1) {
            return (novoCoeficiente > 0 ? "+" : "") + novoCoeficiente + "x";
        } else {
            return (novoCoeficiente > 0 ? "+" : "") + novoCoeficiente + "x^" + novoExpoente;
        }
    }

    private static String derivarLinear(String termo) {
        // Exemplo: 5x -> 5
        String coeficiente = termo.replace("x", "");
        double valor = coeficiente.isEmpty() ? 1 : Double.parseDouble(coeficiente);

        return (valor > 0 ? "+" : "") + valor;
    }

    private static String derivarQuociente(String termo) {
        // Exemplo: (2x/3) -> f'(x)g(x) - f(x)g'(x) / [g(x)]^2
        String[] partes = termo.split("/");
        String numerador = partes[0];
        String denominador = partes[1];

        String derivadaNumerador = identificarEAplicarRegra(numerador);
        String derivadaDenominador = identificarEAplicarRegra(denominador);

        return String.format(
                "(%s*%s - %s*%s)/(%s^2)",
                derivadaNumerador, denominador, numerador, derivadaDenominador, denominador
        );
    }

    private static String derivarProduto(String termo) {
        // Exemplo: (3x*2x) -> f'(x)g(x) + f(x)g'(x)
        String[] fatores = termo.split("\\*");
        String fator1 = fatores[0];
        String fator2 = fatores[1];

        String derivadaFator1 = identificarEAplicarRegra(fator1);
        String derivadaFator2 = identificarEAplicarRegra(fator2);

        return String.format(
                "(%s*%s + %s*%s)",
                derivadaFator1, fator2, fator1, derivadaFator2
        );
    }

    // L'Hôpital: Verificação básica (opcional)
    public static String aplicarLhopital(String numerador, String denominador) {
        if (numerador.equals("0") && denominador.equals("0")) {
            return derivarQuociente(numerador + "/" + denominador);
        }
        return "L'Hôpital não aplicável.";
    }
}
