import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Classe para controlar o Campeonato
class Campeonato {
    private ArrayList<Time> times;
    private ArrayList<String> partidasRealizadas;

    public Campeonato() {
        this.times = new ArrayList<>();
        this.partidasRealizadas = new ArrayList<>();
    }

    public void cadastrarTime() {
        if (times.size() < 10) {
            String nomeTime = ManipuladorIO.lerString("Digite o nome do time a ser cadastrado:");
            if (!nomeTime.isEmpty()) {
                times.add(new Time(nomeTime));
                ManipuladorIO.mostrarMensagem("Time " + nomeTime + " cadastrado com sucesso!");
            } else {
                ManipuladorIO.mostrarMensagem("Nome do time não pode ser vazio.");
            }
        } else {
            ManipuladorIO.mostrarMensagem("Número máximo de times (10) atingido!");
        }
    }

    public void simularPartida() {
        if (times.size() < 2) {
            ManipuladorIO.mostrarMensagem("É necessário cadastrar pelo menos 2 times para simular uma partida.");
            return;
        }

        Object[] opcoesTimes = times.stream().map(Time::getNome).toArray();

        String time1Selecionado = (String) JOptionPane.showInputDialog(
                null,
                "Escolha o primeiro time:",
                "Simulação de Partida",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesTimes,
                opcoesTimes[0]
        );

        if (time1Selecionado == null) return;

        String time2Selecionado = (String) JOptionPane.showInputDialog(
                null,
                "Escolha o segundo time:",
                "Simulação de Partida",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesTimes,
                opcoesTimes[0]
        );

        if (time2Selecionado == null) return;

        if (time1Selecionado.equals(time2Selecionado)) {
            ManipuladorIO.mostrarMensagem("Os times para a partida devem ser diferentes.");
            return;
        }

        Time time1Obj = null;
        Time time2Obj = null;
        for (Time time : times) {
            if (time.getNome().equals(time1Selecionado)) {
                time1Obj = time;
            }
            if (time.getNome().equals(time2Selecionado)) {
                time2Obj = time;
            }
        }

        if (time1Obj != null && time2Obj != null) {
            String partidaString = time1Obj.getNome() + " x " + time2Obj.getNome();
            if (partidasRealizadas.contains(partidaString) || partidasRealizadas.contains(time2Obj.getNome() + " x " + time1Obj.getNome())) {
                ManipuladorIO.mostrarMensagem("Esta partida já foi realizada! Escolha outra dupla de times.");
                return;
            }

            Partida partida = new Partida(time1Obj, time2Obj);
            String resultado = partida.simular();
            ManipuladorIO.mostrarMensagem("Resultado da partida: " + resultado);
            partidasRealizadas.add(partidaString);
            imprimirTabelaPontuacao();
            verificarCampeao();
        } else {
            ManipuladorIO.mostrarMensagem("Erro ao encontrar os times selecionados.");
        }
    }

    public void imprimirTabelaPontuacao() {
        System.out.println("\n--- Tabela de Pontuação ---");
        System.out.println("Time\t\tPontos");
        List<Time> sortedTimes = new ArrayList<>(times);
        sortedTimes.sort((t1, t2) -> Integer.compare(t2.getPontos(), t1.getPontos()));
        for (Time time : sortedTimes) {
            System.out.println(time.getNome() + "\t\t" + time.getPontos());
        }
        System.out.println("---------------------------\n");
    }

    public void verificarCampeao() {
        if (todasPartidasRealizadas()) {
            int maxPontos = -1;
            ArrayList<String> campeoes = new ArrayList<>();
            for (Time time : times) {
                if (time.getPontos() > maxPontos) {
                    maxPontos = time.getPontos();
                    campeoes.clear();
                    campeoes.add(time.getNome());
                } else if (time.getPontos() == maxPontos) {
                    campeoes.add(time.getNome());
                }
            }

            if (!campeoes.isEmpty()) {
                ManipuladorIO.mostrarMensagem("--- CAMPEÃO(ÕES) ---: " + String.join(", ", campeoes));
            }
        }
    }

    private boolean todasPartidasRealizadas() {
        int totalPossiveisPartidas = times.size() * (times.size() - 1) / 2;
        return partidasRealizadas.size() == totalPossiveisPartidas;
    }
}