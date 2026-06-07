package observer;
 
public interface Observado {
    void add(Observador o); 
    void remove(Observador o);
    int get(int i);
}