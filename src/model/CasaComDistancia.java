package model;

 class CasaComDistancia {
    private Casa casa;
    private int distancia;

    public CasaComDistancia(Casa casa, int distancia) {
        this.casa = casa;
        this.distancia = distancia;
    }

    public Casa getCasa() {
        return casa;
    }

    public int getDistancia() {
        return distancia;
    }
}
