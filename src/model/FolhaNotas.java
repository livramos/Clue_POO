package model;
import java.util.ArrayList;
import java.util.List;

public class FolhaNotas {
	private List<String> suspeitos;
    private List<String> armas;
    private List<String> comodos;
    private List<Boolean> suspeitosMarcados;
    private List<Boolean> armasMarcadas;
    private List<Boolean> comodosMarcados;
	    
	public FolhaNotas() {
		suspeitos = new ArrayList<>();
        suspeitos.add("Coronel Mustard");
        suspeitos.add("Professor Plum");
        suspeitos.add("Sr. Green");
        suspeitos.add("Sra. Peacock");
        suspeitos.add("Senhorita Scarlet");
        suspeitos.add("Sra. White");
        
        armas = new ArrayList<>();
        armas.add("Faca");
        armas.add("Candelabro");
        armas.add("Revólver");
        armas.add("Corda");
        armas.add("Cano de Chumbo");
        armas.add("Chave Inglesa");
        
        comodos = new ArrayList<>();
        comodos.add("Corredor");
        comodos.add("Sala de Estar");
        comodos.add("Sala de Jantar");
        comodos.add("Cozinha");
        comodos.add("Salão de Baile");
        comodos.add("Conservatório");
        comodos.add("Sala de Bilhar");
        comodos.add("Biblioteca");
        comodos.add("Escritório");
        
        suspeitosMarcados = new ArrayList<>(List.of(false, false, false, false, false, false));
        armasMarcadas = new ArrayList<>(List.of(false, false, false, false, false, false));
        comodosMarcados = new ArrayList<>(List.of(false, false, false, false, false, false, false, false, false));
	}
	    
	public List<String> getSuspeitos() { 
		return suspeitos; 
		}
    public List<String> getArmas() { 
    	return armas; 
    	}
    public List<String> getComodos() { 
    	return comodos; 
    	}
    public List<Boolean> getSuspeitosMarcados() { 
    	return suspeitosMarcados; 
    	}
    public List<Boolean> getArmasMarcadas() { 
    	return armasMarcadas; 
    	}
    public List<Boolean> getComodosMarcados() { 
    	return comodosMarcados; 
    	}
}