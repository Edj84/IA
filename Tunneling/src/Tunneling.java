/**
 * Programa iniciado em um Dojo pelos alunos em 21/03/2019
 * Foi concluido e comentado pela prof. Silvia em 27/03/2019 
 * Bugs corrigidos tambem pela prof.
 * Adaptado para o algoritmo de TemperedeTunneling por Eduardo J. Silva em 28/3/2019
 * O problema consiste em distribuir de forma harmonica as cargas de tarefas (vetor cargas)
 * entre duas pessoas.
 * Segue o codigo. Bom estudo a todos!!!
 */
import java.util.Random;
public class Tunneling {
    public final static int SIZE = 21; //total de cargas
    public final static int TAM = 11;   //tamanho da populaçao: quantidade de soluçoes
    public final static int MAX = 50;  //numero maximo de geraçoes (iteraçoes)
    
    public static void main(String[] args) {
        Random r = new Random();
        int[] cargas = {10,52,50,20,5,15,11,3,4,18,28,16,9,31,3,8,12,7,11,3,5}; //cargas definidas em aula 
        int[][] populacao = new int [TAM][SIZE+1];                       //populaçao atual: contem os cromossomos (soluçoes candidatas)
        int[][] populacaoIntermediaria = new int [TAM][SIZE+1];          //populaçao intermediaria: corresponde a populaçao em construçao 
                                                                         //Obs: A ultima coluna de cada linha da matriz e para armazenar o valor da funçao de aptidao,
                                                                         //     que indica o quao boa eh a soluçao
                                                                         
        //===========> Ciclo do AG         
        System.out.println("=================================================================");
        System.out.println("Encontrando a melhor distribuiçao usando Algoritmo de Tempered Tunneling");
        System.out.println("=================================================================");
        inicializaPopulacao(r, populacao);   //cria soluçoes aleatoriamente
        
        for(int geracao = 0; geracao<MAX; geracao++) {
            System.out.println("Geraçao:" + geracao);
            calculaFuncoesDeAptidao(populacao, cargas); 
            int melhor = pegaAltaTerra(populacao, populacaoIntermediaria); //highlander, Vulgo elitismo
            if(populacaoIntermediaria[0][SIZE] ==0) {
                printaMatriz(populacao);
                System.out.println(">>>> Achou a melhor distribuiçao: ");
                solucaoOtima(populacaoIntermediaria,cargas);
                break;
            }
        
            printaMatriz(populacao);
        }
    }

    /**
     * Decodifica melhor soluçao
     */
    private static void solucaoOtima(int[][] populacaoIntermediaria, int []cargas){
                System.out.println("Pessoa 0: ");
                int soma = 0;
                for(int j=0; j<SIZE; j++){
                    if(populacaoIntermediaria[0][j]==0) {
                        System.out.print(cargas[j]+ " ");
                        soma = soma + cargas[j];
                    }
                }
                System.out.println(" - Total: " + soma);
                System.out.println("Pessoa 1: ");
                soma = 0;
                for(int j=0; j<SIZE; j++){
                    if(populacaoIntermediaria[0][j]==1) {
                        System.out.print(cargas[j] + " ");
                        soma = soma + cargas[j];
                    }
                }
                System.out.println(" - Total: " + soma);
    }
    /**
     * Printa populaçao na tela
     */
    private static void printaMatriz(int[][] populacao) {
        System.out.println("__________________________________________________________________");
        for(int i = 0; i < populacao.length; i++) {
            System.out.print("(" + i + ") ");
            for(int j = 0; j < populacao[i].length-1; j++) {
                System.out.print(populacao[i][j] + " ");
            }
            System.out.println(" Aptidao: " + populacao[i][populacao[i].length-1]);
        }
        System.out.println("__________________________________________________________________");
    }

    /**
     * Gera populaçao inicial: conjunto de soluçoes candidatas
     */
    private static void inicializaPopulacao(Random r, int[][] populacao) {
        for(int i = 0; i < populacao.length; i++) {
            for(int j = 0; j < populacao[i].length -1; j++) {
                populacao[i][j] = r.nextInt(2);
            }
        }
    }
    
    /**
     * Calcula a funçao de aptidao para a populaçao atual
     */
    private static void calculaFuncoesDeAptidao(int[][] populacao, int[] cargas){
        for(int i = 0; i < populacao.length; i++) {
            funcaoDeAptidao(populacao[i], cargas);
        }
    }
    
    /**
     * Funçao de aptidao: heuristica que estima a qualidade de uma soluçao
     */
    private static void funcaoDeAptidao(int[] linha, int[] cargas) {
        int somaZero = 0;
        int somaUm = 0;
        int i = 0;
        for(; i<linha.length-1; i++) {
            if(linha[i]==0) {
                somaZero+=cargas[i];
            } else {
                somaUm+=cargas[i];
            }
        }
        linha[i] = Math.abs(somaZero - somaUm);     
    }
    
    /** 
     * Seleçao por elitismo. Encontra a melhor soluçao e copia para a populaçao intermediaria
     */
    private static int pegaAltaTerra(int[][] populacao, int[][] populacaoIntermediaria) {
        int highlander = 0;     
        int menor = populacao[0][SIZE];
        
        for(int i=1; i<populacao.length; i++) {
            if(populacao[i][SIZE] < menor) {
                menor = populacao[i][SIZE];
                highlander = i;
            }
        }
        System.out.println("Seleçao por elitismo - melhor dessa geraçao: " + highlander);
        
        for(int i=0; i<SIZE+1; i++) {
            populacaoIntermediaria[0][i] = populacao[highlander][i];
        }
        return highlander;
    }    
    
}