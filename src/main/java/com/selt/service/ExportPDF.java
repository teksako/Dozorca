package com.selt.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;


import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.selt.model.Raport;
import lombok.Data;
import com.itextpdf.kernel.pdf.PdfPage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.itextpdf.layout.element.Paragraph;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.element.Image;

@Data
public class ExportPDF {

    static ByteArrayOutputStream out = new ByteArrayOutputStream();
    //static Document document=new Document();
    static LocalDate today = LocalDate.now();
    static LocalTime actualTime = LocalTime.now();
    static String producent="Huawei";
    static String model="Y6";
    static String serialnumber="GXM1246678Y";
    static String IMEI="4987365487949263";
    static String SIM="123244557668576867";
    static String number="694048092";


    public static  int randomNumber() {
        int min = 0;
        int max = 100;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public static void protcol() throws IOException, DocumentException {
        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfWriter writer = new PdfWriter("src/main/resources/Protocol/"+ randomNumber()+".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfPage pdfPage = pdf.addNewPage();

        PdfCanvas headerLine = new PdfCanvas(pdfPage);
        PdfCanvas footerLine = new PdfCanvas(pdfPage);
        Paragraph paragraph = new Paragraph();
        Paragraph docTitle = new Paragraph("PROTOKÓŁ ODBIORU");
        Paragraph phoneData=new Paragraph();
        phoneData.add("Producent: "+producent+"\n"+ "Model: "+model+"\n"+"Nr seryjny: " + serialnumber+"\n"+ "IMEI: "+IMEI+"\n"+"SIM: "+SIM+"\n"+"Nr telefonu: "+number);
        Paragraph date = new Paragraph("Opole, " + today.format(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.SHORT)));
        String imFile = "src/main/resources/images/logo.png";
        ImageData data = ImageDataFactory.create(imFile);
        paragraph.add("SELT Sp. z o.o.\n" +
                  "ul. Wschodnia 23a | 45-449 Opole\n" +
                    "Sąd Rejonowy w Opolu VIII Wydział Gospodarczy Krajowego Rejestru Sądowego\n" +
                   "KRS 0000589791 | NIP 7543103311\n" +
                   "Kapitał zakładowy 64.000.000 PLN");

        // Creating an Image object
        Image image = new Image(data);
        image.scaleToFit(70,70);
        image.setFixedPosition(10, 760);
        // Adding image to the document

        phoneData.setFixedPosition(220,550,300);
        docTitle.setFixedPosition(220,680,300);
        docTitle.setFontSize(17);
        docTitle.setBold();

        // Initial point of the line
        headerLine.moveTo(0, 750);
        //footerLine.moveTo(0,70);
        date.setFixedPosition(520,730,70);
        date.setFontSize(8);
        paragraph.setFixedPosition(20,10,500);
        paragraph.setFontSize(7);
        // Drawing the line
        headerLine.lineTo(595, 750);
        //footerLine.lineTo(595,70);
        headerLine.closePathStroke();
        //footerLine.closePathStroke();

        document.add(phoneData);
        document.add(docTitle);
        document.add(date);
        document.add(image);
        document.add(paragraph);

        // Closing the document
        document.close();

        System.out.println("Image added");


    }

    public static ByteArrayInputStream tonerRaport(List<Raport> raportList) {

//        try {
//            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
//            Font headFont = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED,15);
//            //Font data = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED,15);
//
//            Paragraph p = new Paragraph();
//            Paragraph header = new Paragraph();
//            Paragraph tableHeader=new Paragraph();
//
//
//            header.add("SELT Sp. z o.o.\n" +
//                    "ul. Wschodnia 23a | 45-449 Opole\n" +
//                    "Sąd Rejonowy w Opolu VIII Wydział Gospodarczy Krajowego Rejestru Sądowego\n" +
//                    "KRS 0000589791 | NIP 7543103311\n" +
//                    "Kapitał zakładowy 64.000.000 PLN");
//            tableHeader.setFont(headFont);
//            tableHeader.setAlignment(Element.ALIGN_CENTER);
//
//
//
//
//
//            p.add("Raport wygenerowany o godzinie " +actualTime.getHour()+":"+actualTime.getMinute()+":"+actualTime.getSecond()+ " dnia "+ today.toString() + " ".replaceAll("\\s+", "\n"));
//            p.setAlignment(Element.ALIGN_CENTER);
//
//            tableHeader.add("Spis wniosków");
//            tableHeader.add(Chunk.NEWLINE);
//            tableHeader.add(Chunk.NEWLINE);
//
//            PdfPTable table = new PdfPTable(7);
//            table.setWidthPercentage(90);
//            table.setWidths(new int[]{5, 3, 5, 5, 3, 8, 5});
//            PdfPCell hcell;
//
//
//            hcell=new PdfPCell(new Phrase("Toner"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell=new PdfPCell(new Phrase("Ilość"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell=new PdfPCell(new Phrase("Drukarka"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell=new PdfPCell(new Phrase("Dział"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell=new PdfPCell(new Phrase("MPK"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell=new PdfPCell(new Phrase("Nr. inwentarzowy"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            hcell=new PdfPCell(new Phrase("Data"));
//            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(hcell);
//
//            for (Raport raport :raportList) {
//
//                PdfPCell cell;
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getToner())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getCount())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getPrinter())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getDepartment())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getMPK())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getInventoryNumber())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//                cell=new PdfPCell(new Phrase(String.valueOf(raport.getDate())));
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                table.addCell(cell);
//
//
//
//
//            }
//
//            PdfWriter.getInstance(document, out);
//            document.add(imgSoc);
//            document.open();
//            document.add(header);
//            document.add(p);
//            document.add(tableHeader);
//            document.add(table);
//            document.close();

//        }   catch (DocumentException | IOException ex) {
//
//        }
//
       return new ByteArrayInputStream(out.toByteArray());
    }


}
