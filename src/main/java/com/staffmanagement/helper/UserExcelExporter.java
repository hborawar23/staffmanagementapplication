package com.staffmanagement.helper;
import com.staffmanagement.entities.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<User> listUsers;
    public UserExcelExporter(List<User> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Staff");
    }

    private void writeHeaderRow(){
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("User ID");

        cell = row.createCell(1);
        cell.setCellValue("E-mail");

        cell = row.createCell(2);
        cell.setCellValue("Full Name");

        cell = row.createCell(3);
        cell.setCellValue("Role");

        cell = row.createCell(4);
        cell.setCellValue("Mobile number");

        cell = row.createCell(4);
        cell.setCellValue("Permanent Address");

        cell = row.createCell(5);
        cell.setCellValue("Present Address");

        cell = row.createCell(6);
        cell.setCellValue("DOB");
    }

    private void writeDataRows(){
        int rowCount = 1;

        for (User user : listUsers)
        {
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(user.getId());

            cell = row.createCell(1);
            cell.setCellValue(user.getEmail());

            cell = row.createCell(2);
            cell.setCellValue(user.getName());

            cell = row.createCell(3);
            cell.setCellValue(user.getRole());

            cell = row.createCell(4);
            cell.setCellValue(user.getMobileNumber());

            cell = row.createCell(5);
            cell.setCellValue(user.getPermanentAddress());

            cell = row.createCell(6);
            cell.setCellValue(user.getPresentAddress());

            cell = row.createCell(5);
            cell.setCellValue(user.getDob());
        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();;
        writeDataRows();

        ServletOutputStream outputStream= response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


}
