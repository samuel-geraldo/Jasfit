package model;

public class KelasGym {
    private int id;
    private String namaKelas;
    private String instruktur;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private int kuota;

    public KelasGym(int id, String namaKelas, String instruktur,
                    String hari, String jamMulai, String jamSelesai, int kuota) {
        this.id = id;
        this.namaKelas = namaKelas;
        this.instruktur = instruktur;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.kuota = kuota;
    }

    // Getters
    public int getId() { return id; }
    public String getNamaKelas() { return namaKelas; }
    public String getInstruktur() { return instruktur; }
    public String getHari() { return hari; }
    public String getJamMulai() { return jamMulai; }
    public String getJamSelesai() { return jamSelesai; }
    public int getKuota() { return kuota; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }
    public void setInstruktur(String instruktur) { this.instruktur = instruktur; }
    public void setHari(String hari) { this.hari = hari; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }
    public void setKuota(int kuota) { this.kuota = kuota; }

    @Override
    public String toString() {
        return namaKelas + " | " + hari + " " + jamMulai + "-" + jamSelesai +
                " | Instruktur: " + instruktur + " | Kuota: " + kuota;
    }
}