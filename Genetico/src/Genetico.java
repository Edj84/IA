/**
 * Programa iniciado em um Dojo pelos alunos em 21/03/2019
 * Foi concluido e comentado pela prof. Silvia em 27/03/2019 
 * Bugs corrigidos tambem pela prof.
 * 
 * O problema consiste em distribuir de forma harmonica as cargas de tarefas (vetor cargas)
 * entre duas pessoas.
 * Segue o codigo. Bom estudo a todos!!!
 */
import java.util.Random;
public class Genetico {
    public final static int SIZE = 36; //total de cargas
    public final static int TAM = 11;   //tamanho da populaçao: quantidade de soluçoes
    public final static int MAX = 50;  //numero maximo de geraçoes (iteraçoes)
    
    public static void main(String[] args) {
        Random r = new Random();
        int[] cargas = {10,5,15,2,3,1,6,8,20,5,8,33,14,27,9,6,13,5,9,18,15,21,3,4,18,2,16,9,3,38,2,6,7,12,3,5}; //cargas definidas em aula 
        int[][] populacao = new int [TAM][SIZE+1];                       //populaçao atual: contem os cromossomos (soluçoes candidatas)
        int[][] populacaoIntermediaria = new int [TAM][SIZE+1];          //populaçao intermediaria: corresponde a populaçao em construçao 
                                                                         //Obs: A ultima coluna de cada linha da matriz e para armazenar o valor da funçao de aptidao,
                                                                         //     que indica o quao boa eh a soluçao
                                                                         
        //===========> Ciclo do AG         
        System.out.println("=================================================================");
        System.out.println("Encontrando a melhor distribuiçao usando Algoritmos Geneticos ...");
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
        /*    if(convergencia(populacao,melhor)){
                 printaMatriz(populacao);
                 System.out.println(">>>> Convergiu: ");
                 break;
            }*/
            printaMatriz(populacao);
            //printaMatriz(populacaoIntermediaria);
            crossOver(populacaoIntermediaria, populacao);
            if(geracao%2==0) mutacao(populacaoIntermediaria);
           // printaMatriz(populacaoIntermediaria);
            populacao = populacaoIntermediaria;
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
                soma =0;
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
    
    /** 
     * Seleçao por torneio. Escolhe cromossomo (soluçao) para cruzamento
     */
    private static int[] torneio(int[][] populacao) {
        Random r = new Random();
        int l1 = r.nextInt(populacao.length);
        int l2 = r.nextInt(populacao.length);
        
        if(populacao[l1][SIZE] < populacao[l2][SIZE]) {
            System.out.println("Cromossomo selecionado para cruzamento: " + l1);
            return populacao[l1];
        }else {
            System.out.println("Cromossomo selecionado para cruzamento: " + l2);
            return populacao[l2];
        }
    }
    
    /**
     * Cruzamento uniponto: gera dois filhos e coloca na populaçao intermediaria
     */
    private static void crossOver(int[][] intermediaria, int[][] populacao) {
        int[] pai;
        int[] pai2;
        int corte = 10;
        
        for(int i=1; i<TAM; i=i+2){
            do{
                pai = torneio(populacao);
                pai2 = torneio(populacao);
            }while(pai==pai2);
            System.out.println("Gerando dois filhos...");
            for(int j=0;j<corte; j++){
                intermediaria[i][j]=pai[j];
                intermediaria[i+1][j]=pai2[j];
            }
            for(int j=corte;j<SIZE; j++){
                intermediaria[i][j]=pai2[j];
                intermediaria[i+1][j]=pai[j];
            }
        }
    }

    
    /**
     * Mutaçao
     */
    private static void mutacao(int[][] intermediaria){
        Random r = new Random();
        //System.out.println("Tentando mutacao");
       
            for(int cont = 1; cont<=2; cont++){
                int linha = r.nextInt(TAM);
                int coluna = r.nextInt(SIZE);
                if(intermediaria[linha][coluna]==0) intermediaria[linha][coluna] = 1;
                else intermediaria[linha][coluna] = 0;
            
                System.out.println("Mutou o cromossomo : " + linha);
            }
        
    }
    
    /**
     * Teste de convergencia
     */
    private static boolean convergencia(int[][] populacao, int melhor){
        int cont = 0;
        for(int i=0; i<TAM; i++) if(populacao[i][SIZE] == populacao[melhor][SIZE]) cont++;
        double perc = cont*100.0/TAM;
        if(perc>98) return true;
        return false;
    }
}