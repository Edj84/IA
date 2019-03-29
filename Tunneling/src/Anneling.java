/**
 * Programa iniciado em um Dojo pelos alunos em 21/03/2019
 * Foi concluido e comentado pela prof. Silvia em 27/03/2019 
 * Bugs corrigidos tambem pela prof.
 * Adaptado para o algoritmo de Tempered Anneling por Eduardo José Silva e Israel Deorce Jr. em 28/3/2019
 * O problema consiste em distribuir de forma harmonica as cargas de tarefas (vetor cargas)
 * entre duas pessoas.
 * Segue o codigo. Bom estudo a todos!!!
 */
import java.util.Random;

import com.sun.javafx.css.CalculatedValue;
public class Anneling {
    public final static int SIZE = 21; //total de cargas
    public static int temperatura = 500; //numero maximo de iteraçoes
    public static Random r = new Random();
    public static int[] aptidoes = new int[temperatura];
    public static int melhor, pior; 
    
    public static void main(String[] args) {
        int[] cargas = {10,52,50,20,5,15,11,3,4,18,28,16,9,31,3,8,12,7,11,3,5};  
        int[] estadoAtual = iniciar(SIZE);
    	funcaoDeAptidao(estadoAtual, cargas);
        int aptidaoAtual = estadoAtual[SIZE-1];
        melhor = aptidaoAtual;
        pior = aptidaoAtual;
        int[] estadoSeguinte; 
        
        //Obs: A ultima posição do vetor de estado armazena o valor da funçao de aptidao, que indica o quao boa eh a soluçao
                                                                         
        //===========> Ciclo do AG         
        System.out.println("=================================================================");
        System.out.println("Encontrando a melhor distribuiçao usando Algoritmo de Tempered Anneling");
        System.out.println("=================================================================");
        
        while(estadoAtual[SIZE-1] != 0) {
        	
        	estadoSeguinte = randomizar(estadoAtual);
        	funcaoDeAptidao(estadoAtual, cargas);
        	funcaoDeAptidao(estadoSeguinte, cargas);
        	
        	//Armazena a aptidão do estado atual em um vetor para conseguir exibir posteriormente de que forma ela evoluiu
        	aptidoes[500-temperatura] = aptidaoAtual;
        	
        	System.out.println("Estado atual " +  exibeStatus(estadoAtual));
        	System.out.println("Aptidão " + aptidaoAtual);
        	System.out.println("Temperatura " + temperatura);
        	
        	boolean mudouEstado = false;
        	
        	if(estadoAtual[SIZE-1] > estadoSeguinte[SIZE-1]) { 
        		estadoAtual = estadoSeguinte;
        		mudouEstado = true;
        	}
        		
        	else if (r.nextInt(2) < Math.exp(-1/temperatura)) {
        		estadoAtual = estadoSeguinte;
        		mudouEstado = true;
        	}
        	
        	if(mudouEstado) {
        		aptidaoAtual = estadoAtual[SIZE-1];
        		
        		if(melhor > aptidaoAtual)
        			melhor = aptidaoAtual;
        		if(pior < aptidaoAtual)
        			pior = aptidaoAtual;
        		
        		System.out.println("Estado mudou para " +  exibeStatus(estadoAtual));
            	System.out.println("Aptidão " + aptidaoAtual);
        	}
        	
        	if(temperatura == 0)
        		break;
        	else temperatura--;
        }
        
        System.out.println("=================================================================");
        System.out.println("Resultado: " + exibeStatus(estadoAtual) +" - Aptidão " + estadoAtual[SIZE-1]);
        System.out.println("Temperatura final: " + temperatura);
        System.out.println("Aptidão inicial: " + aptidoes[0]);
        System.out.println("Melhor aptidão ao longo da execução: " + melhor);
        System.out.println("Pior aptidão ao longo da execução: " + pior);
        System.out.println("=================================================================");     
    
    }            
    
    private static int[] iniciar(int tamanho) {
    	
    	int[] aux = new int[tamanho];
        	
        for(int i = 0; i < tamanho-1; i++) {
        	int pessoa = r.nextInt(2);
        	aux[i] = pessoa;
        	System.out.println("Carga " + (i+1) + " distribuída para pessoa nº " + (pessoa+1));
        }
        
        pior = aux[SIZE-1];
        pior = aux[SIZE-1];
        	
        return aux;
	}
    
    private static int[] randomizar(int[] estadoAtual) {
		
    	int[] aux = estadoAtual.clone();
    	int pos = r.nextInt(SIZE-1); 
    	
    	if(aux[pos] == 0) 
    		aux[pos] = 1;
    	else aux[pos] = 0;   	
    	
    	return aux;
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
    
    private static String exibeStatus(int[] estado) {
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for(int i = 0; i < estado.length-1; i++) {
    		sb.append(estado[i] + " ");
    	}
    	
    	return sb.toString();
    }
     
}