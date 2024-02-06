package printerproyect.model;

public class Document {
    private int pageNumber;
    private String name;

    public Document(int pageNumber, String name) {
        this.pageNumber = pageNumber;
        this.name = name;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getName(){
        return name;
    }
}
