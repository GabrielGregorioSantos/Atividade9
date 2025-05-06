import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    private static final ArrayList<String> times = new ArrayList<>();
    private static final Map<String, Integer> pontuacao = new HashMap<>();
    private static final ArrayList<String> jogosRealizados = new ArrayList<>();
    private static final int MAX_TIMES = 10;

    private static String lerString(String mensagem) {
        String input = JOptionPane.showInputDialog(null, mensagem);
        return (input != null) ? input.trim() : "";
    }

    private static int lerInteiro(String mensagem) {
        String input = lerString(mensagem);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private static void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem);
    }

    private static void cadastrarTime() {
        if (times.size() < MAX_TIMES) {
            String nomeTime = lerString("Digite o nome do time a ser cadastrado:");
            if (!nomeTime.isEmpty() && !times.contains(nomeTime)) {
                times.add(nomeTime);
                pontuacao.put(nomeTime, 0);
                mostrarMensagem("Time " + nomeTime + " cadastrado com sucesso!");
            } else if (times.contains(nomeTime)) {
                mostrarMensagem("Este time já está cadastrado.");
            } else {
                mostrarMensagem("Nome do time não pode ser vazio.");
            }
        } else {
            mostrarMensagem("Número máximo de times (" + MAX_TIMES + ") atingido!");
        }
    }

    private static void simularJogo() {
        if (times.size() < 2) {
            mostrarMensagem("É necessário cadastrar pelo menos 2 times para simular um jogo.");
            return;
        }

        Object[] opcoesTimes = times.toArray();

        String time1Selecionado = (String) JOptionPane.showInputDialog(
                null,
                "Escolha o primeiro time:",
                "Simulação de Jogo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesTimes,
                opcoesTimes[0]
        );

        if (time1Selecionado == null) return;

        String time2Selecionado = (String) JOptionPane.showInputDialog(
                null,
                "Escolha o segundo time:",
                "Simulação de Jogo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesTimes,
                opcoesTimes[0]
        );

        if (time2Selecionado == null) return;

        if (time1Selecionado.equals(time2Selecionado)) {
            mostrarMensagem("Os times para o jogo devem ser diferentes.");
            return;
        }

        String jogoString = time1Selecionado + " x " + time2Selecionado;
        if (jogosRealizados.contains(jogoString) || jogosRealizados.contains(time2Selecionado + " x " + time1Selecionado)) {
            mostrarMensagem("Este jogo já foi realizado! Escolha outra dupla de times.");
            return;
        }

        Random random = new Random();
        int golsTime1 = random.nextInt(5);
        int golsTime2 = random.nextInt(5);

        mostrarMensagem("Resultado do jogo: " + time1Selecionado + " " + golsTime1 + " x " + golsTime2 + " " + time2Selecionado);
        jogosRealizados.add(jogoString);
        atualizarPontuacao(time1Selecionado, time2Selecionado, golsTime1, golsTime2);
        imprimirTabelaPontuacao();
        verificarCampeao();
    }

    private static void atualizarPontuacao(String time1, String time2, int gols1, int gols2) {
        pontuacao.computeIfPresent(time1, (k, v) -> v + (gols1 > gols2 ? 3 : (gols1 == gols2 ? 1 : 0)));
        pontuacao.computeIfPresent(time2, (k, v) -> v + (gols2 > gols1 ? 3 : (gols2 == gols1 ? 1 : 0)));
    }

    private static void imprimirTabelaPontuacao() {
        System.out.println("\n--- Tabela de Pontuação ---");
        System.out.println("Time\t\tPontos");
        times.stream()
                .sorted((t1, t2) -> Integer.compare(pontuacao.get(t2), pontuacao.get(t1)))
                .forEach(time -> System.out.println(time + "\t\t" + pontuacao.get(time)));
        System.out.println("---------------------------\n");
    }

    private static void verificarCampeao() {
        if (todosJogosRealizados()) {
            int maxPontos = -1;
            ArrayList<String> campeoes = new ArrayList<>();
            for (String time : times) {
                int pontosTime = pontuacao.get(time);
                if (pontosTime > maxPontos) {
                    maxPontos = pontosTime;
                    campeoes.clear();
                    campeoes.add(time);
                } else if (pontosTime == maxPontos) {
                    campeoes.add(time);
                }
            }

            if (!campeoes.isEmpty()) {
                mostrarMensagem("--- CAMPEÃO(ÕES) ---: " + String.join(", ", campeoes));
            }
        }
    }

    private static boolean todosJogosRealizados() {
        int totalPossiveisJogos = times.size() * (times.size() - 1) / 2;
        return jogosRealizados.size() == totalPossiveisJogos;
    }

    public static void main(String[] args) {
        int opcao;

        do {
            String[] menuOpcoes = {"Cadastrar Times", "Simular Jogos", "Sair"};
            opcao = JOptionPane.showOptionDialog(
                    null,
                    "Escolha uma opção:",
                    "Menu do Campeonato",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    menuOpcoes,
                    menuOpcoes[0]
            );

            switch (opcao) {
                case 0: // Cadastrar Times
                    cadastrarTime();
                    break;
                case 1: // Simular Jogos
                    simularJogo();
                    break;
                case 2: // Sair
                    mostrarMensagem("Encerrando o programa.");
                    break;
                default:
                    mostrarMensagem("Opção inválida.");
            }
        } while (opcao != 2);
    }
}