package model;

public class Paket {
    private int id;
    private String namaPaket;
    private String jenis;
    private double harga;
    private int durasiBulan;
    private String fasilitas;

    public Paket(int id, String namaPaket, String jenis,
                 double harga, int durasiBulan, String fasilitas) {
        this.id = id;
        this.namaPaket = namaPaket;
        this.jenis = jenis;
        this.harga = harga;
        this.durasiBulan = durasiBulan;
        this.fasilitas = fasilitas;
    }

    // Getters
    public int getId() { return id; }
    public String getNamaPaket() { return namaPaket; }
    public String getJenis() { return jenis; }
    public double getHarga() { return harga; }
    public int getDurasiBulan() { return durasiBulan; }
    public String getFasilitas() { return fasilitas; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNamaPaket(String namaPaket) { this.namaPaket = namaPaket; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setDurasiBulan(int durasiBulan) { this.durasiBulan = durasiBulan; }
    public void setFasilitas(String fasilitas) { this.fasilitas = fasilitas; }

    @Override
    public String toString() {
        return namaPaket + " - Rp " + harga + " (" + durasiBulan + " bulan)";
    }
}