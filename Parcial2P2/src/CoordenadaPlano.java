public class CoordenadaPlano {
    
    private Integer x;
    private Integer y;
    private Double distancia;

    public CoordenadaPlano(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Double getDistancia() {
        return distancia;
    }

    //d = √((x2 - x1)² + (y2 - y1)²)
    public Double calcularDistanciaEntrePuntos(int x, int y) {

        double powX = Math.pow((x - this.x), 2);
        double powY = Math.pow((y - this.y), 2);
        this.distancia = Math.sqrt(powX + powY);

        return this.distancia;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
