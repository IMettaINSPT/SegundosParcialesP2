
public class Codigo {

    private String caracter;
    private String traduccion;

    public Codigo(String character, String traduction) {
        this.caracter = character;
        this.traduccion = traduction;        
    }

    public String getCaracter() {
        return caracter;
    }

    public String getTraduccion() {
        return traduccion;
    }
    
    public boolean equals(String palabra){
     return this.caracter.equals(palabra);
    }
}
