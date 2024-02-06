package printerproyect.logic;

import printerproyect.model.Document;

public class Impression {
    private Document dc;
    private int pageToPrint;
    private boolean color;
    private boolean simplePage;
    private boolean sizeLetter;
    private boolean priority;

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public Document getDc() {
        return dc;
    }

    public void setDc(Document dc) {
        this.dc = dc;
    }

    public int getPageToPrint() {
        return pageToPrint;
    }

    public void setPageToPrint(int pageToPrint) {
        this.pageToPrint = pageToPrint;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isSimplePage() {
        return simplePage;
    }

    public void setSimplePage(boolean simplePage) {
        this.simplePage = simplePage;
    }

    public boolean isSizeLetter() {
        return sizeLetter;
    }

    public void setSizeLetter(boolean sizeLetter) {
        this.sizeLetter = sizeLetter;
    }
}
