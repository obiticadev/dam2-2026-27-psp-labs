/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aleja
 */
public class Huevo {
    double peso;
    String nombreGallina;

    public Huevo(double peso, String nombreGallina) {
        this.peso = peso;
        this.nombreGallina = nombreGallina;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getNombreGallina() {
        return nombreGallina;
    }

    public void setNombreGallina(String nombreGallina) {
        this.nombreGallina = nombreGallina;
    }

    @Override
    public String toString() {
        return "Huevo{" + "peso=" + peso + ", nombreGallina=" + nombreGallina + '}';
    }

}
