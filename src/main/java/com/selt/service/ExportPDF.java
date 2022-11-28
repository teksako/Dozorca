package com.selt.service;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;


import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
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
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Text;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
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
        PdfFont helvetica = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD, BaseFont.CP1250, BaseFont.EMBEDDED);

        PdfWriter writer = new PdfWriter("src/main/resources/Protocol/"+ randomNumber()+".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Text docTitle = new Text("PROTOKÓŁ PRZEKAZANIA");

        PdfPage pdfPage = pdf.addNewPage();
        Paragraph paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();


        Paragraph phoneData=new Paragraph();
        phoneData.add("Producent: "+producent+"\n"+ "Model: "+model+"\n"+"Nr seryjny: " + serialnumber+"\n"+ "IMEI: "+IMEI+"\n"+"SIM: "+SIM+"\n"+"Nr telefonu: "+number);
        Paragraph date = new Paragraph("Opole, " + today.format(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.SHORT)));
        String footerPaath = "src/main/resources/images/FOOTER.png";
        String headerPath= "src/main/resources/images/HEADER.png";
        ImageData data = ImageDataFactory.create(footerPaath);
        ImageData data2 = ImageDataFactory.create(headerPath);


        // Creating an Image object
        Image header = new Image(data2);
        Image footer = new Image(data);
        header.scaleToFit(588,100);
        header.setFixedPosition(5,750);
        footer.scaleToFit(580,100);
        footer.setFixedPosition(7, 0);
        // Adding image to the document

        phoneData.setFixedPosition(100,500,500);
        phoneData.setMarginLeft(100);
        phoneData.setTextAlignment(TextAlignment.LEFT);

        docTitle.setFont(helvetica);
        paragraph2.setFixedPosition(0,640,600);
        paragraph2.setTextAlignment(TextAlignment.CENTER);

        docTitle.setFontSize(17);
        docTitle.setBold();
        docTitle.setFont(helvetica);
        paragraph2.add(docTitle);
        // Initial point of the line
        //headerLine.moveTo(90, 760);
        //footerLine.moveTo(0,70);
        date.setFixedPosition(456,732,100);
        date.setFontSize(11);
        paragraph.setFixedPosition(20,10,500);
        paragraph.setFontSize(7);


        document.add(phoneData);
        document.add(paragraph2);
        document.add(date);
        document.add(header);
        document.add(footer);
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
