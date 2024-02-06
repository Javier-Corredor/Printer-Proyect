package printerproyect.logic;

import java.util.LinkedList;
import java.util.List;

public class Printer {
    public static final int BAndWPrintTime = 1;
    public static final int colorPrintTime = 2;
    public static final int maxFiles = 15;
    public static final List<String> extensionDocs = List.of(".doc");
    public static final List<String> extensionImg = List.of();
    private int leavesRemainingOffice = 50;
    private int leavesRemainingLetter = 50;
    private LinkedList <Impression> remainig = new LinkedList<>();

    public int getLeavesRemainingOffice() {
        return leavesRemainingOffice;
    }

    public int getLeavesRemainingLetter() {
        return leavesRemainingLetter;
    }

    public void addImpression(Impression impression){
        if (impression.isPriority()) remainig.offerFirst(impression);
        else remainig.offer(impression);
        if (impression.isSizeLetter()){
            leavesRemainingLetter -= impression.getPageToPrint();
        } else {
            leavesRemainingOffice -= impression.getPageToPrint();
        }
    }

    public void updateLeavesRemainingOffice(int delta){
        leavesRemainingOffice += delta;
    }

    public void updateLeavesRemainingLetter(int delta){
        leavesRemainingLetter += delta;
    }

    public boolean isEmpty(){
        return remainig.isEmpty();
    }

    public Impression poll(){
        return remainig.poll();
    }

    public int getReamaining(){
        return remainig.size();
    }
}
