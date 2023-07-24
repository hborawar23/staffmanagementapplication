package com.staffmanagement.helper;

import com.staffmanagement.entities.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class UserPdfExporter {

    private User user;

    public UserPdfExporter(User user) {
        this.user = user;
    }


//    public CustomerPDFExporter(com.lowagie.text. ) {
//        this.user = user;
//    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(" Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Food Details", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Amount", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Restaurant Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date/Time", font));
        table.addCell(cell);


    }
    private void writeTableData(PdfPTable table){
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getName());
            table.addCell(user.getEmail());
            table.addCell(String.valueOf(user.getMobileNumber()));
//            table.addCell(String.valueOf(order.getFoodQuantity()));
//            table.addCell(String.valueOf(order.getTotalPrice()));
//            table.addCell(order.getRestaurantName());
//            table.addCell(String.valueOf(order.getDate()));

    }
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.BLUE);
        font.setSize(18);

        Paragraph title = new Paragraph("Order History", font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 3.5f, 3.5f, 3.5f});
        table.setSpacingBefore(10);
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();


    }

        }


