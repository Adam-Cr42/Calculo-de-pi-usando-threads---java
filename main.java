class Main {
  public static void main(String[] args) throws InterruptedException {
    for (int A = 1; A <= 32; A *= 2) {                  // Criando o laço para as threads 1, 2, 4, 8, 16  32;
      System.out.println("\n\n============================================");
      System.out.printf("\nCalculo da %d° thread\n\n", A);
      int qtd_Threads = A;                              // quantidade de threads
      int memoria = A;                                  // quantidade de memoria compartilhada
      long[] duracao = new long[5];                     // vetor que armazenar calculo a cada 5 execucoes
      long soma_threads = 0;                            // calcular o numero de somas por cada thread
      double numero_threads = 1000000/qtd_Threads; 

      for (int B = 0; B < 5; B++) {                     // repetindo o calculo 5 vezes pra cada thread;
        long tempo = System.nanoTime();                 // variavel tempo para calcular o inicio da contagem;
        double[] memory = new double[memoria];          // Criando a memoria compartilhada para as threads;
        for (int i = 0; i < memoria; i++) {             // Gardando a memoria;
          memory[i] = i;
        }

        Thread[] threads = new Thread[qtd_Threads];     // Criando a thread;
          for (int i = 0; i < qtd_Threads; i++) {
            int intervalo = (int)numero_threads * i;
            int fim_inter = intervalo + (int)numero_threads;
            threads[i] = new Threads_criadas(i, memory, intervalo, fim_inter);
            threads[i].start();                         // Dando inicio a thread criada;
          }
        waitThreads(threads);                           // Informa para o main esperar fim das threads;

        double pi = 0;                                  // Informando que o valor inicial da variavel pi é zero;
        for (double memoria_compartilhada : memory) {   // Somando resultados da memoria compartilhada;
          pi += memoria_compartilhada;
        }
        pi *= 4;
        System.out.printf("  O Valor de pi: %2f\n", pi); // Exibindo o valor de pi

        long tempo_final = System.nanoTime();            // Calculando o fim do calculo
        duracao[B] = tempo_final - tempo;                // Calculando contagem do tempo
        soma_threads += duracao[B];                      // Adicionando resultado na média
        System.out.printf("  Duração do %d° calculo: %d ms\n\n", B + 1, duracao[B]/1000000); // Exibindo duração de cada calculo;
     }

      double media = soma_threads / 5;                   // Calculando média dos calculos de pi
      System.out.println("+--------Estatística sobre a thread---------+");
      System.out.printf("\n  Duração média do calculo da %d° thread:\n    %.2fms\n", A, media/1000000); // exibindo media do calculo;

      double[] calculo_desvio = new double[5];           // Calculando parcelas do desvio padrão;
      for (int j = 0; j < 5; j++) {                      // Informando que são a cada 5 rodadas;
        calculo_desvio[j] = duracao[j] - media;          // Matemática do calculo das parcelas;
        Math.pow(calculo_desvio[j], 2);
      }

      double desvio_padrao = 0;                          // O desvio padrão inicial é zero;
      for (double parcela_desvio : calculo_desvio) {
        desvio_padrao += parcela_desvio;                 // Somando todas as parcelas do devio padrão;
      }

      desvio_padrao = Math.sqrt(desvio_padrao / 5);      // Calculando desvio padrão da thread;
      System.out.printf("\n  Desvio Padrão do calculo da %d° thread:\n    %.2fns\n\n", A, desvio_padrao); // exibindo desvio
      System.out.println("============================================");
        }
    }

    // Funcao para fazer o main principal esperar por cada thread
    public static void waitThreads(Thread[] threads) throws InterruptedException {
      for (Thread t : threads) {
        t.join();
      }
    }
    // Criando uma classe para estender thread original
    public static class Threads_criadas extends Thread {
      int thread_principal;
      double[] memory;
      int intervalo, fim_intervalo;

      // Lendo informacoes gerais sobre a thread atual
      public Threads_criadas(int thread_principal, double[] memory, int intervalo, int fim_intervalo){
        this.thread_principal = thread_principal;
        this.memory = memory;
        this.intervalo = intervalo;
        this.fim_intervalo = fim_intervalo;
      }

        @Override                                        // Sobrescrevendo run da thread original;
        public void run() {                              // Função run();
          double calculo_parcial = 0.0;                  // Calculo parcial inicial é zero;
          for (int i = intervalo; i < fim_intervalo; i++) {
            double calculo = Math.pow(-1, (double) i) / (2 * i + 1);
            calculo_parcial += calculo;                  // Calculando o calculo parcial
          }
          memory[thread_principal] = calculo_parcial;    // Adicionando calculo parcial de pi na memoria compartilhada;
       }
    }
}
