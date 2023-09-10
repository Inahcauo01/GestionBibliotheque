public class Livre {
    private int id;
    private String isbn;
    private String title;
    private String auteur;
    private int quantite;
    private int emprunte;

    public Livre(String isbn, String title, String auteur, int quantite, int emprunte) {
        this.isbn = isbn;
        this.title = title;
        this.auteur = auteur;
        this.quantite = quantite;
        this.emprunte = emprunte;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getEmprunte() { return emprunte; }

}
