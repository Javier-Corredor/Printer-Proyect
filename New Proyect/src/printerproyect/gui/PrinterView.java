package printerproyect.gui;

import printerproyect.logic.Impression;
import printerproyect.logic.Printer;
import printerproyect.model.Document;

import javax.swing.*;

public class PrinterView {
    private static final Printer PRINTER = new Printer();

    static {
        new Thread(() -> {
            while (true){
                System.out.println("PRINTER = " + PRINTER.getReamaining());
                if (PRINTER.isEmpty()){
                    PrinterView.sleepThread(1);
                } else {
                    Impression impression = PRINTER.poll();
                    Document document = impression.getDc();
                    int timePerPage = impression.isColor() ? Printer.colorPrintTime : Printer.BAndWPrintTime;
                    int totalTime = document.getPageNumber() * timePerPage;
                    PrinterView.sleepThread(totalTime);
                    JOptionPane.showMessageDialog(null, "Documento \"" + document.getName() + "\" impreso.\nSe imprimieron " + impression.getPageToPrint() + " paginas.");
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        String op1;
        do{
            op1 = (String) JOptionPane.showInputDialog(null, "INICIO", "MENU PRINCIPAL", JOptionPane.PLAIN_MESSAGE, null, new String[]{"Imprimir Archivos", "Insertar Hojas" , "Apagar"}, null);
            while (op1 == null){
                JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                op1 = (String) JOptionPane.showInputDialog(null, "INICIO", "MENU PRINCIPAL", JOptionPane.PLAIN_MESSAGE, null, new String[]{"Imprimir Archivos", "Insertar Hojas" , "Apagar"}, null);
            }
            switch (op1){
                case "Imprimir Archivos":
                    printFile();
                    break;
                case "Insertar Hojas":
                    insertLeaves();
                    break;
                case "Apagar":
                    break;
                default:
                    break;
            }

        } while(!op1.equals("Apagar"));
    }

    public static void sleepThread(int seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e){
            System.err.println(e.getMessage());
        }
    }

    public static void printFile (){
        String aux = JOptionPane.showInputDialog("Ingrese la cantidad de archivos a imprimir");
        while (aux == null || !valueInteger(aux, Printer.maxFiles)){
            JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
            aux = JOptionPane.showInputDialog("Ingrese la cantidad de archivos a imprimir");
        }
        int quantity = Integer.parseInt(aux);
        for (int i = 0; i < quantity; i++) {
            String name = valueName(i+1);
            String ex = name.substring(name.lastIndexOf('.')).toLowerCase();
            int numPages = 1;
            if (Printer.extensionDocs.contains(ex)){
                String nP = JOptionPane.showInputDialog("Ingrese el numero de paginas que tiene el documento");
                while (nP == null || !valueInteger(nP, 100)){
                    JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                    nP = JOptionPane.showInputDialog("Ingrese la cantidad de paginas que tiene el documento");
                }
                numPages = Integer.parseInt(nP);
            }
            Document dc1 = new Document(numPages,name);
            Impression imp = new Impression();
            imp.setDc(dc1);
            int ax = JOptionPane.showOptionDialog(null, "VISTA", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Color", "B & W"}, null);
            while (ax == -1){
                JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                ax = JOptionPane.showOptionDialog(null, "VISTA", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Color", "B & W"}, null);
            }
            imp.setColor(ax==0);

            ax = JOptionPane.showOptionDialog(null, "PAGINA", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Sencilla", "Doble"}, null);
            while (ax == -1){
                JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                ax = JOptionPane.showOptionDialog(null, "PAGINA", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Sencilla", "Doble"}, null);
            }
            imp.setSimplePage(ax==0);

            ax = JOptionPane.showOptionDialog(null, "TAMAÑO", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Carta", "Oficio"}, null);
            while (ax == -1){
                JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                ax = JOptionPane.showOptionDialog(null, "TAMAÑO", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Carta", "Oficio"}, null);
            }
            imp.setSizeLetter(ax == 0);
            int pagesToPrint = pagesToPrint(dc1);
            imp.setPageToPrint(pagesToPrint);
            int sheets = imp.isSimplePage() ? pagesToPrint : (pagesToPrint + 1 )/ 2 ;
            while (sheets > (imp.isSizeLetter() ? PRINTER.getLeavesRemainingLetter() :  PRINTER.getLeavesRemainingOffice())){
                JOptionPane.showMessageDialog(null, "Error: Hojas insuficientes\nDebe insertar mas hojas");
                insertLeaves();
            }
            imp.setPageToPrint(pagesToPrint);
            ax = JOptionPane.showOptionDialog(null, "PRIORIDAD", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Alta", "Baja"}, null);
            while (ax == -1){
                JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                ax = JOptionPane.showOptionDialog(null, "PRIORIDAD", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"Alta", "Baja"}, null);
            }
            imp.setPriority(ax == 0);
            PRINTER.addImpression(imp);
        }
    }
    public static void insertLeaves (){
        int aux = JOptionPane.showOptionDialog(null, "Tipo de Hoja a Insertar", "CONFIGURACION DE IMPRESION", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,new String [] {"Carta","Oficio"},null);
        while (aux == -1){
            JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
            aux = JOptionPane.showOptionDialog(null, "Tipo de Hoja a Insertar", "CONFIGURACION DE IMPRESION", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,new String [] {"Carta","Oficio"},null);
        }
        String n = JOptionPane.showInputDialog("Ingrese el numero de hojas a insertar");
        while(!valueInteger(n,50)){
            JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
            n = JOptionPane.showInputDialog("Ingrese el numero de hojas a insertar");
        }
        if (aux == 0) {
            PRINTER.updateLeavesRemainingLetter(Integer.parseInt(n));
            JOptionPane.showMessageDialog(null, "Hojas Carta Disponibles: "+PRINTER.getLeavesRemainingLetter() +
                    "\nHojas Oficio Disponibles: " + PRINTER.getLeavesRemainingOffice());
        } else {
            PRINTER.updateLeavesRemainingOffice(Integer.parseInt(n));
            JOptionPane.showMessageDialog(null, "Hojas Oficio Disponibles: "+PRINTER.getLeavesRemainingOffice() +
                    "\nHojas Carta Disponibles: " + PRINTER.getLeavesRemainingLetter());
        }
    }

    public static int pagesToPrint(Document document) {
        int temp = JOptionPane.showOptionDialog(null, "IMPRIMIR TODAS LAS PAGINAS", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"SI", "NO"}, null);
        while (temp == -1){
            JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
            temp = JOptionPane.showOptionDialog(null, "IMPRIMIR TODAS LAS PAGINAS", "CONFIGURACION DE IMPRESION",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,new String[]{"SI", "NO"}, null);
        }
        if (temp == 0) return document.getPageNumber();
        String input = JOptionPane.showInputDialog("Ingrese las páginas a imprimir separadas por coma (\",\")");
        String[] pages = input.split(",");
        while (!validatePages(pages, document)){
            JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
            input = JOptionPane.showInputDialog("Ingrese las páginas a imprimir separadas por coma (\",\")");
            pages = input.split(",");
        }
        return pages.length;
    }

    public static boolean validatePages(String[] pages, Document document){
        for (String page: pages){
            if (!valueInteger(page.trim(), document.getPageNumber())){
                return false;
            }
        }
        return true;
    }
    public static boolean valueInteger (String n, int limit){
        try{
            int nC = Integer.parseInt(n);
            return nC <= limit && nC > 0;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static String valueName (int docNumber){
            String name = JOptionPane.showInputDialog("Ingrese el nombre del documento No." + docNumber);
            while (!validateName(name)){
                JOptionPane.showMessageDialog(null, "Error: Opcion Invalida");
                name = JOptionPane.showInputDialog("Ingrese el nombre del documento No." + docNumber);
            }
        return name;
    }
    public static boolean validateName (String name){
            int lastPointIndex = name.lastIndexOf('.');
            if(lastPointIndex <= 0){
                return false;
            }
            String ex = name.substring(lastPointIndex).toLowerCase();
        return Printer.extensionDocs.contains(ex) || Printer.extensionImg.contains(ex);
    }
}